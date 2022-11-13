/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classHelper;

import java.util.ArrayList;
import pojo.User;

/**
 *
 * @author user
 */
public class TSE {
    private ArrayList<QuestionE> questionsE;
    private User user;
    private boolean isPersonalised;

    public boolean isIsPersonalised() {
        return isPersonalised;
    }

    public void setIsPersonalised(boolean isPersonalised) {
        this.isPersonalised = isPersonalised;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<QuestionE> getQuestionsE() {
        return questionsE;
    }

    public void setQuestionsE(ArrayList<QuestionE> questionsE) {
        this.questionsE = questionsE;
    }
    
    public TSE(ArrayList<QuestionE> questions1E, User user1, boolean isPersonalised) {
        this.questionsE = new ArrayList<>();
        if (questions1E != null)
        for (int i = 0; i < questions1E.size(); i++)
            this.questionsE.add(questions1E.get(i));
        this.user = user1;
        this.isPersonalised = isPersonalised;
    }
}
