package com.iron_link_gym.model;

import java.sql.Timestamp;
import java.sql.Date;

public class Attendance {
    private int attendanceId;
    private int memberId;
    String fullname;
    private Date date;
    private Timestamp checkInTime;
    private Timestamp checkOutTime;

    public Attendance(int attendanceId, int memberId, String fullname, Date date, Timestamp checkInTime, Timestamp checkOutTime) {
        this.attendanceId = attendanceId;
        this.memberId = memberId;
        this.date = date;
        this.fullname = fullname;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
    }

    public int getAttendanceId() { return attendanceId; }
    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getFullName(){ return fullname; }
    public void setFullName(String fullname) { this.fullname = fullname; }

    public Timestamp getCheckInTime() { return checkInTime; }
    public void setCheckInTime(Timestamp checkInTime) { this.checkInTime = checkInTime; }

    public Timestamp getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(Timestamp checkOutTime) { this.checkOutTime = checkOutTime; }

    public String getCheckinDisplay(){
        return "Member ID: "+this.getMemberId() + " - Check In At: "+this.getCheckInTime();
    }

    public String getCheckoutDisplay(){
        return "Member ID: "+this.getMemberId() + " - Check Out At: "+this.getCheckOutTime();
    }

    @Override
    public String toString() {
        return "Member ID: " + memberId + " - " + date;
    }
}
