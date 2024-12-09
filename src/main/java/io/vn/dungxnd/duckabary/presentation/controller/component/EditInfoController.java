package io.vn.dungxnd.duckabary.presentation.controller.component;

import io.vn.dungxnd.duckabary.util.LoggerUtils;
import io.vn.dungxnd.duckabary.util.ValidationUtils;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
            errorLabel.setText("New value cannot be empty");
            errorLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if (fieldName.equalsIgnoreCase("phone")) {
            try {
                ValidationUtils.validatePhone(newValue);
            } catch (IllegalArgumentException e) {
                LoggerUtils.error("Error validating phone number", e);
                errorLabel.setText(e.getMessage());
                errorLabel.setStyle("-fx-text-fill: red;");
                return;
            }
        }

        if (fieldName.equalsIgnoreCase("email")) {
            try {
                ValidationUtils.validateEmail(newValue);
            } catch (IllegalArgumentException e) {
                LoggerUtils.error("Error validating email", e);
                errorLabel.setText(e.getMessage());
                errorLabel.setStyle("-fx-text-fill: red;");
                return;
            }
        }

        try {
            saveCallback.accept(entity);

            Platform.runLater(
                    () -> {
                        if (onEditComplete != null) {
                            onEditComplete.run();
                        }
                        closeModal();
                    });

        } catch (Exception e) {
            LoggerUtils.error("Error saving entity", e);
            errorLabel.setText("Failed to save: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red;");
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
}
