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
import pojo.Questionsints;
import pojo.Savestate;
import pojo.Ts;
import pojo.User;
import util.HibernateUtil;

/**
 *
 * @author user
 */
public class TsDAO {
    public boolean insertTS(Ts ts) {
        Transaction transaction=null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean b = false;
        try{
            transaction=session.beginTransaction();           
            session.save(ts);                
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
    
    public Ts getTSbyId(int id) {
       Session session = HibernateUtil.getSessionFactory().openSession();
        Ts ts = null;
        try {
            session.beginTransaction();
            Query q = session.createQuery("From Ts ts where ts.id =:id");
            q.setParameter("id", id);
            ts = (Ts) q.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return ts;
    }
    
    public ArrayList<Ts> getAllTs() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ArrayList<Ts> ts = null;
        try {
            session.beginTransaction();
            Query query = session.createQuery("From Ts ts");
            ts = (ArrayList<Ts>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return ts;
    }
    
    public ArrayList<Ts> getAllTsOrderByName(String ascdesc) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ArrayList<Ts> ts = null;
        try {
            session.beginTransaction();
            Query query = session.createQuery("From Ts ts order by ts.name " + ascdesc);
            ts = (ArrayList<Ts>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return ts;
    }
    
    public ArrayList<Ts> getAllTsOrderByStart(String ascdesc) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ArrayList<Ts> ts = null;
        try {
            session.beginTransaction();
            Query query = session.createQuery("From Ts ts order by ts.dateTimeStart " + ascdesc);
            ts = (ArrayList<Ts>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return ts;
    }
    
     public ArrayList<Ts> getAllTsOrderByEnd(String ascdesc) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ArrayList<Ts> ts = null;
        try {
            session.beginTransaction();
            Query query = session.createQuery("From Ts ts order by ts.dateTimeEnd " + ascdesc);
            ts = (ArrayList<Ts>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return ts;
    }
     
    public void deleteTs(Ts ts) {
        Transaction transaction=null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            transaction=session.beginTransaction();           
            session.delete(ts);                
            transaction.commit();
        }catch(Exception e){
        } 
        finally{
           session.close(); 
        }
    }
    
    
    public Ts getbyTsIdAndCreator(int id, String creatorUsername) {
       Session session = HibernateUtil.getSessionFactory().openSession();
        Ts ts = null;
        try {
            session.beginTransaction();
            Query q = session.createQuery("From Ts ts where ts.id =:id and ts.creatorUsername =:creatorUsername");
            q.setParameter("id", id);
            q.setParameter("creatorUsername", creatorUsername);
            ts = (Ts) q.uniqueResult();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return ts;
    }
    
     public void deleteTsByUserUsername(String creatorUsername) {
        Transaction transaction;
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Ts> ts;
        try{ 
            transaction = session.beginTransaction();           
            Query q = session.createQuery("From Ts q where q.creatorUsername =:creatorUsername");
            q.setParameter("creatorUsername", creatorUsername);
            ts = q.list();
            transaction.commit();
            
            for (int i = 0; i < ts.size(); i++) {
                Ts qts = ts.get(i);
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
