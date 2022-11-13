/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import classHelper.AnswerE;
import classHelper.QuestionE;
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

/**
 *
 * @author user
 */
@SessionScoped
@ManagedBean(name="ExamineeControllerStartSurvey")
public class ExamineeControllerStartSurvey implements Serializable {
    
    private final UserDAO userDAO = new UserDAO();
    private final TsDAO tsDAO = new TsDAO();
    private final QuestionsInTsDAO questionsInTsDAO = new QuestionsInTsDAO();
    private final AnswerDAO answerDAO = new AnswerDAO();
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final SaveStateDAO saveStateDAO = new SaveStateDAO();
    private final CompletedtsDAO completedtsDAO = new CompletedtsDAO();
    
    private ArrayList<QuestionE> questionsE = new ArrayList<>();
    private List<QuestionE> questionsEToShow = new ArrayList<>();

    public List<QuestionE> getQuestionsEToShow() {
        return questionsEToShow;
    }

    public void setQuestionsEToShow(List<QuestionE> questionsEToShow) {
        this.questionsEToShow = questionsEToShow;
    }
    private Ts currentSurvey;
    private String username = "";
    
    private int flag = 0;
    private int questionsNo = 0, pagesNo = 0, currPage = 1, currQIndex = 0;
    private List<Integer> questionsOnPage = new ArrayList<>();

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public int getQuestionsNo() {
        return questionsNo;
    }

    public void setQuestionsNo(int questionsNo) {
        this.questionsNo = questionsNo;
    }

    public int getPagesNo() {
        return pagesNo;
    }

    public void setPagesNo(int pagesNo) {
        this.pagesNo = pagesNo;
    }
    
    private double perCentDone = 0;
    private String perCentString = "";

    public String getPerCentString() {
        return perCentString;
    }

    public void setPerCentString(String perCentString) {
        this.perCentString = perCentString;
    }

    public double getPerCentDone() {
        return perCentDone;
    }

    public void setPerCentDone(double perCentDone) {
        this.perCentDone = perCentDone;
    }

    public ArrayList<QuestionE> getQuestionsE() {
        return questionsE;
    }

    public void setQuestionsE(ArrayList<QuestionE> questionsE) {
        this.questionsE = questionsE;
    }
    
    public String backFromFinishedTestToDoAnother() {
        return "examinee";
    }
  
    
    public String onload() { 
        boolean hasPreviosState;
        flag++; String s = ""; String userAnswer = "";  
        if (flag == 1) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);
            username =  (String) httpSession.getAttribute("username");
            currentSurvey = tsDAO.getTSbyId((int) httpSession.getAttribute("currentSurveyId"));

            questionsE = new ArrayList<>();
            hasPreviosState = !saveStateDAO.getSaveState(currentSurvey.getId(), username).isEmpty();


            ArrayList<Questionsints> questionsints = questionsInTsDAO.getQuestionsForTS(currentSurvey.getId());
            for (int i = 0; i < questionsints.size(); i++) {
                ArrayList<Answer> possibleAnswers = answerDAO.getAnswersbyQuestionId(questionsints.get(i).getQuestionId());
                ArrayList<AnswerE> answersE = new ArrayList<>(); 
                for (int j = 0; j < possibleAnswers.size(); j++) {
                    Answer possibleAnswer = possibleAnswers.get(j);
                    //userAnswer = "";
                    if (hasPreviosState) {
                        userAnswer = saveStateDAO.getUserAnswer(questionsints.get(i).getQuestionId(), currentSurvey.getId(), j + 1, username);
                        int k = j+1;
                        s += userAnswer + " " + questionsints.get(i).getQuestionId() + " " + currentSurvey.getId() + " " + k + " " + username + "*";
                       
                    }
                    AnswerE answerE = new AnswerE(possibleAnswer, userAnswer);
                    answersE.add(answerE);
                }
                
                Question question = questionDAO.returnQuestionWithId(questionsints.get(i).getQuestionId());
                QuestionE questionE = new QuestionE(question, answersE);
                questionE.setQuestionNoToShow(i+1);
                if (possibleAnswers.size() == 0) {
                    userAnswer = "";
                    if (hasPreviosState) {
                        userAnswer = saveStateDAO.getUserAnswer(questionsints.get(i).getQuestionId(), currentSurvey.getId(), 0, username);
                        s += userAnswer + " " + questionsints.get(i).getQuestionId() + " " + currentSurvey.getId() + " " + 0 + " " + username + "*";
                        //s += "#####" + userAnswer + "####";
                        questionE.setUserAnswer(userAnswer);
                    }
                }
                if (questionE.getQuestion().getType() == 4)
                {
                    userAnswer = saveStateDAO.getUserAnswerRadio(questionsints.get(i).getQuestionId(), currentSurvey.getId(), username);
                    questionE.setUserAnswer(userAnswer);
                    s += "OVDE " + userAnswer + " OVDE";
                }
                questionsE.add(questionE);
            }
          
