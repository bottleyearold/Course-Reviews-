package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.function.UnaryOperator;

public class AddCourseController {
    private MainController mainController;
    private CourseService courseService;
    private User user;

    @FXML
    private Label courseName;
    @FXML
    private TextField subjectField;
    @FXML
    private TextField numberField;
    @FXML
    private TextField titleField;
    @FXML
    private Label messageLabel;

    public void initialize(){
        courseService = new CourseService(new DatabaseDriver(new Configuration()));
        addFieldLengthRestriction();
    }
    private void addFieldLengthRestriction(){
        subjectField.setTextFormatter(new TextFormatter<>(createLimitingUnaryOperator(4)));
        numberField.setTextFormatter(new TextFormatter<>(createLimitingUnaryOperator(4)));
        titleField.setTextFormatter(new TextFormatter<>(createLimitingUnaryOperator(50)));
    }

    private UnaryOperator<TextFormatter.Change> createLimitingUnaryOperator(int limit){
        return change -> {
            if(change.getControlNewText().length() <= limit){
                return change;
            }else{
                return null;
            }
        };
    }

    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }

    public void setUser(User user){
        this.user = user;
        this.courseName.setText("Logged in as: " + user.getUsername());
    }

    public void handleAddAction() {
        try {
            var subject = subjectField.getText().toUpperCase();
            var number = numberField.getText();
            var title = titleField.getText();
            verifyInfo(subject, number, title);
            courseService.addCourse(new Course(subject, Integer.parseInt(number), title));
            messageLabel.setText("Course created successfully");
            clearTextFields();
        } catch (InvalidSubjectException e) {
            messageLabel.setText("Subject must be 2-4 alphabetic characters");
        } catch (NumberFormatException | InvalidNumException e) {
            messageLabel.setText("Number must be 4 numeric characters");
        } catch (InvalidTitleException e) {
            messageLabel.setText("Title must be at least 1 character");
        } catch (CourseExistAlreadyException e) {
            messageLabel.setText("Course already exists");
        }
    }

    private void verifyInfo(String subject, String number, String title) throws InvalidSubjectException, InvalidNumException, InvalidTitleException {
        if (!subject.matches("[a-zA-Z]{2,4}")) {
            throw new InvalidSubjectException();
        }
        if (!number.matches("\\d{4}")) {
            throw new InvalidNumException();
        }
        if (title.length() < 1 || title.length() > 50) {
            throw new InvalidTitleException();
        }
    }

    private void clearTextFields() {
        subjectField.clear();
        numberField.clear();
        titleField.clear();
    }

    public void handleGoBackAction() {
        clearTextFields();
        messageLabel.setText("");
        mainController.switchToCourseSearch(user);
    }


}
