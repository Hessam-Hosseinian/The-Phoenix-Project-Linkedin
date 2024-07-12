package com.nessam.server.models;

import jakarta.persistence.*;

import java.text.SimpleDateFormat;
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

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "dateCreated")
    private String dateCreated;

    @Column(name = "author")
    private String author;

    @OneToMany(mappedBy = "likes", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;



    public Post(String title, String content, String author, String filePath) {
        this.title = title;
        this.content = content;
        this.author = author;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        this.dateCreated = formatter.format(new Date());
        this.filePath = filePath;
    }

    public Post(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        this.dateCreated = formatter.format(new Date());
        //without post image
    }


    public Post() {
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", filePath='" + filePath + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", author='" + author + '\'' +
                ", likes=" + likes +
                '}';
    }
}

