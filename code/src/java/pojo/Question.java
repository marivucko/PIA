package pojo;
// Generated Feb 2, 2020 6:12:39 PM by Hibernate Tools 4.3.1



/**
 * Question generated by hbm2java
 */
public class Question  implements java.io.Serializable {


     private Integer id;
     private String questionText;
     private int type;
     private int numOfSubquestions;
     private int points;
     private String correctAnswer;

    public Question() {
    }

    public Question(String questionText, int type, int numOfSubquestions, int points, String correctAnswer) {
       this.questionText = questionText;
       this.type = type;
       this.numOfSubquestions = numOfSubquestions;
       this.points = points;
       this.correctAnswer = correctAnswer;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public String getQuestionText() {
        return this.questionText;
    }
    
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    public int getType() {
        return this.type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    public int getNumOfSubquestions() {
        return this.numOfSubquestions;
    }
    
    public void setNumOfSubquestions(int numOfSubquestions) {
        this.numOfSubquestions = numOfSubquestions;
    }
    public int getPoints() {
        return this.points;
    }
    
    public void setPoints(int points) {
        this.points = points;
    }
    public String getCorrectAnswer() {
        return this.correctAnswer;
    }
    
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }




}


