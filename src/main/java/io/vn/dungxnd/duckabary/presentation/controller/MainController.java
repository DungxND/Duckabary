package io.vn.dungxnd.duckabary.presentation.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private AnchorPane usOption;

    @FXML
    private AnchorPane libOption;

    @FXML
    private AnchorPane borOption;

    @FXML
    private AnchorPane userPane;

    @FXML
    private AnchorPane libPane;

    @FXML
    private AnchorPane borrowPane;

    @FXML
    private AnchorPane settingPane;

    @FXML
    private SVGPath leftUserSvg;

    @FXML
    private SVGPath leftBorSvg;

    @FXML
    private SVGPath leftLibSvg;

    @FXML
    private ImageView avaButton;

    @FXML
    private ImageView setAvaButton;

    @FXML
    private AnchorPane docList;

    @FXML
    private AnchorPane authorList;

    @FXML
    private AnchorPane publishList;

    @FXML
    private AnchorPane docIcon;

    @FXML
    private AnchorPane authorIcon;

    @FXML
    private AnchorPane publishIcon;

    @FXML
    private SVGPath publishSvg;

    @FXML
    private SVGPath authorSvg;

    @FXML
    private SVGPath docSvg;




    public void enterDocManage() {
        docList.setVisible(true);
        authorList.setVisible(false);
        publishList.setVisible(false);

        docIcon.setStyle("-fx-background-color: #3A3026");
        authorIcon.setStyle("-fx-background-color: #FFFFFF");
        publishIcon.setStyle("-fx-background-color: #FFFFFF");
        docSvg.setFill(Color.WHITE);
        authorSvg.setFill(Color.BLACK);
        publishSvg.setFill(Color.BLACK);

    }

    public void enterAuthorManage() {
        authorList.setVisible(true);
        publishList.setVisible(false);
        docList.setVisible(false);

        docIcon.setStyle("-fx-background-color: #FFFFFF");
        authorIcon.setStyle("-fx-background-color: #3A3026");
        publishIcon.setStyle("-fx-background-color: #FFFFFF");
        docSvg.setFill(Color.BLACK);
        authorSvg.setFill(Color.WHITE);
        publishSvg.setFill(Color.BLACK);
    }

    public void enterPublishManage() {
        publishList.setVisible(true);
        authorList.setVisible(false);
        docList.setVisible(false);

        docIcon.setStyle("-fx-background-color: #FFFFFF");
        authorIcon.setStyle("-fx-background-color: #FFFFFF");
        publishIcon.setStyle("-fx-background-color: #3A3026");
        docSvg.setFill(Color.BLACK);
        authorSvg.setFill(Color.BLACK);
        publishSvg.setFill(Color.WHITE);
    }

    public void enterLibManage() {
        userPane.setVisible(false);
        borrowPane.setVisible(false);
        libPane.setVisible(true);
        settingPane.setVisible(false);

        leftLibSvg.setFill(Color.BLACK);

        leftBorSvg.setFill(null);
        leftBorSvg.setStroke(Color.BLACK);
        leftUserSvg.setFill(null);
        leftUserSvg.setStroke(Color.BLACK);
        leftUserSvg.setStrokeWidth(2);
        leftBorSvg.setStrokeWidth(2);
    }

    public void enterUserManage() {
        libPane.setVisible(false);
        borrowPane.setVisible(false);
        userPane.setVisible(true);
        settingPane.setVisible(false);

        leftLibSvg.setFill(null);
        leftBorSvg.setFill(null);
        leftLibSvg.setStroke(Color.BLACK);
        leftBorSvg.setStroke(Color.BLACK);

        leftUserSvg.setFill(Color.BLACK);
        leftLibSvg.setStrokeWidth(2);
        leftBorSvg.setStrokeWidth(2);
    }

    public void enterBorrowManage() {
        libPane.setVisible(false);
        userPane.setVisible(false);
        borrowPane.setVisible(true);
        settingPane.setVisible(false);


        leftBorSvg.setFill(Color.BLACK);

        leftLibSvg.setFill(null);
        leftUserSvg.setFill(null);
        leftLibSvg.setStroke(Color.BLACK);
        leftUserSvg.setStroke(Color.BLACK);
        leftLibSvg.setStrokeWidth(2);
        leftUserSvg.setStrokeWidth(2);
    }

    public void enterSetting() {
        settingPane.setVisible(true);
        userPane.setVisible(false);
        borrowPane.setVisible(false);
        libPane.setVisible(false);

        leftLibSvg.setFill(null);
        leftUserSvg.setFill(null);
        leftBorSvg.setFill(null);
        leftLibSvg.setStroke(Color.BLACK);
        leftUserSvg.setStroke(Color.BLACK);
        leftBorSvg.setStroke(Color.BLACK);
        leftLibSvg.setStrokeWidth(2);
        leftUserSvg.setStrokeWidth(2);
        leftBorSvg.setStrokeWidth(2);
    }

    public void handleAvatarChange() {
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
                clip.setRadius(Math.min(avaButton.getFitWidth(), avaButton.getFitHeight()) * 17 / 40);
                Image avatarImage = new Image(selectedFile.toURI().toString());
                avaButton.setImage(avatarImage);
                avaButton.setClip(clip);

                Circle clip1 = new Circle();
                clip1.setCenterX(setAvaButton.getFitWidth() / 2);
                clip1.setCenterY(setAvaButton.getFitHeight() / 2);
                clip1.setRadius(Math.min(setAvaButton.getFitWidth(), setAvaButton.getFitHeight()) * 17 / 40);
                setAvaButton.setClip(clip1);
                setAvaButton.setImage(avatarImage);

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
        clip.setRadius(Math.min(avaButton.getFitWidth(), avaButton.getFitHeight()) * 17 / 40);
        avaButton.setClip(clip);

        Circle clip1 = new Circle();
        clip1.setCenterX(setAvaButton.getFitWidth() / 2);
        clip1.setCenterY(setAvaButton.getFitHeight() / 2);
        clip1.setRadius(Math.min(setAvaButton.getFitWidth(), setAvaButton.getFitHeight()) * 17 / 40);
        setAvaButton.setClip(clip1);

        docIcon.setStyle("-fx-background-color: #3A3026");
        authorIcon.setStyle("-fx-background-color: #FFFFFF");
        publishIcon.setStyle("-fx-background-color: #FFFFFF");
        docSvg.setFill(Color.WHITE);
        authorSvg.setFill(Color.BLACK);
        publishSvg.setFill(Color.BLACK);

    }


}
