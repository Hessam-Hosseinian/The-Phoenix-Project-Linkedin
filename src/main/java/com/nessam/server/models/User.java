package com.nessam.server.models;


import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String ID;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "firstName")
    private String firstName;//20

    @Column(name = "lastName")
    private String lastName;//40

    @Column(name = "additionalName")
    private String additionalName;//40

    @Column(name = "profilePicture")
    private String profilePicture;//path

    @Column(name = "backgroundPicture")
    private String backgroundPicture;//path

    @Column(name = "title")
    private String title;//220

    @Column(name = "location")
    private String location;

    @Column(name = "profession")
    private String profession;

    @Column(name = "seekingOpportunity")
    private String seekingOpportunity;

    public User() {

    }

    public User(String email, String password, String firstName, String lastName, String additionalName, String profilePicture, String backgroundPicture, String title, String location, String profession, String seekingOpportunity) {

        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.additionalName = additionalName;
        this.profilePicture = profilePicture;
        this.backgroundPicture = backgroundPicture;
        this.title = title;
        this.location = location;
        this.profession = profession;
        this.seekingOpportunity = seekingOpportunity;
    }

    public User(String ID, String email, String password, String firstName, String lastName, String additionalName, String profilePicture, String backgroundPicture, String title, String location, String profession, String seekingOpportunity) {
        this.ID = ID;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.additionalName = additionalName;
        this.profilePicture = profilePicture;
        this.backgroundPicture = backgroundPicture;
        this.title = title;
        this.location = location;
        this.profession = profession;
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

    public String getSeekingOpportunity() {
        return seekingOpportunity;
    }

    public void setSeekingOpportunity(String seekingOpportunity) {
        this.seekingOpportunity = seekingOpportunity;
    }


}
