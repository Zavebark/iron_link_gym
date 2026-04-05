package com.iron_link_gym.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.iron_link_gym.db.DBConnection;
import com.iron_link_gym.model.Trainer;

public class TrainerDAO {
    public List<Trainer> getAllTrainers() throws SQLException {
        List<Trainer> trainers = new ArrayList<>();
        String sql = "SELECT * FROM TRAINERS ORDER BY trainer_id";
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            trainers.add(new Trainer(
                rs.getInt("trainer_id"),
                rs.getString("full_name"),
                rs.getString("phone"),
                rs.getString("specialization")
            ));
        }
        return trainers;
    }
}
