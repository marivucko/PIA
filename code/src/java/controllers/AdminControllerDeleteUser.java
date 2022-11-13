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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.validator.ValidatorException;
import org.primefaces.context.RequestContext;
import pojo.User;

/**
 *
 * @author user
 */
@SessionScoped
@ManagedBean(name="AdminControllerDeleteUser")
public class AdminControllerDeleteUser implements Serializable {
    
   public static UserDAO userDAO = new UserDAO();
   public final CompletedtsDAO completedtsDAO = new CompletedtsDAO();
   public final SaveStateDAO saveStateDAO = new SaveStateDAO();
   public final TsDAO tsDAO = new TsDAO();
   
   public String username = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String delete() {
       
        User user1 = userDAO.checkExists(username);
        if (user1 == null) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", "User with this username does not exist.");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            return "admin_deleteUser";
        } 
        else {
            completedtsDAO.deleteCompletedtsByUserUsername(user1.getUsername());
            saveStateDAO.deleteSaveStateByUserUsername(user1.getUsername());
            tsDAO.deleteTsByUserUsername(user1.getUsername());
            userDAO.deleteUser(user1);
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", "User is deleted.");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            return "admin_deleteUser";
        }
    }
    
    public String back() {
        return "admin";
    }
    
    
}
