package io.vn.dungxnd.duckabary.presentation.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class Login extends Application {

    @FXML private ImageView anhSach;

    @FXML private TextField enterPassword;

    @FXML private TextField enterUsername;

    @FXML private AnchorPane imagePart;

    @FXML private Button loginButton;

    @FXML private Label passWord;

    @FXML private BorderPane signInGUI;

    @FXML private Button signupButton;

    @FXML private Label userName;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
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
    public void start(Stage stage) throws Exception {}
}
