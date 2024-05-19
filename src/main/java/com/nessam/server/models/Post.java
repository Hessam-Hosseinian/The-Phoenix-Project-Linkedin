package com.nessam.server.models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "post")
public class Post {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "user_id", nullable = false)
   private String userId;

   @Column(name = "text", length = 3000, nullable = false)
   private String text;

   @Column(name = "likedBy")
   private Set<User> likedBy;

   public Post(Long id, String userId, String text, Set<User> likedBy, Set<Comment> comments) {
      this.id = id;
      this.userId = userId;
      this.text = text;
      this.likedBy = likedBy;
      this.comments = comments;
   }

   public Post() {

   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getUserId() {
      return userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }

   public String getText() {
      return text;
   }

   public void setText(String text) {
      this.text = text;
   }

   public Set<Comment> getComments() {
      return comments;
   }

   public void setComments(Set<Comment> comments) {
      this.comments = comments;
   }

   @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
   @JoinTable(
           name = "post_likes",
           joinColumns = @JoinColumn(name = "post_id"),
           inverseJoinColumns = @JoinColumn(name = "user_id")
   )



   public Set<User> getLikedBy() {
      return this.likedBy;
   }

   public void setLikedBy(Set<User> likedBy) {
      this.likedBy = likedBy;
   }
   @OneToMany(mappedBy = "post")
   private Set<Comment> comments;

}
