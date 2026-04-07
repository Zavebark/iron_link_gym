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
import javafx.scene.layout.GridPane;

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
            statusLabel.setText(members.size() + " member(s) found.");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML private void handleSearch() {
        try {
            String kw = searchField.getText().trim();
            if (kw.isEmpty()) { loadAll(); return; }
            List<Member> res = memberDAO.searchMembers(kw);
            memberTable.setItems(FXCollections.observableArrayList(res));
            statusLabel.setText(res.size() + " result(s) found.");
        } catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
    }

    @FXML private void handleShowAll() { searchField.clear(); loadAll(); }

    @FXML private void handleAdd() {
        Optional<Member> result = buildDialog(null).showAndWait();
        result.ifPresent(m -> {
            try { memberDAO.addMember(m); statusLabel.setText("Member added."); loadAll(); }
            catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
        });
    }

    @FXML private void handleEdit() {
        Member sel = memberTable.getSelectionModel().getSelectedItem();
        if (sel == null) { statusLabel.setText("Select a member to edit."); return; }
        Optional<Member> result = buildDialog(sel).showAndWait();
        result.ifPresent(m -> {
            try { memberDAO.updateMember(m); statusLabel.setText("Member updated."); loadAll(); }
            catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
        });
    }

    @FXML private void handleDelete() {
        Member sel = memberTable.getSelectionModel().getSelectedItem();
        if (sel == null) { statusLabel.setText("Select a member to delete."); return; }
        Alert a = new Alert(Alert.AlertType.CONFIRMATION,
            "Delete " + sel.getFullName() + "? This also removes their memberships and attendance.",
            ButtonType.YES, ButtonType.NO);
        a.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                try { memberDAO.deleteMember(sel.getMemberId()); statusLabel.setText("Member deleted."); loadAll(); }
                catch (Exception e) { statusLabel.setText("Error: " + e.getMessage()); }
            }
        });
    }

    private Dialog<Member> buildDialog(Member ex) {
        Dialog<Member> dlg = new Dialog<>();
        dlg.setTitle(ex == null ? "Add Member" : "Edit Member");

        TextField nameField    = new TextField(ex != null ? ex.getFullName()    : "");
        TextField phoneField   = new TextField(ex != null ? ex.getPhone()       : "");
        TextField emailField   = new TextField(ex != null ? ex.getEmail()       : "");
        TextField notesField   = new TextField(ex != null ? ex.getMedicalNotes(): "");

        nameField.setPromptText("Full Name *");
        phoneField.setPromptText("Phone");
        emailField.setPromptText("Email");
        notesField.setPromptText("Medical Notes (optional)");

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(8);
        grid.setStyle("-fx-padding: 10px;");
        grid.addRow(0, new Label("Full Name:"),    nameField);
        grid.addRow(1, new Label("Phone:"),        phoneField);
        grid.addRow(2, new Label("Email:"),        emailField);
        grid.addRow(4, new Label("Medical Notes:"),notesField);

        dlg.getDialogPane().setContent(grid);
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dlg.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                int id = ex != null ? ex.getMemberId() : 0;
                return new Member(id, nameField.getText(), phoneField.getText(), emailField.getText(),
                    new Date(System.currentTimeMillis()), notesField.getText());
            }
            return null;
        });
        return dlg;
    }
}
