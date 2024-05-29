package com.nessam.server.models;

import jakarta.persistence.Column;


public class Like {
    @Column(name = "liker")
    private String liker;

    @Column(name = "liked")
    private String liked;

    public Like () {

    }


    public Like(String liker, String liked) {
        this.liker = liker;
        this.liked = liked;
    }

    public String getLiker() {
        return liker;
    }

    public void setLiker(String liker) {
        this.liker = liker;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    @Override
    public String toString() {
        return "Like{" +
                "liker='" + liker + '\'' +
                ", liked='" + liked + '\'' +
                '}';
    }

}
