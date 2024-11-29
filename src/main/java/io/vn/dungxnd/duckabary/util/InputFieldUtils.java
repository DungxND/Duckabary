package io.vn.dungxnd.duckabary.util;

import javafx.application.Platform;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class InputFieldUtils {
    public static void maintainCaretPosition(TextField textField) {
        Platform.runLater(
                () -> {
                    textField.requestFocus();
                    textField.deselect();
                    int length = textField.getText().length();
                    textField.positionCaret(length);
                });
    }

    public static void maintainCaretPosition(PasswordField passwordField) {
        Platform.runLater(
                () -> {
                    passwordField.requestFocus();
                    passwordField.deselect();
                    int length = passwordField.getText().length();
                    passwordField.positionCaret(length);
                });
    }
}
