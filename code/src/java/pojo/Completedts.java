package pojo;
// Generated Feb 2, 2020 6:12:39 PM by Hibernate Tools 4.3.1



/**
 * Completedts generated by hbm2java
 */
public class Completedts  implements java.io.Serializable {


     private Integer id;
     private int tsId;
     private String userUsername;
     private double maxPoints;
     private double userPoints;

    public Completedts() {
    }

    public Completedts(int tsId, String userUsername, double maxPoints, double userPoints) {
       this.tsId = tsId;
       this.userUsername = userUsername;
       this.maxPoints = maxPoints;
       this.userPoints = userPoints;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public int getTsId() {
        return this.tsId;
    }
    
    public void setTsId(int tsId) {
        this.tsId = tsId;
    }
    public String getUserUsername() {
        return this.userUsername;
    }
    
    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }
    public double getMaxPoints() {
        return this.maxPoints;
    }
    
    public void setMaxPoints(double maxPoints) {
        this.maxPoints = maxPoints;
    }
    public double getUserPoints() {
        return this.userPoints;
    }
    
    public void setUserPoints(double userPoints) {
        this.userPoints = userPoints;
    }




}


