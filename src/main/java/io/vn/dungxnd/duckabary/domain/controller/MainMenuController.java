package io.vn.dungxnd.duckabary.domain.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML private ImageView avaButton;

    @FXML private AnchorPane borOption;

    @FXML private AnchorPane brBg;

    @FXML private AnchorPane docBg;

    @FXML private AnchorPane docOption;

    @FXML private AnchorPane usOption;

    @FXML private AnchorPane userBg;

    public void enterDocManage() {
        userBg.setVisible(false);
        brBg.setVisible(false);
        docBg.setVisible(true);
    }

    public void enterUserManage() {
        docBg.setVisible(false);
        brBg.setVisible(false);
        userBg.setVisible(true);
    }

    public void enterBorrowManage() {
        docBg.setVisible(false);
        userBg.setVisible(false);
        brBg.setVisible(true);
    }

    @FXML
    private void handleAvatarChange() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh đại diện");

        fileChooser
                .getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(avaButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                Circle clip = new Circle();
                clip.setCenterX(avaButton.getFitWidth() / 2);
                clip.setCenterY(avaButton.getFitHeight() / 2);
                clip.setRadius(Math.min(avaButton.getFitWidth(), avaButton.getFitHeight()) / 2);

                avaButton.setClip(clip);

                Image avatarImage = new Image(selectedFile.toURI().toString());
                avaButton.setImage(avatarImage);
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
        Circle clip = new Circle();
        clip.setCenterX(avaButton.getFitWidth() / 2);
        clip.setCenterY(avaButton.getFitHeight() / 2);
        clip.setRadius(Math.min(avaButton.getFitWidth(), avaButton.getFitHeight()) / 2);

        avaButton.setClip(clip);
    }
}
