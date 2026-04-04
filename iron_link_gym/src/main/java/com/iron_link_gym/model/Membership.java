package com.iron_link_gym.model;

import java.sql.Date;

public class Membership {
    private int subscriptionId;
    private int memberId;
    private String memberName;
    private int planId;
    private String planName;
    private Date startDate;
    private Date endDate;
    private String status;

    public Membership(int subscriptionId, int memberId, String memberName, int planId, String planName, Date startDate, Date endDate, String status) {
        this.subscriptionId = subscriptionId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.planId = planId;
        this.planName = planName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public int getSubscriptionId() { return subscriptionId; }
    public void setSubscriptionId(int subscriptionId) { this.subscriptionId = subscriptionId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public int getPlanId() { return planId; }
    public void setPlanId(int planId) { this.planId = planId; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return memberName + " - " + planName + " (" + status + ")";
    }
}