package io.vn.dungxnd.duckabary.presentation.controller.page;

import io.vn.dungxnd.duckabary.domain.model.user.Manager;
import io.vn.dungxnd.duckabary.domain.service.ServiceManager;
import io.vn.dungxnd.duckabary.domain.service.user.ManagerService;
import io.vn.dungxnd.duckabary.domain.session.SessionManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class SettingMgmtController {
    private final ManagerService managerService;
    private final Manager currentManager;

    @FXML private AnchorPane settingPane;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private ImageView setAvatarBtn;
    @FXML private Button changePasswordBtn;

    public SettingMgmtController() {
        this.managerService = ServiceManager.getInstance().getManagerService();
        this.currentManager = SessionManager.getCurrentManager();
    }

    public void initialize() {
        setManagerInfo();
    }

    private void setManagerInfo() {
        usernameLabel.setText(currentManager.username());
        emailLabel.setText(currentManager.email());
    }

    @FXML
    private void handleChangePassword() {}

    @FXML
    private void handleAvatarChange() {}

    @FXML
    private void openChangeUsernameModal() {}

    @FXML
    private void openChangeEmailModal() {}
}
