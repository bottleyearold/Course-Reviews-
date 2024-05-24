package edu.virginia.sde.reviews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

public class MyReviewController {
    private MainController mainController;
    private ReviewService reviewService;
    private User user;

    @FXML
    private Label userLabel;

    @FXML
    private ListView<Review> reviewList;

    private final ObservableList<Review> reviews = FXCollections.observableArrayList();
    private boolean initialized = false;

    public void initializeReviewListView() {
        reviewList.setItems(reviews);
        reviewList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Review review, boolean empty) {
                super.updateItem(review, empty);
                setText(empty ? null : reviewService.getReviewString(review));
            }
        });

        reviewList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !reviewList.getSelectionModel().isEmpty()) {
                Review selectedReview = reviewList.getSelectionModel().getSelectedItem();
                mainController.switchToCourseReviews(user, reviewService.getCourseById(selectedReview.getCourseID()));
            }
        });
        initialized = true;
    }

    public boolean isReviewListViewInitialized() {
        return initialized;
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

    public void refreshReviewList() {
        reviews.clear();
        reviews.addAll(reviewService.getReviewsByUserId(user.getId()));
    }

    public void handleGoBackAction() {
        mainController.switchToCourseSearch(user);
    }
}



