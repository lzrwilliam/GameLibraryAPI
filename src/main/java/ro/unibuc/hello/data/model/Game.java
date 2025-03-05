package ro.unibuc.hello.data.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "games")
public class Game {

    @Id
    private String id;
    private String title;
    private String genre;
    private double price;
    private int maxPlayers;
    private LocalDate addedDate;
    private double purchasePrice;
    private AgeCategory ageCategory;

    public Game() {}

    public Game(String title, String genre, double price, int maxPlayers, LocalDate addedDate, double purchasePrice, AgeCategory ageCategory) {
        this.title = title;
        this.genre = genre;
        this.price = price;
        this.maxPlayers = maxPlayers;
        this.addedDate = addedDate;
        this.purchasePrice = purchasePrice;
        this.ageCategory = ageCategory;
    }

   
    public AgeCategory getAgeCategory() { return ageCategory; }
    public void setAgeCategory(AgeCategory ageCategory) { this.ageCategory = ageCategory; }
       public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public LocalDate getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDate addedDate) { this.addedDate = addedDate; }

    public double getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(double purchasePrice) { this.purchasePrice = purchasePrice; }

}
