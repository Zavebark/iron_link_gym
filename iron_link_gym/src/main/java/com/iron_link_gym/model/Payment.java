package com.iron_link_gym.model;

import java.sql.Date;

public class Payment {
    private int paymentId;
    private int membershipId;
    private double amount;
    private Date paymentDate;
    private String paymentMethod;

    public Payment(int paymentId, int membershipId, double amount, Date paymentDate, String paymentMethod) {
        this.paymentId = paymentId;
        this.membershipId = membershipId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getMembershipId() { return membershipId; }
    public void setMembershipId(int membershipId) { this.membershipId = membershipId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    @Override
    public String toString() {
        return "Payment: ₹" + amount + " on " + paymentDate;
    }
}
