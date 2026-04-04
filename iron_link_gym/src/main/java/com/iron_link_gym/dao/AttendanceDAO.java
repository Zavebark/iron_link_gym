package com.iron_link_gym.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.iron_link_gym.db.DBConnection;
import com.iron_link_gym.model.Attendance;

public class AttendanceDAO {

    public List<Attendance> getAllAttendance() throws SQLException {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT a.attendance_id, a.member_id, m.full_name, a.checkin_time " +
                     "FROM ATTENDANCE a JOIN MEMBERS m ON a.member_id = m.member_id " +
                     "ORDER BY a.checkin_time DESC";
        Connection conn = DBConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            list.add(new Attendance(
                rs.getInt("attendance_id"),
                rs.getInt("member_id"),
                rs.getString("full_name"),
                rs.getTimestamp("checkin_time")
            ));
        }
        return list;
    }

    public List<Attendance> getAttendanceByMember(int memberId) throws SQLException {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT a.attendance_id, a.member_id, m.full_name, a.checkin_time " +
                     "FROM ATTENDANCE a JOIN MEMBERS m ON a.member_id = m.member_id " +
                     "WHERE a.member_id = ? ORDER BY a.checkin_time DESC";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, memberId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new Attendance(
                rs.getInt("attendance_id"),
                rs.getInt("member_id"),
                rs.getString("full_name"),
                rs.getTimestamp("checkin_time")
            ));
        }
        return list;
    }

    public void checkin(int memberId) throws SQLException {
        String sql = "INSERT INTO ATTENDANCE (member_id) VALUES (?)";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, memberId);
        ps.executeUpdate();
    }
}