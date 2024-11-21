package io.vn.dungxnd.duckabary.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {
    @FXML
    private ImageView avatarButton;

    @FXML
    private AnchorPane DOC;

    @FXML
    private AnchorPane USER;

    @FXML
    private AnchorPane BORROW;


    public void enterDocManage() {
        USER.setVisible(false);
        BORROW.setVisible(false);
        DOC.setVisible(true);
    }

    public void enterUserManage() {
        DOC.setVisible(false);
        BORROW.setVisible(false);
        USER.setVisible(true);

    }

    public void enterBorrowManage() {
        DOC.setVisible(false);
        USER.setVisible(false);
        BORROW.setVisible(true);

    }


    @FXML
    private void handleAvatarChange() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh đại diện");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

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

    }
}
