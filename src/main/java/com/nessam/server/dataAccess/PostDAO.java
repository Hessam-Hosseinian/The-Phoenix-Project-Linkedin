package com.nessam.server.dataAccess;

import com.mysql.cj.jdbc.DatabaseMetaData;
import com.nessam.server.models.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

public class PostDAO {
    private EntityManager entityManager;

    public PostDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void createPost(Post post) {
        entityManager.getTransaction().begin();
        entityManager.persist(post);
        entityManager.getTransaction().commit();
    }

    public Post findPost(Long id) {
        return entityManager.find(Post.class, id);
    }

    public List<Post> findAllPosts() {
        TypedQuery<Post> query = entityManager.createQuery("SELECT p FROM Post p", Post.class);
        return query.getResultList();
    }

    public void updatePost(Post post) {
        entityManager.getTransaction().begin();
        entityManager.merge(post);
        entityManager.getTransaction().commit();
    }

    public void deletePost(Post post) {
        entityManager.getTransaction().begin();
        entityManager.remove(post);
        entityManager.getTransaction().commit();
    }

    public List<Post> getPostsForUserFeed(Long userId) {
        List<Post> posts = new ArrayList<>();
        // SQL Query to select posts from followed users
        String sql = "SELECT p.* FROM posts p JOIN follows f ON p.authorId = f.followingId WHERE f.followerId = ?";
        DatabaseMetaData dataSource = null;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                posts.add(new Post(rs.getLong("id"), rs.getString("title"), rs.getString("content"), rs.getLong("authorId")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

}

