package com.nessam.server.models;

import jakarta.persistence.*;

@Entity
@Table(name = "user_contact_info")
public class UserContactInfo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_link", length = 40)
    private String profileLink;

    @Column(name = "email", length = 40)
    private String email;

    @Column(name = "phone_number", length = 40)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "phone_type")
    private PhoneType phoneType;

    @Column(name = "address", length = 220)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "birth_month")
    private Month birthMonth;

    @Column(name = "birth_day")
    private int birthDay;

    @Enumerated(EnumType.STRING)
    @Column(name = "birth_display_policy")
    private DisplayPolicy birthDisplayPolicy;

    @Column(name = "instant_contact_method", length = 40)
    private String instantMessagingId;

    public enum PhoneType {
        MOBILE, HOME, WORK
    }

    public enum Month {
        JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
    }

    public enum BirthDisplayPolicy {
        ONLY_ME, MY_CONNECTIONS, MY_NETWORK, EVERYONE
    }

    public enum DisplayPolicy {
        PUBLIC, PRIVATE, FRIENDS_ONLY
    }

    public UserContactInfo() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Month getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(Month birthMonth) {
        this.birthMonth = birthMonth;
    }

    public int getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(int birthDay) {
        this.birthDay = birthDay;
    }

    public DisplayPolicy getBirthDisplayPolicy() {
        return birthDisplayPolicy;
    }

    public void setBirthDisplayPolicy(DisplayPolicy birthDisplayPolicy) {
        this.birthDisplayPolicy = birthDisplayPolicy;
    }

    public String getInstantMessagingId() {
        return instantMessagingId;
    }

    public void setInstantMessagingId(String instantMessagingId) {
        this.instantMessagingId = instantMessagingId;
    }

    @Override
    public String toString() {
        return "UserContactInfo{" + "profileLink='" + profileLink + '\'' + ", email='" + email + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", phoneType=" + phoneType + ", address='" + address + '\'' + ", birthMonth=" + birthMonth + ", birthDay=" + birthDay + ", birthDisplayPolicy=" + birthDisplayPolicy + ", instantMessagingId='" + instantMessagingId + '\'' + '}';
    }
}
