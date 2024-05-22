package com.nessam.server.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private Date dateCreated;

    public Comment() {
    }

    public Comment(String content, Post post, User author) {
        this.content = content;
        this.post = post;
        this.author = author;
        this.dateCreated = new Date();
    }

    // Getters and setters
}

