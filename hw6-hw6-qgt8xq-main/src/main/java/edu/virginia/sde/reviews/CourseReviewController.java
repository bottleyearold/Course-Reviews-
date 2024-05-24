package edu.virginia.sde.reviews;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import java.awt.*;

public class CourseReviewController {
    private MainController mainController;
    private ReviewService reviewService;
    private User user;
    private Course course;
    private Review review;
    private boolean initialised = false;

    @FXML
    private Label userLabel;
    @FXML
    private Label courseLabel;
    @FXML
    private ListView<Review> reviewListView;

    private final ObservableList<Review> reviews = FXCollections.observableArrayList();

    @FXML
    private Label messageLabel;

    public void initializeReviewListView() {
        refreshReviewList();
        reviewListView.setItems(reviews);
        reviewListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Review review, boolean empty) {
                super.updateItem(review, empty);
                setText(empty ? null : reviewService.getReviewStringNoCourseName(review));
            }
        });
        initialised = true;
    }

    public boolean isReviewListInitialized() {
        return initialised;
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
        courseLabel.setText("Reviews for " + course.getSubject() + " " + course.getNumber() + ":");
    }

    public void refreshReviewList() {
        review = reviewService.getReview(user.getId(), course.getId());
        reviews.clear();
        reviews.addAll(reviewService.getReviewsByCourseId(course.getId()));
    }

    public void handleAddReviewAction() {
        if (review == null) {
            messageLabel.setText("");
            mainController.switchToSubmitReview(user, course, review);
        } else {
            messageLabel.setText("You already reviewed this course");
        }
    }

    public void handleEditReviewAction() {
        if (review != null) {
            messageLabel.setText("");
            mainController.switchToSubmitReview(user, course, review);
        } else {
            messageLabel.setText("You have not reviewed this course");
        }
    }

    public void handleDeleteReviewAction() {
        if (review != null) {
            reviewService.deleteReview(review);
            review = null;
            refreshReviewList();
            messageLabel.setText("Your review was deleted successfully");
        } else {
            messageLabel.setText("You have not reviewed this course");
        }
    }

    public void handleGoBackAction() {
        messageLabel.setText("");
        mainController.switchToCourseSearch(user);
    }
}
