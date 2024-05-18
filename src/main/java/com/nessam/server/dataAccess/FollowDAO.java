package com.nessam.server.dataAccess;

import com.nessam.server.models.Follow;
import com.nessam.server.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class FollowDAO {

    private final SessionFactory sessionFactory;

    public FollowDAO() {
        // Create Hibernate SessionFactory
        this.sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public void saveFollow(Follow follow) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(follow);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteFollow(Follow follow1) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Follow follow = session.get(Follow.class, follow1.getId());
            if (follow != null) {
                session.delete(follow);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


//    public void deleteAllFollows() {
//        Transaction transaction = null;
//        try (Session session = sessionFactory.openSession()) {
//            transaction = session.beginTransaction();
//            List<Follow> followList = session.createQuery("FROM Follow", Follow.class).list();
//            for (Follow follow : followList) {
//                session.delete(follow);
//            }
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            e.printStackTrace();
//        }
//    }
    public void deleteAllFollows() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM Follow").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    public List<Follow> getFollowsByFollower(String followerId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Follow WHERE follower = :followerId";
            Query<Follow> query = session.createQuery(hql, Follow.class);
            query.setParameter("followerId", followerId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Follow getFollowById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Follow.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<Follow> getFollowers(String userId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Follow WHERE followed = :userId";
            Query<Follow> query = session.createQuery(hql, Follow.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Follow> getAllFollows() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Follow", Follow.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isFollowing(String followerId, String followedId) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(*) FROM Follow WHERE follower = :followerId AND followed = :followedId";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("followerId", followerId);
            query.setParameter("followedId", followedId);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        sessionFactory.close();
    }
}
