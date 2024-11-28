package io.vn.dungxnd.duckabary.presentation.controller;

import io.vn.dungxnd.duckabary.domain.service.ServiceManager;
import io.vn.dungxnd.duckabary.domain.service.user.ManagerService;
import io.vn.dungxnd.duckabary.presentation.ui.MainApplication;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class LoginController {
    private final ManagerService managerService;

    @FXML private VBox title;
    @FXML private Label si;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;
    @FXML private Label registerRedirectBtn;
    @FXML private HBox down;
    @FXML private VBox main;

    public LoginController() {
        this.managerService = ServiceManager.getInstance().getManagerService();
    }

    @FXML
    private void initialize() {

        loginButton.setOnAction(event -> handleLogin());
        registerRedirectBtn.setOnMouseClicked(event -> handleRegister());
        registerRedirectBtn.setFont(Font.font("Montserrat", 700));

        errorLabel.setVisible(false);
        passwordField.setOnKeyPressed(
                event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        handleLogin();
                    }
                });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password are required");
            return;
        }

        try {
            if (managerService.validateCredentials(username, password)) {
                MainApplication.getInstance().showMainView();
            } else {
                showError("Invalid username or password");
                passwordField.clear();
            }
        } catch (Exception e) {
            showError("System Error: " + e.getMessage());
        }
    }

    private void handleRegister() {
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

    private boolean validateCredentials(String username, String password) {
        return username != null && !username.isEmpty() && password != null && !password.isEmpty();
    }
}
