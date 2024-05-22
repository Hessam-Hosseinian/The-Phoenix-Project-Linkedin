package com.nessam.server.models;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "contact_information")
public class ContactInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "profile_link", length = 40)
    private String profileLink;

    @Column(name = "email", length = 40)
    private String email;

    @Column(name = "phone_number", length = 40)
    private String phoneNumber;

    @Column(name = "phone_type")
    private int phoneType;

    @Column(name = "address", length = 220)
    private String address;

    @Column(name = "birth_month")
    private Date birthMonth;

    @Column(name = "birth_day")
    private Date birthDay;

    @Column(name = "birth_privacy_policy")
    private int birthPrivacyPolicy;

    @Column(name = "instant_contact_method", length = 40)
    private String instantContactMethod;

    public ContactInformation() {
    }

    public ContactInformation(User user, String profileLink, String email, String phoneNumber, int phoneType, String address, Date birthMonth, Date birthDay, int birthPrivacyPolicy, String instantContactMethod) {
        this.user = user;
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

    // Getters and setters...

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
