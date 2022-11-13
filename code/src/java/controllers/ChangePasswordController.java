/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.UserDAO;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import pojo.User;
import org.primefaces.context.RequestContext;
import util.HibernateUtil;

/**
 *
 * @author user
 */
@SessionScoped
@ManagedBean(name="ChangePasswordController")
public class ChangePasswordController implements Serializable {
    
    private String currPass, newPass1, newPass2, message; 
    
    private final UserDAO userDAO = new UserDAO();
    
    public String changePassword() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);
        String showMessage = "";
        boolean changedPassword = false;
        User user = userDAO.checkExists((String) httpSession.getAttribute("username"));
        
        if (!currPass.equals(user.getPassword()))
            showMessage = "The password you typed in is not your current password. Try again";
        else if (!newPass1.equals(newPass2)) {
            showMessage = "New password and confim password are not equal. Please try again";
        }
        else {
            changedPassword = userDAO.changePassword(user.getUsername(), newPass1);
            if (changedPassword)
                showMessage = "Your password is changed.";
        }

        FacesMessage facesMessage;
        if (!changedPassword) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", showMessage);
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            return "changePassword";
        }
        else {    
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", showMessage);
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);   
            httpSession.invalidate();
            return "login";
        }
    }

    public String getCurrPass() {
        return currPass;
    }

    public void setCurrPass(String currPass) {
        this.currPass = currPass;
    }

    public String getNewPass1() {
        return newPass1;
    }

    public void setNewPass1(String newPass1) {
        this.newPass1 = newPass1;
    }

    public String getNewPass2() {
        return newPass2;
    }

    public void setNewPass2(String newPass2) {
        this.newPass2 = newPass2;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
