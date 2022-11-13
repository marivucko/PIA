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
import dao.TsDAO;
import dao.UserDAO;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import pojo.Ts;

/**
 *
 * @author user
 */
@SessionScoped
@ManagedBean(name="ExamineeControllerStartTest")
public class ExamineeControllerStartTest implements Serializable {

    public String backFromFinishedTestToDoAnother() {
        return "examinee";
    }
    
    public List<Question> getCurrentQuestions() {
        return currentQuestions;
    }

    public void setCurrentQuestions(ArrayList<Question> currentQuestions) {
        this.currentQuestions = currentQuestions;
    }
    
    private String ss = "";

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }
    private final TsDAO tsDAO = new TsDAO();
    private final UserDAO userDAO = new UserDAO();
    private final CompletedtsDAO completedtsDAO = new CompletedtsDAO();
    private final QuestionsInTsDAO questionsInTsDAO = new QuestionsInTsDAO();
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final AnswerDAO answerDAO = new AnswerDAO();
    
    private Ts currentTest;

    public List<QuestionE> getQuestionsE() {
        return questionsE;
    }

    public void setQuestionsE(ArrayList<QuestionE> questionsE) {
        this.questionsE = questionsE;
    }
    private ArrayList<Questionsints> currentQuestionsInTS;
    private ArrayList<Question> currentQuestions;
    private ArrayList<Answer> currentAnswers;
    private ArrayList<QuestionE> questionsE;
 
    private double totalPoints, maxPoints, perCentDone;

    public double getPerCentDone() {
        return perCentDone;
    }

    public void setPerCentDone(double perCentDone) {
        this.perCentDone = perCentDone;
    }

    public double getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(double totalPoints) {
        this.totalPoints = totalPoints;
    }

    public double getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(double maxPoints) {
        this.maxPoints = maxPoints;
    }
    
    private int time, time2;

    public List<Answer> getCurrentAnswers() {
        return currentAnswers;
    }

    public void setCurrentAnswers(ArrayList<Answer> currentAnswers) {
        this.currentAnswers = currentAnswers;
    }
    
    private String username;
    private Ts test;
    private List<Questionsints> questionsints;
    private int a = 0;
    
    public ArrayList<AnswerE> getRandomAnswers(QuestionE qE) {
        
        ArrayList<AnswerE> randomA = new ArrayList<>();
        List<Integer> randomIntegers = new ArrayList<>();
        
        if (qE != null) {
            for (int i = 0; i < qE.getAnswersE().size(); i++) {
                int random = (int) ((Math.random() * ((qE.getAnswersE().size() - 1) + 1)) + 1);
                while (randomIntegers.contains(random))
                    random = (int) ((Math.random() * ((qE.getAnswersE().size() - 1) + 1)) + 1);
                randomIntegers.add(random);
            }
            for (int i = 0; i < qE.getAnswersE().size(); i++) {
                randomA.add(qE.getAnswersE().get(randomIntegers.get(i)-1));
            }    
        }
        
        return randomA;
    }
    
    public ArrayList<QuestionE> getRandomQuestions() {
        
        ArrayList<QuestionE> randomQ = new ArrayList<>();
        List<Integer> randomIntegers = new ArrayList<>();
        
        for (int i = 0; i < questionsE.size(); i++) {
            int random = (int) ((Math.random() * ((questionsE.size() - 1) + 1)) + 1);
            while (randomIntegers.contains(random))
                random = (int) ((Math.random() * ((questionsE.size() - 1) + 1)) + 1);
            randomIntegers.add(random);
        }
        
        for (int i = 0; i < questionsE.size(); i++) {
            randomQ.add(questionsE.get(randomIntegers.get(i)-1));
            QuestionE qE = questionsE.get(randomIntegers.get(i)-1);
            if (qE.getQuestion().getNumOfSubquestions() > 0)
                qE.setAnswersE(getRandomAnswers(qE));
        }
        
        return randomQ;
    }
    
    public String onload() {
        a++; if (a == 1) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);
            username =  (String) httpSession.getAttribute("username");
            currentTest = tsDAO.getTSbyId((int) httpSession.getAttribute("currentTestId"));

            currentQuestionsInTS = new ArrayList<>();
            currentQuestions = new ArrayList<>();
            questionsE = new ArrayList<>();

            currentQuestionsInTS = questionsInTsDAO.getQuestionsForTS(currentTest.getId());
            for (int i = 0; i < currentQuestionsInTS.size(); i++) {
                Question question = questionDAO.returnQuestionWithId(currentQuestionsInTS.get(i).getQuestionId());
                currentQuestions.add(questionDAO.returnQuestionWithId(currentQuestionsInTS.get(i).getQuestionId()));

                ArrayList<Answer> possibleAnswer = answerDAO.getAnswersbyQuestionId(question.getId());
                ArrayList<AnswerE> answersE = new ArrayList<>();

                for (int j = 0; j < possibleAnswer.size(); j++) {
                    AnswerE answerE = new AnswerE(possibleAnswer.get(j), "");
                    answersE.add(answerE);
                }


                QuestionE questionE = new QuestionE(question, answersE);
                questionsE.add(questionE);
            }

            time = currentTest.getDurationPagesNo();
            time2 = currentTest.getDurationPagesNo();
            
            questionsE = getRandomQuestions();
        }
        return "examinee-startTest";
    }
    
    public void examineeTick(){
        
        if(time>0) {
            time--;
        }
        else {
            try {
                String finish = finish();
                FacesContext.getCurrentInstance().getExternalContext().redirect("examinee-finishedTest.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(ExamineeControllerStartTest.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
    public void examineeTick2(){
        
        if(time2>0) 
            time2--;
    }
    
    public String getTime(){
        return time + "";
        //return "";
    }
    
     public String getTime2(){
        return (time2 >= 11) ? "" : time2 + ""; 
    }
    
    public String alterTest() {
        return "examinee-startTest";
    }
    
    public String wantToFinish() {
        perCentDone = perCentDone();
        return "examinee-wantToFinish";
    }
    
    public double perCentDone() {
        int numOfQuestions = 0;
        int numOfUserAnswers = 0;
        for (int i = 0; i < questionsE.size(); i++) {
            Question q = questionsE.get(i).getQuestion();
            if (q.getType() == 1) {
                if (questionsE.get(i).getUserAnswer() != null) {
                    numOfUserAnswers++;
                }
            }
            else if (q.getType() == 2) {
                if (!"".equals(questionsE.get(i).getUserAnswer()))
                    numOfUserAnswers++;
            }
            else if (q.getType() == 4) {
                 if (questionsE.get(i).getUserAnswer() != null) {
                    numOfUserAnswers++;
                }
            }
            else if (q.getType() == 5) {
                for (int j = 0; j < questionsE.get(i).getAnswersE().size(); j++) {
                    if (!"false".equals(questionsE.get(i).getAnswersE().get(j).getUserAnswer())) {
                        numOfUserAnswers++;
                        break;
                    } else {
                    }
                }
            }
            numOfQuestions++; 
        }
        return (((double)numOfUserAnswers / numOfQuestions) * 100);
    }
    
    
    public String finish() {
//        FacesMessage facesMessage = 
//                new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", 
//                        "*1  "+
//                                 questionsE.get(0).getAnswersE().get(0).getUserAnswer() + " "
//                                + questionsE.get(0).getAnswersE().get(1).getUserAnswer() + " "
//                                + questionsE.get(0).getAnswersE().get(2).getUserAnswer() + " "
//                                + questionsE.get(0).getAnswersE().get(3).getUserAnswer() + " "
//                                + questionsE.get(0).getAnswersE().get(4).getUserAnswer() + " "
//                                        
//                                        
//                                        
//                                        
//                                        + "   *2" + questionsE.get(1).getUserAnswer()
//                                + questionsE.get(1).getAnswersE().get(0).getPossibleAnswer().getSubQuestionText() + " "
//                                + questionsE.get(1).getAnswersE().get(1).getPossibleAnswer().getSubQuestionText() + " "
//                                + questionsE.get(1).getAnswersE().get(2).getPossibleAnswer().getSubQuestionText() + " "
//                               
//                                        + "   *3" + questionsE.get(2).getUserAnswer() 
//                                        + "   *4" + questionsE.get(3).getUserAnswer()
//                        
//                                   + "   *5" + questionsE.get(4).getUserAnswer()
//                                + questionsE.get(4).getAnswersE().get(0).getPossibleAnswer().getSubQuestionText() + " "
//                                + questionsE.get(4).getAnswersE().get(1).getPossibleAnswer().getSubQuestionText() + " "
//                                + questionsE.get(4).getAnswersE().get(2).getPossibleAnswer().getSubQuestionText() + " "         
//                                + questionsE.get(4).getAnswersE().get(3).getPossibleAnswer().getSubQuestionText() + " "
//                                + questionsE.get(4).getAnswersE().get(4).getPossibleAnswer().getSubQuestionText() + " " 
//                                
//                
//                
//                );
//        
        totalPoints = 0; maxPoints = 0; String s ="";
        for (int i = 0; i < questionsE.size(); i++) {
            Question q = questionsE.get(i).getQuestion();
            ss += q.getQuestionText() + "\n";
            if (q.getType() == 1) {
                if (q.getCorrectAnswer().equals(questionsE.get(i).getUserAnswer()))
                    questionsE.get(i).setUserPoints(q.getPoints()); 
                totalPoints += questionsE.get(i).getUserPoints();
                maxPoints += q.getPoints();
            } 
            else if (q.getType() == 2) {
                if (q.getCorrectAnswer().equals(questionsE.get(i).getUserAnswer()))
                    questionsE.get(i).setUserPoints(q.getPoints()); 
                totalPoints += questionsE.get(i).getUserPoints();
                maxPoints += q.getPoints();
            }
            else if (q.getType() == 4) {
                for (int j = 0; j < questionsE.get(i).getAnswersE().size(); j++) {
                    if (questionsE.get(i).getAnswersE().get(j).getPossibleAnswer().getCorrectAnswer().equals("true")) {
                        String correctAnswer = questionsE.get(i).getAnswersE().get(j).getPossibleAnswer().getSubQuestionText();
                        questionsE.get(i).getQuestion().setCorrectAnswer(questionsE.get(i).getAnswersE().get(j).getPossibleAnswer().getSubQuestionText());
                        if ((correctAnswer).equals(questionsE.get(i).getUserAnswer()))
                            questionsE.get(i).setUserPoints(q.getPoints());
                    }
                }
                totalPoints += questionsE.get(i).getUserPoints();
                maxPoints += q.getPoints();
            }
            else if (q.getType() == 5) {
                int numOfCorrectAnswers = 0; double points = 0;
                for (int j = 0; j < questionsE.get(i).getAnswersE().size(); j++) {
                    if (questionsE.get(i).getAnswersE().get(j).getPossibleAnswer().getCorrectAnswer().equals("true"))
                        numOfCorrectAnswers++;
                }                
                double pointsForOneAnswer = (double) questionsE.get(i).getQuestion().getPoints() / numOfCorrectAnswers;
                //int pointsForOneAnswer = questionsE.get(i).getQuestion().getPoints() / questionsE.get(i).getQuestion().getNumOfSubquestions();
                //s = pointsForOneAnswer + " " + questionsE.get(i).getQuestion().getPoints() + "/" + numOfCorrectAnswers;
                for (int j = 0; j < questionsE.get(i).getAnswersE().size(); j++) {
                   
                    
                    if (questionsE.get(i).getAnswersE().get(j).getPossibleAnswer().getCorrectAnswer().equals(questionsE.get(i).getAnswersE().get(j).getUserAnswer()) 
                            && questionsE.get(i).getAnswersE().get(j).getUserAnswer().equals("true")) {
                        points += pointsForOneAnswer;
                    }
                }
                questionsE.get(i).setUserPoints((double)points);
                //s+= " userPoints " + questionsE.get(i).getUserPoints();
                totalPoints += questionsE.get(i).getUserPoints();
                maxPoints += q.getPoints();
            }
        }
        
        Completedts completedts = new Completedts();
        completedts.setMaxPoints(maxPoints);
        completedts.setUserPoints(totalPoints);
        completedts.setTsId(currentTest.getId());
        completedts.setUserUsername(username);
        completedtsDAO.insertCompletedts(completedts);
        //FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", completedts.getUserUsername() + " " + completedts.getUserPoints() + " ovde");
        //RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
        return "examinee-finishedTest";
    }
    
}
