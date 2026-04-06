package com.iron_link_gym.model;

public class Trainer {
    private int trainerId;
    private String name;
    private String phone;
    private String specialization;
    private String timings;

    public Trainer(int trainerId, String name, String phone, String specialization, String timings) {
        this.trainerId = trainerId;
        this.name = name;
        this.phone = phone;
        this.specialization = specialization;
        this.timings = timings;
    }

    public int getTrainerId() { return trainerId; }
    public String getFullName() { return name; }
    public String getPhone() { return phone; }
    public String getSpecialization() { return specialization; }
    public String getTimings() { return timings; }

    public void setTrainerId(int trainerId) { this.trainerId = trainerId; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void setTimings(String timings) { this.timings = timings; }

    @Override
    public String toString() {
        return name + " (" + specialization + ")";
    }
}