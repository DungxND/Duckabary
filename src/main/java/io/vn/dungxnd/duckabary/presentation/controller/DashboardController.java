package io.vn.dungxnd.duckabary.presentation.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private ImageView avatarButton;

    @FXML private VBox menuPane;

    @FXML private StackPane contentArea;

    @FXML private Button borrowedManagement;

    @FXML private Button btnExit;

    @FXML private Button docManagement;

    @FXML private Button userManagement;

    @FXML
    private void handleAvatarChange() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh đại diện");

        fileChooser
                .getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(avatarButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                Image avatarImage = new Image(selectedFile.toURI().toString());
                avatarButton.setImage(avatarImage);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText("Không thể tải ảnh");
                alert.setContentText("Định dạng ảnh không hợp lệ hoặc bị lỗi.");
                alert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuPane.setVisible(true);
        menuPane.setManaged(true);
        menuPane.getChildren()
                .filtered(node -> node instanceof Button)
                .forEach(
                        button -> {
                            ((Button) button)
                                    .setOnAction(
                                            e -> {
                                                String buttonText =
                                                        ((Button) e.getSource()).getText();
                                                contentArea.getChildren().clear();
                                                contentArea
                                                        .getChildren()
                                                        .add(
                                                                new javafx.scene.control.Label(
                                                                        buttonText + " selected"));
                                            });
                        });
    }
}
