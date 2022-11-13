/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.CompletedtsDAO;
import dao.SaveStateDAO;
import dao.TsDAO;
import dao.UserDAO;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import pojo.User;

/**
 *
 * @author user
 */
@SessionScoped
@ManagedBean(name="AdminController")
public class AdminController implements Serializable {
    
    private ArrayList<User> users;
    
    public final UserDAO userDAO = new UserDAO();
    public final CompletedtsDAO completedtsDAO = new CompletedtsDAO();
    public final SaveStateDAO saveStateDAO = new SaveStateDAO();
    public final TsDAO tsDAO = new TsDAO();
    
    public AdminController() {
        users = userDAO.getAllUsers(); 
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public String addUser() {
        return "admin-addUser";
    }

    public String updateUser() {
      return "admin-updateUser";  
    }
      
    public String acceptRequest() {
        return "admin-acceptRequest";
    }
    
    public String addNewUser() {
        return "admin-addUser";
    }
    
    public String deleteUser() {
        return "admin-deleteUser";
    }
    
    public String accept(User u) {
        
        u.setUserRegistered(true);
        userDAO.updateUser(u);
        
        return "admin";
    }
    
    public String delete(User u) {
        completedtsDAO.deleteCompletedtsByUserUsername(u.getUsername());
        saveStateDAO.deleteSaveStateByUserUsername(u.getUsername());
        tsDAO.deleteTsByUserUsername(u.getUsername());
        userDAO.deleteUser(u);
        users = userDAO.getAllUsers();
        return "admin";
    }
    
    public String update(User u) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);       
        httpSession.setAttribute("usernameForUpdate", u.getUsername());   
        return "admin-updateUser";
    }
    
    
    
    public String refresh() {
        users = userDAO.getAllUsers();
        return "admin";
    }
}
