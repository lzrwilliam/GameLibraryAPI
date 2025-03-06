package ro.unibuc.hello.data.model;

import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "Rents")
public class Rent {
  

    private String userID;
    private String gameID;
    private LocalDate startDate;
    private LocalDate endDate;
    private int length;
    private double price;


    public Rent(String userID, String gameID, LocalDate date, int length, double price){
        this.userID = userID;
        this.gameID = gameID;
        this.startDate = date;
        this.length = length;
        this.endDate = startDate.plusDays(length);
        this.price = price;
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
    
    public int getLength(){
        return length;
    }
    public void setLength(int length){
        this.length = length;
    }

    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price = price;
    }


}
