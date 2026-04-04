package com.iron_link_gym.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.iron_link_gym.db.DBConnection;
import com.iron_link_gym.model.Plan;

public class PlanDAO {

    public List<Plan> getAllPlans() throws SQLException {
        List<Plan> plans = new ArrayList<>();
        String sql = "SELECT * FROM PLANS ORDER BY plan_id";
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            plans.add(new Plan(
                rs.getInt("plan_id"),
                rs.getString("plan_name"),
                rs.getString("plan_type"),
                rs.getInt("duration_days"),
                rs.getDouble("price"),
                rs.getString("is_active")
            ));
        }
        return plans;
    }

    public List<Plan> getActivePlans() throws SQLException {
        List<Plan> plans = new ArrayList<>();
        String sql = "SELECT * FROM PLANS WHERE is_active='Y' ORDER BY plan_id";
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            plans.add(new Plan(
                rs.getInt("plan_id"),
                rs.getString("plan_name"),
                rs.getString("plan_type"),
                rs.getInt("duration_days"),
                rs.getDouble("price"),
                rs.getString("is_active")
            ));
        }
        return plans;
    }

    public void addPlan(Plan p) throws SQLException {
        String sql = "INSERT INTO PLANS (plan_name, plan_type, duration_days, price, is_active) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, p.getPlanName());
        ps.setString(2, p.getPlanType());
        ps.setInt(3, p.getDurationDays());
        ps.setDouble(4, p.getPrice());
        ps.setString(5, p.getIsActive());
        ps.executeUpdate();
    }

    public void updatePlan(Plan p) throws SQLException {
        String sql = "UPDATE PLANS SET plan_name=?, plan_type=?, duration_days=?, price=?, is_active=? WHERE plan_id=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, p.getPlanName());
        ps.setString(2, p.getPlanType());
        ps.setInt(3, p.getDurationDays());
        ps.setDouble(4, p.getPrice());
        ps.setString(5, p.getIsActive());
        ps.setInt(6, p.getPlanId());
        ps.executeUpdate();
    }
}