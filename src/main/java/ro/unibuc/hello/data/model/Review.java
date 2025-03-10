package ro.unibuc.hello.data.model;

public class Review {
    private String userId;
    private String reviewText;
    private int rating;

    public Review() {}

    public Review(String userId, String reviewText, int rating) {
        this.userId = userId;
        this.reviewText = reviewText;
        this.rating = rating;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
