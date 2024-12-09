package io.vn.dungxnd.duckabary.presentation.controller.auth;

import static io.vn.dungxnd.duckabary.util.InputFieldUtils.maintainCaretPosition;
import static io.vn.dungxnd.duckabary.util.PasswordUtils.hashPassword;

import io.vn.dungxnd.duckabary.domain.model.user.Manager;
import io.vn.dungxnd.duckabary.domain.service.ServiceManager;
import io.vn.dungxnd.duckabary.domain.service.user.ManagerService;
import io.vn.dungxnd.duckabary.domain.session.SessionManager;
import io.vn.dungxnd.duckabary.util.LoggerUtils;
import io.vn.dungxnd.duckabary.util.ValidationUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChangePasswordController {
    private final ManagerService managerService;
    @FXML private PasswordField oldPasswordInput;
    @FXML private TextField oldPasswordText;
    @FXML private Button openEye1PasswordBtn;
    @FXML private Button closeEye1PasswordBtn;
    @FXML private PasswordField newPasswordInput;
    @FXML private TextField newPasswordText;
    @FXML private Button openEye2PasswordBtn;
    @FXML private Button closeEye2PasswordBtn;
    @FXML private PasswordField confirmPasswordInput;
    @FXML private TextField confirmPasswordText;
    @FXML private Button openEye3PasswordBtn;
    @FXML private Button closeEye3PasswordBtn;
    @FXML private Label errorLabel;
    @FXML private Button saveBtn;

    public ChangePasswordController() {
        this.managerService = ServiceManager.getInstance().getManagerService();
    }

    public void initialize() {
        oldPasswordInput.textProperty().bindBidirectional(oldPasswordText.textProperty());
        newPasswordInput.textProperty().bindBidirectional(newPasswordText.textProperty());
        confirmPasswordInput.textProperty().bindBidirectional(confirmPasswordText.textProperty());

        oldPasswordText.setVisible(false);
        newPasswordText.setVisible(false);
        confirmPasswordText.setVisible(false);
        closeEye1PasswordBtn.setVisible(false);
        closeEye2PasswordBtn.setVisible(false);
        closeEye3PasswordBtn.setVisible(false);

        openEye1PasswordBtn.setOnAction(_ -> toggleOldPasswordVisibility());
        closeEye1PasswordBtn.setOnAction(_ -> toggleOldPasswordVisibility());
        openEye2PasswordBtn.setOnAction(_ -> toggleNewPasswordVisibility());
        closeEye2PasswordBtn.setOnAction(_ -> toggleNewPasswordVisibility());
        openEye3PasswordBtn.setOnAction(_ -> toggleConfirmPasswordVisibility());
        closeEye3PasswordBtn.setOnAction(_ -> toggleConfirmPasswordVisibility());

        errorLabel.setVisible(false);
        saveBtn.setOnAction(_ -> handleChangePassword());
    }

    @FXML
    private void handleChangePassword() {
        String oldPassword = oldPasswordInput.getText();
        String newPassword = newPasswordInput.getText();
        String confirmPassword = confirmPasswordInput.getText();

        // Validate inputs
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields are required");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showError("New passwords do not match");
            return;
        }

        try {
            ValidationUtils.validateRawPassword(newPassword);
        } catch (Exception e) {
            showError(e.getMessage());
            return;
        }

        try {
            Manager currentManager = SessionManager.getCurrentManager();

            if (!managerService.isValidCredentials(currentManager.username(), oldPassword)) {
                showError("Current password is incorrect");
                return;
            }

            Manager updatedManager =
                    new Manager(
                            currentManager.managerId(),
                            currentManager.username(),
                            currentManager.email(),
                            hashPassword(newPassword),
                            currentManager.avatarPath());

            managerService.saveManager(updatedManager);
            SessionManager.setCurrentManager(updatedManager);

            ((Stage) saveBtn.getScene().getWindow()).close();

        } catch (Exception e) {
            LoggerUtils.error("Error changing password", e);
            showError("Failed to change password");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    @FXML
    private void toggleOldPasswordVisibility() {
        hideShowPassword(
                oldPasswordInput, oldPasswordText, openEye1PasswordBtn, closeEye1PasswordBtn);
    }

    @FXML
    private void toggleNewPasswordVisibility() {
        hideShowPassword(
                newPasswordInput, newPasswordText, openEye2PasswordBtn, closeEye2PasswordBtn);
    }

    @FXML
    private void toggleConfirmPasswordVisibility() {
        hideShowPassword(
                confirmPasswordInput,
                confirmPasswordText,
                openEye3PasswordBtn,
                closeEye3PasswordBtn);
    }

    private void hideShowPassword(
            PasswordField hiddenText,
            TextField visiblePassword,
            Button openEyeBtn,
            Button closeEyeBtn) {
        hiddenText.setVisible(!hiddenText.isVisible());
        visiblePassword.setVisible(!visiblePassword.isVisible());
        openEyeBtn.setVisible(!openEyeBtn.isVisible());
        closeEyeBtn.setVisible(!closeEyeBtn.isVisible());
        if (hiddenText.isVisible()) {
            hiddenText.requestFocus();
            maintainCaretPosition(hiddenText);
        } else {
            visiblePassword.requestFocus();
            maintainCaretPosition(visiblePassword);
        }
    }
}
