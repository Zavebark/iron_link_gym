package com.iron_link_gym.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainController {

    @FXML private TabPane tabPane;
    @FXML private Tab membersTab;
    @FXML private Tab plansTab;
    @FXML private Tab membershipsTab;
    @FXML private Tab attendanceTab;

    @FXML
    public void initialize() {
        try {
            membersTab.setContent(FXMLLoader.load(getClass().getResource("/com/iron_link_gym/members.fxml")));
            plansTab.setContent(FXMLLoader.load(getClass().getResource("/com/iron_link_gym/plans.fxml")));
            membershipsTab.setContent(FXMLLoader.load(getClass().getResource("/com/iron_link_gym/memberships.fxml")));
            attendanceTab.setContent(FXMLLoader.load(getClass().getResource("/com/iron_link_gym/attendance.fxml")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}