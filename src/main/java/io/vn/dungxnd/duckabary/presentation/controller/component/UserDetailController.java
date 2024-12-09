package io.vn.dungxnd.duckabary.presentation.controller.component;

import io.vn.dungxnd.duckabary.domain.model.user.User;
import io.vn.dungxnd.duckabary.domain.service.ServiceManager;
import io.vn.dungxnd.duckabary.domain.service.borrow.BorrowService;
import io.vn.dungxnd.duckabary.domain.service.user.UserService;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class UserDetailController {

    private final UserService userService;
    private final BorrowService borrowService;

    private Runnable onUserUpdated;

    @FXML private Label uidLabel;
    @FXML private Label usernameLabel;
    @FXML private Button editUsernameBtn;
    @FXML private Label firstNameLabel;
    @FXML private Button editFirstNameBtn;
    @FXML private Label lastNameLabel;
    @FXML private Button editLastNameBtn;
    @FXML private Label emailLabel;
    @FXML private Button editEmailBtn;
    @FXML private Label phoneLabel;
    @FXML private Button editPhoneBtn;
    @FXML private Label addressLabel;
    @FXML private Button editAddressBtn;
    @FXML private Label errorLabel;
    @FXML private Button deleteBtn;

    private User user;

    public UserDetailController() {
        this.userService = ServiceManager.getInstance().getUserService();
        this.borrowService = ServiceManager.getInstance().getBorrowService();
    }

    public void initialize() {
        errorLabel.setVisible(false);
        deleteBtn.setOnMouseClicked(_ -> handleDelete());
        editUsernameBtn.setOnMouseClicked(_ -> openChangeUsernameModal());
        editFirstNameBtn.setOnMouseClicked(_ -> openChangeFirstNameModal());
        editLastNameBtn.setOnMouseClicked(_ -> openChangeLastNameModal());
        editEmailBtn.setOnMouseClicked(_ -> openChangeEmailModal());
        editPhoneBtn.setOnMouseClicked(_ -> openChangePhoneModal());
        editAddressBtn.setOnMouseClicked(_ -> openChangeAddressModal());
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
            if (borrowService.getBorrowsByUser(user.id()) != null) {
                errorLabel.setText("User has borrow record, cannot delete");
                errorLabel.setVisible(true);
                LoggerUtils.info("User has borrow record, cannot delete");
                return;
            }
            errorLabel.setVisible(false);
            userService.deleteUser(user.id());
            if (onUserUpdated != null) {
                onUserUpdated.run();
            }
            closeModal();
        } catch (Exception e) {
            LoggerUtils.error("Failed to delete user", e);
        }
    }

    @FXML
    private void openChangeUsernameModal() {
        try {

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/component/EditInfoModal.fxml"));
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Change Username");
            modalStage.setScene(new Scene(loader.load()));

            EditInfoController<User> controller = loader.getController();
            controller.setEditInfo(
                    user,
                    "username",
                    user.username(),
                    _ -> {
                        User newUser =
                                User.createUser(
                                        user.id(),
                                        controller.getNewValue(),
                                        user.firstName(),
                                        user.lastName(),
                                        user.email(),
                                        user.phone(),
                                        user.address());
                        user = userService.saveUser(newUser);
                    });
            controller.setOnEditComplete(() -> Platform.runLater(this::getUserDetail));
            modalStage.showAndWait();
        } catch (IOException e) {
            LoggerUtils.error("Error loading username edit modal", e);
        }
    }

    @FXML
    private void openChangeFirstNameModal() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/component/EditInfoModal.fxml"));
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Change First Name");
            modalStage.setScene(new Scene(loader.load()));

            EditInfoController<User> controller = loader.getController();
            controller.setEditInfo(
                    user,
                    "first name",
                    user.firstName(),
                    _ -> {
                        User newUser =
                                User.createUser(
                                        user.id(),
                                        user.username(),
                                        controller.getNewValue(),
                                        user.lastName(),
                                        user.email(),
                                        user.phone(),
                                        user.address());
                        user = userService.saveUser(newUser);
                    });
            controller.setOnEditComplete(() -> Platform.runLater(this::getUserDetail));
            modalStage.showAndWait();
        } catch (IOException e) {
            LoggerUtils.error("Error loading first name edit modal", e);
        }
    }

    @FXML
    private void openChangeLastNameModal() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/component/EditInfoModal.fxml"));
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Change Last Name");
            modalStage.setScene(new Scene(loader.load()));

            EditInfoController<User> controller = loader.getController();
            controller.setEditInfo(
                    user,
                    "last name",
                    user.lastName(),
                    _ -> {
                        User newUser =
                                User.createUser(
                                        user.id(),
                                        user.username(),
                                        user.firstName(),
                                        controller.getNewValue(),
                                        user.email(),
                                        user.phone(),
                                        user.address());
                        user = userService.saveUser(newUser);
                    });
            controller.setOnEditComplete(() -> Platform.runLater(this::getUserDetail));
            modalStage.showAndWait();
        } catch (IOException e) {
            LoggerUtils.error("Error loading last name edit modal", e);
        }
    }

    @FXML
    private void openChangeEmailModal() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/component/EditInfoModal.fxml"));
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Change Email");
            modalStage.setScene(new Scene(loader.load()));

            EditInfoController<User> controller = loader.getController();
            controller.setEditInfo(
                    user,
                    "email",
                    user.email(),
                    _ -> {
                        User newUser =
                                User.createUser(
                                        user.id(),
                                        user.username(),
                                        user.firstName(),
                                        user.lastName(),
                                        controller.getNewValue(),
                                        user.phone(),
                                        user.address());
                        user = userService.saveUser(newUser);
                    });
            controller.setOnEditComplete(() -> Platform.runLater(this::getUserDetail));
            modalStage.showAndWait();
        } catch (IOException e) {
            LoggerUtils.error("Error loading email edit modal", e);
        }
    }

    @FXML
    private void openChangePhoneModal() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/component/EditInfoModal.fxml"));
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Change Phone");
            modalStage.setScene(new Scene(loader.load()));

            EditInfoController<User> controller = loader.getController();
            controller.setEditInfo(
                    user,
                    "phone",
                    user.phone(),
                    _ -> {
                        User newUser =
                                User.createUser(
                                        user.id(),
                                        user.username(),
                                        user.firstName(),
                                        user.lastName(),
                                        user.email(),
                                        controller.getNewValue(),
                                        user.address());
                        user = userService.saveUser(newUser);
                    });
            controller.setOnEditComplete(() -> Platform.runLater(this::getUserDetail));
            modalStage.showAndWait();
        } catch (IOException e) {
            LoggerUtils.error("Error loading phone edit modal", e);
        }
    }

    @FXML
    private void openChangeAddressModal() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/component/EditInfoModal.fxml"));
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Change Address");
            modalStage.setScene(new Scene(loader.load()));

            EditInfoController<User> controller = loader.getController();
            controller.setEditInfo(
                    user,
                    "address",
                    user.address(),
                    _ -> {
                        User newUser =
                                User.createUser(
                                        user.id(),
                                        user.username(),
                                        user.firstName(),
                                        user.lastName(),
                                        user.email(),
                                        user.phone(),
                                        controller.getNewValue());
                        user = userService.saveUser(newUser);
                    });
            controller.setOnEditComplete(() -> Platform.runLater(this::getUserDetail));
            modalStage.showAndWait();
        } catch (IOException e) {
            LoggerUtils.error("Error loading address edit modal", e);
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