            questionsNo = questionsE.size();
            pagesNo = currentSurvey.getDurationPagesNo();
            if (pagesNo == 0) 
            {
                pagesNo = 1;
               // currPage = 1;
            }
            currPage = 1;
            
            for (int i = 0; i < pagesNo; i++) {
                int num = 0;
                questionsOnPage.add(num);
            }
            
            for (int i = 0; i < questionsNo; i++) {
                questionsOnPage.set(i % pagesNo, questionsOnPage.get(i % pagesNo) + 1); 
            }
            
            questionsEToShow = questionsE.subList((currPage - 1)*questionsOnPage.get(0), currPage*questionsOnPage.get(0)); 
            currQIndex = currPage*questionsOnPage.get(0);
          
        }
      
        return "examineeSurveyStart";
    }
    
    public String backFromExamineeSurveyStart() {
        return "examinee";
    } 
    
    public String wantToFinish() {
        int doneQuestions = 0; int numOfQuestions = 0;
        for (int i = 0; i < questionsE.size(); i++) {
            Question q = questionsE.get(i).getQuestion();
            if (q.getType() == 1) {
                if(q.getNumOfSubquestions() == 0) {
                    if (questionsE.get(i).getUserAnswer() != null)
                        doneQuestions++;
                }
                else {
                    boolean all = true;
                    for (int j = 0; j < questionsE.get(i).getAnswersE().size(); j++) {
                        if (questionsE.get(i).getAnswersE().get(j).getUserAnswer() == null) {
                            all = false; break;
                        }                       
                    }
                    if (all) doneQuestions++;
                }
            }
            else if (q.getType() == 2) {
                if(q.getNumOfSubquestions() == 0) {
                    if (!"".equals(questionsE.get(i).getUserAnswer()))
                        doneQuestions++;
                }
                else {
                    boolean all = true;
                    for (int j = 0; j < questionsE.get(i).getAnswersE().size(); j++) {
                        if ("".equals(questionsE.get(i).getAnswersE().get(j).getUserAnswer())) {
                            all = false; break;
                        }                       
                    }
                    if (all) doneQuestions++;
                }
            }
            else if (q.getType() == 3) {
                if (!"".equals(questionsE.get(i).getUserAnswer()))
                    doneQuestions++;
            }
            else if (q.getType() == 4) {
                if (questionsE.get(i).getUserAnswer() != null)
                    doneQuestions++;
            }
            else if (q.getType() == 5) {
                for (int j = 0; j < questionsE.get(i).getAnswersE().size(); j++) {
                    if (!"false".equals(questionsE.get(i).getAnswersE().get(j).getUserAnswer())) {
                        doneQuestions++;
                        break;
                    } 
                }
            }
            numOfQuestions++;
        }
        perCentDone = (numOfQuestions == 0) ? 0 : (((double) doneQuestions) / numOfQuestions * 100);       
        perCentString = doneQuestions + "/" + numOfQuestions;
        String s = saveCurrAnswers();
        return "examineee-wantToFinishSurvey";
    }
    
    public String alterSurvey() {
        return "examineee-startSurvey";
    }
    
    public String finish() {
        Completedts completedts = new Completedts();
        completedts.setTsId(currentSurvey.getId());
        completedts.setUserUsername(username);
        completedtsDAO.insertCompletedts(completedts);
        return "examineee-finishedSurvey";
    }
    
    public void saveState(int idQ, int idS, int subQNo, String userAnswer, String username1) {
        Savestate ss = new Savestate();
        ss.setIdQuestion(idQ);
        ss.setIdSurvey(idS);
        ss.setSubQuestionNo(subQNo);
        ss.setUserAnswer(userAnswer);
        ss.setUserUsername(username1);
        saveStateDAO.insertSaveState(ss);
    }
    
    public String saveCurrAnswers() {
        saveStateDAO.deleteSaveState(currentSurvey.getId(), username);
        for (int i = 0; i < questionsE.size(); i++) {
            Question q = questionsE.get(i).getQuestion(); 
            
            if (q.getType() == 1) {
               if(q.getNumOfSubquestions() == 0) {
                   if (questionsE.get(i).getUserAnswer() != null)
                       saveState(q.getId(), currentSurvey.getId(), 0, questionsE.get(i).getUserAnswer(), username);
               }
               else {
                   for (int j = 0; j < questionsE.get(i).getAnswersE().size(); j++) {
                       if (questionsE.get(i).getAnswersE().get(j).getUserAnswer() != null) {
                            saveState(q.getId(), currentSurvey.getId(), j + 1, questionsE.get(i).getAnswersE().get(j).getUserAnswer(), username);
                       }                       
                   }
               }
           }
           else if (q.getType() == 2) {
               if(q.getNumOfSubquestions() == 0) {
                   if (!"".equals(questionsE.get(i).getUserAnswer()))
                        saveState(q.getId(), currentSurvey.getId(), 0, questionsE.get(i).getUserAnswer(), username);
               }
               else {
                   for (int j = 0; j < questionsE.get(i).getAnswersE().size(); j++) {
                       if (!"".equals(questionsE.get(i).getAnswersE().get(j).getUserAnswer())) {
                            saveState(q.getId(), currentSurvey.getId(), j + 1, questionsE.get(i).getAnswersE().get(j).getUserAnswer(), username);
                       }                       
                    }
               }
           }
           else if (q.getType() == 3) {
               if (!"".equals(questionsE.get(i).getUserAnswer()))
                    saveState(q.getId(), currentSurvey.getId(), 0, questionsE.get(i).getUserAnswer(), username);
           }
           else if (q.getType() == 4) {
               if (questionsE.get(i).getUserAnswer() != null) {
                  for (int j = 0; j < questionsE.get(i).getAnswersE().size(); j++) {
                      if (questionsE.get(i).getUserAnswer().equals(questionsE.get(i).getAnswersE().get(j).getPossibleAnswer().getSubQuestionText())) {
                        saveState(q.getId(), currentSurvey.getId(), j + 1, questionsE.get(i).getUserAnswer(), username);
                      }
                  }
               }

           }
           else if (q.getType() == 5) {
               for (int j = 0; j < questionsE.get(i).getAnswersE().size(); j++) {
                   if (!"false".equals(questionsE.get(i).getAnswersE().get(j).getUserAnswer())) {
                    saveState(q.getId(), currentSurvey.getId(), j + 1, questionsE.get(i).getAnswersE().get(j).getUserAnswer(), username);    
                   } 
               }
           }
        }
      
        return "examineee-wantToFinishSurvey";
    }
      
    public String left() {
        if (currPage > 1)
            currPage--;
        else 
            return "examineee-StartSurvey";
        int k = currQIndex - questionsOnPage.get(currPage)-questionsOnPage.get(currPage-1);
        int kk = currQIndex - questionsOnPage.get(currPage);
        
        questionsEToShow = questionsE.subList(k, kk); 
        currQIndex = kk;
        //FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "currPage " + currPage + " questionNo " + questionsNo + " pagesNo " + pagesNo + " start: " + k + " end: " + kk);
        //RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
        return "examineee-StartSurvey";
    }
    
    public String right() {
        if (currPage < pagesNo)
            currPage++;
        else
            return "examineee-StartSurvey";
        int k = currQIndex;
        int kk = currQIndex + questionsOnPage.get(currPage-1);
        
        questionsEToShow = questionsE.subList(currQIndex, currQIndex + questionsOnPage.get(currPage - 1)); 
        currQIndex = currQIndex + questionsOnPage.get(currPage - 1);
        //FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "currPage " + currPage + " questionNo " + questionsNo + " pagesNo " + pagesNo + " start: " + k + " end: " + kk);
        //RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
        
        return "examineee-StartSurvey";
    }
    
    public String viewSurveyResults(Ts ts) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);        
        httpSession.setAttribute("currentSurveyId", ts.getId());         
        onload();
        
        return "examineee-finishedSurvey";
    }
    
  
}
