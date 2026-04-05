package com.iron_link_gym.model;

import java.sql.Date;

public class Member {
    private int memberId;
    private String name;
    private String phone;
    private String email;
    private Date dateOfJoining;
    private String medicalNotes;

    public Member(int memberId, String name, String phone, String email, Date dateOfJoining, String medicalNotes) {
        this.memberId = memberId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.dateOfJoining = dateOfJoining;
        this.medicalNotes = medicalNotes;
    }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public String getFullName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Date getDateJoining() { return dateOfJoining; }
    public void setDateOfJoining(Date dateOfJoining) { this.dateOfJoining = dateOfJoining; }

    public String getMedicalNotes() { return medicalNotes; }
    public void setMedicalNotes(String medicalNotes) { this.medicalNotes = medicalNotes; }

    @Override
    public String toString() {
        return name + " (" + phone + ")";
    }
}
