package com.iron_link_gym.controller;

import java.util.List;
import java.util.Optional;

import com.iron_link_gym.dao.PlanDAO;
import com.iron_link_gym.model.Plan;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class PlanController {

    @FXML private TableView<Plan> planTable;
    @FXML private TableColumn<Plan, Integer> colId;
    @FXML private TableColumn<Plan, String>  colName;
    @FXML private TableColumn<Plan, String>  colType;
    @FXML private TableColumn<Plan, Integer> colDuration;
    @FXML private TableColumn<Plan, Double>  colPrice;
    @FXML private TableColumn<Plan, String>  colActive;
    @FXML private Label statusLabel;

    private final PlanDAO planDAO = new PlanDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("planId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("planName"));
        colType.setCellValueFactory(new PropertyValueFactory<>("planType"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("durationDays"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colActive.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        loadAll();
    }

    private void loadAll() {
        try {
            List<Plan> plans = planDAO.getAllPlans();
            planTable.setItems(FXCollections.observableArrayList(plans));
            statusLabel.setText(plans.size() + " plan(s) loaded.");
        } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    @FXML private void handleAdd() {
        Optional<Plan> r = buildDialog(null).showAndWait();
        r.ifPresent(p -> {
            try { planDAO.addPlan(p); statusLabel.setText("Plan added."); loadAll(); }
            catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
        });
    }

    @FXML private void handleEdit() {
        Plan sel = planTable.getSelectionModel().getSelectedItem();
        if (sel == null) { statusLabel.setText("Select a plan to edit."); return; }
        Optional<Plan> r = buildDialog(sel).showAndWait();
        r.ifPresent(p -> {
            try { planDAO.updatePlan(p); statusLabel.setText("Plan updated."); loadAll(); }
            catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
        });
    }

    @FXML private void handleDelete() {
        Plan sel = planTable.getSelectionModel().getSelectedItem();
        if (sel == null) { statusLabel.setText("Select a plan to delete."); return; }
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,
            "Delete plan \"" + sel.getPlanName() + "\"?", ButtonType.YES, ButtonType.NO);
        a.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try { planDAO.deletePlan(sel.getPlanId()); statusLabel.setText("Plan deleted."); loadAll(); }
                catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
            }
        });
    }

    @FXML private void handleShowAll() { loadAll(); }

    private Dialog<Plan> buildDialog(Plan ex) {
        Dialog<Plan> dlg = new Dialog<>();
        dlg.setTitle(ex == null ? "Add Plan" : "Edit Plan");

        TextField nameField     = new TextField(ex != null ? ex.getPlanName()              : "");
        TextField typeField     = new TextField(ex != null ? ex.getPlanType()              : "");
        TextField durationField = new TextField(ex != null ? String.valueOf(ex.getDurationDays()) : "");
        TextField priceField    = new TextField(ex != null ? String.valueOf(ex.getPrice()) : "");
        ComboBox<String> activeBox = new ComboBox<>(
            FXCollections.observableArrayList("Y", "N"));
        activeBox.setValue(ex != null ? ex.getIsActive() : "Y");

        nameField.setPromptText("Plan Name *");
        typeField.setPromptText("e.g. Monthly, Yearly, Daily");
        durationField.setPromptText("Duration in days *");
        priceField.setPromptText("Price (₹) *");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(8);
        grid.setStyle("-fx-padding:10px;");
        grid.addRow(0, new Label("Plan Name:"),  nameField);
        grid.addRow(1, new Label("Type:"),        typeField);
        grid.addRow(2, new Label("Duration (days):"), durationField);
        grid.addRow(3, new Label("Price (₹):"),   priceField);
        grid.addRow(4, new Label("Active:"),       activeBox);

        dlg.getDialogPane().setContent(grid);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dlg.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    int id  = ex != null ? ex.getPlanId() : 0;
                    int dur = Integer.parseInt(durationField.getText().trim());
                    double pr = Double.parseDouble(priceField.getText().trim());
                    return new Plan(id, nameField.getText().trim(), typeField.getText().trim(),
                        dur, pr, activeBox.getValue());
                } catch (NumberFormatException nfe) {
                    new Alert(Alert.AlertType.ERROR,
                        "Duration must be a whole number; Price must be numeric.").showAndWait();
                }
            }
            return null;
        });
        return dlg;
    }
}
