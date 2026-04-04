package com.iron_link_gym.model;

import java.sql.Date;

public class Member {
    private int memberId;
    private String fullName;
    private String phone;
    private String email;
    private Date dateJoining;
    private String medicalNotes;

    // Constructor
    public Member(int memberId, String fullName, String phone, String email, Date dateJoining, String medicalNotes) {
        this.memberId = memberId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.dateJoining = dateJoining;
        this.medicalNotes = medicalNotes;
    }

    // Getters and Setters
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Date getDateJoining() { return dateJoining; }
    public void setDateJoining(Date dateJoining) { this.dateJoining = dateJoining; }

    public String getMedicalNotes() { return medicalNotes; }
    public void setMedicalNotes(String medicalNotes) { this.medicalNotes = medicalNotes; }

    @Override
    public String toString() {
        return fullName + " (" + phone + ")";
    }
}