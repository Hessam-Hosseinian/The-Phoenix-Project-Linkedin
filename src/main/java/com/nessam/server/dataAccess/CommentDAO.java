package com.nessam.server.dataAccess;


import com.nessam.server.models.Comment;
import java.util.List;
import jakarta.persistence.*;

public class CommentDAO {
    private EntityManager entityManager;

    public CommentDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void createComment(Comment comment) {
        entityManager.getTransaction().begin();
        entityManager.persist(comment);
        entityManager.getTransaction().commit();
    }

    public Comment findComment(Long id) {
        return entityManager.find(Comment.class, id);
    }

    public List<Comment> findAllComments() {
        TypedQuery<Comment> query = entityManager.createQuery("SELECT c FROM Comment c", Comment.class);
        return query.getResultList();
    }

    public void updateComment(Comment comment) {
        entityManager.getTransaction().begin();
        entityManager.merge(comment);
        entityManager.getTransaction().commit();
    }

    public void deleteComment(Comment comment) {
        entityManager.getTransaction().begin();
        entityManager.remove(comment);
        entityManager.getTransaction().commit();
    }
}

