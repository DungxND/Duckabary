package io.vn.dungxnd.duckabary.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private ImageView anhSach;

    @FXML
    private TextField enterPassword;

    @FXML
    private TextField enterUsername;

    @FXML
    private AnchorPane imagePart;

    @FXML
    private Button loginButton;

    @FXML
    private Label passWord;

    @FXML
    private BorderPane signInGUI;

    @FXML
    private Button signupButton;

    @FXML
    private Label userName;

    @FXML
    private void enterMainMenu(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
            Parent mainMenuRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainMenuRoot));
            stage.setTitle("Library Management - Main Menu");
            stage.setWidth(1200);
            stage.setHeight(800);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openRegisterPopup(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            Parent root = loader.load();

            Stage registerStage = new Stage();
            registerStage.setTitle("Đăng ký");
            registerStage.setScene(new Scene(root, 200, 180));
            registerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
