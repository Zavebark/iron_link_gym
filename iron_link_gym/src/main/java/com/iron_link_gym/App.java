package com.iron_link_gym;

import com.iron_link_gym.db.DBConnection;
import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("Connected to Oracle DB!");
            DBConnection.closeConnection();
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}