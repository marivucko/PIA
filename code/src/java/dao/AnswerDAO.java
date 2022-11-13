/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import pojo.Answer;
import pojo.Question;
import pojo.Ts;
import util.HibernateUtil;

/**
 *
 * @author user
 */
public class AnswerDAO {
     public boolean insertAnswer(Answer answer) {
        Transaction transaction=null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean b = false;
        try{
            transaction=session.beginTransaction();           
            session.save(answer);                
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
     
    public ArrayList<Answer> getAnswersbyQuestionId(int idQuestion) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ArrayList<Answer> answers = new ArrayList<>();
        try {
            session.beginTransaction();
            Query query = session.createQuery("From Answer a where a.idQuestion =:idQuestion");
            query.setParameter("idQuestion", idQuestion);
            answers = (ArrayList<Answer>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return answers;
    }

}
