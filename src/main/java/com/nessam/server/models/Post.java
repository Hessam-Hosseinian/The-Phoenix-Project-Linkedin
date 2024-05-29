package com.nessam.server.models;

import jakarta.persistence.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_Id")
    private Long id;


    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, length = 5000)
    private String content;


    @Column(name = "file-path")
    private String filePath;

    @Column(name = "dateCreated")
    private String dateCreated;
    @Column(name = "author")
    private String author;

    @Column(name = "likes")
    private int likes;

    @Column(name = "dislikes")
    private int dislikes;


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();


    public Post(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        this.dateCreated = formatter.format(new Date());

        this.likes = 0;
        this.dislikes = 0;
    }


    public Post() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }


    public void likePost() {
        this.likes++;
    }

    public void dislikePost() {
        this.dislikes++;
    }

    public void onlikePost() {
        this.likes--;
    }

    public void ondislikePost() {
        this.dislikes--;
    }


}

