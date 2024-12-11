package io.vn.dungxnd.duckabary.presentation.controller.component;

import io.vn.dungxnd.duckabary.util.LoggerUtils;
import io.vn.dungxnd.duckabary.util.ValidationUtils;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.function.Consumer;

public class EditInfoController<T> {
    @FXML private Label editWhatLabel;
    @FXML private Label oldValueLabel;
    @FXML private TextField oldValueInput;
    @FXML private Label newValueLabel;
    @FXML private TextField newValueInput;
    @FXML private Label errorLabel;
    @FXML private Button saveBtn;

    private T entity;
    private String fieldName;
    private Consumer<T> saveCallback;
    private Runnable onEditComplete;

    public void initialize() {
        oldValueInput.setEditable(false);
        oldValueInput.setMouseTransparent(true);
        oldValueInput.setDisable(true);
        saveBtn.setOnAction(event -> handleSave());
        errorLabel.setText("");
    }

    public void setEditInfo(T entity, String fieldName, String oldValue, Consumer<T> saveCallback) {
        this.entity = entity;
        this.fieldName = fieldName;
        this.saveCallback = saveCallback;

        editWhatLabel.setText(fieldName);
        oldValueInput.setText(oldValue);
        newValueInput.setText("");
        errorLabel.setText("");
    }

    @FXML
    private void handleSave() {
        String newValue = newValueInput.getText().trim();
        if (newValue.isEmpty()) {
            showError("New value cannot be empty");
            return;
        }

        try {
            if (fieldName.equalsIgnoreCase("phone")) {
                ValidationUtils.validatePhone(newValue);
            } else if (fieldName.equalsIgnoreCase("email")) {
                ValidationUtils.validateEmail(newValue);
            }

            try {
                saveCallback.accept(entity);

                Platform.runLater(() -> {
                    if (onEditComplete != null) {
                        onEditComplete.run();
                    }
                    closeModal();
                });

            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }

        } catch (Exception e) {
            LoggerUtils.error("Error saving entity", e);
            showError("Failed to save: " + e.getMessage());
        }
    }


    public void setOnEditComplete(Runnable callback) {
        this.onEditComplete = callback;
    }

    private void closeModal() {
        ((Stage) saveBtn.getScene().getWindow()).close();
    }

    public String getNewValue() {
        return newValueInput.getText().trim();
    }

    public void showError(String message) {
        Platform.runLater(
                () -> {
                    LoggerUtils.warn(message);
                    errorLabel.setVisible(true);
                    errorLabel.setText(message);
                    errorLabel.setStyle("-fx-text-fill: red;");

                    PauseTransition delay = new PauseTransition(Duration.seconds(3));
                    delay.setOnFinished(event -> errorLabel.setVisible(false));
                    delay.play();
                });
    }
}
