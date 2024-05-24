package edu.virginia.sde.reviews;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.converter.IntegerStringConverter;
public class CourseSearchController {
    private MainController mainController;
    private CourseService courseService;
    private User user;

    @FXML
    private Label userLabel;

    @FXML
    private TextField subjectFilter, numberFilter, titleFilter;

    @FXML
    private ListView<Course> courseListView;

    private final ObservableList<Course> courses = FXCollections.observableArrayList();
    private FilteredList<Course> filteredData;

    public void initialize() {
        filteredData = new FilteredList<>(courses, p -> true);
        courseListView.setItems(filteredData);
        courseListView.setCellFactory(lv -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);
                setText(empty ? null : course.toString());
            }
        });

        courseListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !courseListView.getSelectionModel().isEmpty()) {
                Course selectedCourse = courseListView.getSelectionModel().getSelectedItem();
                mainController.switchToCourseReviews(user, selectedCourse);
            }
        });

        addFilterLengthRestrictions();
        addFilterListeners();
    }

    private void addFilterLengthRestrictions() {
        subjectFilter.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 4 ? change : null));
        numberFilter.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null, change ->
                change.getControlNewText().matches("\\d{0,4}") ? change : null));
        titleFilter.setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().length() <= 50 ? change : null));
    }

    private void addFilterListeners() {
        subjectFilter.textProperty().addListener((observable, oldValue, newValue) -> updateFilter());
        numberFilter.textProperty().addListener((observable, oldValue, newValue) -> updateFilter());
        titleFilter.textProperty().addListener((observable, oldValue, newValue) -> updateFilter());
    }

    private void updateFilter() {
        filteredData.setPredicate(course -> {
            if (!subjectFilter.getText().isEmpty() && !course.getSubject().toLowerCase().contains(subjectFilter.getText().toLowerCase()))
                return false;
            if (!numberFilter.getText().isEmpty() && !String.valueOf(course.getNumber()).equals(numberFilter.getText()))
                return false;
            if (!titleFilter.getText().isEmpty() && !course.getTitle().toLowerCase().contains(titleFilter.getText().toLowerCase()))
                return false;
            return true;
        });
    }

    public void refreshCourseList() {
        courses.clear();
        courses.addAll(courseService.getCourses());
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    public void setUser(User user) {
        this.user = user;
        userLabel.setText("Logged in as: " + user.getUsername());
    }

    public void refreshCourses(){
        courses.clear();
        courses.addAll(courseService.getCourses());
    }

    public void handleAddCourseAction(){
        mainController.switchToAddCourse(user);
    }

    public void handleMyReviewsAction(){
        mainController.switchToMyReviews(user);
    }

    public void handleLogoutAction(){
        mainController.switchToLogin();
    }
}
