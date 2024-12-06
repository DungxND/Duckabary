package io.vn.dungxnd.duckabary.presentation.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    private static MainApplication instance;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch();
    }

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void start(Stage stage) {
        instance = this;
        this.primaryStage = stage;
        showLoginView();
    }

    public void showLoginView() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/auth/LoginModal.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.setTitle("Duckabary - Login");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Duckabary Library Management System");
            primaryStage.show();
            primaryStage.setResizable(true);
            primaryStage.centerOnScreen();
            primaryStage.setMinHeight(720);
            primaryStage.setMinWidth(1280);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
