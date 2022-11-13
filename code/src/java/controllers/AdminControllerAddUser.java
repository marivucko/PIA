/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.captcha.botdetect.web.jsf.JsfCaptcha;
import dao.UserDAO;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import pojo.User;

/**
 *
 * @author user
 */
@SessionScoped
@ManagedBean(name="AdminControllerAddUser")
public class AdminControllerAddUser implements Serializable {
    
    private final UserDAO userDAO = new UserDAO();
    private JsfCaptcha captcha;
    private String captchaCode = "";
    byte photo[];
    private String userType = "0";

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public JsfCaptcha getCaptcha() {
        return captcha;
    }

    public void setCaptcha(JsfCaptcha captcha) {
        this.captcha = captcha;
    }

    public String getCaptchaCode() {
        return captchaCode;
    }

    public void setCaptchaCode(String captchaCode) {
        this.captchaCode = captchaCode;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
     public void handleFileUpload(FileUploadEvent event) {
        photo = event.getFile().getContents();
    // ...
    }
      
    private UploadedFile file;
    
    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }
    
    User newUser = new User();
    String confirmPassword = "";

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public String addNewUser() {
        
        FacesMessage facesMessage;
        
        if (newUser.getPassword() == null ? confirmPassword != null : !newUser.getPassword().equals(confirmPassword)) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "Password and the confirmation of the password are not equal.");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            return "admin-addUser";
        }
        
        if (userDAO.usersWithEmail(newUser.getEmail()) == 2) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "The maximum number of the accounts with same email address is 2.");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            return "admin-addUser";
        }
        
        if (!this.captcha.validate(captchaCode)) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "Captha code is not valid. Please try again.");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            captchaCode = null;
            return "admin-addUser";
          }
        captchaCode = "";
        
        if(file!=null){
                photo=file.getContents();
        }else{
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "Profile picture is not uploaded!");
                RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
                return "admin-addUser";
        }
        if (photo.length == 0) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "Profile picture is not uploaded!");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            return "admin-addUser";
        }
          
        newUser.setPhoto(photo);
        newUser.setUserRegistered(true);
        if (userType.equals("0")) newUser.setUserType(0);
        else if (userType.equals("1")) newUser.setUserType(1);
        else newUser.setUserType(2);
        
        if (userDAO.insertUser(newUser)) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", "The user is inserted in the database.");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            newUser = new User();
        } 
        else {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "The user is not inserted in the database." + 
            newUser.getBirthday() + " " + newUser.getEmail() + " " + newUser.getJmbg() + " " + newUser.getName() + " " + newUser.getPassword() + " " +
                    newUser.getPhone() + " " + newUser.getSurname() + " " + newUser.getBirthday() + " " + ((newUser.getPhoto()==null) ? "null photo " : "postoji photo " + " ") + newUser.getUserType());
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
        }
        
        return "admin-addUser";
    }
    
    public String back() {
        return "admin";
    }
    
}
