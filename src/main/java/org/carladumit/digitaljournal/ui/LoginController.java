package org.carladumit.digitaljournal.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.carladumit.digitaljournal.dao.impl.JournalEntryDAOJdbc;
import org.carladumit.digitaljournal.dao.impl.UserDAOJdbc;
import org.carladumit.digitaljournal.exceptions.DatabaseException;
import org.carladumit.digitaljournal.exceptions.InvalidPasswordException;
import org.carladumit.digitaljournal.exceptions.UserAlreadyExistsException;
import org.carladumit.digitaljournal.exceptions.UserNotFoundException;
import org.carladumit.digitaljournal.service.JournalService;
import org.carladumit.digitaljournal.service.UserService;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label formTitleLabel;
    @FXML
    private Label toggleLabel;
    @FXML
    private Button primaryActionButton;
    @FXML
    private Button secondaryActionButton;

    private final UserService userService;
    private final JournalService journalService;

    private boolean signUpMode = false;

    public LoginController() {

        var userDAO = new UserDAOJdbc();
        var journalDAO = new JournalEntryDAOJdbc();

        this.userService = new UserService(userDAO);
        this.journalService = new JournalService(journalDAO, userService);
    }


    @FXML
    private void initialize() {
        updateFormMode();
    }

    @FXML
    private void handlePrimaryAction() {

        if (signUpMode) {
            handleSignUp();
        } else {
            handleLogin();
        }
    }

    @FXML
    private void handleSecondaryAction() {

        signUpMode = !signUpMode;

        clearFields();
        clearStatus();

        updateFormMode();
    }

    @FXML
    private void toggleMode() {

        signUpMode = !signUpMode;

        clearFields();
        clearStatus();

        updateFormMode();
    }

    private void updateFormMode() {

        if (signUpMode) {

            if (formTitleLabel != null) {
                formTitleLabel.setText("Create your account");
            }

            if (confirmPasswordLabel != null) {
                confirmPasswordLabel.setVisible(true);
                confirmPasswordLabel.setManaged(true);
            }

            if (confirmPasswordField != null) {
                confirmPasswordField.setVisible(true);
                confirmPasswordField.setManaged(true);
            }

            if (primaryActionButton != null) {
                primaryActionButton.setText("Create Account");
            }

            if (secondaryActionButton != null) {
                secondaryActionButton.setText("Back to Login");
            }

            if (toggleLabel != null) {
                toggleLabel.setText("Already have an account? Log in.");
            }

        } else {
            if (formTitleLabel != null) {
                formTitleLabel.setText("Welcome back");
            }
            if (confirmPasswordLabel != null) {
                confirmPasswordLabel.setVisible(false);
                confirmPasswordLabel.setManaged(false);
            }
            if (confirmPasswordField != null) {
                confirmPasswordField.setVisible(false);
                confirmPasswordField.setManaged(false);
            }
            if (primaryActionButton != null) {
                primaryActionButton.setText("Log In");
            }
            if (secondaryActionButton != null) {
                secondaryActionButton.setText("Sign Up");
            }
            if (toggleLabel != null) {
                toggleLabel.setText("Don't have an account? Sign up.");
            }
        }
    }

    private void handleLogin() {
        String username = usernameField.getText().trim().toLowerCase();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        try {
            userService.login(username, password);
            openMainJournalView();
        } catch (UserNotFoundException e) {
            showError("User not found.");
        } catch (InvalidPasswordException e) {
            showError("Incorrect password.");
        } catch (DatabaseException e) {
            showError("Database unavailable.");
        }
    }

    private void handleSignUp() {

        String username = usernameField.getText().trim().toLowerCase();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            confirmPasswordField.requestFocus();
            return;
        }

        try {
            userService.register(username, password);
            userService.login(username, password);
            openMainJournalView();
        } catch (UserAlreadyExistsException e) {
            showError("Username already exists.");
        } catch (DatabaseException e) {
            showError("Database unavailable.");
        } catch (InvalidPasswordException | UserNotFoundException e) {
            showError("Error logging in after registration.");
        }
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    private void clearStatus() {
        statusLabel.setText("");
    }

    private void showError(String message) {
        statusLabel.getStyleClass().removeAll("status-success", "status-error");
        statusLabel.getStyleClass().add("status-error");
        statusLabel.setText(message);
    }

    private void openMainJournalView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainJournalView.fxml"));
            Scene scene = new Scene(loader.load(), 900, 650);

            MainJournalController controller = loader.getController();
            controller.initServices(userService, journalService);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Digital Journal - Home");
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading main view.");
        }
    }
}