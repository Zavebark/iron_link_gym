package com.iron_link_gym.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.iron_link_gym.db.DBConnection;
import com.iron_link_gym.model.Membership;

public class MembershipDAO {

    private static final String BASE_QUERY =
        "SELECT ms.subscription_id, ms.member_id, m.full_name, ms.plan_id, p.plan_name, " +
        "ms.start_date, ms.end_date, ms.status " +
        "FROM MEMBERSHIPS ms " +
        "JOIN MEMBERS m ON ms.member_id = m.member_id " +
        "JOIN PLANS p ON ms.plan_id = p.plan_id ";

    public List<Membership> getAllMemberships() throws SQLException {
        List<Membership> list = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(BASE_QUERY + "ORDER BY ms.subscription_id DESC");
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public List<Membership> getMembershipsByStatus(String status) throws SQLException {
        List<Membership> list = new ArrayList<>();
        String sql = BASE_QUERY + "WHERE ms.status = ? ORDER BY ms.subscription_id DESC";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, status);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    public void addMembership(Membership ms) throws SQLException {
        String sql = "INSERT INTO MEMBERSHIPS (member_id, plan_id, start_date, end_date, status) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, ms.getMemberId());
        ps.setInt(2, ms.getPlanId());
        ps.setDate(3, ms.getStartDate());
        ps.setDate(4, ms.getEndDate());
        ps.setString(5, ms.getStatus());
        ps.executeUpdate();
    }

    public void updateStatus(int subscriptionId, String status) throws SQLException {
        String sql = "UPDATE MEMBERSHIPS SET status=? WHERE subscription_id=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, status);
        ps.setInt(2, subscriptionId);
        ps.executeUpdate();
    }

    public void refreshExpired() throws SQLException {
        String sql = "UPDATE MEMBERSHIPS SET status='Expired' WHERE status='Active' AND end_date < SYSDATE";
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(sql);
    }

    private Membership mapRow(ResultSet rs) throws SQLException {
        return new Membership(
            rs.getInt("subscription_id"),
            rs.getInt("member_id"),
            rs.getString("full_name"),
            rs.getInt("plan_id"),
            rs.getString("plan_name"),
            rs.getDate("start_date"),
            rs.getDate("end_date"),
            rs.getString("status")
        );
    }
}