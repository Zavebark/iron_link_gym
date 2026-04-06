package com.iron_link_gym.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
                rs.getString("specialization"),
                rs.getString("timings")
            ));
        }
        return trainers;
    }

    public void addTrainer(Trainer t) throws SQLException {
        String sql = "INSERT INTO TRAINERS (full_name, phone, specialization, timings) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, t.getFullName());
        ps.setString(2, t.getPhone());
        ps.setString(3, t.getSpecialization());
        ps.setString(4, t.getTimings());
        ps.executeUpdate();
    }

    public void updateTrainer(Trainer t) throws SQLException {
        String sql = "UPDATE TRAINERS SET full_name=?, phone=?, specialization=?, timings=? WHERE trainer_id=?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setString(1, t.getFullName());
        ps.setString(2, t.getPhone());
        ps.setString(3, t.getSpecialization());
        ps.setString(4, t.getTimings());
        ps.setInt(5, t.getTrainerId());
        ps.executeUpdate();
    }

    public void deleteTrainer(int trainerId) throws SQLException {
        String sql = "DELETE FROM TRAINERS WHERE trainer_id=?";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ps.setInt(1, trainerId);
        ps.executeUpdate();
    }
}