package com.nessam.server.dataAccess;

import com.nessam.server.models.User;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class UserDAO {

    private final SessionFactory sessionFactory;

    public UserDAO() {
        // Create Hibernate SessionFactory
        this.sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public void saveUser(User user) {

        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteUser(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    public void deleteUser(String email) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, email);
            if (user != null) {
                session.delete(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteAllUsers() {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            List<User> userList = session.createQuery("FROM User", User.class).list();
            for (User user : userList) {
                session.delete(user);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    public void updateUser(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    public User getUserByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public User getUserByEmailAndPassword(String email, String password) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM User u WHERE u.email = :email AND u.password = :password";
            Query query = session.createQuery(hql, User.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            // Handle case where no user with given email and password found
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    public void close() {
        sessionFactory.close();
    }
}
