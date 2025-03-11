package ro.unibuc.hello.data.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Document(collection = "games")
public class Game {

    @Id
    @MongoId(targetType = FieldType.INT32) // id setat drept int
    private int id;

    private String title;
    private String genre;
    private double price;
    private int maxPlayers;
    private int totalCopies;
    private int availableCopies;
    private LocalDate addedDate;
    private double purchasePrice;
    private AgeCategory ageCategory;
    private List<Review> reviews = new ArrayList<>();


    public Game() {}

    public Game(int id,String title, String genre, double price, int totalCopies, int availableCopies, int maxPlayers, LocalDate addedDate, double purchasePrice, AgeCategory ageCategory) {
       this.id=id;
        this.title = title;
        this.genre = genre;
        this.price = price;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.maxPlayers = maxPlayers;
        this.addedDate = addedDate;
        this.purchasePrice = purchasePrice;
        this.ageCategory = ageCategory;
        this.reviews = new ArrayList<>();
    }

   
    public AgeCategory getAgeCategory() { return ageCategory; }
    public void setAgeCategory(AgeCategory ageCategory) { this.ageCategory = ageCategory; }
     

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public LocalDate getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDate addedDate) { this.addedDate = addedDate; }

    public double getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(double purchasePrice) { this.purchasePrice = purchasePrice; }


     public void addReview(Review review) {
        reviews.add(review);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

}
