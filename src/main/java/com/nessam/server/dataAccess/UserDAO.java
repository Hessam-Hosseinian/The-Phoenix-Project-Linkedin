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
        executeTransaction(session -> session.save(user));
    }

    public void deleteUser(Long id) {
        executeTransaction(session -> {
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
        });
    }

    public void deleteUserByEmail(String email) {
        executeTransaction(session -> {
            Query<User> query = session.createQuery("FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            User user = query.uniqueResult();
            if (user != null) {
                session.delete(user);
            }
        });
    }

    public void deleteAllUsers() {
        executeTransaction(session -> {
            List<User> userList = session.createQuery("FROM User", User.class).list();
            for (User user : userList) {
                session.delete(user);
            }
        });
    }

    public void updateUser(User user) {
        executeTransaction(session -> session.update(user));
    }

    public User getUserByEmail(String email) {
        return executeSession(session -> session.createQuery("FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .uniqueResult());
    }

    public User getUserByEmailAndPassword(String email, String password) {
        return executeSession(session -> {
            String hql = "FROM User u WHERE u.email = :email AND u.password = :password";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("email", email);
            query.setParameter("password", password);
            try {
                return query.getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        });
    }

    public List<User> getAllUsers() {
        return executeSession(session -> session.createQuery("FROM User", User.class).list());
    }

    public void close() {
        sessionFactory.close();
    }

    private void executeTransaction(TransactionAction action) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            action.execute(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    private <T> T executeSession(SessionFunction<T> function) {
        try (Session session = sessionFactory.openSession()) {
            return function.apply(session);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @FunctionalInterface
    private interface TransactionAction {
        void execute(Session session);
    }

    @FunctionalInterface
    private interface SessionFunction<T> {
        T apply(Session session);
    }
}
