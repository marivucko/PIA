/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.AnswerDAO;
import dao.QuestionDAO;
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
import pojo.Question;
import pojo.User;

/**
 *
 * @author user
 */
@SessionScoped
@ManagedBean(name="InsertAnswerController")
public class InsertAnswerController implements Serializable {
    
    private final QuestionDAO questionDAO = new QuestionDAO();
    private final AnswerDAO answerDAO = new AnswerDAO();   
    
    public int count = 0;
    public int flag = 0;
    public String selectOne = "";

    public String getSelectOne() {
        return selectOne;
    }

    public void setSelectOne(String selectOne) {
        this.selectOne = selectOne;
    }
    
    public Question question;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
    
    public List<Answer> answers = new ArrayList<>();

    public String getLongText() {
        return longText;
    }

    public void setLongText(String longText) {
        this.longText = longText;
    }
    public String longText = "";

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
    
    public String onLoad() {
        count++;
        if (count == 1) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession httpSession = (HttpSession) facesContext.getExternalContext().getSession(true);
            boolean changedPassword = false;
            question = questionDAO.returnQuestionWithId((int) httpSession.getAttribute("questionId"));

            for (int i = 0; i < question.getNumOfSubquestions(); i++) {
                Answer a = new Answer();
                a.setSubQuestionNo(i + 1);
                a.setIdQuestion(question.getId());
                answers.add(a);
            }
        }
        
        return "insertAnswer";
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
    
    public String back() {
        return "insertQuestion";
    }
    
    public String insertAnswer() {
        
        if (question.getType() == 3) {
            Answer answer = new Answer();
            answer.setIdQuestion(question.getId());
            answer.setSubQuestionNo(1);
            answer.setSubQuestionText(question.getQuestionText());
            answer.setCorrectAnswer(longText);
            if (answerDAO.insertAnswer(answer)) {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "Correct answer is attached to the question correctly.");
                RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            }
            else {
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "Correct answer is not attached to the question");
                RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
            }
        }
        else  {
            for (int i = 0; i < answers.size(); i++) {
                answerDAO.insertAnswer(answers.get(i));
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_WARN, "warn", "Correct answers are attached to the question correctly.");
                RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
                flag = 1;
            }
        }

        
        return "insertAnswer";
    }
}
