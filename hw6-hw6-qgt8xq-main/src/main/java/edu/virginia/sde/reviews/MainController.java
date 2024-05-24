package edu.virginia.sde.reviews;

import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainController {
    private final Stage primaryStage;
    private final DatabaseDriver databaseDriver;
    private Scene loginScene;
    private Scene newUserScene;
    private Scene addCourseScene;
    private Scene courseReviewsScene;
    private Scene submitReviewScene;
    private Scene myReviewScene;
    private Scene courseSelecionScene;

    private CourseSearchController courseSearchController;
    private AddCourseController addCourseController;
    private CourseReviewController courseReviewsController;
    private MyReviewController myReviewsController;
    private SubmitReviewController submitReviewController;

    public MainController(Stage primaryStage, DatabaseDriver databaseDriver){
        this.primaryStage = primaryStage;
        this.databaseDriver = databaseDriver;
        sceneBuilder();
    }
    private void sceneBuilder() {
        try {
            var userInfoService = new UserInferface(databaseDriver);
            var courseService = new CourseService(databaseDriver);
            var reviewService = new ReviewService(databaseDriver);

            var loginLoader = new FXMLLoader(getClass().getResource("LoginScreen.fxml"));
            loginScene = new Scene(loginLoader.load(), 500, 500);
            LogInController loginController = loginLoader.getController();
            loginController.setMainController(this);
            loginController.setUserInterface(userInfoService);
//
            var newUserLoader = new FXMLLoader(getClass().getResource("NewUserScreen.fxml"));
            newUserScene = new Scene(newUserLoader.load(), 500, 500);
            NewUserController newUserController = newUserLoader.getController();
            newUserController.setMainController(this);
            newUserController.setUserInfoService(userInfoService);

            var courseSearchLoader = new FXMLLoader(getClass().getResource("CourseSearchScreen.fxml"));
            courseSelecionScene = new Scene(courseSearchLoader.load(), 800, 500);
            courseSearchController = courseSearchLoader.getController();
            courseSearchController.setMainController(this);
            courseSearchController.setCourseService(courseService);
            courseSearchController.initialize();
//
            var addCourseLoader = new FXMLLoader(getClass().getResource("AddCoursesScreen.fxml"));
            addCourseScene = new Scene(addCourseLoader.load(), 500, 400);
            addCourseController = addCourseLoader.getController();
            addCourseController.setMainController(this);

            var courseReviewsLoader = new FXMLLoader(getClass().getResource("CourseReviewScreen.fxml"));
            courseReviewsScene = new Scene(courseReviewsLoader.load(), 700, 500);
            courseReviewsController = courseReviewsLoader.getController();
            courseReviewsController.setMainController(this);
            courseReviewsController.setReviewService(reviewService);
//
            var submitReviewLoader = new FXMLLoader(getClass().getResource("SubmitReviewScreen.fxml"));
            submitReviewScene = new Scene(submitReviewLoader.load(), 400, 450);
            submitReviewController = submitReviewLoader.getController();
            submitReviewController.setMainController(this);
            submitReviewController.setReviewService(reviewService);
//
            var myReviewsLoader = new FXMLLoader(getClass().getResource("MyReviewScreen.fxml"));
            myReviewScene = new Scene(myReviewsLoader.load(), 700, 400);
            myReviewsController = myReviewsLoader.getController();
            myReviewsController.setMainController(this);
            myReviewsController.setReviewService(reviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load scenes", e);
        }
    }
    public void switchToLogin() {
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }
    public void switchToAddCourse(User user) {
        primaryStage.setScene(addCourseScene);
        addCourseController.setUser(user);
        primaryStage.show();
    }

    public void switchToNewUserSetup() {
        primaryStage.setScene(newUserScene);
        primaryStage.show();
    }

    public void switchToCourseSearch(User user) {
        primaryStage.setScene(courseSelecionScene);
        courseSearchController.setUser(user);
        courseSearchController.refreshCourseList();
        primaryStage.show();
    }


    public void switchToCourseReviews(User user, Course course) {
        primaryStage.setScene(courseReviewsScene);
        courseReviewsController.setUser(user);
        courseReviewsController.setCourse(course);
        if (courseReviewsController.isReviewListInitialized()){
            courseReviewsController.refreshReviewList();
        }else{
            courseReviewsController.initializeReviewListView();
        }
        primaryStage.show();
    }
    public void switchToMyReviews(User user) {
        primaryStage.setScene(myReviewScene);
        myReviewsController.setUser(user);  // Use the field directly
        if (myReviewsController.isReviewListViewInitialized()) {
            myReviewsController.refreshReviewList();
        } else {
            myReviewsController.initializeReviewListView();
        }
        primaryStage.show();
    }
    public void switchToSubmitReview(User user, Course course, Review review) {
        submitReviewController.setUser(user);
        submitReviewController.setCourse(course);
        submitReviewController.setUserReview(review);
        submitReviewController.initializeFields();
        primaryStage.setScene(submitReviewScene);
        primaryStage.show();
    }



}
