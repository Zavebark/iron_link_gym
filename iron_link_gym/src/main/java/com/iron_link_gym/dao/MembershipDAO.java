package com.iron_link_gym.dao;

import java.sql.*;
import java.util.*;
import com.iron_link_gym.db.DBConnection;
import com.iron_link_gym.model.Membership;

public class MembershipDAO {

    private static final String BASE_QUERY =
        "SELECT ms.subscription_id, ms.member_id, m.full_name," +
        "       ms.plan_id, p.plan_name," +
        "       NVL(ms.trainer_id, 0) AS trainer_id," +
        "       NVL(t.full_name, 'None') AS trainer_name," +
        "       ms.start_date, ms.end_date, ms.status " +
        "FROM MEMBERSHIPS ms " +
        "JOIN MEMBERS m  ON ms.member_id = m.member_id " +
        "JOIN PLANS   p  ON ms.plan_id   = p.plan_id " +
        "LEFT JOIN TRAINERS t ON ms.trainer_id = t.trainer_id ";

    private Membership mapRow(ResultSet rs) throws SQLException {
        return new Membership(
            rs.getInt("subscription_id"),
            rs.getInt("member_id"),    rs.getString("full_name"),
            rs.getInt("plan_id"),      rs.getString("plan_name"),
            rs.getInt("trainer_id"),   rs.getString("trainer_name"),
            rs.getDate("start_date"),  rs.getDate("end_date"),
            rs.getString("status")
        );
    }

    /** Auto-expire any memberships whose end_date has passed. */
    public void refreshExpired() throws SQLException {
        String sql = "UPDATE MEMBERSHIPS SET status='Expired' " +
                     "WHERE status='Active' AND end_date < TRUNC(SYSDATE)";
        try (Statement st = DBConnection.getConnection().createStatement()) {
            st.executeUpdate(sql);
        }
    }

    public List<Membership> getAllMemberships() throws SQLException {
        refreshExpired();
        List<Membership> list = new ArrayList<>();
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(BASE_QUERY + "ORDER BY ms.subscription_id DESC")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Membership> getMembershipsByStatus(String status) throws SQLException {
        refreshExpired();
        List<Membership> list = new ArrayList<>();
        String sql = BASE_QUERY + "WHERE ms.status = ? ORDER BY ms.subscription_id DESC";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Membership> getMembershipsByMember(int memberId) throws SQLException {
        refreshExpired();
        List<Membership> list = new ArrayList<>();
        String sql = BASE_QUERY + "WHERE ms.member_id = ? ORDER BY ms.start_date DESC";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    /** Returns the single active membership for a member, or null. */
    public Membership getActiveMembership(int memberId) throws SQLException {
        refreshExpired();
        String sql = BASE_QUERY + "WHERE ms.member_id = ? AND ms.status = 'Active'";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public void addMembership(Membership ms) throws SQLException {
        String sql = "INSERT INTO MEMBERSHIPS (member_id, plan_id, trainer_id, start_date, end_date, status) " +
                     "VALUES (?, ?, ?, ?, ?, 'Active')";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, ms.getMemberId());
            ps.setInt(2, ms.getPlanId());
            if (ms.getTrainerId() == 0) ps.setNull(3, Types.INTEGER);
            else                        ps.setInt(3, ms.getTrainerId());
            ps.setDate(4, ms.getStartDate());
            ps.setDate(5, ms.getEndDate());
            ps.executeUpdate();
        }
    }

    /** Renew: push end_date forward by plan's duration_days and re-activate. */
    public void renewMembership(int subscriptionId, Date newEndDate) throws SQLException {
        String sql = "UPDATE MEMBERSHIPS SET end_date=?, status='Active' WHERE subscription_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setDate(1, newEndDate);
            ps.setInt(2, subscriptionId);
            ps.executeUpdate();
        }
    }

    public void updateStatus(int subscriptionId, String status) throws SQLException {
        String sql = "UPDATE MEMBERSHIPS SET status=? WHERE subscription_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, subscriptionId);
            ps.executeUpdate();
        }
    }

    public void deleteMembership(int subscriptionId) throws SQLException {
        String sql = "DELETE FROM MEMBERSHIPS WHERE subscription_id=?";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, subscriptionId);
            ps.executeUpdate();
        }
    }
}
