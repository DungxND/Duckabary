package io.vn.dungxnd.duckabary.presentation.controller.component;

import io.vn.dungxnd.duckabary.domain.model.library.BorrowRecord;
import io.vn.dungxnd.duckabary.domain.service.ServiceManager;
import io.vn.dungxnd.duckabary.domain.service.borrow.BorrowService;
import io.vn.dungxnd.duckabary.domain.service.library.DocumentService;
import io.vn.dungxnd.duckabary.domain.service.user.UserService;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import io.vn.dungxnd.duckabary.util.TimeUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class BorrowRecordDetailController {
    private final BorrowService borrowService;

    private final UserService userService;

    private final DocumentService documentService;
    private Runnable onRecordEdited;

    @FXML private Label userLabel;

    @FXML private Label docLabel;

    @FXML private Label borrowDateLabel;

    @FXML private Label returnedLabel;

    @FXML private Button switchReturnStateBtn;

    @FXML private Button editDueDateBtn;

    //    @FXML private Button editQuantityBtn;

    @FXML private Label quantityLabel;

    private BorrowRecord borrowRecord;

    public BorrowRecordDetailController() {
        this.borrowService = ServiceManager.getInstance().getBorrowService();
        this.userService = ServiceManager.getInstance().getUserService();
        this.documentService = ServiceManager.getInstance().getDocumentService();
    }

    public void initialize() {
        switchReturnStateBtn.setOnMouseClicked(_ -> handleSwitchReturnState());
        editDueDateBtn.setOnMouseClicked(event -> handleEditDueDate());
        //        editQuantityBtn.setOnMouseClicked(event -> handleEditQuantity());
    }

    private void handleEditQuantity() {}

    private void handleEditDueDate() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/component/EditInfoModal.fxml"));
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Edit Due Date");
            modalStage.setScene(new Scene(loader.load()));

            EditInfoController<BorrowRecord> controller = loader.getController();
            controller.setEditInfo(
                    borrowRecord,
                    "due date (yyyy-MM-dd HH:mm)",
                    TimeUtils.getFormattedDateTimeHour(borrowRecord.dueDate()),
                    _ -> {
                        LocalDateTime newDueDate = TimeUtils.getDateTimeHourFromString(controller.getNewValue());

                        if (newDueDate.isBefore(borrowRecord.borrowDate())) {
                            throw new IllegalArgumentException("Due date cannot be before borrow date");
                        }

                        if (newDueDate.isBefore(LocalDateTime.now())) {
                            throw new IllegalArgumentException("Due date cannot be in the past");
                        }

                        BorrowRecord updatedRecord = BorrowRecord.createBorrowRecord(
                                borrowRecord.recordId(),
                                borrowRecord.userId(),
                                borrowRecord.documentId(),
                                borrowRecord.quantity(),
                                borrowRecord.borrowDate(),
                                newDueDate,
                                borrowRecord.returnDate().orElse(null)
                        );

                        borrowService.updateBorrowRecord(updatedRecord);
                        setBorrowRecord(updatedRecord);
                    }
            );


            controller.setOnEditComplete(() ->
                    Platform.runLater(() -> {
                        if (onRecordEdited != null) {
                            getBorrowDetail();
                            onRecordEdited.run();
                        }
                    })
            );

            modalStage.showAndWait();
        } catch (IOException e) {
            LoggerUtils.error("Error loading due date edit modal", e);
        }
    }


    private void handleSwitchReturnState() {
        try {
            BorrowRecord updatedRecord;
            if (borrowRecord.returnDate().isEmpty()) {
                updatedRecord =
                        BorrowRecord.createBorrowRecord(
                                borrowRecord.recordId(),
                                borrowRecord.userId(),
                                borrowRecord.documentId(),
                                borrowRecord.quantity(),
                                borrowRecord.borrowDate(),
                                borrowRecord.dueDate(),
                                LocalDateTime.now());

            } else {
                updatedRecord =
                        BorrowRecord.createBorrowRecord(
                                borrowRecord.recordId(),
                                borrowRecord.userId(),
                                borrowRecord.documentId(),
                                borrowRecord.quantity(),
                                borrowRecord.borrowDate(),
                                borrowRecord.dueDate(),
                                null);
            }
            borrowService.updateBorrowRecord(updatedRecord);
            setBorrowRecord(updatedRecord);
            if (onRecordEdited != null) {
                onRecordEdited.run();
            }
        } catch (Exception e) {
            LoggerUtils.error("", e);
        }
    }

    public void setBorrowRecord(BorrowRecord borrowRecord) {
        this.borrowRecord = borrowRecord;
        getBorrowDetail();
    }

    public void setOnRecordEdited(Runnable callback) {
        this.onRecordEdited = callback;
    }

    private void getBorrowDetail() {
        userLabel.setText(userService.getUserById(borrowRecord.userId()).username());
        docLabel.setText(documentService.getDocumentById(borrowRecord.documentId()).title());
        returnedLabel.setText(
                borrowRecord.returnDate().isEmpty()
                        ? "Not yet"
                        : TimeUtils.getFormattedDateTimeHour(borrowRecord.returnDate().get()));
        borrowDateLabel.setText(borrowRecord.borrowDate().toString());
        quantityLabel.setText(String.valueOf(borrowRecord.quantity()));
    }
}
