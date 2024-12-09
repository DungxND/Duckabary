package io.vn.dungxnd.duckabary.presentation.controller.component;

import io.vn.dungxnd.duckabary.domain.model.user.User;
import io.vn.dungxnd.duckabary.domain.service.ServiceManager;
import io.vn.dungxnd.duckabary.domain.service.user.UserService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddUserController {
    private final UserService userService;
    private Runnable onUserAdded;

    @FXML private TextField emailInput;
    @FXML private TextField usernameInput;
    @FXML private TextField firstNameInput;
    @FXML private TextField lastNameInput;
    @FXML private TextField phoneInput;
    @FXML private TextField addressInput;
    @FXML private Button saveBtn;
    @FXML private Label errorLabel;

    public AddUserController() {
        this.userService = ServiceManager.getInstance().getUserService();
    }

    public void initialize() {
        errorLabel.setVisible(false);
        saveBtn.setOnMouseClicked(_ -> handleSave());
    }

    @FXML
    private void handleSave() {
        if (usernameInput.getText().trim().isEmpty() || lastNameInput.getText().trim().isEmpty()) {
            errorLabel.setText("Please fill in all required fields");
            errorLabel.setVisible(true);
            return;
        }

        try {
            User newUser =
                    User.createUser(
                            0,
                            usernameInput.getText().trim(),
                            firstNameInput.getText().trim(),
                            lastNameInput.getText().trim(),
                            emailInput.getText().trim(),
                            phoneInput.getText().trim(),
                            addressInput.getText().trim());

            userService.saveUser(newUser);

            if (onUserAdded != null) {
                onUserAdded.run();
            }

            closeModal();
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
            errorLabel.setVisible(true);
        }
    }

    private void closeModal() {
        ((Stage) saveBtn.getScene().getWindow()).close();
    }

    public void setOnUserAdded(Runnable callback) {
        this.onUserAdded = callback;
    }
}
