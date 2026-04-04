package com.iron_link_gym.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.iron_link_gym.db.DBConnection;
import com.iron_link_gym.model.Member;

public class MemberDAO {

    public List<Member> getAllMembers() throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM MEMBERS ORDER BY member_id";
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            members.add(new Member(
                rs.getInt("member_id"),
                rs.getString("full_name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getDate("date_joining"),
                rs.getString("medical_notes")
            ));
        }
        return members;
    }

    public List<Member> searchMembers(String keyword) throws SQLException {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM MEMBERS WHERE LOWER(full_name) LIKE ? OR phone LIKE ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword.toLowerCase() + "%");
        ps.setString(2, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            members.add(new Member(
                rs.getInt("member_id"),
                rs.getString("full_name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getDate("date_joining"),
                rs.getString("medical_notes")
            ));
        }
        return members;
    }

    public void addMember(Member m) throws SQLException {
        String sql = "INSERT INTO MEMBERS (full_name, phone, email, date_joining, medical_notes) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, m.getFullName());
        ps.setString(2, m.getPhone());
        ps.setString(3, m.getEmail());
        ps.setDate(4, m.getDateJoining());
        ps.setString(5, m.getMedicalNotes());
        ps.executeUpdate();
    }

    public void updateMember(Member m) throws SQLException {
        String sql = "UPDATE MEMBERS SET full_name=?, phone=?, email=?, medical_notes=? WHERE member_id=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, m.getFullName());
        ps.setString(2, m.getPhone());
        ps.setString(3, m.getEmail());
        ps.setString(4, m.getMedicalNotes());
        ps.setInt(5, m.getMemberId());
        ps.executeUpdate();
    }

    public void deleteMember(int memberId) throws SQLException {
        String sql = "DELETE FROM MEMBERS WHERE member_id=?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, memberId);
        ps.executeUpdate();
    }
}