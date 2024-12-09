package io.vn.dungxnd.duckabary.presentation.controller.component;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class NotificationController {
    @FXML private VBox notificationPane;
    @FXML private Label messageLabel;
    private Popup popup;

    public static void show(String message, NotificationType type, Node ownerNode) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(
                            NotificationController.class.getResource(
                                    "/fxml/component/NotificationView.fxml"));
            loader.load();

            NotificationController controller = loader.getController();
            controller.messageLabel.setText(message);
            controller.notificationPane.getStyleClass().add(type.toString().toLowerCase());

            controller.notificationPane.applyCss();
            controller.notificationPane.layout();

            Stage ownerStage = (Stage) ownerNode.getScene().getWindow();
            double notificationWidth = controller.notificationPane.prefWidth(-1);

            controller.popup.show(
                    ownerStage,
                    ownerStage.getX() + (ownerStage.getWidth() - notificationWidth) / 2,
                    ownerStage.getY() + 20);

            PauseTransition delay = new PauseTransition(Duration.seconds(3));
            delay.setOnFinished(e -> controller.popup.hide());
            delay.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        popup = new Popup();
        popup.getContent().add(notificationPane);
    }

    public enum NotificationType {
        SUCCESS,
        ERROR,
        WARNING,
        INFO
    }
}
