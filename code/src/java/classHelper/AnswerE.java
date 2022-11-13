/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classHelper;

import java.util.ArrayList;
import pojo.Answer;

/**
 *
 * @author user
 */
public class AnswerE {
    private Answer possibleAnswer;
    private String userAnswer;
    private int num = 0;
    private int qType;
    private ArrayList<SubAnswerE> userAnswers = null;
    private int subQNo = 0; // za vizuelizaciju pitanja tipa 1 i 2
    private int realSubQNo = 0; // koji imaju vise od 0 podpitanja

    public int getRealSubQNo() {
        return realSubQNo;
    }

    public void setRealSubQNo(int realSubQNo) {
        this.realSubQNo = realSubQNo;
    }
   
    public int getSubQNo() {
        return subQNo;
    }

    public void setSubQNo(int subQNo) {
        this.subQNo = subQNo;
    }
    
    public int getqType() {
        return qType;
    }

    public void setqType(int qType) {
        this.qType = qType;
    }

    public ArrayList<SubAnswerE> getUserAnswers() {
        return userAnswers;
    }

    public void setUserAnswers(ArrayList<SubAnswerE> userAnswers) {
        this.userAnswers = userAnswers;
    }

    public AnswerE(Answer possibleAnswer, String userAnswer) {
        this.possibleAnswer = possibleAnswer;
        this.userAnswer = userAnswer;
        this.num = 0;
    }
    
    public AnswerE(Answer possibleAnswer, String userAnswer, int num) {
        this.possibleAnswer = possibleAnswer;
        this.userAnswer = userAnswer;
        this.num = num;
    }

    public AnswerE(Answer possibleAnswer, String userAnswer, int num, int qType) {
        this.possibleAnswer = possibleAnswer;
        this.userAnswer = userAnswer;
        this.num = num;
        this.qType = qType;
    }

    
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    

    public Answer getPossibleAnswer() {
        return possibleAnswer;
    }

    public void setPossibleAnswer(Answer possibleAnswer) {
        this.possibleAnswer = possibleAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }
}
