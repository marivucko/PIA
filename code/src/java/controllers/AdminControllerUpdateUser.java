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
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import pojo.User;

/**
 *
 * @author user
 */
@SessionScoped
@ManagedBean(name="AdminControllerUpdateUser")
public class AdminControllerUpdateUser  implements Serializable {
    
    public final UserDAO userDAO = new UserDAO();
    public User user = new User();
    private UploadedFile file;
    byte photo[];

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
     public void handleFileUpload(FileUploadEvent event) {
        photo = event.getFile().getContents();
    // ...
    } 
    
    public String back() {
        return "admin";
    }
    
    public String refresh() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);        
        user = userDAO.checkExists((String) httpSession.getAttribute("usernameForUpdate")); 
        return "admin";
    }
    
    public String update() {
        if (userDAO.updateUser(user)) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", "User is updated.");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);   
        }
        return "admin-updateUser";
    }
    
}
