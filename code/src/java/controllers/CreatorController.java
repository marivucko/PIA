/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.QuestionDAO;
import dao.QuestionsInTsDAO;
import dao.TsDAO;
import dao.UserDAO;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import pojo.Question;
import pojo.Questionsints;
import pojo.Ts;
import pojo.User;

/**
 *
 * @author user
 */
@SessionScoped
@ManagedBean(name="CreatorController")
public class CreatorController implements Serializable {
    
    public final TsDAO tsDAO = new TsDAO();
    public final UserDAO userDAO = new UserDAO();
    public final QuestionDAO questionDAO = new QuestionDAO();
    public final QuestionsInTsDAO questionsInTsDAO = new QuestionsInTsDAO();
    
    private Ts ts = new Ts();
    private List<Question> questions = questionDAO.getAllQuestions();

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Ts getTs() {
        return ts;
    }

    public void setTs(Ts ts) {
        this.ts = ts;
    }

    public String onLoad() {
        ts = new Ts();
        return "creator-createTS";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    private int a = 0;
    private User user = new User();
    public String onloadCreator() {
        a++;
        if (a==1) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);    
            user = userDAO.checkExists((String) httpSession.getAttribute("username"));
        }
        return "creator";
    }
    
    public String back() {
       return "creator";
    } 
    
    public String backFromAddQuestions() {
       return "creator-createTS";
    } 
    
    public String createTS() {
        String s = "survey";
        if (ts.isTestSurvey()) 
        {
            s = "test"; ts.setIsPersonalised(true);
        }
        if (ts.getDateTimeStart().after(ts.getDateTimeEnd())) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", "The start of the " + s + " can not be after the end of the survey.");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            return "creator-createTS";
        }
        
        
          
            
        if (user != null)
            ts.setCreatorUsername(user.getUsername());
        else
            ts.setCreatorUsername("");
        tsDAO.insertTS(ts);
        
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", "The " + s + " is created successfully.");
        RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
   
        if (ts.isTestSurvey()) {
           questions = questionDAO.getQuestionsForTest();
        }
        else {
            questions = questionDAO.getAllQuestions();
        }
        
        return "creator-createTS-addQuestions";
    }
    
    public String addQuestion(Question q) {
        String s = "survey";
        if (ts.isTestSurvey()) 
            s = "test";
        Questionsints questionints = new Questionsints();
        questionints.setQuestionId(q.getId());
        questionints.setTsId(ts.getId());
        if (questionsInTsDAO.insertQuestionsInTS(questionints)) {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", "The question is added to the " + s + " successfully.");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            boolean remove = questions.remove(q);
        }
        else {
            FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", "The question is not added to the " + s + "!");
            RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
        }
        return "creator-createTS-addQuestions";
    }
    
    public String insetQuestion() {
        return "insertQuestion";
    }
    
    public String ts() {
        return "creator-createTS";
    }
    
    public String review() {
        return "creator-review";
    }
    
    public String doTestSurvey() {
        return "examinee";
    }
    
    public String backFromExaminee() {
       return "creator"; 
    }

}

