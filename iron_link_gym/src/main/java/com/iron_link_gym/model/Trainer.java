package com.iron_link_gym.model;

public class Trainer {
    private int trainerId;
    private String name;
    private String phone;
    private String specialization;

    public Trainer(int trainerId, String name, String phone, String specialization) {
        this.trainerId = trainerId;
        this.name = name;
        this.phone = phone;
        this.specialization = specialization;
    }

    public int getTrainerId() { return trainerId; }
    public void setTrainerId(int trainerId) { this.trainerId = trainerId; }

    public String getFullName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    @Override
    public String toString() {
        return name + " (" + specialization + ")";
    }
}
