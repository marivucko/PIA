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
import util.HibernateUtil;

/**
 *
 * @author user
 */
public class QuestionsInTsDAO {
    public boolean insertQuestionsInTS(Questionsints questionsints) {
        Transaction transaction=null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean b = false;
        try{
            transaction=session.beginTransaction();           
            session.save(questionsints);                
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
    
     public void deleteQuestionsInTsByTsId(int tsId) {
        Transaction transaction;
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Questionsints> questionsints;
        try{ 
            transaction = session.beginTransaction();           
            Query q = session.createQuery("From Questionsints q where q.tsId =:tsId");
            q.setParameter("tsId", tsId);
            questionsints = q.list();
            transaction.commit();
            
            for (int i = 0; i < questionsints.size(); i++) {
                Questionsints qts = questionsints.get(i);
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
     
       public ArrayList<Questionsints> getQuestionsForTS(int tsId) {
       Session session = HibernateUtil.getSessionFactory().openSession();
         ArrayList<Questionsints> questions = new ArrayList<>();
        try {
            session.beginTransaction();
            Query q = session.createQuery("From Questionsints q where q.tsId =:tsId");
            q.setParameter("tsId", tsId);
            questions = (ArrayList<Questionsints>) q.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return questions;
    }
       
       public ArrayList<Questionsints> getQuestionsForTSOrderByQuestionId(int tsId) {
       Session session = HibernateUtil.getSessionFactory().openSession();
         ArrayList<Questionsints> questions = new ArrayList<>();
        try {
            session.beginTransaction();
            Query q = session.createQuery("From Questionsints q where q.tsId =:tsId order by q.questionId");
            q.setParameter("tsId", tsId);
            questions = (ArrayList<Questionsints>) q.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return questions;
    }
}