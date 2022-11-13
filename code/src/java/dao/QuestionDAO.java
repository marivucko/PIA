/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import pojo.Question;
import util.HibernateUtil;

/**
 *
 * @author user
 */
public class QuestionDAO {
     public boolean insertQuestion(Question question) {
        Transaction transaction=null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean b = false;
        try{
            transaction=session.beginTransaction();           
            session.save(question);                
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
     
    public Question returnQuestionWithId(int id) {
        Session session = null;
        Transaction transaction = null;
        Question question = null;
        
        try{
            session=HibernateUtil.getSessionFactory().openSession();
            transaction=session.beginTransaction();
            
            Criteria query = session.createCriteria(Question.class);
            question = (Question) query.add(Restrictions.eq("id", id)).uniqueResult();
                
            transaction.commit();
            
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
           session.close(); 
        }
        return question;
    }
    
    public ArrayList<Question> getAllQuestions() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ArrayList<Question> users = null;
        try {
            session.beginTransaction();
            Query query = session.createQuery("From Question q");
            users = (ArrayList<Question>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return users;
    }
    
     public List<Question> getQuestionsForTest() {
       Session session = HibernateUtil.getSessionFactory().openSession();
         List<Question> questions = new ArrayList<>();
        try {
            session.beginTransaction();
            Query qu = session.createQuery("From Question q where (q.type ='1' and q.numOfSubquestions ='0') or (q.type ='2' and q.numOfSubquestions ='0') or q.type ='4' or q.type ='5'");
            questions = qu.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return questions;
    }
}
