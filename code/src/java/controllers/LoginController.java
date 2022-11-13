/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.UserDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.primefaces.context.RequestContext;
import pojo.User;
import util.HibernateUtil;


/**
 *
 * @author user
 */
@SessionScoped
@ManagedBean(name="LoginController")
public class LoginController implements Serializable {
    
    private User user = new User();
    private final UserDAO userDAO = new UserDAO();    
    
    public String checkUser() {
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true); 
        httpSession.invalidate();
        String showMessage = "";
        
        User checkUser = userDAO.checkExists(user.getUsername());
        if (checkUser != null) {
            if (checkUser.getPassword().equals(user.getPassword())) {
                
                if (checkUser.isUserRegistered() == false) {
                    showMessage = "Your request for registration is not accepted yet.";
                    FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", showMessage);
                    RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
                    return "login";
                }

                facesContext = FacesContext.getCurrentInstance();
                httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);       
                httpSession.setAttribute("username", user.getUsername());              
                
                switch (checkUser.getUserType()) {
                    case 0: 
                        return "admin";
                    case 1:
                        return "creator";
                    default:
                        return "examinee";
                }
                
            } else {
                showMessage = "Wrong password!";
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", showMessage);
                RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
                return "login";
            }
        }
        else {
            showMessage = "There is no user with that username.";
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", showMessage);
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            return "login";
        }

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String changePassword() {
        return "changePassword";
    }
   
   
}
