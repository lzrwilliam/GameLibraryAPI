package ro.unibuc.hello.data.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "Rents")
public class Rent {
  
    @Id
    private String id;

    private String userID;
    private String gameID;
    private LocalDate startDate;
    private LocalDate endDate;
    private int length;
    private double price;

    public Rent(){
    }

    public Rent(String userID, String gameID, LocalDate startDate, int length, double price){
        this.userID = userID;
        this.gameID = gameID;
        this.startDate = startDate;
        this.length = length;
        this.endDate = startDate.plusDays(length);
        this.price = price;
    }

    public String getId(){
        return id;
    }

    public String getUserID(){
        return userID;
    }
    public void setUserID(String userID){
        this.userID = userID;
    }

    public String getGameID(){
        return gameID;
    }
    public void setGameID(String gameID){
        this.gameID = gameID;
    }

    public LocalDate getStartDate(){
        return startDate;
    }
    public void setStartDate(LocalDate date){
        this.startDate = date;
    }

    public LocalDate getEndDate(){
        return endDate;
    }
    public void setEndDate(LocalDate date){
        this.endDate = date;
    }
    public void addToEndDate(int noDays){
        endDate = endDate.plusDays(noDays); 
        addToLength(noDays);
    }

    public int getLength(){
        return length;
    }
    public void setLength(int length){
        this.length = length;
    }
    public void addToLength(int addingLength){
        this.length += addingLength;
    }

    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public void addToPrice(double price){
        this.price += price;
    }

}
