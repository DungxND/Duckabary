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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
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

    @FXML
    private SVGPath docSvg;

    @FXML
    private SVGPath authorSvg;

    @FXML
    private SVGPath publishSvg;

    @FXML
    private SVGPath leftUserSvg;

    @FXML
    private SVGPath leftLibSvg;

    @FXML
    private SVGPath leftBorSvg;

    @FXML
    private AnchorPane leftBorIconPane;

    @FXML
    private AnchorPane leftLibIconPane;

    @FXML
    private AnchorPane leftUserIconPane;

    public void enterDocManage() {
        docList.setVisible(true);
        authorList.setVisible(false);
        publisherList.setVisible(false);
        docIcon.setStyle("-fx-background-color: #3A3026");
        authorIcon.setStyle("-fx-background-color: #FFFFFF");
        publishIcon.setStyle("-fx-background-color: #FFFFFF");
        docSvg.setFill(Color.WHITE);
        authorSvg.setFill(Color.BLACK);
        publishSvg.setFill(Color.BLACK);
        libTitle.setText("Document List");

    }
    public void enterAuthorManage() {
        authorList.setVisible(true);
        publisherList.setVisible(false);
        docList.setVisible(false);
        docIcon.setStyle("-fx-background-color: #FFFFFF");
        authorIcon.setStyle("-fx-background-color: #3A3026");
        publishIcon.setStyle("-fx-background-color: #FFFFFF");
        docSvg.setFill(Color.BLACK);
        authorSvg.setFill(Color.WHITE);
        publishSvg.setFill(Color.BLACK);
        libTitle.setText("Author List");
    }

    public void enterPublishManage() {
        publisherList.setVisible(true);
        authorList.setVisible(false);
        docList.setVisible(false);
        docIcon.setStyle("-fx-background-color: #FFFFFF");
        authorIcon.setStyle("-fx-background-color: #FFFFFF");
        publishIcon.setStyle("-fx-background-color: #3A3026");
        docSvg.setFill(Color.BLACK);
        authorSvg.setFill(Color.BLACK);
        publishSvg.setFill(Color.WHITE);
        libTitle.setText("Publisher List");
    }

    public void enterLibManage() {
        userBg.setVisible(false);
        brBg.setVisible(false);
        libBg.setVisible(true);
        settingBg.setVisible(false);
        leftLibIconPane.setStyle("-fx-background-color: #9D9D95");
        leftBorIconPane.setStyle("-fx-background-color: #E0DFDC");
        leftUserIconPane.setStyle("-fx-background-color: #E0DFDC");
        leftLibSvg.setFill(Color.WHITE);
        leftBorSvg.setFill(Color.BLACK);
        leftUserSvg.setFill(Color.BLACK);
    }

    public void enterUserManage() {
        libBg.setVisible(false);
        brBg.setVisible(false);
        userBg.setVisible(true);
        settingBg.setVisible(false);
        leftLibIconPane.setStyle("-fx-background-color: #E0DFDC");
        leftBorIconPane.setStyle("-fx-background-color: #E0DFDC");
        leftUserIconPane.setStyle("-fx-background-color: #9D9D95");
        leftLibSvg.setFill(Color.BLACK);
        leftBorSvg.setFill(Color.BLACK);
        leftUserSvg.setFill(Color.WHITE);
    }

    public void enterBorrowManage() {
        libBg.setVisible(false);
        userBg.setVisible(false);
        brBg.setVisible(true);
        settingBg.setVisible(false);
        leftLibIconPane.setStyle("-fx-background-color: #E0DFDC");
        leftBorIconPane.setStyle("-fx-background-color: #9D9D95");
        leftUserIconPane.setStyle("-fx-background-color: #E0DFDC");
        leftLibSvg.setFill(Color.BLACK);
        leftBorSvg.setFill(Color.WHITE);
        leftUserSvg.setFill(Color.BLACK);
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
                clip.setCenterX(avaButton.getFitWidth() / 2 );
                clip.setCenterY(avaButton.getFitHeight() / 2);
                clip.setRadius(Math.min(avaButton.getFitWidth(), avaButton.getFitHeight()) *17/40);
                Image avatarImage = new Image(selectedFile.toURI().toString());
                avaButton.setImage(avatarImage);
                avaButton.setClip(clip);

                Circle clip1 = new Circle();
                clip1.setCenterX(setAva.getFitWidth() / 2);
                clip1.setCenterY(setAva.getFitHeight() / 2);
                clip1.setRadius(Math.min(setAva.getFitWidth(), setAva.getFitHeight()) *17 /40);
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
        clip.setRadius(Math.min(avaButton.getFitWidth(), avaButton.getFitHeight()) *17/ 40);
        avaButton.setClip(clip);

        Circle clip1 = new Circle();
        clip1.setCenterX(setAva.getFitWidth() / 2);
        clip1.setCenterY(setAva.getFitHeight() / 2);
        clip1.setRadius(Math.min(setAva.getFitWidth(), setAva.getFitHeight()) *17/40);
        setAva.setClip(clip1);

        docIcon.setStyle("-fx-background-color: #3A3026");
        authorIcon.setStyle("-fx-background-color: #FFFFFF");
        publishIcon.setStyle("-fx-background-color: #FFFFFF");
        docSvg.setFill(Color.WHITE);

        leftLibIconPane.setStyle("-fx-background-color: #9D9D95");
        leftLibSvg.setFill(Color.WHITE);
    }
}
