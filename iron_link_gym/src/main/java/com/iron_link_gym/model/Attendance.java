package com.iron_link_gym.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Attendance {
    private int attendanceId;
    private int memberId;
    private String memberName;
    private Date checkinDate;
    private Timestamp checkinTime;
    private Timestamp checkoutTime;

    public Attendance(int attendanceId, int memberId, String memberName,
                      Date checkinDate, Timestamp checkinTime, Timestamp checkoutTime) {
        this.attendanceId = attendanceId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.checkinDate = checkinDate;
        this.checkinTime = checkinTime;
        this.checkoutTime = checkoutTime;
    }

    public int getAttendanceId() { return attendanceId; }
    public int getMemberId() { return memberId; }
    public String getMemberName() { return memberName; }
    public String getFullName() { return memberName; }
    public Date getCheckinDate() { return checkinDate; }
    public Timestamp getCheckinTime() { return checkinTime; }
    public Timestamp getCheckoutTime() { return checkoutTime; }

    public String getCheckinDisplay() {
        return checkinTime != null ? checkinTime.toLocalDateTime()
            .toLocalTime().toString().substring(0, 5) : "—";
    }

    public String getCheckoutDisplay() {
        return checkoutTime != null ? checkoutTime.toLocalDateTime()
            .toLocalTime().toString().substring(0, 5) : "Not checked out";
    }

    @Override
    public String toString() {
        return memberName + " - " + checkinDate;
    }
}