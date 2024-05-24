package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class LogInController {
    private MainController mainController;
    private UserInferface userInterface;

    @FXML
    private TextField usernameF;

    @FXML
    private PasswordField passwordF;

    @FXML
    private Label errorMessage;

    @FXML
    private Button exitButton;

    protected void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    protected void setUserInterface(UserInferface userInterface){
        this.userInterface = userInterface;
    }

    @FXML
    private void handleLoginAction() {
        String username = usernameF.getText();
        String password = passwordF.getText();
        try {
            User user = userInterface.getUser(username, password);
            clearControls();
            mainController.switchToCourseSearch(user);
        } catch (InvalidUsernameException e) {
            errorMessage.setText("Username is incorrect or does not exist");
        } catch (IncorrectPassException e) {
            errorMessage.setText("Password is incorrect");
        }
    }

    @FXML
    private void handleNewUserAction() {
        clearControls();
        mainController.switchToNewUserSetup();
    }

    private void clearControls() {
        errorMessage.setText("");
        usernameF.clear();
        passwordF.clear();
    }

    @FXML
    private void handleExitAction() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }



}
