package io.vn.dungxnd.duckabary.presentation.controller;

import static javafx.application.Platform.exit;

import io.vn.dungxnd.duckabary.domain.service.PreferencesManager;
import io.vn.dungxnd.duckabary.domain.session.SessionManager;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;

import java.io.IOException;
import java.util.Objects;

public class MainController {
    @FXML private ImageView avatarImg;
    @FXML private VBox sideMenu;
    @FXML private AnchorPane mainContainer;
    @FXML private Label usernameLabel;

    @FXML private VBox settingMenuBtn;
    @FXML private VBox userMgmtLabelBox;
    @FXML private VBox libraryMgmtLabelBox;
    @FXML private VBox borrowMgmtLabelBox;
    @FXML private VBox userMgmtMenuBtn;
    @FXML private VBox libraryMgmtMenuBtn;
    @FXML private VBox borrowMgmtMenuBtn;
    @FXML private SVGPath menuUserIcon;
    @FXML private SVGPath menuBookIcon;
    @FXML private SVGPath menuBorrowIcon;
    @FXML private Button logoutBtn;

    public void initialize() {
        Circle clip = new Circle();
        clip.radiusProperty().bind(avatarImg.fitWidthProperty().divide(2));
        clip.centerXProperty().bind(avatarImg.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(avatarImg.fitHeightProperty().divide(2));
        avatarImg.setClip(clip);

        usernameLabel.textProperty().bind(SessionManager.currentUsernameProperty());
        avatarImg.imageProperty().bind(SessionManager.currentAvatarProperty());
        setupEventHandlers();

        loadSettingPage();
    }

    @FXML
    private void setupEventHandlers() {
        settingMenuBtn.setOnMouseClicked(event -> handleSettingsClick());
        userMgmtMenuBtn.setOnMouseClicked(event -> selectSection(userMgmtLabelBox, menuUserIcon));
        libraryMgmtMenuBtn.setOnMouseClicked(
                event -> selectSection(libraryMgmtLabelBox, menuBookIcon));
        borrowMgmtMenuBtn.setOnMouseClicked(
                event -> selectSection(borrowMgmtLabelBox, menuBorrowIcon));
        logoutBtn.setOnMouseClicked(event -> handleLogout());
    }

    private void handleSettingsClick() {
        resetStyles();
        loadSettingPage();
    }

    private void selectSection(VBox labelBox, SVGPath icon) {
        resetStyles();

        if (labelBox == null) {
            loadSettingPage();
            return;
        }

        for (Node node : labelBox.getChildren()) {
            if (node instanceof Label) {
                node.getStyleClass().add("bold-text");
            }
        }

        icon.getStyleClass().add("selected-icon");

        if (labelBox == userMgmtLabelBox) {
            loadUserMgmtPage();
        } else if (labelBox == libraryMgmtLabelBox) {
            loadLibraryMgmtPage();
        } else if (labelBox == borrowMgmtLabelBox) {
            loadBorrowMgmtPage();
        }
    }

    private void resetStyles() {
        for (Node node : userMgmtLabelBox.getChildren()) {
            if (node instanceof Label) {
                node.getStyleClass().remove("bold-text");
            }
        }
        for (Node node : libraryMgmtLabelBox.getChildren()) {
            if (node instanceof Label) {
                node.getStyleClass().remove("bold-text");
            }
        }
        for (Node node : borrowMgmtLabelBox.getChildren()) {
            if (node instanceof Label) {
                node.getStyleClass().remove("bold-text");
            }
        }

        menuUserIcon.getStyleClass().remove("selected-icon");
        menuBookIcon.getStyleClass().remove("selected-icon");
        menuBorrowIcon.getStyleClass().remove("selected-icon");
    }

    public void loadSettingPage() {
        loadFXML("/fxml/page/SettingMgmt.fxml");
    }

    public void loadUserMgmtPage() {
        loadFXML("/fxml/page/UserMgmt.fxml");
    }

    public void loadLibraryMgmtPage() {
        loadFXML("/fxml/page/LibraryMgmt.fxml");
    }

    public void loadBorrowMgmtPage() {
        loadFXML("/fxml/page/BorrowMgmt.fxml");
    }

    private void loadFXML(String fxmlFile) {
        try {
            Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));

            mainContainer.getChildren().setAll(node);

            AnchorPane.setTopAnchor(node, 3.0);
            AnchorPane.setBottomAnchor(node, 3.0);
            AnchorPane.setLeftAnchor(node, 3.0);
            AnchorPane.setRightAnchor(node, 3.0);
        } catch (IOException e) {
            e.printStackTrace();
            LoggerUtils.error("Failed to load FXML file: " + fxmlFile, e);
        }
    }

    @FXML
    private void handleLogout() {
        SessionManager.clearSession();
        PreferencesManager.clearLoginCredentials();
        exit();
        LoggerUtils.info("User logged out successfully.");
    }
}
