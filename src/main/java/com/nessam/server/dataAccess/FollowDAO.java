package com.nessam.server.dataAccess;

import com.nessam.server.models.Follow;
import com.nessam.server.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

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

    public Follow getFollowById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Follow.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        sessionFactory.close();
    }
}
