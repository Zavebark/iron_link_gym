package com.iron_link_gym.model;

public class Plan {
    private int planId;
    private String planName;
    private String planType;
    private int durationDays;
    private double price;
    private String isActive;

    // Constructor
    public Plan(int planId, String planName, String planType, int durationDays, double price, String isActive) {
        this.planId = planId;
        this.planName = planName;
        this.planType = planType;
        this.durationDays = durationDays;
        this.price = price;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getPlanId() { return planId; }
    public void setPlanId(int planId) { this.planId = planId; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public String getPlanType() { return planType; }
    public void setPlanType(String planType) { this.planType = planType; }

    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getIsActive() { return isActive; }
    public void setIsActive(String isActive) { this.isActive = isActive; }

    @Override
    public String toString() {
        return planName + " - ₹" + price + " (" + planType + ")";
    }
}