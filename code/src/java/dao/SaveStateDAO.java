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
import pojo.Question;
import pojo.Questionsints;
import pojo.Savestate;
import util.HibernateUtil;

/**
 *
 * @author user
 */
public class SaveStateDAO {
    public boolean insertSaveState(Savestate savestate) {
        Transaction transaction=null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean b = false;
        try{
            transaction=session.beginTransaction();           
            session.save(savestate);                
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
    public void deleteSaveState(int idSurvey, String userUsername) {
        Transaction transaction;
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Savestate> savestates;
        try{ 
            transaction = session.beginTransaction();           
            Query q = session.createQuery("From Savestate s where s.idSurvey =:idSurvey and s.userUsername =:userUsername");
            q.setParameter("idSurvey", idSurvey);
            q.setParameter("userUsername", userUsername);
            savestates = q.list();
            transaction.commit();
            
            for (int i = 0; i < savestates.size(); i++) {
                Savestate ss = savestates.get(i);
                transaction = session.beginTransaction();           
                session.delete(ss);
                transaction.commit();
            }

        }catch(Exception e){
                System.out.println(e);
        } 
        finally{
           session.close(); 
        }
    }
    public List<Savestate> getSaveState(int idSurvey, String userUsername) {
        Transaction transaction;
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Savestate> savestates = new ArrayList<>();
        try{ 
            transaction = session.beginTransaction();           
            Query q = session.createQuery("From Savestate s where s.idSurvey =:idSurvey and s.userUsername =:userUsername");
            q.setParameter("idSurvey", idSurvey);
            q.setParameter("userUsername", userUsername);
            savestates = q.list();
            transaction.commit();
        }catch(Exception e){
                System.out.println(e);
        } 
        finally{
           session.close(); 
        }
        return savestates;
    }
    
    public String getUserAnswer(int idQuestion, int idSurvey, int subQuestionNo, String userUsername) {
        Transaction transaction;
        Session session = HibernateUtil.getSessionFactory().openSession();
        String userAnswer = "";
        Savestate ss;
        try{ 
            transaction = session.beginTransaction();           
            Query q = session.createQuery("From Savestate s where s.idQuestion =:idQuestion and s.idSurvey =:idSurvey and s.subQuestionNo =:subQuestionNo and s.userUsername =:userUsername");
            q.setParameter("idQuestion", idQuestion);
            q.setParameter("idSurvey", idSurvey);
            q.setParameter("subQuestionNo", subQuestionNo);
            q.setParameter("userUsername", userUsername);
            ss = (Savestate) q.uniqueResult();
            if (ss != null)
                userAnswer = ss.getUserAnswer();
            transaction.commit();
        }catch(Exception e){
                System.out.println(e);
        } 
        finally{
           session.close(); 
        }
        return userAnswer;
    }
    
    public String getUserAnswerRadio(int idQuestion, int idSurvey, String userUsername) {
        Transaction transaction;
        Session session = HibernateUtil.getSessionFactory().openSession();
        String userAnswer = "";
        Savestate ss;
        try{ 
            transaction = session.beginTransaction();           
            Query q = session.createQuery("From Savestate s where s.idQuestion =:idQuestion and s.idSurvey =:idSurvey and s.userUsername =:userUsername");
            q.setParameter("idQuestion", idQuestion);
            q.setParameter("idSurvey", idSurvey);
            q.setParameter("userUsername", userUsername);
            ss = (Savestate) q.uniqueResult();
            if (ss != null)
                userAnswer = ss.getUserAnswer();
            transaction.commit();
        }catch(Exception e){
                System.out.println(e);
        } 
        finally{
           session.close(); 
        }
        return userAnswer;
    }
    
     public void deleteSaveStateByUserUsername(String userUsername) {
        Transaction transaction;
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Savestate> savestate;
        try{ 
            transaction = session.beginTransaction();           
            Query q = session.createQuery("From Savestate q where q.userUsername =:userUsername");
            q.setParameter("userUsername", userUsername);
            savestate = q.list();
            transaction.commit();
            
            for (int i = 0; i < savestate.size(); i++) {
                Savestate qts = savestate.get(i);
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
     
     public void deleteSaveStateByTsId(int idSurvey) {
     Transaction transaction;
     Session session = HibernateUtil.getSessionFactory().openSession();
     List<Savestate> savestate;
     try{ 
         transaction = session.beginTransaction();           
         Query q = session.createQuery("From Savestate q where q.idSurvey =:idSurvey");
         q.setParameter("idSurvey", idSurvey);
         savestate = q.list();
         transaction.commit();

         for (int i = 0; i < savestate.size(); i++) {
             Savestate qts = savestate.get(i);
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
