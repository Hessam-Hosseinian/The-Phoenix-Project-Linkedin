package com.nessam.server.dataAccess;

import com.nessam.server.models.Education;
import com.nessam.server.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class EducationDAO {

    private final SessionFactory sessionFactory;

    public EducationDAO() {
        // Create Hibernate SessionFactory
        this.sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public void saveEducation(Education education) {
        executeTransaction(session -> session.save(education));
    }

    public void deleteEducation(Long id) {
        executeTransaction(session -> {
            Education education = session.get(Education.class, id);
            if (education != null) {
                session.delete(education);
            }
        });
    }

    public void updateEducation(Education education) {
        executeTransaction(session -> session.update(education));
    }

    public Education getEducationById(Long id) {
        return executeSession(session -> session.get(Education.class, id));
    }

    public List<Education> getAllEducations() {
        return executeSession(session -> session.createQuery("FROM Education", Education.class).list());
    }

    public List<Education> getEducationsByUser(User user) {
        return executeSession(session -> {
            String hql = "FROM Education e WHERE e.user = :user";
            return session.createQuery(hql, Education.class)
                    .setParameter("user", user)
                    .list();
        });
    }
    public List<Education> getEducationsByUserEmail(String userEmail) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Education e WHERE e.userEmail = :userEmail";
            Query<Education> query = session.createQuery(hql, Education.class);
            query.setParameter("userEmail", userEmail);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
