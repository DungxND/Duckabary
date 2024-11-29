package io.vn.dungxnd.duckabary.presentation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private ImageView avaButton;

    @FXML
    private ImageView setAva;

    @FXML
    private AnchorPane borOption;

    @FXML
    private AnchorPane brBg;

    @FXML
    private AnchorPane libBg;

    @FXML
    private AnchorPane docOption;

    @FXML
    private AnchorPane usOption;

    @FXML
    private AnchorPane userBg;

    @FXML
    private AnchorPane settingBg;

    @FXML
    private AnchorPane docIcon;

    @FXML
    private AnchorPane authorIcon;

    @FXML
    private AnchorPane publishIcon;

    @FXML
    private AnchorPane docList;
    @FXML
    private AnchorPane authorList;
    @FXML
    private AnchorPane publisherList;

    @FXML
    private Label libTitle;

    @FXML
    private Button allButton;

    public void enterDocManage() {
        docList.setVisible(true);
        authorList.setVisible(false);
        publisherList.setVisible(false);
        docIcon.setStyle("-fx-background-color: grey");
        authorIcon.setStyle("-fx-background-color: #FFFFFF");
        publishIcon.setStyle("-fx-background-color: #FFFFFF");
        libTitle.setText("Document List");

    }
    public void enterAuthorManage() {
        authorList.setVisible(true);
        publisherList.setVisible(false);
        docList.setVisible(false);
        docIcon.setStyle("-fx-background-color: #FFFFFF");
        authorIcon.setStyle("-fx-background-color: grey");
        publishIcon.setStyle("-fx-background-color: #FFFFFF");
        libTitle.setText("Author List");
    }

    public void enterPublishManage() {
        publisherList.setVisible(true);
        authorList.setVisible(false);
        docList.setVisible(false);
        docIcon.setStyle("-fx-background-color: #FFFFFF");
        authorIcon.setStyle("-fx-background-color: #FFFFFF");
        publishIcon.setStyle("-fx-background-color: grey");
        libTitle.setText("Publisher List");
    }

    public void enterLibManage() {
        userBg.setVisible(false);
        brBg.setVisible(false);
        libBg.setVisible(true);
        settingBg.setVisible(false);
    }

    public void enterUserManage() {
        libBg.setVisible(false);
        brBg.setVisible(false);
        userBg.setVisible(true);
        settingBg.setVisible(false);
    }

    public void enterBorrowManage() {
        libBg.setVisible(false);
        userBg.setVisible(false);
        brBg.setVisible(true);
        settingBg.setVisible(false);
    }

    public void enterSetting() {
        settingBg.setVisible(true);
        userBg.setVisible(false);
        brBg.setVisible(false);
        libBg.setVisible(false);
    }


    @FXML
    private void handleAvatarChange() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh đại diện");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

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

                Circle clip1 = new Circle();
                clip1.setCenterX(setAva.getFitWidth() / 2);
                clip1.setCenterY(setAva.getFitHeight() / 2);
                clip1.setRadius(Math.min(setAva.getFitWidth(), setAva.getFitHeight()) / 2);
                setAva.setClip(clip1);
                setAva.setImage(avatarImage);

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

        Circle clip1 = new Circle();
        clip1.setCenterX(setAva.getFitWidth() / 2);
        clip1.setCenterY(setAva.getFitHeight() / 2);
        clip1.setRadius(Math.min(setAva.getFitWidth(), setAva.getFitHeight()) / 2);
        setAva.setClip(clip1);

        docIcon.setStyle("-fx-background-color: grey");
        authorIcon.setStyle("-fx-background-color: #FFFFFF");
        publishIcon.setStyle("-fx-background-color: #FFFFFF");

    }
}
