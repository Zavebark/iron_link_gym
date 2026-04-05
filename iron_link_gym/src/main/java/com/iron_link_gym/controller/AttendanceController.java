package com.iron_link_gym.controller;

import java.util.List;

import com.iron_link_gym.dao.AttendanceDAO;
import com.iron_link_gym.dao.MemberDAO;
import com.iron_link_gym.model.Attendance;
import com.iron_link_gym.model.Member;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class AttendanceController {

    @FXML private TextField memberSearchField;

    @FXML private TableView<Attendance> attendanceTable;
    @FXML private TableColumn<Attendance, Integer> colId;
    @FXML private TableColumn<Attendance, String>  colName;
    @FXML private TableColumn<Attendance, String>  colDate;
    @FXML private TableColumn<Attendance, String>  colCheckin;
    @FXML private TableColumn<Attendance, String>  colCheckout;

    @FXML private Label statusLabel;

    private final AttendanceDAO attendanceDAO = new AttendanceDAO();
    private final MemberDAO     memberDAO     = new MemberDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("attendanceId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("checkinDate"));

        // Use derived display strings for times
        colCheckin.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty) { setText(null); return; }
                Attendance a = getTableView().getItems().get(getIndex());
                setText(a.getCheckinDisplay());
            }
        });
        colCheckout.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                if (empty) { setText(null); return; }
                Attendance a = getTableView().getItems().get(getIndex());
                setText(a.getCheckoutDisplay());
            }
        });

        handleShowToday();
    }

    // ── View buttons ──────────────────────────────────────────────────────────

    @FXML private void handleShowToday() {
        try {
            List<Attendance> list = attendanceDAO.getTodayAttendance();
            attendanceTable.setItems(FXCollections.observableArrayList(list));
            statusLabel.setText("Today: " + list.size() + " check-in(s).");
        } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    @FXML private void handleShowAll() {
        memberSearchField.clear();
        try {
            List<Attendance> list = attendanceDAO.getAllAttendance();
            attendanceTable.setItems(FXCollections.observableArrayList(list));
            statusLabel.setText(list.size() + " total record(s).");
        } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    @FXML private void handleSearchByMember() {
        String kw = memberSearchField.getText().trim();
        if (kw.isEmpty()) { handleShowAll(); return; }
        try {
            List<Member> members = memberDAO.searchMembers(kw);
            if (members.isEmpty()) { statusLabel.setText("No member found."); return; }
            Member m = members.get(0);
            List<Attendance> list = attendanceDAO.getAttendanceByMember(m.getMemberId());
            attendanceTable.setItems(FXCollections.observableArrayList(list));
            statusLabel.setText(list.size() + " record(s) for " + m.getFullName());
        } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    // ── Check-in ──────────────────────────────────────────────────────────────

    @FXML private void handleCheckIn() {
        String kw = memberSearchField.getText().trim();
        if (kw.isEmpty()) {
            statusLabel.setText("Enter member name or phone, then click Check In.");
            return;
        }
        try {
            List<Member> members = memberDAO.searchMembers(kw);
            if (members.isEmpty()) { statusLabel.setText("No member found matching \"" + kw + "\"."); return; }

            if (members.size() > 1) {
                // Let user pick
                ChoiceDialog<Member> pick = new ChoiceDialog<>(members.get(0), members);
                pick.setTitle("Select Member");
                pick.setHeaderText("Multiple matches found — select one:");
                pick.setContentText("Member:");
                members.stream().findFirst();
                pick.showAndWait().ifPresent(m -> doCheckIn(m));
            } else {
                doCheckIn(members.get(0));
            }
        } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    private void doCheckIn(Member m) {
        try {
            boolean done = attendanceDAO.checkIn(m.getMemberId());
            if (done) {
                statusLabel.setText("✅ " + m.getFullName() + " checked in successfully.");
            } else {
                statusLabel.setText("⚠ " + m.getFullName() + " is already checked in today.");
            }
            handleShowToday();
        } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    // ── Check-out ─────────────────────────────────────────────────────────────

    @FXML private void handleCheckOut() {
        // Priority: use selected row, else search field
        Attendance sel = attendanceTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            doCheckOut(sel.getMemberId(), sel.getFullName());
            return;
        }
        String kw = memberSearchField.getText().trim();
        if (kw.isEmpty()) {
            statusLabel.setText("Select a row or enter member name/phone to check out.");
            return;
        }
        try {
            List<Member> members = memberDAO.searchMembers(kw);
            if (members.isEmpty()) { statusLabel.setText("No member found."); return; }
            doCheckOut(members.get(0).getMemberId(), members.get(0).getFullName());
        } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    private void doCheckOut(int memberId, String name) {
        try {
            boolean done = attendanceDAO.checkOut(memberId);
            statusLabel.setText(done
                ? "✅ " + name + " checked out."
                : "⚠ No open check-in found for " + name + " today.");
            handleShowToday();
        } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
    }
}
