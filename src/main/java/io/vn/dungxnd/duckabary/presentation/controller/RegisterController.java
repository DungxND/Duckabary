package io.vn.dungxnd.duckabary.presentation.controller;

import static io.vn.dungxnd.duckabary.util.InputFieldUtils.maintainCaretPosition;
import static io.vn.dungxnd.duckabary.util.PasswordUtils.hashPassword;
import static io.vn.dungxnd.duckabary.util.ValidationUtils.validateRawPassword;

import io.vn.dungxnd.duckabary.domain.model.user.Manager;
import io.vn.dungxnd.duckabary.domain.service.ServiceManager;
import io.vn.dungxnd.duckabary.domain.service.user.ManagerService;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RegisterController {
    private final ManagerService managerService;
    @FXML private AnchorPane mainPane;

    @FXML private Button backToLoginBtn;

    @FXML private TextField emailInput;

    @FXML private TextField usernameInput;

    @FXML private TextField passwordText;

    @FXML private PasswordField passwordInput;

    @FXML private TextField confirmPasswordText;

    @FXML private PasswordField confirmPasswordInput;

    @FXML private Button openEye1PasswordBtn;

    @FXML private Button closeEye1PasswordBtn;

    @FXML private Button openEye2PasswordBtn;

    @FXML private Button closeEye2PasswordBtn;

    @FXML private Label errMessage;

    @FXML private Button registerBtn;

    @FXML private AnchorPane titlePane;

    public RegisterController() {
        this.managerService = ServiceManager.getInstance().getManagerService();
    }

    @FXML
    private void initialize() {
        backToLoginBtn.setOnAction(_ -> closeRegisterAndShowLogin());
        registerBtn.setOnAction(this::handleRegister);

        passwordText.textProperty().bindBidirectional(passwordInput.textProperty());
        confirmPasswordText.textProperty().bindBidirectional(confirmPasswordInput.textProperty());

        errMessage.setVisible(false);
    }

    @FXML
    private void showPassword(ActionEvent event) {
        Button source = (Button) event.getSource();
        if (source == openEye1PasswordBtn) {
            passwordText.setVisible(true);
            passwordInput.setVisible(false);
            openEye1PasswordBtn.setVisible(false);
            closeEye1PasswordBtn.setVisible(true);
            passwordText.requestFocus();
            maintainCaretPosition(passwordText);
        } else if (source == openEye2PasswordBtn) {
            confirmPasswordText.setVisible(true);
            confirmPasswordInput.setVisible(false);
            openEye2PasswordBtn.setVisible(false);
            closeEye2PasswordBtn.setVisible(true);
            confirmPasswordText.requestFocus();
            maintainCaretPosition(confirmPasswordText);
        }
    }

    @FXML
    private void hidePassword(ActionEvent event) {
        Button source = (Button) event.getSource();
        if (source == closeEye1PasswordBtn) {
            passwordText.setVisible(false);
            passwordInput.setVisible(true);
            openEye1PasswordBtn.setVisible(true);
            closeEye1PasswordBtn.setVisible(false);
            passwordInput.requestFocus();
            maintainCaretPosition(passwordInput);
        } else if (source == closeEye2PasswordBtn) {
            confirmPasswordText.setVisible(false);
            confirmPasswordInput.setVisible(true);
            openEye2PasswordBtn.setVisible(true);
            closeEye2PasswordBtn.setVisible(false);
            confirmPasswordInput.requestFocus();
            maintainCaretPosition(confirmPasswordInput);
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String email = emailInput.getText().trim();
        String username = usernameInput.getText().trim();
        String password = passwordInput.getText();
        String confirmPassword = confirmPasswordInput.getText();

        if (email.isEmpty()
                || username.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()) {
            showError("All fields are required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        validateRawPassword(password);

        try {
            Manager newManager = Manager.createManager(0, username, email, hashPassword(password));
            managerService.saveManager(newManager);
            NotificationController.show(
                    "Registration Successful!",
                    NotificationController.NotificationType.SUCCESS,
                    registerBtn
            );
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(e -> closeRegisterAndShowLogin());
            delay.play();
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errMessage.setText(message);
        errMessage.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> errMessage.setVisible(false));
        pause.play();
    }


    private void closeRegisterAndShowLogin() {
        Stage currentStage = (Stage) backToLoginBtn.getScene().getWindow();
        currentStage.close();
    }
}
