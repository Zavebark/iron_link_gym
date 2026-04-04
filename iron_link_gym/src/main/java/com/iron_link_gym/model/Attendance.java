package com.iron_link_gym.model;

import java.sql.Timestamp;

public class Attendance {
    private int attendanceId;
    private int memberId;
    private String memberName;
    private Timestamp checkinTime;

    public Attendance(int attendanceId, int memberId, String memberName, Timestamp checkinTime) {
        this.attendanceId = attendanceId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.checkinTime = checkinTime;
    }

    public int getAttendanceId() { return attendanceId; }
    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public Timestamp getCheckinTime() { return checkinTime; }
    public void setCheckinTime(Timestamp checkinTime) { this.checkinTime = checkinTime; }

    @Override
    public String toString() {
        return memberName + " - " + checkinTime.toString();
    }
}