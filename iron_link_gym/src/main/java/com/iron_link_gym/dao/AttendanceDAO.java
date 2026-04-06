package com.iron_link_gym.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.iron_link_gym.db.DBConnection;
import com.iron_link_gym.model.Attendance;

public class AttendanceDAO {

    private Attendance mapRow(ResultSet rs) throws SQLException {
        return new Attendance(
            rs.getInt("attendance_id"),
            rs.getInt("member_id"),
            rs.getString("full_name"),
            rs.getDate("checkin_date"),
            rs.getTimestamp("checkin_time"),
            rs.getTimestamp("checkout_time")
        );
    }

    public List<Attendance> getAllAttendance() throws SQLException {
        List<Attendance> list = new ArrayList<>();
        String sql =
            "SELECT a.attendance_id, a.member_id, m.full_name, " +
            "       a.checkin_date, a.checkin_time, a.checkout_time " +
            "FROM ATTENDANCE a JOIN MEMBERS m ON a.member_id = m.member_id " +
            "ORDER BY a.checkin_time DESC";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    /** Today's attendance only. */
    public List<Attendance> getTodayAttendance() throws SQLException {
        List<Attendance> list = new ArrayList<>();
        String sql =
            "SELECT a.attendance_id, a.member_id, m.full_name, " +
            "       a.checkin_date, a.checkin_time, a.checkout_time " +
            "FROM ATTENDANCE a JOIN MEMBERS m ON a.member_id = m.member_id " +
            "WHERE TRUNC(a.checkin_date) = TRUNC(SYSDATE) " +
            "ORDER BY a.checkin_time DESC";
        try (Statement st = DBConnection.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Attendance> getAttendanceByMember(int memberId) throws SQLException {
        List<Attendance> list = new ArrayList<>();
        String sql =
            "SELECT a.attendance_id, a.member_id, m.full_name, " +
            "       a.checkin_date, a.checkin_time, a.checkout_time " +
            "FROM ATTENDANCE a JOIN MEMBERS m ON a.member_id = m.member_id " +
            "WHERE a.member_id = ? ORDER BY a.checkin_time DESC";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public boolean checkIn(int memberId) throws SQLException {
        String sql = "{call check_in_member(?)}";

        try (var conn = DBConnection.getConnection();
             var cs = conn.prepareCall(sql)) {

            cs.setInt(1, memberId);
            cs.execute();
            return true;
        }
    }

    /**
     * Mark checkout for today's open attendance record of a member.
     * Returns true if updated, false if no open record found.
     */
    public boolean checkOut(int memberId) throws SQLException {
        String sql =
            "UPDATE ATTENDANCE SET checkout_time = SYSTIMESTAMP " +
            "WHERE member_id = ? AND TRUNC(checkin_date) = TRUNC(SYSDATE) " +
            "  AND checkout_time IS NULL";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, memberId);
            return ps.executeUpdate() > 0;
        }
    }

    /** True if the member has an open (no checkout) record for today. */
    public boolean isCheckedInToday(int memberId) throws SQLException {
        String sql =
            "SELECT COUNT(*) FROM ATTENDANCE " +
            "WHERE member_id = ? AND TRUNC(checkin_date) = TRUNC(SYSDATE) " +
            "  AND checkout_time IS NULL";
        try (PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }
}
