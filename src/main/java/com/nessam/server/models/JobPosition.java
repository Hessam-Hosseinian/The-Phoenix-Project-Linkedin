

package com.nessam.server.models;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.Date;
import java.util.List;

public class JobPosition {



    public JobPosition(String userEmail, String title, EmploymentType employmentType, String companyName, String workLocation, WorkLocationType workLocationType, boolean currentlyWorking, String startDate, String endDate, String description, String skills, boolean notifyNetwork) {
    }

    public enum EmploymentType {
        FULL_TIME, PART_TIME, SELF_EMPLOYED, FREELANCE, CONTRACT, INTERNSHIP, PAID_INTERNSHIP, SEASONAL
    }

    public enum WorkLocationType {
        ONSITE, HYBRID, REMOTE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userEmail")
    private String userEmail;
    @Column(name = "title")
    private String title;
    @Column(name = "employment_type")// max 40 characters
    private EmploymentType employmentType;
    @Column(name = "companyName")
    private String companyName;
    @Column(name = "workLocation")// max 40 characters
    private String workLocation;// max 40 characters
    @Column(name = "workLocationType")
    private WorkLocationType workLocationType;
    @Column(name = "currentlyWorking")
    private boolean currentlyWorking;
    @Column(name = "startDate")
    private String startDate;
    @Column(name = "endDate")
    private String endDate;
    @Column(name = "description")
    private String description;
    @Column(name = "skills")// max 1000 characters
    private String skills;
    @Column(name = "notifyNetwork")// max 5 skills, each skill is a string
    private boolean notifyNetwork;


    public JobPosition() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWorkLocation() {
        return workLocation;
    }

    public void setWorkLocation(String workLocation) {
        this.workLocation = workLocation;
    }

    public WorkLocationType getWorkLocationType() {
        return workLocationType;
    }

    public void setWorkLocationType(WorkLocationType workLocationType) {
        this.workLocationType = workLocationType;
    }

    public boolean isCurrentlyWorking() {
        return currentlyWorking;
    }

    public void setCurrentlyWorking(boolean currentlyWorking) {
        this.currentlyWorking = currentlyWorking;
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

    public boolean isNotifyNetwork() {
        return notifyNetwork;
    }

    public void setNotifyNetwork(boolean notifyNetwork) {
        this.notifyNetwork = notifyNetwork;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "JobPosition{" +
                "id=" + id +
                ", userEmail='" + userEmail + '\'' +
                ", title='" + title + '\'' +
                ", employmentType=" + employmentType +
                ", companyName='" + companyName + '\'' +
                ", workLocation='" + workLocation + '\'' +
                ", workLocationType=" + workLocationType +
                ", currentlyWorking=" + currentlyWorking +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", description='" + description + '\'' +
                ", skills='" + skills + '\'' +
                ", notifyNetwork=" + notifyNetwork +
                '}';
    }
}