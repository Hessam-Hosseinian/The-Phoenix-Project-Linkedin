package com.nessam.server.dataAccess;

import com.nessam.server.models.Comment;
import com.nessam.server.models.Post;
import com.nessam.server.models.User;
import com.nessam.server.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class PostDAO {

    public void savePost(Post post) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(post);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Post> getPosts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Post", Post.class).list();
        }
    }

    public Post getPostById(long postId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Post.class, postId);
        }
    }

    public void updatePost(Post post) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(post);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deletePost(long postId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Post post = session.get(Post.class, postId);
            if (post != null) {
                session.delete(post);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void addLikeToPost(long postId, long userId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Post post = session.get(Post.class, postId);
            User user = session.get(User.class, userId);
            if (post != null && user != null) {
                post.getLikedBy().add(user);
                session.saveOrUpdate(post);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void removeLikeFromPost(long postId, long userId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Post post = session.get(Post.class, postId);
            User user = session.get(User.class, userId);
            if (post != null && user != null) {
                post.getLikedBy().remove(user);
                session.saveOrUpdate(post);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void addCommentToPost(long postId, Comment comment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Post post = session.get(Post.class, postId);
            if (post != null) {
                comment.setPost(post);
                session.save(comment);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void removeCommentFromPost(long commentId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Comment comment = session.get(Comment.class, commentId);
            if (comment != null) {
                session.delete(comment);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
