package io.vn.dungxnd.duckabary.presentation.controller.auth;

import static io.vn.dungxnd.duckabary.util.InputFieldUtils.maintainCaretPosition;

import io.vn.dungxnd.duckabary.domain.model.user.Manager;
import io.vn.dungxnd.duckabary.domain.service.PreferencesManager;
import io.vn.dungxnd.duckabary.domain.service.ServiceManager;
import io.vn.dungxnd.duckabary.domain.service.user.ManagerService;
import io.vn.dungxnd.duckabary.domain.session.SessionManager;
import io.vn.dungxnd.duckabary.presentation.ui.MainApplication;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {
    private final ManagerService managerService;

    @FXML private VBox title;
    @FXML private Label si;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordInput;
    @FXML private TextField passwordText;
    @FXML private Button openEyePasswordBtn;
    @FXML private Button closeEyePasswordBtn;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;
    @FXML private Label registerRedirectBtn;
    @FXML private HBox down;
    @FXML private VBox main;
    @FXML private CheckBox rememberLoginChkBox;

    public LoginController() {
        this.managerService = ServiceManager.getInstance().getManagerService();
    }

    @FXML
    private void initialize() {

        loginButton.setOnMouseClicked(event -> handleLogin());
        registerRedirectBtn.setOnMouseClicked(event -> openRegisterModal());
        passwordText.textProperty().bindBidirectional(passwordInput.textProperty());
        errorLabel.setVisible(false);
        usernameField.setOnKeyPressed(
                event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        handleLogin();
                    }
                });
        passwordInput.setOnKeyPressed(
                event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        handleLogin();
                    }
                });
        passwordText.setOnKeyPressed(
                event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        handleLogin();
                    }
                });

        String savedUsername = PreferencesManager.getRememberedUsername();
        if (savedUsername != null) {
            usernameField.setText(savedUsername);
            passwordInput.requestFocus();
            rememberLoginChkBox.setSelected(true);
        }
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordInput.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password are required");
            return;
        }

        try {
            Manager manager = managerService.authenticate(username, password).orElse(null);

            if (manager != null) {
                if (rememberLoginChkBox.isSelected()) {
                    PreferencesManager.saveLoginCredentials(username);
                } else {
                    PreferencesManager.clearLoginCredentials();
                }

                SessionManager.setCurrentManager(manager);
                MainApplication.getInstance().showMainView();
            } else {
                showError("Invalid username or password");
                passwordInput.clear();
            }
        } catch (Exception e) {
            showError("System Error: " + e.getMessage());
        }
    }

    private void openRegisterModal() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/auth/RegisterModal.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Register New Manager");
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Cannot open registration form: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> errorLabel.setVisible(false));
        pause.play();
    }

    @FXML
    private void showPassword() {
        passwordText.setVisible(true);
        passwordInput.setVisible(false);
        openEyePasswordBtn.setVisible(false);
        closeEyePasswordBtn.setVisible(true);
        passwordText.requestFocus();
        maintainCaretPosition(passwordText);
    }

    @FXML
    private void hidePassword() {
        passwordText.setVisible(false);
        passwordInput.setVisible(true);
        openEyePasswordBtn.setVisible(true);
        closeEyePasswordBtn.setVisible(false);
        passwordInput.requestFocus();
        maintainCaretPosition(passwordInput);
    }
}
