package com.iron_link_gym.controller;

import java.util.List;
import java.util.Optional;

import com.iron_link_gym.dao.TrainerDAO;
import com.iron_link_gym.model.Trainer;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class TrainerController {

    @FXML private TableView<Trainer> trainerTable;
    @FXML private TableColumn<Trainer, Integer> colId;
    @FXML private TableColumn<Trainer, String>  colName;
    @FXML private TableColumn<Trainer, String>  colPhone;
    @FXML private TableColumn<Trainer, String>  colSpecialization;
    @FXML private TableColumn<Trainer, String>  colTimings;
    @FXML private Label statusLabel;

    private final TrainerDAO trainerDAO = new TrainerDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("trainerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colTimings.setCellValueFactory(new PropertyValueFactory<>("timings"));
        loadAll();
    }

    private void loadAll() {
        try {
            List<Trainer> trainers = trainerDAO.getAllTrainers();
            trainerTable.setItems(FXCollections.observableArrayList(trainers));
            statusLabel.setText(trainers.size() + " trainer(s) found.");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML private void handleShowAll() { loadAll(); }

    @FXML private void handleAdd() {
        Optional<Trainer> r = buildDialog(null).showAndWait();
        r.ifPresent(t -> {
            try { trainerDAO.addTrainer(t); statusLabel.setText("Trainer added!"); loadAll(); }
            catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
        });
    }

    @FXML private void handleEdit() {
        Trainer sel = trainerTable.getSelectionModel().getSelectedItem();
        if (sel == null) { statusLabel.setText("Select a trainer to edit."); return; }
        Optional<Trainer> r = buildDialog(sel).showAndWait();
        r.ifPresent(t -> {
            try { trainerDAO.updateTrainer(t); statusLabel.setText("Trainer updated!"); loadAll(); }
            catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
        });
    }

    @FXML private void handleDelete() {
        Trainer sel = trainerTable.getSelectionModel().getSelectedItem();
        if (sel == null) { statusLabel.setText("Select a trainer to delete."); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Delete " + sel.getFullName() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try { trainerDAO.deleteTrainer(sel.getTrainerId()); statusLabel.setText("Trainer deleted."); loadAll(); }
                catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
            }
        });
    }

    private Dialog<Trainer> buildDialog(Trainer ex) {
        Dialog<Trainer> dlg = new Dialog<>();
        dlg.setTitle(ex == null ? "Add Trainer" : "Edit Trainer");

        TextField nameField  = new TextField(ex != null ? ex.getFullName()       : "");
        TextField phoneField = new TextField(ex != null ? ex.getPhone()          : "");
        TextField specField  = new TextField(ex != null ? ex.getSpecialization() : "");
        TextField timeField  = new TextField(ex != null ? ex.getTimings()        : "");

        nameField.setPromptText("Full Name");
        phoneField.setPromptText("Phone");
        specField.setPromptText("e.g. Strength Training");
        timeField.setPromptText("e.g. Mon-Fri 6AM-10AM");

        VBox box = new VBox(10,
            new Label("Full Name:"),     nameField,
            new Label("Phone:"),         phoneField,
            new Label("Specialization:"),specField,
            new Label("Timings:"),       timeField
        );
        box.setStyle("-fx-padding: 10px;");
        dlg.getDialogPane().setContent(box);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dlg.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                int id = ex != null ? ex.getTrainerId() : 0;
                return new Trainer(id, nameField.getText(), phoneField.getText(),
                        specField.getText(), timeField.getText());
            }
            return null;
        });
        return dlg;
    }
}