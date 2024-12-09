package io.vn.dungxnd.duckabary.presentation.controller.page;

import io.vn.dungxnd.duckabary.domain.model.user.Manager;
import io.vn.dungxnd.duckabary.domain.service.ServiceManager;
import io.vn.dungxnd.duckabary.domain.service.user.ManagerService;
import io.vn.dungxnd.duckabary.domain.session.SessionManager;
import io.vn.dungxnd.duckabary.presentation.controller.component.EditInfoController;
import io.vn.dungxnd.duckabary.presentation.controller.component.NotificationController;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class SettingMgmtController {
    private final ManagerService managerService;
    private final Manager currentManager;

    @FXML private AnchorPane settingPane;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Button editUsernameBtn;
    @FXML private Button editEmailBtn;
    @FXML private ImageView avatarImg;
    @FXML private Button changeAvatarBtn;
    @FXML private Button changePasswordBtn;

    public SettingMgmtController() {
        this.managerService = ServiceManager.getInstance().getManagerService();
        this.currentManager = SessionManager.getCurrentManager();
    }

    public void initialize() {
        refreshManagerInfo();

        Circle clip = new Circle();
        clip.radiusProperty().bind(avatarImg.fitWidthProperty().divide(2));
        clip.centerXProperty().bind(avatarImg.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(avatarImg.fitHeightProperty().divide(2));
        avatarImg.setClip(clip);

        editUsernameBtn.setOnMouseClicked(_ -> openChangeUsernameModal());
        editEmailBtn.setOnMouseClicked(_ -> openChangeEmailModal());
        changePasswordBtn.setOnAction(_ -> handleChangePassword());
        changeAvatarBtn.setOnAction(_ -> handleAvatarChange());
    }

    @FXML
    private void handleChangePassword() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/auth/ChangePasswordModal.fxml"));
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Change Password");
            modalStage.setScene(new Scene(loader.load()));

            modalStage.showAndWait();
        } catch (IOException e) {
            LoggerUtils.error("Error loading change password modal", e);
        }
    }

    @FXML
    private void handleAvatarChange() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Avatar Image");
        fileChooser
                .getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Task<Void> avatarTask =
                    new Task<>() {
                        @Override
                        protected Void call() {
                            Manager manager =
                                    managerService.updateManagerAvatar(
                                            currentManager.managerId(), selectedFile);
                            SessionManager.setCurrentManager(manager);
                            return null;
                        }
                    };

            avatarTask.setOnSucceeded(
                    _ ->
                            Platform.runLater(
                                    () -> {
                                        try {
                                            Image avatarImage =
                                                    managerService.getAvatarImage(
                                                            currentManager.managerId());
                                            avatarImg.setImage(avatarImage);
                                            showNotification(
                                                    "Avatar updated successfully",
                                                    NotificationController.NotificationType
                                                            .SUCCESS);
                                        } catch (Exception e) {
                                            LoggerUtils.error("Error updating avatar", e);
                                            showNotification(
                                                    "Failed to update avatar",
                                                    NotificationController.NotificationType.ERROR);
                                        }
                                    }));

            avatarTask.setOnFailed(
                    _ ->
                            Platform.runLater(
                                    () -> {
                                        LoggerUtils.error(
                                                "Error saving avatar image",
                                                avatarTask.getException());
                                        showNotification(
                                                "Failed to update avatar",
                                                NotificationController.NotificationType.ERROR);
                                    }));

            new Thread(avatarTask).start();
        }
    }

    private void showNotification(String message, NotificationController.NotificationType type) {
        NotificationController.show(message, type, avatarImg);
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.play();
    }

    @FXML
    private void openChangeUsernameModal() {
        try {
            Manager currentManager = SessionManager.getCurrentManager();
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/component/EditInfoModal.fxml"));
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Change Username");
            modalStage.setScene(new Scene(loader.load()));

            EditInfoController<Manager> controller = loader.getController();
            controller.setEditInfo(
                    currentManager,
                    "username",
                    currentManager.username(),
                    _ -> {
                        Manager newManager =
                                new Manager(
                                        currentManager.managerId(),
                                        controller.getNewValue(),
                                        currentManager.email(),
                                        currentManager.hashedPassword(),
                                        currentManager.avatarPath());
                        managerService.saveManager(newManager);
                        SessionManager.setCurrentManager(newManager);
                    });
            controller.setOnEditComplete(() -> Platform.runLater(this::refreshManagerInfo));
            modalStage.showAndWait();
        } catch (IOException e) {
            LoggerUtils.error("Error loading username edit modal", e);
        }
    }

    private void refreshManagerInfo() {
        Manager manager = SessionManager.getCurrentManager();
        usernameLabel.setText(manager.username());
        emailLabel.setText(manager.email());
        avatarImg.setImage(managerService.getAvatarImage(manager.managerId()));
    }

    @FXML
    private void openChangeEmailModal() {
        try {
            Manager currentManager = SessionManager.getCurrentManager();
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/component/EditInfoModal.fxml"));
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Change Email");
            modalStage.setScene(new Scene(loader.load()));

            EditInfoController<Manager> controller = loader.getController();
            controller.setEditInfo(
                    currentManager,
                    "email",
                    currentManager.email(),
                    _ -> {
                        Manager newManager =
                                new Manager(
                                        currentManager.managerId(),
                                        currentManager.username(),
                                        controller.getNewValue(),
                                        currentManager.hashedPassword(),
                                        currentManager.avatarPath());
                        managerService.saveManager(newManager);
                        SessionManager.setCurrentManager(newManager);
                    });
            controller.setOnEditComplete(() -> Platform.runLater(this::refreshManagerInfo));

            modalStage.showAndWait();
        } catch (IOException e) {
            LoggerUtils.error("Error loading email edit modal", e);
        }
    }
}
