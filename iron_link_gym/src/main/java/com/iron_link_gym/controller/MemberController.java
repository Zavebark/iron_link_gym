package com.iron_link_gym.controller;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import com.iron_link_gym.dao.MemberDAO;
import com.iron_link_gym.model.Member;

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

public class MemberController {

    @FXML private TextField searchField;
    @FXML private TableView<Member> memberTable;
    @FXML private TableColumn<Member, Integer> colId;
    @FXML private TableColumn<Member, String>  colName;
    @FXML private TableColumn<Member, String>  colPhone;
    @FXML private TableColumn<Member, String>  colEmail;
    @FXML private TableColumn<Member, Date>    colJoined;
    @FXML private TableColumn<Member, String>  colNotes;
    @FXML private Label statusLabel;

    private final MemberDAO memberDAO = new MemberDAO();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colJoined.setCellValueFactory(new PropertyValueFactory<>("dateJoining"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("medicalNotes"));
        loadAll();
    }

    private void loadAll() {
        try {
            List<Member> members = memberDAO.getAllMembers();
            memberTable.setItems(FXCollections.observableArrayList(members));
            statusLabel.setText(members.size() + " members found.");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        try {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) { loadAll(); return; }
            List<Member> results = memberDAO.searchMembers(keyword);
            memberTable.setItems(FXCollections.observableArrayList(results));
            statusLabel.setText(results.size() + " result(s) found.");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleShowAll() {
        searchField.clear();
        loadAll();
    }

    @FXML
    private void handleAdd() {
        Dialog<Member> dialog = buildDialog(null);
        Optional<Member> result = dialog.showAndWait();
        result.ifPresent(m -> {
            try {
                memberDAO.addMember(m);
                statusLabel.setText("Member added successfully!");
                loadAll();
            } catch (Exception e) {
                statusLabel.setText("Error: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleEdit() {
        Member selected = memberTable.getSelectionModel().getSelectedItem();
        if (selected == null) { statusLabel.setText("Please select a member to edit."); return; }
        Dialog<Member> dialog = buildDialog(selected);
        Optional<Member> result = dialog.showAndWait();
        result.ifPresent(m -> {
            try {
                memberDAO.updateMember(m);
                statusLabel.setText("Member updated successfully!");
                loadAll();
            } catch (Exception e) {
                statusLabel.setText("Error: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleDelete() {
        Member selected = memberTable.getSelectionModel().getSelectedItem();
        if (selected == null) { statusLabel.setText("Please select a member to delete."); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Delete " + selected.getFullName() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try {
                    memberDAO.deleteMember(selected.getMemberId());
                    statusLabel.setText("Member deleted.");
                    loadAll();
                } catch (Exception e) {
                    statusLabel.setText("Error: " + e.getMessage());
                }
            }
        });
    }

    private Dialog<Member> buildDialog(Member existing) {
        Dialog<Member> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Add Member" : "Edit Member");

        TextField nameField   = new TextField(existing != null ? existing.getFullName() : "");
        TextField phoneField  = new TextField(existing != null ? existing.getPhone() : "");
        TextField emailField  = new TextField(existing != null ? existing.getEmail() : "");
        TextField notesField  = new TextField(existing != null ? existing.getMedicalNotes() : "");

        nameField.setPromptText("Full Name");
        phoneField.setPromptText("Phone");
        emailField.setPromptText("Email");
        notesField.setPromptText("Medical Notes (optional)");

        VBox box = new VBox(10, new Label("Full Name:"), nameField,
                                new Label("Phone:"),     phoneField,
                                new Label("Email:"),     emailField,
                                new Label("Medical Notes:"), notesField);
        box.setStyle("-fx-padding: 10px;");
        dialog.getDialogPane().setContent(box);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                int id = existing != null ? existing.getMemberId() : 0;
                return new Member(id, nameField.getText(), phoneField.getText(),
                        emailField.getText(), new Date(System.currentTimeMillis()), notesField.getText());
            }
            return null;
        });
        return dialog;
    }
}