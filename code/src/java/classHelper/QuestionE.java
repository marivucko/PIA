/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classHelper;

import java.util.ArrayList;
import java.util.List;
import pojo.Answer;
import pojo.Question;

/**
 *
 * @author user
 */
public class QuestionE implements java.io.Serializable {
    
    private Question question;    
    private ArrayList<AnswerE> answersE;
    private String userAnswer = "";                     // njega proveravam da li je tacan tako sto ga uporedjujem sa question.correctAnswer
    private double userPoints = 0;
    private int questionNoToShow = 0;

    public int getQuestionNoToShow() {
        return questionNoToShow;
    }

    public void setQuestionNoToShow(int questionNoToShow) {
        this.questionNoToShow = questionNoToShow;
    }

    public double getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(double userPoints) {
        this.userPoints = userPoints;
    }

    public ArrayList<AnswerE> getAnswersE() {
        return answersE;
    }

    public void setAnswersE(ArrayList<AnswerE> answersE) {
        this.answersE = answersE;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public QuestionE(Question question, ArrayList<AnswerE> answers1) {
        this.question = question;
        //this.answers = answers;
        this.answersE = new ArrayList<>();
        if (answers1 != null)
        for (int i = 0; i < answers1.size(); i++)
            this.answersE.add(answers1.get(i));
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }  
    
}
