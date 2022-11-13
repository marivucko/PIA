/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.CompletedtsDAO;
import dao.QuestionDAO;
import dao.QuestionsInTsDAO;
import dao.TsDAO;
import dao.UserDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import pojo.Completedts;
import pojo.Question;
import pojo.Questionsints;
import pojo.Ts;
import pojo.User;

/**
 *
 * @author user
 */
@SessionScoped
@ManagedBean(name="ExamineeController")
public class ExamineeController implements Serializable {
    
    private Ts currentTest;
    private Ts currentSurvey;
    private List<Questionsints> currentQuestionsInTS;
    private List<Question> currentQuestions;
    
    private User user = new User();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    private Completedts completedts;

    public Completedts getCompletedts() {
        return completedts;
    }

    public void setCompletedts(Completedts completedts) {
        this.completedts = completedts;
    }

    public double getPerCent() {
        return perCent;
    }

    public void setPerCent(double perCent) {
        this.perCent = perCent;
    }
    private double perCent;

    public List<Question> getCurrentQuestions() {
        return currentQuestions;
    }

    public void setCurrentQuestions(List<Question> currentQuestions) {
        this.currentQuestions = currentQuestions;
    }
    
    public Ts getCurrentTest() {
        return currentTest;
    }

    public void setCurrentTest(Ts currentTest) {
        this.currentTest = currentTest;
    }

    private final TsDAO tsDAO = new TsDAO();
    private final UserDAO userDAO = new UserDAO();
    private final CompletedtsDAO completedtsDAO = new CompletedtsDAO();
    private final QuestionsInTsDAO questionsInTsDAO = new QuestionsInTsDAO();
    private final QuestionDAO questionDAO = new QuestionDAO();
    
    private List<Ts> ts = tsDAO.getAllTs();

    private List<Integer> completedtsIDs = new ArrayList<>();
    private String userUsername = "";

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public List<Integer> getCompletedtsIDs() {
        return completedtsIDs;
    }

    public void setCompletedtsIDs(List<Integer> completedtsIDs) {
        this.completedtsIDs = completedtsIDs;
    }
    
    public String onload() {
        //ts = tsDAO.getAllTs();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);
        String userUsername1 = (String) httpSession.getAttribute("username");
        //if (!userUsername.equals(userUsername1)) {
            user = userDAO.checkExists(userUsername1);
            userUsername = userUsername1;
            completedtsIDs = new ArrayList<>();
            List<Completedts> completedForUser = completedtsDAO.getCompltedtsByUser(userUsername);

            for (int i = 0; i < completedForUser.size(); i++) {
                if (!completedtsIDs.contains(completedForUser.get(i).getTsId()))
                completedtsIDs.add(completedForUser.get(i).getTsId());
            }
        //}
        return "examinee";
    }

    public List<Ts> getTs() {
        return ts;
    }

    public void setTs(List<Ts> ts) {
        this.ts = ts;
    }
    
    public String orderByNameASC() {
        ts = tsDAO.getAllTsOrderByName("ASC");
        return "examinee";
    }
    
    public String orderByNameDESC() {
        ts = tsDAO.getAllTsOrderByName("DESC");
        return "examinee";
    }
    
    public String orderByStartASC() {
        ts = tsDAO.getAllTsOrderByStart("ASC");
        return "examinee";
    }
    
    public String orderByStartDESC() {
        ts = tsDAO.getAllTsOrderByStart("DESC");
        return "examinee";
    }
    
    public String orderByEndASC() {
        ts = tsDAO.getAllTsOrderByEnd("ASC");
        return "examinee";
    }
    
    public String orderByEndDESC() {
        ts = tsDAO.getAllTsOrderByEnd("DESC");
        return "examinee";
    }
    
    public boolean isTsUpToDate(Ts ts) {
        Date date = new Date();
        return (date.after(ts.getDateTimeStart()) && date.before(ts.getDateTimeEnd()));
    }
    
    public boolean madeTest(Ts ts, User creator) {       
        Ts tss = tsDAO.getbyTsIdAndCreator(ts.getId(), creator.getUsername());
        if (tss == null)
            return false;
        else
            return true;
    }
    
    public String startTest(Ts ts) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);       
        httpSession.setAttribute("currentTestId", ts.getId());     
//        currentQuestionsInTS = new ArrayList<>();
//        currentQuestions = new ArrayList<>();
//        currentQuestionsInTS = questionsInTsDAO.getQuestionsForTS(ts.getId());
//        for (int i = 0; i < currentQuestionsInTS.size(); i++) {
//            currentQuestions.add(questionDAO.returnQuestionWithId(currentQuestionsInTS.get(i).getQuestionId()));
//        }
        return "examinee-startTest";
    }
    
    public String backFromExamineeSeeResults() {
        return "examinee";
    }
    
    public String viewTestResults(Ts ts) {
        user = userDAO.checkExists(userUsername);
        completedts = completedtsDAO.getCompltedtsByUserAndTs(userUsername, ts.getId());
        if (completedts != null) {
            if (completedts.getMaxPoints() != 0)
                perCent = 100 * (completedts.getUserPoints() / completedts.getMaxPoints());
            else 
                perCent = 0;
        }
        else 
            perCent = 0;
        return "examinee-seeTestResults";
    }
    
    public String startSurvey(Ts ts) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);       
        httpSession.setAttribute("currentSurveyId", ts.getId());
        return "examineee-startSurvey";
    }
    
    public String viewSurveyResults(Ts ts) {
        
        
        return "examineee-finishedSurvey";
    }
    
}
