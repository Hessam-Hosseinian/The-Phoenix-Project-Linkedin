package com.nessam.server.models;

//import jakarta.persistence.*;
import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "current_job_positions")
public class CurrentJobPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "job_title", length = 40)
    private String jobTitle;

    @Column(name = "employment_type")
    private int employmentType;

    @Column(name = "company_name", length = 40)
    private String companyName;

    @Column(name = "work_location", length = 40)
    private String workLocation;

    @Column(name = "workplace_type")
    private int workplaceType;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "skills", length = 40)
    private String skills;

    @Column(name = "notify_changes")
    private boolean notifyChanges;

    public CurrentJobPosition(Long id, User user, String jobTitle, int employmentType, String companyName, String workLocation, int workplaceType, boolean isActive, Date startDate, Date endDate, String description, String skills, boolean notifyChanges) {
        this.id = id;
        this.user = user;
        this.jobTitle = jobTitle;
        this.employmentType = employmentType;
        this.companyName = companyName;
        this.workLocation = workLocation;
        this.workplaceType = workplaceType;
        this.isActive = isActive;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.skills = skills;
        this.notifyChanges = notifyChanges;
    }

    public CurrentJobPosition() {

    }
// Getters and setters

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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public int getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(int employmentType) {
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

    public int getWorkplaceType() {
        return workplaceType;
    }

    public void setWorkplaceType(int workplaceType) {
        this.workplaceType = workplaceType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
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

    public boolean isNotifyChanges() {
        return notifyChanges;
    }

    public void setNotifyChanges(boolean notifyChanges) {
        this.notifyChanges = notifyChanges;
    }
}
