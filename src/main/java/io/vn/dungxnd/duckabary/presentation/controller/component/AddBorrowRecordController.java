package io.vn.dungxnd.duckabary.presentation.controller.component;

import static io.vn.dungxnd.duckabary.util.TimeUtils.getDateTimeHourFromString;

import io.vn.dungxnd.duckabary.domain.model.library.Document;
import io.vn.dungxnd.duckabary.domain.model.user.User;
import io.vn.dungxnd.duckabary.domain.service.ServiceManager;
import io.vn.dungxnd.duckabary.domain.service.borrow.BorrowService;
import io.vn.dungxnd.duckabary.domain.service.library.DocumentService;
import io.vn.dungxnd.duckabary.domain.service.user.UserService;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class AddBorrowRecordController {
    private final BorrowService borrowService;
    private final DocumentService documentService;
    private final UserService userService;
    private Runnable onBorrowRecordAdded;

    @FXML private TextField userInput;
    @FXML private Label foundUserLabel;
    @FXML private TextField documentInput;
    @FXML private Label foundDocumentLabel;
    @FXML private TextField quantityInput;
    @FXML private TextField dueDateInput;
    @FXML private Button saveBtn;
    @FXML private Label errorLabel;

    private User selectedUser;
    private Document selectedDocument;

    public AddBorrowRecordController() {
        this.borrowService = ServiceManager.getInstance().getBorrowService();
        this.documentService = ServiceManager.getInstance().getDocumentService();
        this.userService = ServiceManager.getInstance().getUserService();
    }

    public void initialize() {
        resetForm();
        bindInputs();
    }

    private void resetForm() {
        foundUserLabel.setText("No user selected");
        foundDocumentLabel.setText("No document selected");
        errorLabel.setText("");
        saveBtn.setDisable(true);
        selectedUser = null;
        selectedDocument = null;
    }

    private void bindInputs() {
        userInput
                .textProperty()
                .addListener(
                        (_, _, newValue) -> {
                            if (newValue.isEmpty()) {
                                foundUserLabel.setText("No user selected");
                                foundUserLabel.setStyle("-fx-text-fill: black;");
                                return;
                            }
                            searchUser(newValue);
                        });

        documentInput
                .textProperty()
                .addListener(
                        (_, _, newValue) -> {
                            if (newValue.isEmpty()) {
                                foundDocumentLabel.setText("No document selected");
                                foundDocumentLabel.setStyle("-fx-text-fill: black;");
                                return;
                            }
                            searchDocument(newValue);
                        });

        quantityInput
                .textProperty()
                .addListener(
                        (_, _, newValue) -> {
                            if (!newValue.matches("\\d*")) {
                                quantityInput.setText(newValue.replaceAll("\\D", ""));
                            }
                            validateForm();
                        });

        dueDateInput.textProperty().addListener((_, _, _) -> validateForm());
    }

    private void searchUser(String input) {
        Task<User> searchTask =
                new Task<>() {
                    @Override
                    protected User call() {
                        try {
                            int userId = Integer.parseInt(input);
                            return userService.getUserById(userId);
                        } catch (NumberFormatException e) {
                            return userService.searchByUsername(input).orElse(null);
                        }
                    }
                };

        searchTask.setOnSucceeded(
                _ -> {
                    selectedUser = searchTask.getValue();
                    Platform.runLater(this::updateUserLabel);
                });

        searchTask.setOnFailed(
                _ ->
                        Platform.runLater(
                                () -> {
                                    foundUserLabel.setText("No user found");
                                    foundUserLabel.setStyle("-fx-text-fill: red;");
                                }));

        new Thread(searchTask).start();
    }

    private void searchDocument(String input) {
        Task<Document> searchTask =
                new Task<>() {
                    @Override
                    protected Document call() {
                        try {
                            long documentId = Long.parseLong(input);
                            return documentService.getDocumentById(documentId);
                        } catch (NumberFormatException e) {
                            return documentService.searchByTitle(input).stream()
                                    .findFirst()
                                    .orElse(null);
                        }
                    }
                };

        searchTask.setOnSucceeded(
                _ -> {
                    selectedDocument = searchTask.getValue();
                    Platform.runLater(this::updateDocumentLabel);
                });

        searchTask.setOnFailed(
                _ ->
                        Platform.runLater(
                                () -> {
                                    foundDocumentLabel.setText("No document found");
                                    foundDocumentLabel.setStyle("-fx-text-fill: red;");
                                }));

        new Thread(searchTask).start();
    }

    private void updateUserLabel() {
        if (selectedUser != null) {
            foundUserLabel.setText(
                    String.format(
                            "User found: %s (ID: %d)", selectedUser.username(), selectedUser.id()));
            foundUserLabel.setStyle("-fx-text-fill: green;");
        } else {
            foundUserLabel.setText("User not found");
            foundUserLabel.setStyle("-fx-text-fill: red;");
        }
        validateForm();
    }

    private void updateDocumentLabel() {
        if (selectedDocument != null) {
            foundDocumentLabel.setText(
                    String.format(
                            "Document found: %s (ID: %d), Available stock: %d",
                            selectedDocument.title(),
                            selectedDocument.id(),
                            selectedDocument.quantity()));
            foundDocumentLabel.setStyle("-fx-text-fill: green;");
        } else {
            foundDocumentLabel.setText("Document not found");
            foundDocumentLabel.setStyle("-fx-text-fill: red;");
        }
        validateForm();
    }

    private boolean isValidQuantity() {
        try {
            int quantity = Integer.parseInt(quantityInput.getText());
            return quantity > 0
                    && selectedDocument != null
                    && quantity <= selectedDocument.quantity();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDueDate() {
        try {
            LocalDateTime dueDate = getDateTimeHourFromString(dueDateInput.getText());
            return dueDate.isAfter(LocalDateTime.now());
        } catch (Exception e) {
            return false;
        }
    }

    @FXML
    private void handleSave() {
        Task<Void> borrowTask =
                new Task<>() {
                    @Override
                    protected Void call() {
                        int quantity = Integer.parseInt(quantityInput.getText());
                        LocalDateTime dueDate = getDateTimeHourFromString(dueDateInput.getText());
                        borrowService.borrowDocument(
                                selectedUser.id(), selectedDocument.id(), quantity, dueDate);
                        return null;
                    }
                };

        borrowTask.setOnSucceeded(
                _ ->
                        Platform.runLater(
                                () -> {
                                    showSuccessAlert();
                                    if (onBorrowRecordAdded != null) {
                                        onBorrowRecordAdded.run();
                                    }
                                    closeModal();
                                }));

        borrowTask.setOnFailed(
                _ ->
                        Platform.runLater(
                                () ->
                                        showErrorAlert(
                                                "Failed to create borrow record: "
                                                        + borrowTask.getException().getMessage())));

        new Thread(borrowTask).start();
    }

    private void validateForm() {
        boolean isValid =
                selectedUser != null
                        && selectedDocument != null
                        && isValidQuantity()
                        && isValidDueDate();
        saveBtn.setDisable(!isValid);

        if (isValid) {
            errorLabel.setText("");
        } else {
            updateErrorLabel();
        }
    }

    private void updateErrorLabel() {
        if (selectedUser == null) {
            errorLabel.setText("Please select a valid user");
        } else if (selectedDocument == null) {
            errorLabel.setText("Please select a valid document");
        } else if (!isValidQuantity()) {
            errorLabel.setText("Invalid quantity");
        } else if (!isValidDueDate()) {
            errorLabel.setText("Invalid due date");
        }
        errorLabel.setStyle("-fx-text-fill: red;");
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Borrow record created successfully");
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeModal() {
        ((Stage) saveBtn.getScene().getWindow()).close();
    }

    public void setOnBorrowRecordAdded(Runnable callback) {
        this.onBorrowRecordAdded = callback;
    }
}
