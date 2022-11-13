/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import classHelper.AnswerE;
import classHelper.QuestionE;
import classHelper.SubAnswerE;
import classHelper.TSE;
import dao.AnswerDAO;
import dao.CompletedtsDAO;
import dao.QuestionDAO;
import dao.QuestionsInTsDAO;
import dao.SaveStateDAO;
import dao.TsDAO;
import dao.UserDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import pojo.Answer;
import pojo.Completedts;
import pojo.Question;
import pojo.Questionsints;
import pojo.Savestate;
import pojo.Ts;
import pojo.User;

/**
 *
 * @author user
 */
@SessionScoped
@ManagedBean(name="CreatorReviewController")
public class CreatorReviewController implements Serializable {
    
    public User user;
    int aaa = 1;

    public int getAaa() {
        return aaa;
    }

    public void setAaa(int aaa) {
        this.aaa = aaa;
    } 
    public ArrayList<Completedts> completedts = new ArrayList<>();
    
    public ArrayList<Double> testReviewInPerCent = new ArrayList<>();
    public ArrayList<Integer> arrayint = new ArrayList<>();
    
    public Ts currSurvey = new Ts();

    public Ts getCurrSurvey() {
        return currSurvey;
    }

    public void setCurrSurvey(Ts currSurvey) {
        this.currSurvey = currSurvey;
    }

    public ArrayList<Integer> getArrayint() {
        return arrayint;
    }

    public void setArrayint(ArrayList<Integer> arrayint) {
        this.arrayint = arrayint;
    }

    public ArrayList<Completedts> getCompletedts() {
        return completedts;
    }

    public ArrayList<Double> getTestReviewInPerCent() {
        return testReviewInPerCent;
    }

    public void setTestReviewInPerCent(ArrayList<Double> testReviewInPerCent) {
        this.testReviewInPerCent = testReviewInPerCent;
    }

    public void setCompletedts(ArrayList<Completedts> completedts) {
        this.completedts = completedts;
    }
    
    private final UserDAO userDAO = new UserDAO();
    private final QuestionsInTsDAO questionsInTsDAO = new QuestionsInTsDAO();
    private final CompletedtsDAO completedtsDAO = new CompletedtsDAO();
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final SaveStateDAO saveStateDAO = new SaveStateDAO();
    private final AnswerDAO answerDAO = new AnswerDAO();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Ts> getTs() {
        return ts;
    }

    public void setTs(List<Ts> ts) {
        this.ts = ts;
    }
    
    public final TsDAO tsDAO = new TsDAO();
    private List<Ts> ts = tsDAO.getAllTs();
    
