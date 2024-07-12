package com.nessam.server.models;

import jakarta.persistence.*;

@Entity
@Table(name = "Follow")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    @Column(name = "follower", nullable = false)
    private String follower;
    @Column(name = "followed", nullable = false)

    private String followed;

    public Follow(String follower, String followed) {
        this.follower = follower;
        this.followed = followed;
    }

    public Follow() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowed() {
        return followed;
    }

    public void setFollowed(String followed) {
        this.followed = followed;
    }

    @Override
    public String toString() {
        return "Follow{" +
                "id=" + id +
                ", follower='" + follower + '\'' +
                ", followed='" + followed + '\'' +
                '}';
    }
}
