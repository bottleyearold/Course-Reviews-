package edu.virginia.sde.reviews;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.Timestamp;
import java.util.NoSuchElementException;

public class SubmitReviewController {
    private MainController mainController;
    private ReviewService reviewService;
    private User user;
    private Course course;
    private Review userReview;

    @FXML
    private Label userLabel;
    @FXML
    private Label courseLabel;
    @FXML
    private TextField ratingField;
    @FXML
    private TextField commentField;
    @FXML
    private Label messageLabel;

    public void initializeFields(){
        ratingField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")){
                ratingField.setText(oldValue);
            }else if(!newValue.isEmpty()){
                int value = Integer.parseInt(newValue);
                if (value <1 || value > 5){
                    ratingField.setText(oldValue);
                }
            }
        });
    }


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    public void setUser(User user) {
        this.user = user;
        userLabel.setText("Logged in as: " + user.getUsername());
    }

    public void setCourse(Course course) {
        this.course = course;
        courseLabel.setText("Reviewing for: " + course.getSubject() + " " + course.getNumber());
    }

    public void setUserReview(Review review) {
        this.userReview = review;
    }

    private void clearFields() {
        ratingField.clear();
        commentField.clear();
    }

    public void handleSubmissionAction() {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            int rating = Integer.parseInt(ratingField.getText());
            String comment = commentField.getText().isEmpty() ? "N/A" : commentField.getText();
            Review newReview = new Review(course.getId(), user.getId(), rating, comment, timestamp);
           // System.out.println("IM HERE 1");
            if (userReview != null) {
                reviewService.deleteReview(userReview);
            }
           // System.out.println("IM HERE 2");
            reviewService.addReview(newReview);
           // System.out.println("IM HERE 3");
            userReview = newReview;
            clearFields();
           // System.out.println("IM HERE 4");
            messageLabel.setText("Review submitted successfully.");
        } catch (NumberFormatException e) {
            messageLabel.setText("Rating is empty or invalid.");
        } catch (NoSuchElementException e) {
            messageLabel.setText("No such course or user exists.");
        } catch (Exception e) {
            messageLabel.setText("Failed to submit review.");
        }
    }

    public void handleGoBackAction() {
        clearFields();
        messageLabel.setText("");
        mainController.switchToCourseReviews(user, course);
    }

}
