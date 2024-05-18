package com.nessam.server.models;

//import jakarta.persistence.*;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "education")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "school_name", length = 40, nullable = false)
    private String schoolName;

    @Column(name = "field_of_study", length = 40, nullable = false)
    private String fieldOfStudy;

    @Column(name = "education_start_date")
    private Date educationStartDate;

    @Column(name = "education_end_date")
    private Date educationEndDate;

    @Column(name = "grade", length = 40)
    private String grade;

    @Column(name = "activities_description", length = 500)
    private String activitiesDescription;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "skills", length = 40)
    private String skills;

    @Column(name = "notify_changes", nullable = false)
    private boolean notifyChanges;

    public Education() {
    }

    public Education(User user, String schoolName, String fieldOfStudy, Date educationStartDate, Date educationEndDate, String grade, String activitiesDescription, String description, String skills, boolean notifyChanges) {
        this.user = user;
        this.schoolName = schoolName;
        this.fieldOfStudy = fieldOfStudy;
        this.educationStartDate = educationStartDate;
        this.educationEndDate = educationEndDate;
        this.grade = grade;
        this.activitiesDescription = activitiesDescription;
        this.description = description;
        this.skills = skills;
        this.notifyChanges = notifyChanges;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Date getEducationStartDate() {
        return educationStartDate;
    }

    public void setEducationStartDate(Date educationStartDate) {
        this.educationStartDate = educationStartDate;
    }

    public Date getEducationEndDate() {
        return educationEndDate;
    }

    public void setEducationEndDate(Date educationEndDate) {
        this.educationEndDate = educationEndDate;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getActivitiesDescription() {
        return activitiesDescription;
    }

    public void setActivitiesDescription(String activitiesDescription) {
        this.activitiesDescription = activitiesDescription;
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

    public boolean isNotifyChanges() {
        return notifyChanges;
    }

    public void setNotifyChanges(boolean notifyChanges) {
        this.notifyChanges = notifyChanges;
    }
}