    public String onLoad() {
        ts = tsDAO.getAllTs();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);    
        user = userDAO.checkExists((String) httpSession.getAttribute("username"));
        return "creator-review";
    }
    
    public String back() {
        return "creator";
    }
    
    public String delete(Ts t) {
        String s = "survey";
        if (t.isTestSurvey()) 
        {
            s = "test"; 
        }
        tsDAO.deleteTs(t);
        completedtsDAO.deleteCompltetedtsByTsId(t.getId());///
        saveStateDAO.deleteSaveStateByTsId(t.getId());////
        questionsInTsDAO.deleteQuestionsInTsByTsId(t.getId());
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "info", "The " + s + " is deleted successfully.");
        RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
        return "creator-review"; 
    }
    
    public String backFromCreatorReviewOneTs() {
        return "creator-review";
    }
    
    public String seeTestDetails(Ts ts) {
        double d = 0;
        currSurvey = ts;
        testReviewInPerCent = new ArrayList<>();
        arrayint = new ArrayList<>();
        completedts = (ArrayList<Completedts>) completedtsDAO.getCompltedtsWithSameTsId(ts.getId());
        for (int i = 0; i < 10; i++) {
            testReviewInPerCent.add(0.0);
            arrayint.add(i);
        }           
        for (int i = 0; i < completedts.size(); i++) {
            Completedts cts = completedts.get(i);
            if (cts.getMaxPoints() != 0) {
                    d = Math.round((cts.getUserPoints() / cts.getMaxPoints()) * 100);
                    for (int j = 0; j < 10; j++) {
                        if ((d >= (j*10 + ((j == 0) ? 0 : 1))) && (d <= (j + 1) * 10)) {
                            testReviewInPerCent.set(j, testReviewInPerCent.get(j) + 1);
                        }
                    }    
                }
            }
        for (int i = 0; i < 10; i++) {
            testReviewInPerCent.set(i, testReviewInPerCent.get(i) / completedts.size() * 100);
        }       
        return "creator-testReview.xhtml";
    }

    public ArrayList<TSE> getTse() {
        return tse;
    }

    public void setTse(ArrayList<TSE> tse) {
        this.tse = tse;
    }
    
    
    private ArrayList<TSE> tse = new ArrayList<>();
    
    public String seeSurveyDetails(Ts ts) {
        completedts = (ArrayList<Completedts>) completedtsDAO.getCompltedtsWithSameTsId(ts.getId());
        tse = new ArrayList<>();
        String s = "";
        currSurvey = ts;
        
        for (int i = 0; i < completedts.size(); i++) { //ide kroz listu ts-a raznih korisnika     
            ArrayList<Questionsints> questionsintses = questionsInTsDAO.getQuestionsForTS(ts.getId());
            
            for (int j = 0; j < questionsintses.size(); j++) { // j ide po questionsima koji su u tsu
                ArrayList<QuestionE> questionsE = new ArrayList<>();
                ArrayList<Answer> possibleAnswersForQuestion = answerDAO.getAnswersbyQuestionId(questionsintses.get(j).getQuestionId());
             
                    Question question = questionDAO.returnQuestionWithId(questionsintses.get(j).getQuestionId());
                    ArrayList<AnswerE> answersE = new ArrayList<>(); 
                    for (int l = 0; l < possibleAnswersForQuestion.size(); l++) { 
                        Answer possibleAnswer = possibleAnswersForQuestion.get(l);
                        String userAnswer = saveStateDAO.getUserAnswer(question.getId(), ts.getId(), l + 1, completedts.get(i).getUserUsername());
                        AnswerE answerE = new AnswerE(possibleAnswer, userAnswer);
                        answersE.add(answerE);
                    }    
                    QuestionE questionE = new QuestionE(question, answersE);
                    questionE.setQuestionNoToShow(j + 1);
                    if (question.getType() == 4) 
                    {
                        questionE.setUserAnswer(saveStateDAO.getUserAnswerRadio(question.getId(), ts.getId(), completedts.get(i).getUserUsername()));
                        s+=saveStateDAO.getUserAnswerRadio(question.getId(), ts.getId(), completedts.get(i).getUserUsername()) + " " + question.getId()+" "+ ts.getId()+" "+ completedts.get(i).getUserUsername();
                    }
                    if (possibleAnswersForQuestion.size() == 0) {
                        questionE.setUserAnswer(saveStateDAO.getUserAnswer(question.getId(), ts.getId(), 0, completedts.get(i).getUserUsername()));
                    }
                    questionsE.add(questionE);
                
                User user1 = userDAO.checkExists(completedts.get(i).getUserUsername());
                    
                TSE oneTSE = new TSE(questionsE, user1, ts.isIsPersonalised());
                tse.add(oneTSE);
            }
        }
        //FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", s);
        //RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
        return "creator-surveyReview";
    }
    
    public String backFromVisualReview() {
        return "creator-surveyReview";
    }
    
    
    
    ArrayList<QuestionE> visualQuestionsE = new ArrayList<>();
    
    public String visualSurveyReview() {
        visualQuestionsE = new ArrayList<>();
        String s = "";  int kk = 0;
        for (int i = 0; i < completedts.size(); i++) { //ide kroz listu ts-a raznih korisnika     
            ArrayList<Questionsints> questionsintses = questionsInTsDAO.getQuestionsForTS(currSurvey.getId());
            
            for (int j = 0; j < questionsintses.size(); j++) { // j ide po questionsima koji su u tsu

                Question question = questionDAO.returnQuestionWithId(questionsintses.get(j).getQuestionId());

                if (i == 0) {
                    ArrayList<AnswerE> answersE = new ArrayList<>(); 
                    QuestionE questionE = new QuestionE(question, answersE);
                    questionE.setQuestionNoToShow(kk++);
                    visualQuestionsE.add(questionE);
                }
                  ArrayList<Answer> possibleAnswersForQuestion = answerDAO.getAnswersbyQuestionId(questionsintses.get(j).getQuestionId());             

                    if (question.getType() == 1 || question.getType() == 2) {
                        if (question.getNumOfSubquestions() == 0) {
                            boolean b = false;
                            String userAnswer1 = saveStateDAO.getUserAnswer(question.getId(), currSurvey.getId(), 0, completedts.get(i).getUserUsername());
                            if (!userAnswer1.equals(""))
                            for (int k = 0; k < visualQuestionsE.get(j).getAnswersE().size(); k++) {
                                if (visualQuestionsE.get(j).getAnswersE().get(k).getUserAnswer().equals(userAnswer1)) {
                                    b = true;
                                    visualQuestionsE.get(j).getAnswersE().get(k).setNum(visualQuestionsE.get(j).getAnswersE().get(k).getNum() + 1);
                                    break;
                                }
                            }
                            if (!b && !userAnswer1.equals("")) {
                                visualQuestionsE.get(j).getAnswersE().add(new AnswerE(null, userAnswer1, 1, 3));
                            } 
                        }
                        else {
                            for (int l = 0; l < possibleAnswersForQuestion.size(); l++) {
                                if (i == 0) {
                                    Answer possibleAnswer = possibleAnswersForQuestion.get(l); //possibleAnswer je ovde tekst podpitanja, a userAnswers svi moguci odg na to podpitanje
                                    AnswerE answerE = new AnswerE(possibleAnswer, "", 0, 12); // za tip pitanja 1 i 2 koji imaju vise subuqestiona
                                    answerE.setUserAnswers(new ArrayList<>());
                                    visualQuestionsE.get(j).getAnswersE().add(answerE);  
                                }
                                boolean b = false;
                                String userAnswer1 = saveStateDAO.getUserAnswer(question.getId(), currSurvey.getId(), l + 1, completedts.get(i).getUserUsername());              
                                if (!userAnswer1.equals(""))
                                for (int k = 0; k < visualQuestionsE.get(j).getAnswersE().get(l).getUserAnswers().size(); k++) {
                                    if (visualQuestionsE.get(j).getAnswersE().get(l).getUserAnswers().get(k).getSubAnswer().equals(userAnswer1)) {
                                        b = true;
                                        visualQuestionsE.get(j).getAnswersE().get(l).getUserAnswers().get(k).setNum(visualQuestionsE.get(j).getAnswersE().get(l).getUserAnswers().get(k).getNum() + 1);
                                        break;
                                    }
                                }
                                if (!b && !userAnswer1.equals("")) {
                                    visualQuestionsE.get(j).getAnswersE().get(l).getUserAnswers().add(new SubAnswerE(userAnswer1, 1));
                                } 
                            }
                        }
                    }
                  
                  
                    if (question.getType() == 3) {
                        boolean b = false;
                        String userAnswer1 = saveStateDAO.getUserAnswer(question.getId(), currSurvey.getId(), 0, completedts.get(i).getUserUsername());
                        if (!userAnswer1.equals(""))
                        for (int k = 0; k < visualQuestionsE.get(j).getAnswersE().size(); k++) {
                            if (visualQuestionsE.get(j).getAnswersE().get(k).getUserAnswer().equals(userAnswer1)) {
                                b = true;
                                visualQuestionsE.get(j).getAnswersE().get(k).setNum(visualQuestionsE.get(j).getAnswersE().get(k).getNum() + 1);
                                break;
                            }
                        }
                        if (!b && !userAnswer1.equals("")) {
                            visualQuestionsE.get(j).getAnswersE().add(new AnswerE(null, userAnswer1, 1, 3));
                        }
                    }

                    if (question.getType() == 4 || question.getType() == 5)  {          
                        for (int l = 0; l < possibleAnswersForQuestion.size(); l++) {
                            if (i == 0) {
                                Answer possibleAnswer = possibleAnswersForQuestion.get(l);
                                AnswerE answerE = new AnswerE(possibleAnswer, "", 0);
                                visualQuestionsE.get(j).getAnswersE().add(answerE);  
                            }
                            String userAnswer = saveStateDAO.getUserAnswer(question.getId(), currSurvey.getId(), l + 1, completedts.get(i).getUserUsername());              
                            if (question.getType() == 4)
                            for (int k = 0; k < visualQuestionsE.get(j).getAnswersE().size(); k++) {
                                if (visualQuestionsE.get(j).getAnswersE().get(k).getPossibleAnswer().getSubQuestionText().equals(userAnswer)) {
                                    visualQuestionsE.get(j).getAnswersE().get(k).setNum(visualQuestionsE.get(j).getAnswersE().get(k).getNum() + 1);
                                }
                            } 
                            if (question.getType() == 5) {
                                  if (userAnswer.equals("true"))
                                      visualQuestionsE.get(j).getAnswersE().get(l).setNum(visualQuestionsE.get(j).getAnswersE().get(l).getNum() + 1);
                            }         
                        }
                    }

               
             

            }
        }
        
        
        for (int i = 0; i < visualQuestionsE.size(); i++) 
        {       int realSubQNoForType5 = visualQuestionsE.get(i).getAnswersE().size();
                if (visualQuestionsE.get(i).getAnswersE().size() < 10) 
                {
                    for (int j = visualQuestionsE.get(i).getAnswersE().size(); j < 10; j++) 
                    {
                        visualQuestionsE.get(i).getAnswersE().add(new AnswerE(null, ""));
                        
                        
                         if ((visualQuestionsE.get(i).getQuestion().getType() == 3)||(visualQuestionsE.get(i).getQuestion().getType() == 1 && visualQuestionsE.get(i).getQuestion().getNumOfSubquestions() == 0)||(visualQuestionsE.get(i).getQuestion().getType() == 2 && visualQuestionsE.get(i).getQuestion().getNumOfSubquestions() == 0)) {
                            visualQuestionsE.get(i).getAnswersE().get(j).setSubQNo(j);
                            visualQuestionsE.get(i).getAnswersE().get(j).setRealSubQNo(realSubQNoForType5);
                        }
                    }     
                }
                
                if (((visualQuestionsE.get(i).getQuestion().getType() == 1 || visualQuestionsE.get(i).getQuestion().getType() == 2) && ( visualQuestionsE.get(i).getQuestion().getNumOfSubquestions() > 0)) || (visualQuestionsE.get(i).getQuestion().getType() == 5)) 
                        {
                            s+=visualQuestionsE.get(i).getQuestionNoToShow()+":"+ visualQuestionsE.get(i).getAnswersE().size();
                            for (int j = 0; j < 10; j++) 
                            {
                                if (visualQuestionsE.get(i).getAnswersE().get(j).getUserAnswers() == null)
                                    visualQuestionsE.get(i).getAnswersE().get(j).setUserAnswers(new ArrayList<>());
                            
                                int realSubNo = visualQuestionsE.get(i).getAnswersE().get(j).getUserAnswers().size();
                                for (int k = visualQuestionsE.get(i).getAnswersE().get(j).getUserAnswers().size(); k < 10; k++) 
                                {
                                    visualQuestionsE.get(i).getAnswersE().get(j).getUserAnswers().add(new SubAnswerE("", 0));
                                }
                                visualQuestionsE.get(i).getAnswersE().get(j).setSubQNo(j);////////////
                                visualQuestionsE.get(i).getAnswersE().get(j).setRealSubQNo(realSubNo + 1);
                                if (visualQuestionsE.get(i).getQuestion().getType() == 5) {
                                    visualQuestionsE.get(i).getAnswersE().get(j).setRealSubQNo(realSubQNoForType5);
                                }
                            }
                        }
                        
    
        
        }
           //FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", s);
           //RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
        
        return "creator-surveyReviewVisual";
    }

    public ArrayList<QuestionE> getVisualQuestionsE() {
        return visualQuestionsE;
    }

    public void setVisualQuestionsE(ArrayList<QuestionE> visualQuestionsE) {
        this.visualQuestionsE = visualQuestionsE;
    }
    
    public String getName(String username) {
        User user1 = userDAO.checkExists(username);
        if (user1 != null)
            return user1.getName();
        else
            return "";
    }
    
    public String getSurname(String username) {
        User user1 = userDAO.checkExists(username);
        if (user1 != null)
            return user1.getSurname();
        else
            return "";
    }
    
    public String returnFromCreatorSruveyReview() {
        return "creator-review";
    }
}
