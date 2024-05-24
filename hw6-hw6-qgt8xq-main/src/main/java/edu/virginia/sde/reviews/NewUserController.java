package edu.virginia.sde.reviews;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class NewUserController {
    private MainController mainController;
    private UserInferface userInfoService;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label messageLabel;

    @FXML
    private void handleCreateAccountAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        try {
            validateUserInfo(username, password, confirmPassword);
            userInfoService.addUser(new User(username, password));
            messageLabel.setText("User creation successful (navigate to login)");
            clearTextFields();
        } catch (InvalidUsernameException e) {
            messageLabel.setText("Username is empty");
        } catch (UsernameNotAvailableException e) {
            messageLabel.setText(String.format("Username '%s' is already in use", username));
        } catch (IncorrectPassException e) {
            messageLabel.setText("Password must be at least 8 characters");
        } catch (InvalidPassException e) {
            messageLabel.setText("Passwords do not match");
        }
    }

    private void validateUserInfo(String username, String password, String confirmPassword) throws InvalidUsernameException, UsernameNotAvailableException, InvalidPassException, IncorrectPassException {
        if (username.isEmpty()) {
            throw new InvalidUsernameException();
        }
        if (!userInfoService.isUsernameAvailable(username)) {
            throw new UsernameNotAvailableException();
        }
        if (password.length() < 8) {
            throw new IncorrectPassException();
        }
        if (!confirmPassword.equals(password)) {
            throw new InvalidPassException();
        }
    }

    private void clearTextFields() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    @FXML
    private void handleGoBackAction() {
        messageLabel.setText("");
        clearTextFields();
        mainController.switchToLogin();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setUserInfoService(UserInferface userInfoService) {
        this.userInfoService = userInfoService;
    }
}


