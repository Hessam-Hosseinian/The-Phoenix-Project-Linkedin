//package com.nessam.server.dataAccess;
//
//import com.nessam.server.models.CurrentJobPosition;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;
//import org.hibernate.cfg.Configuration;
//
//public class CurrentJobPositionDAO {
//
//    private SessionFactory sessionFactory;
//
//    public CurrentJobPositionDAO() {
//        sessionFactory = new Configuration().configure().buildSessionFactory();
//    }
//
//    public void saveCurrentJobPosition(CurrentJobPosition currentJobPosition) {
//        Session session = sessionFactory.openSession();
//        Transaction transaction = session.beginTransaction();
//        session.save(currentJobPosition);
//        transaction.commit();
//        session.close();
//    }
//}
////this is a test comment