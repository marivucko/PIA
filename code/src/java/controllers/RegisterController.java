/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.captcha.botdetect.web.jsf.JsfCaptcha;
import dao.UserDAO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
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

@ManagedBean(name="RegisterController")
@SessionScoped
public class RegisterController implements Serializable {
    
    private JsfCaptcha captcha;
    private String captchaCode = "";
    byte photo[];
    
     
    private UploadedFile file;


    public String getCaptchaCode() {
        return captchaCode;
    }

    public void setCaptchaCode(String captchaCode) {
        this.captchaCode = captchaCode;
    }

    public JsfCaptcha getCaptcha() {
        return captcha;
    }

    public void setCaptcha(JsfCaptcha captcha) {
        this.captcha = captcha;
    }
    
    private final UserDAO userDAO = new UserDAO();
    
    private User newUser = new User();
    private String confirmPassword = "";
    
    
    public RegisterController() {
        newUser.setUsername("");
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
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
    
    public String sendRegisterRequest() {
        
        FacesMessage facesMessage;
        
        if (newUser.getPassword() == null ? confirmPassword != null : !newUser.getPassword().equals(confirmPassword)) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "Password and the confirmation of the password are not equal.");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            return "register";
        }
        
//        if (userDAO.usersWithJMBG(newUser.getJmbg()) == 1) {
//            facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "There is already a user with this JMBG.");
//            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
//            return "register";
//        }
        
        if (userDAO.usersWithEmail(newUser.getEmail()) == 2) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "The maximum number of the accounts with same email address is 2.");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            return "register";
        }
               
          if (!this.captcha.validate(captchaCode)) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "Invaid code for Captha. Please try again.");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            captchaCode = null;
            return "register";
          }
          captchaCode = "";

        
        //newUser.setUserType(2);
//        File inputFile = new File(filePath);
//        FileInputStream inputStream;
        if(file!=null){
                photo=file.getContents();
        }else{
                facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "Profile picture is not uploaded!");
                RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
                return "register";
        }
        if (photo.length == 0) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "Profile picture is not uploaded!");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            return "register";
        }
        
        newUser.setPhoto(photo);
        newUser.setUserRegistered(false);
        newUser.setUserType(2);

        

        if (userDAO.insertUser(newUser)) {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", "The request for registration is sent.");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            newUser = new User();
        } 
        else {
            facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "The request for registration is not sent.");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
        }

        return "register";
    }
    
      

    
 
}
