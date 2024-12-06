package io.vn.dungxnd.duckabary.presentation.controller.modal;

import io.vn.dungxnd.duckabary.util.LoggerUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.util.function.Consumer;

public class EditInfoController<T> {
    @FXML private Label editLabel;
    @FXML private Label oldValueLabel;
    @FXML private TextField newValueInput;
    @FXML private Button saveBtn;

    private T entity;
    private String fieldName;
    private Consumer<T> saveCallback;

    public void initialize() {
        saveBtn.setOnAction(event -> handleSave());
    }

    public void setEditInfo(T entity, String fieldName, String oldValue, Consumer<T> saveCallback) {
        this.entity = entity;
        this.fieldName = fieldName;
        this.saveCallback = saveCallback;

        editLabel.setText("Edit " + fieldName);
        oldValueLabel.setText(oldValue);
    }

    private void handleSave() {
        String newValue = newValueInput.getText();
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, newValue);
            saveCallback.accept(entity);
            closeModal();
        } catch (Exception e) {
            LoggerUtils.error("Failed to save new value", e);
        }
    }

    private void closeModal() {
        ((Stage) saveBtn.getScene().getWindow()).close();
    }
}
