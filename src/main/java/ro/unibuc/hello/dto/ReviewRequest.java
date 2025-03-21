package ro.unibuc.hello.dto;

public class ReviewRequest {
    private String userId;
    private String gameId;
    private String reviewText;
    private int rating;

    // Constructori
    public ReviewRequest() {}

    public ReviewRequest(String userId, String gameId, String reviewText, int rating) {
        this.userId = userId;
        this.gameId = gameId;
        this.reviewText = reviewText;
        this.rating = rating;
    }

    // Getteri și setteri
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }

    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
}
