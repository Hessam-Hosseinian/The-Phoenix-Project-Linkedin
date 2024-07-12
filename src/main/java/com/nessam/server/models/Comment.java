package com.nessam.server.models;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "content")
    private String content;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "author")
    private String author;
    @Column(name = "dateCreated")
    private String dateCreated;

    @Column(name = "postAuthor")
    private String postAuthor;
    @Column(name =  "postTitle")
    private String postTitle;
    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(String postAuthor) {
        this.postAuthor = postAuthor;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;

    }
}
