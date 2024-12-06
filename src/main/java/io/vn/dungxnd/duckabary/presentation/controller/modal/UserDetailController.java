package io.vn.dungxnd.duckabary.presentation.controller.modal;

import io.vn.dungxnd.duckabary.domain.model.user.User;
import io.vn.dungxnd.duckabary.domain.service.ServiceManager;
import io.vn.dungxnd.duckabary.domain.service.user.UserService;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UserDetailController {

    private final UserService userService;

    private Runnable onUserUpdated;
    @FXML private Label uidLabel;
    @FXML private Label usernameLabel;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label addressLabel;
    @FXML private Button deleteBtn;

    private User user;

    public UserDetailController() {
        this.userService = ServiceManager.getInstance().getUserService();
    }

    public void initialize() {
        deleteBtn.setOnMouseClicked(event -> handleDelete());
    }

    private void getUserDetail() {
        uidLabel.setText(String.valueOf(user.id()));
        usernameLabel.setText(user.username());
        firstNameLabel.setText(user.firstName());
        lastNameLabel.setText(user.lastName());
        emailLabel.setText(user.email());
        phoneLabel.setText(user.phone());
        addressLabel.setText(user.address());
    }

    @FXML
    private void handleDelete() {
        try {
            userService.deleteUser(user.id());
            if (onUserUpdated != null) {
                onUserUpdated.run();
            }
            closeModal();
        } catch (Exception e) {
            LoggerUtils.error("Failed to delete user", e);
        }
    }

    private void closeModal() {
        ((Stage) deleteBtn.getScene().getWindow()).close();
    }

    public void setUser(User user) {
        this.user = user;
        getUserDetail();
    }

    public void setOnUserUpdated(Runnable callback) {
        this.onUserUpdated = callback;
    }
}
