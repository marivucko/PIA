/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classHelper;

/**
 *
 * @author user
 */
public class SubAnswerE {
    private String subAnswer;
    private int num;

    public String getSubAnswer() {
        return subAnswer;
    }

    public void setSubAnswer(String subAnswer) {
        this.subAnswer = subAnswer;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
    
    public SubAnswerE(String subAnswer, int num) {
        this.subAnswer = subAnswer;
        this.num = num;
    }
}
