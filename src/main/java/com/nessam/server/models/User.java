package com.nessam.server.models;

//import jakarta.persistence.*;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", length = 20)
    private String firstName;

    @Column(name = "last_name", length = 40)
    private String lastName;

    @Column(name = "additional_name", length = 40)
    private String additionalName;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "background_picture")
    private String backgroundPicture;

    @Column(name = "title", length = 220)
    private String title;

    @Column(name = "location")
    private String location;

    @Column(name = "profession")
    private String profession;

    @Column(name = "seeking_opportunity")
    private String seekingOpportunity;

//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Education> educationRecords = new HashSet<>();
//
//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    private ContactInformation contactInformation ;
//
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Post> posts = new HashSet<>();
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "user_likes_post",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "post_id")
//    )
//    private Set<Post> likedPosts = new HashSet<>();

    // Constructors, getters, and setters

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
//    public Set<Post> getPosts() {
//        return posts;
//    }
//
//    public void setPosts(Set<Post> posts) {
//        this.posts = posts;
//    }


}
