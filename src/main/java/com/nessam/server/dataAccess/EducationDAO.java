package com.nessam.server.dataAccess;

import com.nessam.server.models.Education;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class EducationDAO {

    private final SessionFactory sessionFactory;

    public EducationDAO() {
        // Create Hibernate SessionFactory
        this.sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public void saveEducation(Education education) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(education);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteEducation(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Education education = session.get(Education.class, id);
            if (education != null) {
                session.delete(education);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateEducation(Education education) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(education);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Education getEducationById(String userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Education.class, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Education> getAllEducations() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Education", Education.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        sessionFactory.close();
    }
}
