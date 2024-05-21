//package com.nessam.server.dataAccess;
//
//import com.nessam.server.models.ContactInformation;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.cfg.Configuration;
//
//import java.util.List;
//
//public class ContactInformationDAO {
//
//    private final SessionFactory sessionFactory;
//
//    public ContactInformationDAO() {
//        this.sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
//    }
//
//    public void saveContactInformation(ContactInformation contactInformation) {
//        Transaction transaction = null;
//        try (Session session = sessionFactory.openSession()) {
//            transaction = session.beginTransaction();
//            session.save(contactInformation);
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            e.printStackTrace();
//        }
//    }
//
//    public ContactInformation getContactInformationById(Long id) {
//        try (Session session = sessionFactory.openSession()) {
//            return session.get(ContactInformation.class, id);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public List<ContactInformation> getAllContactInformation() {
//        try (Session session = sessionFactory.openSession()) {
//            return session.createQuery("FROM ContactInformation", ContactInformation.class).list();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public void updateContactInformation(ContactInformation contactInformation) {
//        Transaction transaction = null;
//        try (Session session = sessionFactory.openSession()) {
//            transaction = session.beginTransaction();
//            session.update(contactInformation);
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            e.printStackTrace();
//        }
//    }
//
//    public void deleteContactInformation(Long id) {
//        Transaction transaction = null;
//        try (Session session = sessionFactory.openSession()) {
//            transaction = session.beginTransaction();
//            ContactInformation contactInformation = session.get(ContactInformation.class, id);
//            if (contactInformation != null) {
//                session.delete(contactInformation);
//            }
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            e.printStackTrace();
//        }
//    }
//
//    public void close() {
//        sessionFactory.close();
//    }
//}
