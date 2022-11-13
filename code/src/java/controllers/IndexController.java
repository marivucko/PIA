/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

//ALTER TABLE answer CHANGE id id INT(11) NOT NULL AUTO_INCREMENT
//
//ALTER TABLE tablename AUTO_INCREMENT = 1
//
//<div class="datalist-noborder">



/**
 *
 * @author user
 */
@SessionScoped
@ManagedBean(name="IndexController")
public class IndexController implements Serializable {
    
    public String login() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);
        httpSession.invalidate();
        
        
        return "login";
    }
    
    public String register() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);
        httpSession.invalidate();
        
        return "register";
    }
    
    public String logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);
        httpSession.invalidate();
        
        return "index";
    }
    
    public String home() {
        return "index";
    }
    
}
