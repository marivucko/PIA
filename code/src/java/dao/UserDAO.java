/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import pojo.User;
import util.HibernateUtil;

/**
 *
 * @author user
 */
public class UserDAO {
    public User checkExists(String username) {
        Session session=null;
        Transaction transaction=null;
        User user=null;
        
        try{
            session=HibernateUtil.getSessionFactory().openSession();
            transaction=session.beginTransaction();
            
            Criteria query = session.createCriteria(User.class);
            user = (User) query.add(Restrictions.eq("username", username)).uniqueResult();
                
            transaction.commit();
            
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
           session.close(); 
        }
        return user;
    }
    
    public User checkExistsWithJMBG(String jmbg) {
        Session session=null;
        Transaction transaction=null;
        User user=null;
        
        try{
            session=HibernateUtil.getSessionFactory().openSession();
            transaction=session.beginTransaction();
            
            Criteria query = session.createCriteria(User.class);
            user = (User) query.add(Restrictions.eq("jmbg", jmbg)).uniqueResult();
                
            transaction.commit();
            
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
           session.close(); 
        }
        return user;
    }
    
    public boolean changePassword(String username, String newPassword) {
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);
        String showMessage = "";
        boolean changedPassword = false;
        User user=null;
 
        Session session = null;
        Transaction transaction = null;
        
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            Criteria query = session.createCriteria(User.class);
            user = (User) query.add(Restrictions.eq("username", username)).uniqueResult();
            transaction.commit();
            if (user != null)
            {                  
                user.setPassword(newPassword);
                transaction=session.beginTransaction();
                session.saveOrUpdate(user);
                transaction.commit();      
                changedPassword = true;
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
           session.close(); 
        }
        
        return changedPassword;
    }
    
    public boolean updateUser(User user) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);
        boolean updatedUser = false;
 
        Session session = null;
        Transaction transaction = null;
        
        try{
            session = HibernateUtil.getSessionFactory().openSession();
            
            if (user != null)
            {   
                transaction=session.beginTransaction();
                session.saveOrUpdate(user);
                transaction.commit();      
                updatedUser = true;
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
           session.close(); 
        }
        
        return updatedUser;
    }
    
    public int usersWithEmail(String email) {
        Session session=null;
        Transaction transaction=null;
        List<User> users = new ArrayList<User>();
        
        try{
            session=HibernateUtil.getSessionFactory().openSession();
            transaction=session.beginTransaction();
            
            Criteria query = session.createCriteria(User.class);
            users = (List<User>)(query.add(Restrictions.eq("email", email))).list();
                
            transaction.commit();
            
        }
        catch(Exception e){
            System.out.println(e);
        }
        finally{
           session.close(); 
        }
        return users.size();
    }
    
    
    
    public boolean insertUser(User user) {
        Transaction transaction=null;
        Session session=null;
        boolean b = false;
        try{
            session=HibernateUtil.getSessionFactory().openSession();
            transaction=session.beginTransaction();           
            session.save(user);                
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
    
    public ArrayList<User> getAllUsers() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        ArrayList<User> users = null;
        try {
            session.beginTransaction();
            Query query = session.createQuery("From User u");
            users = (ArrayList<User>) query.list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return users;
    }
    
    public void deleteUser(User user) {
        Transaction transaction=null;
        Session session=null;
        try{
            session=HibernateUtil.getSessionFactory().openSession();
            transaction=session.beginTransaction();           
            session.delete(user);                
            transaction.commit();

        }catch(Exception e){
                System.out.println(e);
        } 
        finally{
           session.close(); 
        }
    }
}


        
         

