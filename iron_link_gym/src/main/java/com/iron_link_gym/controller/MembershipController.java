package com.iron_link_gym.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.iron_link_gym.dao.MemberDAO;
import com.iron_link_gym.dao.MembershipDAO;
import com.iron_link_gym.dao.PlanDAO;
import com.iron_link_gym.dao.TrainerDAO;
import com.iron_link_gym.model.Member;
import com.iron_link_gym.model.Membership;
import com.iron_link_gym.model.Plan;
import com.iron_link_gym.model.Trainer;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class MembershipController {

    /* ── table ── */
    @FXML private TableView<Membership> membershipTable;
    @FXML private TableColumn<Membership, Integer> colSubId;
    @FXML private TableColumn<Membership, String>  colMember;
    @FXML private TableColumn<Membership, String>  colPlan;
    @FXML private TableColumn<Membership, String>  colTrainer;
    @FXML private TableColumn<Membership, Date>    colStart;
    @FXML private TableColumn<Membership, Date>    colEnd;
    @FXML private TableColumn<Membership, String>  colStatus;

    /* ── filter & status bar ── */
    @FXML private ComboBox<String> filterStatus;
    @FXML private TextField searchMemberField;
    @FXML private Label statusLabel;

    private final MembershipDAO membershipDAO = new MembershipDAO();
    private final MemberDAO     memberDAO     = new MemberDAO();
    private final PlanDAO       planDAO       = new PlanDAO();
    private final TrainerDAO    trainerDAO    = new TrainerDAO();

    @FXML
    public void initialize() {
        colSubId.setCellValueFactory(new PropertyValueFactory<>("subscriptionId"));
        colMember.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        colPlan.setCellValueFactory(new PropertyValueFactory<>("planName"));
        colTrainer.setCellValueFactory(new PropertyValueFactory<>("trainerName"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Color-code status column
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty || s == null) { setText(null); setStyle(""); return; }
                setText(s);
                switch (s) {
                    case "Active"  -> setStyle("-fx-text-fill: #1a8a1a; -fx-font-weight:bold;");
                    case "Expired" -> setStyle("-fx-text-fill: #cc0000; -fx-font-weight:bold;");
                    default        -> setStyle("-fx-text-fill: #e07000; -fx-font-weight:bold;");
                }
            }
        });

        filterStatus.setItems(FXCollections.observableArrayList(
            "All", "Active", "Expired", "Suspended"));
        filterStatus.setValue("All");
        loadAll();
    }

    // ── Load ──────────────────────────────────────────────────────────────────

    private void loadAll() {
        try {
            List<Membership> list = membershipDAO.getAllMemberships();
            membershipTable.setItems(FXCollections.observableArrayList(list));
            statusLabel.setText(list.size() + " membership(s) found.");
        } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    @FXML private void handleFilter() {
        try {
            String filter = filterStatus.getValue();
            List<Membership> list = "All".equals(filter)
                ? membershipDAO.getAllMemberships()
                : membershipDAO.getMembershipsByStatus(filter);
            membershipTable.setItems(FXCollections.observableArrayList(list));
            statusLabel.setText(list.size() + " result(s).");
        } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    @FXML private void handleSearchByMember() {
        String kw = searchMemberField.getText().trim();
        if (kw.isEmpty()) { loadAll(); return; }
        try {
            List<Member> members = memberDAO.searchMembers(kw);
            if (members.isEmpty()) { statusLabel.setText("No members found."); return; }
            List<Membership> list = membershipDAO.getMembershipsByMember(
                members.get(0).getMemberId());
            membershipTable.setItems(FXCollections.observableArrayList(list));
            statusLabel.setText(list.size() + " membership(s) for " + members.get(0).getFullName());
        } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    @FXML private void handleShowAll() { searchMemberField.clear(); filterStatus.setValue("All"); loadAll(); }

    // ── Validate (check active membership for a member) ───────────────────────

    @FXML private void handleValidate() {
        String kw = searchMemberField.getText().trim();
        if (kw.isEmpty()) { statusLabel.setText("Enter member name or phone to validate."); return; }
        try {
            List<Member> members = memberDAO.searchMembers(kw);
            if (members.isEmpty()) { statusLabel.setText("No member found matching \"" + kw + "\"."); return; }
            Member m = members.get(0);
            Membership active = membershipDAO.getActiveMembership(m.getMemberId());
            if (active == null) {
                showInfo("Validation Result",
                    "❌ " + m.getFullName() + " has NO active membership.");
            } else {
                showInfo("Validation Result",
                    "✅ " + m.getFullName() + "\n" +
                    "Plan    : " + active.getPlanName() + "\n" +
                    "Trainer : " + active.getTrainerName() + "\n" +
                    "Valid until: " + active.getEndDate());
            }
        } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    // ── Add ───────────────────────────────────────────────────────────────────

    @FXML private void handleAdd() {
        try {
            List<Member>  members  = memberDAO.getAllMembers();
            List<Plan>    plans    = planDAO.getActivePlans();
            List<Trainer> trainers = trainerDAO.getAllTrainers();
            if (members.isEmpty()) { statusLabel.setText("Add members first."); return; }
            if (plans.isEmpty())   { statusLabel.setText("Add active plans first."); return; }
            Optional<Membership> r = buildAddDialog(members, plans, trainers).showAndWait();
            r.ifPresent(ms -> {
                try { membershipDAO.addMembership(ms); statusLabel.setText("Membership added."); loadAll(); }
                catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
            });
        } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    // ── Renew ─────────────────────────────────────────────────────────────────

    @FXML private void handleRenew() {
        Membership sel = membershipTable.getSelectionModel().getSelectedItem();
        if (sel == null) { statusLabel.setText("Select a membership to renew."); return; }

        try {
            // Find the plan to know the duration
            List<Plan> all = planDAO.getAllPlans();
            Plan plan = all.stream().filter(p -> p.getPlanId() == sel.getPlanId())
                           .findFirst().orElse(null);
            int days = plan != null ? plan.getDurationDays() : 30;

            // New end = max(today, existing end) + plan duration
            LocalDate base = sel.getEndDate().toLocalDate().isBefore(LocalDate.now())
                ? LocalDate.now() : sel.getEndDate().toLocalDate();
            LocalDate newEnd = base.plusDays(days);
            Date sqlNewEnd = Date.valueOf(newEnd);

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Renew membership for " + sel.getMemberName() + "?\n" +
                "New end date: " + newEnd, ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    try {
                        membershipDAO.renewMembership(sel.getSubscriptionId(), sqlNewEnd);
                        statusLabel.setText("Membership renewed until " + newEnd);
                        loadAll();
                    } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
                }
            });
        } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    // ── Suspend / Expire manually ─────────────────────────────────────────────

    @FXML private void handleSuspend() { changeStatus("Suspended"); }
    @FXML private void handleExpire()  { changeStatus("Expired"); }

    private void changeStatus(String newStatus) {
        Membership sel = membershipTable.getSelectionModel().getSelectedItem();
        if (sel == null) { statusLabel.setText("Select a membership first."); return; }
        try {
            membershipDAO.updateStatus(sel.getSubscriptionId(), newStatus);
            statusLabel.setText("Status changed to " + newStatus + ".");
            loadAll();
        } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    @FXML private void handleDelete() {
        Membership sel = membershipTable.getSelectionModel().getSelectedItem();
        if (sel == null) { statusLabel.setText("Select a membership to delete."); return; }
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,
            "Delete membership #" + sel.getSubscriptionId() + " for " + sel.getMemberName() + "?",
            ButtonType.YES, ButtonType.NO);
        a.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try { membershipDAO.deleteMembership(sel.getSubscriptionId());
                      statusLabel.setText("Membership deleted."); loadAll(); }
                catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
            }
        });
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void showInfo(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setTitle(title); a.setHeaderText(null); a.showAndWait();
    }

    private Dialog<Membership> buildAddDialog(
            List<Member> members, List<Plan> plans, List<Trainer> trainers) {

        Dialog<Membership> dlg = new Dialog<>();
        dlg.setTitle("Add Membership");

        ComboBox<Member>  memberBox  = new ComboBox<>(FXCollections.observableArrayList(members));
        ComboBox<Plan>    planBox    = new ComboBox<>(FXCollections.observableArrayList(plans));
        ComboBox<Trainer> trainerBox = new ComboBox<>(FXCollections.observableArrayList(trainers));
        trainerBox.getItems().add(0, null); // "no trainer" option
        trainerBox.setPromptText("(No trainer)");

        DatePicker startPicker = new DatePicker(LocalDate.now());
        Label endLabel = new Label("—");

        // Auto-calculate end date when plan or start changes
        Runnable calcEnd = () -> {
            if (planBox.getValue() != null && startPicker.getValue() != null) {
                LocalDate end = startPicker.getValue()
                    .plusDays(planBox.getValue().getDurationDays());
                endLabel.setText(end.toString());
            }
        };
        planBox.setOnAction(e -> calcEnd.run());
        startPicker.setOnAction(e -> calcEnd.run());

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(10);
        grid.setStyle("-fx-padding:12px;");
        grid.addRow(0, new Label("Member: *"),  memberBox);
        grid.addRow(1, new Label("Plan: *"),    planBox);
        grid.addRow(2, new Label("Trainer:"),   trainerBox);
        grid.addRow(3, new Label("Start Date:"),startPicker);
        grid.addRow(4, new Label("End Date:"),  endLabel);

        dlg.getDialogPane().setContent(grid);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dlg.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                if (memberBox.getValue() == null || planBox.getValue() == null) {
                    new Alert(Alert.AlertType.ERROR, "Member and Plan are required.").showAndWait();
                    return null;
                }
                Member  m = memberBox.getValue();
                Plan    p = planBox.getValue();
                Trainer t = trainerBox.getValue();

                LocalDate start  = startPicker.getValue();
                LocalDate end    = start.plusDays(p.getDurationDays());

                return new Membership(0,
                    m.getMemberId(), m.getFullName(),
                    p.getPlanId(),   p.getPlanName(),
                    t != null ? t.getTrainerId() : 0,
                    t != null ? t.getFullName()  : "None",
                    Date.valueOf(start), Date.valueOf(end), "Active");
            }
            return null;
        });
        return dlg;
    }
}
