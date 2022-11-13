/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import pojo.Completedts;
import pojo.Questionsints;
import pojo.User;
import util.HibernateUtil;

/**
 *
 * @author user
 */
public class CompletedtsDAO {
     public List<Completedts> getCompltedtsByUser(String userUsername) {
         Session session = HibernateUtil.getSessionFactory().openSession();
         List<Completedts> completedts = new ArrayList<>();
        try {
            session.beginTransaction();
            Query q = session.createQuery("From Completedts c where c.userUsername =:userUsername");
            q.setParameter("userUsername", userUsername);
            completedts = q.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return completedts;
    }
     public Completedts getCompltedtsByUserAndTs(String userUsername, int tsId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Completedts completedts = null;
        try {
            session.beginTransaction();
            Query q = session.createQuery("From Completedts c where c.userUsername =:userUsername and c.tsId =:tsId");
            q.setParameter("userUsername", userUsername);
            q.setParameter("tsId", tsId);
            completedts = (Completedts) q.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return completedts;
    }
      public boolean insertCompletedts(Completedts completedts) {
        Transaction transaction;
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean b = false;
        try{
            transaction=session.beginTransaction();           
            session.save(completedts);                
            transaction.commit();
            b = true;
        }catch(Exception e){
                System.out.println(e);
        } 
        finally{
           session.close(); 
        }
        return b;
    }
      public List<Completedts> getCompltedtsWithSameTsId(int tsId) {
         Session session = HibernateUtil.getSessionFactory().openSession();
         List<Completedts> completedts = new ArrayList<>();
        try {
            session.beginTransaction();
            Query q = session.createQuery("From Completedts c where c.tsId =:tsId");
            q.setParameter("tsId", tsId);
            completedts = q.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return completedts;
    }
      
       public void deleteCompletedtsByUserUsername(String userUsername) {
        Transaction transaction;
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Completedts> completedts;
        try{ 
            transaction = session.beginTransaction();           
            Query q = session.createQuery("From Completedts q where q.userUsername =:userUsername");
            q.setParameter("userUsername", userUsername);
            completedts = q.list();
            transaction.commit();
            
            for (int i = 0; i < completedts.size(); i++) {
                Completedts qts = completedts.get(i);
                transaction = session.beginTransaction();           
                session.delete(qts);
                transaction.commit();
            }

        }catch(Exception e){
                System.out.println(e);
        } 
        finally{
           session.close(); 
        }
    }
       
    public void deleteCompltetedtsByTsId(int tsId) {
     Transaction transaction;
     Session session = HibernateUtil.getSessionFactory().openSession();
     List<Completedts> completedts;
     try{ 
         transaction = session.beginTransaction();           
         Query q = session.createQuery("From Completedts q where q.tsId =:tsId");
         q.setParameter("tsId", tsId);
         completedts = q.list();
         transaction.commit();

         for (int i = 0; i < completedts.size(); i++) {
             Completedts qts = completedts.get(i);
             transaction = session.beginTransaction();           
             session.delete(qts);
             transaction.commit();
         }

     }catch(Exception e){
             System.out.println(e);
     } 
     finally{
        session.close(); 
     }
    }
}
