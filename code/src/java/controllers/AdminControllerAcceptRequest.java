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
import org.primefaces.context.RequestContext;
import pojo.User;

/**
 *
 * @author user
 */
@SessionScoped
@ManagedBean(name="AdminControllerAcceptRequest")
public class AdminControllerAcceptRequest  implements Serializable {
    
    public static UserDAO userDAO = new UserDAO();
    
    public String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String accept() {
        User user = userDAO.checkExists(username);
        if (user != null) {
            user.setUserRegistered(true);
            if (userDAO.updateUser(user)) {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", "User with username " + username + " is registered now.");
                RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            }
            else {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "User is not registered.");
                RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            }
        }
        return "admin-acceptRequest";
    }
    
    public String back() {
        return "admin";
    }
    
    
}
