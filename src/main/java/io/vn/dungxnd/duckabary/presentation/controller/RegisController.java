package io.vn.dungxnd.duckabary.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;

public class RegisController {

    @FXML
    private Label erMessage;

    @FXML
    private SVGPath eyeCfPass;

    @FXML
    private SVGPath eyeHiddenCfPass;

    @FXML
    private SVGPath eyeHideen;

    @FXML
    private SVGPath eyePass;

    @FXML
    private PasswordField hiddenCfPass;

    @FXML
    private PasswordField hiddenPass;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField textCfPass;

    @FXML
    private TextField textPass;

    @FXML
    private AnchorPane titlePane;

    @FXML
    void hidePassword(MouseEvent event) {
        textPass.setVisible(false);
        textCfPass.setVisible(false);
        hiddenCfPass.setVisible(true);
        hiddenPass.setVisible(true);
        eyeHideen.setVisible(false);
        eyeHiddenCfPass.setVisible(false);
        eyePass.setVisible(true);
        eyeCfPass.setVisible(true);
        hiddenPass.setText(textPass.getText());
        hiddenCfPass.setText(textCfPass.getText());
    }

    @FXML
    void showPassword(MouseEvent event) {
        textPass.setVisible(true);
        textCfPass.setVisible(true);
        hiddenCfPass.setVisible(false);
        hiddenPass.setVisible(false);
        eyeHideen.setVisible(true);
        eyeHiddenCfPass.setVisible(true);
        eyePass.setVisible(false);
        eyeCfPass.setVisible(false);
        textPass.setText(hiddenPass.getText());
        textCfPass.setText(hiddenCfPass.getText());
    }



}
