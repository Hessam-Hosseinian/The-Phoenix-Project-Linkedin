package com.nessam.server.models;

import java.sql.Date;

public class Education {
    private String UserID;
    private String schoolName;//40
    private String fieldOfStudy;//40
    private Date educationStartDate;
    private Date educationEndDate;
    private String grade;//40
    private String activitiesDescription;//500
    private String description;//1000
    private String skills;//40
    private boolean notifyChanges;

    public Education(String userID, String schoolName, String fieldOfStudy, Date educationStartDate, Date educationEndDate, String grade, String activitiesDescription, String description, String skills, boolean notifyChanges) {
        UserID = userID;
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

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
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
