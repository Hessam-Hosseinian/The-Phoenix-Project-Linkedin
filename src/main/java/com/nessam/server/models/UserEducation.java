package com.nessam.server.models;

import jakarta.persistence.*;

@Entity
@Table(name = "user_education")
public class UserEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "school_name", length = 40, nullable = false)
    private String schoolName;

    @Column(name = "field_of_study", length = 40, nullable = false)
    private String fieldOfStudy;

    @Column(name = "start_date", nullable = false)
    private String startDate; // Using String for simplicity, ideally use LocalDate

    @Column(name = "end_date")
    private String endDate; // Using String for simplicity, ideally use LocalDate

    @Column(name = "grade", length = 40)
    private String grade;

    @Column(name = "activities", length = 500)
    private String activities;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "skill", length = 40)
    private String skills;

    @Column(name = "notify_network")
    private Boolean notifyNetwork;

    @Column(name = "user_email")
    private String user_email;

    public UserEducation() {
    }

    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public Boolean getNotifyNetwork() {
        return notifyNetwork;
    }

    public void setNotifyNetwork(Boolean notifyNetwork) {
        this.notifyNetwork = notifyNetwork;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}
