package com.nessam.server.models;

import jakarta.persistence.*;

@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String liker;

    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;

    public Like() {
    }

    public Like(Post post, String liker) {
        this.post = post;
        this.liker = liker;
    }

    public Long getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public String getLiker() {
        return liker;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setPost(Post post) {
        this.post = post;
    }

    public void setLiker(String liker) {
        this.liker = liker;
    }

    @Override
    public String toString() {
        return "Like{" +
                "id=" + id +
                ", post=" + post +
                ", liker='" + liker + '\'' +
                '}';
    }

}
