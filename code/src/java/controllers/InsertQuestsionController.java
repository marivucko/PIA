/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.QuestionDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import pojo.Question;
import pojo.User;

/**
 *
 * @author user
 */
@SessionScoped
@ManagedBean(name="InsertQuestsionController")
public class InsertQuestsionController implements Serializable {
    
    private final QuestionDAO questionDAO = new QuestionDAO();
    
    public Question question = new Question();

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
    
    String questionText = "";
    
    public int answerType = 1; 

    public int getAnswerType() {
        return answerType;
    }

    public void setAnswerType(int answerType) {
        this.answerType = answerType;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    
    public String back() {
        return "creator";
    }
    
    public String addQuestion() {

        String message = "";
        if (questionDAO.insertQuestion(question)) {
            
            if (question.getType() == 3) {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn",  "Question is successfully added in database.");
                RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
                question = new Question();
                return "insertQuestion";
            } 
            else if (((question.getType() == 1) || (question.getType() == 2)) && (question.getNumOfSubquestions() == 0)) {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn",  "Question is successfully added in database.");
                RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
                question = new Question(); 
                return "insertQuestion";
            }      
            else {
                //FacesContext facesContext = FacesContext.getCurrentInstance();
                //HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true); 
                //httpSession.invalidate();

                FacesContext facesContext = FacesContext.getCurrentInstance();
                HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);       
                httpSession.setAttribute("questionId", question.getId());    
                return "insertAnswer";
            }
        }
        else {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "Question is not added in database.");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            return "insertQuestion";
        }
        
    }
    
    public String addAnswer() {
        return "insetQuestion";
    }
    
    public String refresh() {
        return "insertQuestion";
    }
}
