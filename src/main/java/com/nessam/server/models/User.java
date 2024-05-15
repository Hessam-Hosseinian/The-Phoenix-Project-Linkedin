package com.nessam.server.models;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;


public class User {


    @JsonProperty("ID")
    private String ID;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("firstName")
    private String firstName;//20

    @JsonProperty("lastName")
    private String lastName;//40

    @JsonProperty("additionalName")
    private String additionalName;//40

    @JsonProperty("profilePicture")
    private String profilePicture;//path

    @JsonProperty("backgroundPicture")
    private String backgroundPicture;//path

    @JsonProperty("title")
    private String title;//220

    @JsonProperty("currentJobPosition")
    private CurrentJobPosition currentJobPosition;

    @JsonProperty("education")
    private Education education;

    @JsonProperty("location")
    private String location;

    @JsonProperty("profession")
    private String profession;

    @JsonProperty("contactInformation")
    private ContactInformation contactInformation;

    @JsonProperty("seekingOpportunity")
    private String seekingOpportunity;

    public User() {

    }

    public User(String ID, String email, String password, String firstName, String lastName, String additionalName, String profilePicture, String backgroundPicture, String title, CurrentJobPosition currentJobPosition, Education education, String location, String profession, ContactInformation contactInformation, String seekingOpportunity) {
        this.ID = ID;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.additionalName = additionalName;
        this.profilePicture = profilePicture;
        this.backgroundPicture = backgroundPicture;
        this.title = title;
        this.currentJobPosition = currentJobPosition;
        this.education = education;
        this.location = location;
        this.profession = profession;
        this.contactInformation = contactInformation;
        this.seekingOpportunity = seekingOpportunity;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAdditionalName() {
        return additionalName;
    }

    public void setAdditionalName(String additionalName) {
        this.additionalName = additionalName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBackgroundPicture() {
        return backgroundPicture;
    }

    public void setBackgroundPicture(String backgroundPicture) {
        this.backgroundPicture = backgroundPicture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CurrentJobPosition getCurrentJobPosition() {
        return currentJobPosition;
    }

    public void setCurrentJobPosition(CurrentJobPosition currentJobPosition) {
        this.currentJobPosition = currentJobPosition;
    }

    public Education getEducation() {
        return education;
    }

    public void setEducation(Education education) {
        this.education = education;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public ContactInformation getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(ContactInformation contactInformation) {
        this.contactInformation = contactInformation;
    }

    public String getSeekingOpportunity() {
        return seekingOpportunity;
    }

    public void setSeekingOpportunity(String seekingOpportunity) {
        this.seekingOpportunity = seekingOpportunity;
    }

    public static class CurrentJobPosition {
        @JsonProperty("userID")
        private String userID;
        @JsonProperty("jobTitle")
        private String jobTitle;//40
        @JsonProperty("employmentType")
        private int employmentType;//1-8
        @JsonProperty("companyName")
        private String companyName;//40
        @JsonProperty("workLocation")
        private String workLocation;//40
        @JsonProperty("workplaceType")
        private int workplaceType;//1-3
        @JsonProperty("isActive")
        private boolean isActive;
        @JsonProperty("startDate")
        private Date startDate;
        @JsonProperty("endDate")
        private Date endDate;
        @JsonProperty("description")
        private String description;//1000
        @JsonProperty("skills")
        private String skills;//40
        @JsonProperty("notifyChanges")
        private boolean notifyChanges;


        public CurrentJobPosition(String userID, String jobTitle, int employmentType, String companyName, String workLocation, int workplaceType, boolean isActive, Date startDate, Date endDate, String description, String skills, boolean notifyChanges) {
            this.userID = userID;
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

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userId) {
            this.userID = userId;
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

    public class ContactInformation {

        private String profileLink;//40
        private String email;//40
        private String phoneNumber;//40
        private int phoneType;//1-3
        private String address;//220
        private Date birthMonth;
        private Date birthDay;
        private int birthPrivacyPolicy;//1-5
        private String instantContactMethod; //40

        public ContactInformation(String profileLink, String email, String phoneNumber, int phoneType, String address, Date birthMonth, Date birthDay, int birthPrivacyPolicy, String instantContactMethod) {
            this.profileLink = profileLink;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.phoneType = phoneType;
            this.address = address;
            this.birthMonth = birthMonth;
            this.birthDay = birthDay;
            this.birthPrivacyPolicy = birthPrivacyPolicy;
            this.instantContactMethod = instantContactMethod;
        }

        public String getProfileLink() {
            return profileLink;
        }

        public void setProfileLink(String profileLink) {
            this.profileLink = profileLink;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public int getPhoneType() {
            return phoneType;
        }

        public void setPhoneType(int phoneType) {
            this.phoneType = phoneType;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Date getBirthMonth() {
            return birthMonth;
        }

        public void setBirthMonth(Date birthMonth) {
            this.birthMonth = birthMonth;
        }

        public Date getBirthDay() {
            return birthDay;
        }

        public void setBirthDay(Date birthDay) {
            this.birthDay = birthDay;
        }

        public int getBirthPrivacyPolicy() {
            return birthPrivacyPolicy;
        }

        public void setBirthPrivacyPolicy(int birthPrivacyPolicy) {
            this.birthPrivacyPolicy = birthPrivacyPolicy;
        }

        public String getInstantContactMethod() {
            return instantContactMethod;
        }

        public void setInstantContactMethod(String instantContactMethod) {
            this.instantContactMethod = instantContactMethod;
        }
    }
}
