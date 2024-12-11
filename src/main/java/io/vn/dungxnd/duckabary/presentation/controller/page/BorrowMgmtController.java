package io.vn.dungxnd.duckabary.presentation.controller.page;

import io.vn.dungxnd.duckabary.domain.model.library.BorrowRecord;
import io.vn.dungxnd.duckabary.domain.model.library.Document;
import io.vn.dungxnd.duckabary.domain.model.user.User;
import io.vn.dungxnd.duckabary.domain.service.ServiceManager;
import io.vn.dungxnd.duckabary.domain.service.borrow.BorrowService;
import io.vn.dungxnd.duckabary.domain.service.library.DocumentService;
import io.vn.dungxnd.duckabary.domain.service.user.UserService;
import io.vn.dungxnd.duckabary.presentation.controller.component.AddBorrowRecordController;
import io.vn.dungxnd.duckabary.presentation.controller.component.BorrowRecordDetailController;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BorrowMgmtController {
    private final BorrowService borrowService;
    private final UserService userService;
    private final DocumentService documentService;

    @FXML private TableView<BorrowRecord> borrowTable;
    @FXML private TableColumn<BorrowRecord, String> ridColumn;
    @FXML private TableColumn<BorrowRecord, String> UDColumn;
    @FXML private TableColumn<BorrowRecord, String> quantityColumn;
    @FXML private TableColumn<BorrowRecord, String> returnedColumn;
    @FXML private TableColumn<BorrowRecord, String> detailColumn;

    @FXML private Button addBorrowRecordBtn;

    public BorrowMgmtController() {
        this.borrowService = ServiceManager.getInstance().getBorrowService();
        this.userService = ServiceManager.getInstance().getUserService();
        this.documentService = ServiceManager.getInstance().getDocumentService();
    }

    public void initialize() {
        setupColumns();
        loadBorrows();
        addBorrowRecordBtn.setOnMouseClicked(_ -> showAddBorrowRecordModal());
    }

    private void showAddBorrowRecordModal() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource("/fxml/component/AddBorrowRecordModal.fxml"));
            Parent root = loader.load();
            AddBorrowRecordController controller = loader.getController();
            controller.setOnBorrowRecordAdded(this::loadBorrows);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Borrow Record");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            LoggerUtils.error("Failed to show Add Borrow Record modal", e);
        }
    }

    private void setupColumns() {
        ridColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(String.valueOf(cellData.getValue().recordId())));

        UDColumn.setCellValueFactory(
                cellData -> {
                    BorrowRecord record = cellData.getValue();

                    String userInfo;
                    User user = userService.getUserById(record.userId());
                    if (user != null) {
                        userInfo = "User: " + user.username() + " (" + user.email() + ")";
                    } else {
                        userInfo = "Unknown User";
                    }

                    String docInfo;
                    Document doc = documentService.getDocumentById(record.documentId());
                    if (doc != null) {
                        docInfo = "Document: " + doc.title() + " (" + doc.id() + ")";
                    } else {
                        docInfo = "Unknown Document";
                    }

                    return new SimpleStringProperty(userInfo + "\n" + docInfo);
                });

        quantityColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(String.valueOf(cellData.getValue().quantity())));

        returnedColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(cellData.getValue().isReturned() ? "Yes" : "No"));

        detailColumn.setCellFactory(
                column ->
                        new TableCell<>() {
                            private final Button detailsButton = new Button("Details");

                            {
                                detailsButton.setOnAction(
                                        event -> {
                                            BorrowRecord record = getTableRow().getItem();
                                            if (record != null) {
                                                showBorrowRecordDetailsDialog(record);
                                            }
                                        });
                            }

                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(detailsButton);
                                }
                            }
                        });

        UDColumn.setCellFactory(
                tc -> {
                    TableCell<BorrowRecord, String> cell = new TableCell<>();
                    Text text = new Text();
                    cell.setGraphic(text);
                    cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                    text.wrappingWidthProperty().bind(UDColumn.widthProperty());
                    text.textProperty().bind(cell.itemProperty());
                    return cell;
                });
    }

    private void showBorrowRecordDetailsDialog(BorrowRecord record) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource("/fxml/component/BorrowRecordDetailModal.fxml"));
            Parent root = loader.load();

            BorrowRecordDetailController controller = loader.getController();
            controller.setBorrowRecord(record);
            controller.setOnRecordEdited(this::loadBorrows);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Borrow Record Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            LoggerUtils.error("Failed to show Borrow Record Details modal", e);
        }
    }

    private void loadBorrows() {
        borrowTable.getItems().clear();
        borrowTable.getItems().addAll(borrowService.getAllBorrows());
    }
}
