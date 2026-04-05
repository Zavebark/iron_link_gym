package com.iron_link_gym.model;

import java.sql.Date;

public class Membership {
    private int subscriptionId;
    private int memberId;
    private String name;
    private int planId;
    private String planName;
    private int trainerId;
    private String trainerName;
    private Date startDate;
    private Date endDate;
    private String status;

    public Membership(  int subscriptionId,
                        int memberId,
                        String name,
                        int planId,
                        String planName,
                        int trainerId,
                        String trainerName,
                        Date startDate,
                        Date endDate,
                        String status) {
        this.subscriptionId = subscriptionId;
        this.memberId = memberId;
        this.name = name;
        this.planId = planId;
        this.planName = planName;
        this.trainerId = trainerId;
        this.trainerName = trainerName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public int getSubscriptionId() { return subscriptionId; }
    public void setSubscriptionId(int subscriptionId) { this.subscriptionId = subscriptionId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public String getMemberName() { return name; }
    public void setMemberName(String name) { this.name = name; }

    public int getPlanId() { return planId; }
    public void setPlanId(int planId) { this.planId = planId; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public int getTrainerId(){ return trainerId; }
    public void setTrainerId(int trainerId) {this.trainerId = trainerId; }

    public String getTrainerName() { return trainerName; }
    public void setTrainerName(String trainerName) { this.trainerName = trainerName; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    @Override
    public String toString() {
        return "Membership ID: " + subscriptionId;
    }
}
