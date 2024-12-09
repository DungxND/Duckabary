package io.vn.dungxnd.duckabary.presentation.controller.page;

import io.vn.dungxnd.duckabary.domain.model.entity.Author;
import io.vn.dungxnd.duckabary.domain.model.entity.Publisher;
import io.vn.dungxnd.duckabary.domain.model.library.Document;
import io.vn.dungxnd.duckabary.domain.service.ServiceManager;
import io.vn.dungxnd.duckabary.domain.service.entity.AuthorService;
import io.vn.dungxnd.duckabary.domain.service.entity.PublisherService;
import io.vn.dungxnd.duckabary.domain.service.library.DocumentService;
import io.vn.dungxnd.duckabary.presentation.controller.component.DocumentDetailController;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class LibraryMgmtController {
    private final DocumentService documentService;
    private final AuthorService authorService;
    private final PublisherService publisherService;

    @FXML private TableView<Object> libraryTable;
    @FXML private Label displayingWhatLabel;
    @FXML private Button docBtn;
    @FXML private SVGPath docSvg;
    @FXML private Button authorBtn;
    @FXML private SVGPath authorSvg;
    @FXML private Button publisherBtn;
    @FXML private SVGPath publisherSvg;
    @FXML private Button addNewBtn;
    @FXML private TextField searchInput;

    public LibraryMgmtController() {
        this.documentService = ServiceManager.getInstance().getDocumentService();
        this.authorService = ServiceManager.getInstance().getAuthorService();
        this.publisherService = ServiceManager.getInstance().getPublisherService();
    }

    public void initialize() {
        displayingWhatLabel.setText("Documents");
        setStyle("Documents");
        setupDocumentTable();

        searchInput
                .textProperty()
                .addListener(
                        (_, _, newValue) -> {
                            String currentDisplay = displayingWhatLabel.getText();
                            switch (currentDisplay) {
                                case "Documents" -> {
                                    if (newValue.isEmpty()) {
                                        loadDocuments();
                                        return;
                                    }
                                    try {
                                        Long id = Long.parseLong(newValue);
                                        Document document = documentService.getDocumentById(id);
                                        libraryTable.setItems(
                                                FXCollections.observableArrayList(
                                                        List.of(document)));
                                    } catch (NumberFormatException e) {
                                        List<Document> documents =
                                                documentService.searchByTitle(newValue);
                                        libraryTable.setItems(
                                                FXCollections.observableArrayList(documents));
                                    }
                                }
                                case "Authors" -> {
                                    if (newValue.isEmpty()) {
                                        loadAuthors();
                                        return;
                                    }
                                    try {
                                        Long id = Long.parseLong(newValue);
                                        Author author = authorService.getAuthorById(id);
                                        libraryTable.setItems(
                                                FXCollections.observableArrayList(List.of(author)));
                                    } catch (NumberFormatException e) {
                                        List<Author> authors =
                                                authorService.getAuthorByNamePattern(newValue);
                                        libraryTable.setItems(
                                                FXCollections.observableArrayList(authors));
                                    }
                                }
                                case "Publishers" -> {
                                    if (newValue.isEmpty()) {
                                        loadPublishers();
                                        return;
                                    }
                                    try {
                                        Long id = Long.parseLong(newValue);
                                        Publisher publisher = publisherService.getPublisherById(id);
                                        libraryTable.setItems(
                                                FXCollections.observableArrayList(
                                                        List.of(publisher)));
                                    } catch (NumberFormatException e) {
                                        List<Publisher> publishers =
                                                publisherService.getPublisherByNamePattern(
                                                        newValue);
                                        libraryTable.setItems(
                                                FXCollections.observableArrayList(publishers));
                                    }
                                }
                            }
                        });

        docBtn.setOnAction(_ -> handleDocBtnClick());
        authorBtn.setOnAction(_ -> handleAuthorBtnClick());
        publisherBtn.setOnAction(_ -> handlePublisherBtnClick());
        addNewBtn.setOnAction(_ -> handleAddNewBtnClick());
    }

    private void setupDocumentTable() {
        libraryTable.getColumns().clear();

        TableColumn<Object, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(
                data -> {
                    Document doc = (Document) data.getValue();
                    return new SimpleStringProperty(String.valueOf(doc.id()));
                });
        idCol.setPrefWidth(idCol.getMinWidth() + 15);

        TableColumn<Object, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(
                data -> {
                    Document doc = (Document) data.getValue();
                    return new SimpleStringProperty(doc.title());
                });

        TableColumn<Object, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(
                data -> {
                    Document doc = (Document) data.getValue();
                    Author author = authorService.getAuthorById(doc.author_id());
                    return new SimpleStringProperty(author.fullName());
                });

        TableColumn<Object, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(
                data -> {
                    Document doc = (Document) data.getValue();
                    return new SimpleStringProperty(doc.type());
                });

        TableColumn<Object, String> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(
                data -> {
                    Document doc = (Document) data.getValue();
                    return new SimpleStringProperty(String.valueOf(doc.quantity()));
                });

        TableColumn<Object, Void> detailsCol = new TableColumn<>("Details");
        detailsCol.setCellFactory(
                column ->
                        new TableCell<>() {
                            private final Button detailsButton = new Button("Details");

                            {
                                detailsButton.setOnAction(
                                        event -> {
                                            Document document = (Document) getTableRow().getItem();
                                            if (document != null) {
                                                showDocumentDetailsDialog(document);
                                            }
                                        });
                            }

                            @Override
                            protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(detailsButton);
                                }
                            }
                        });

        libraryTable
                .getColumns()
                .addAll(idCol, titleCol, authorCol, typeCol, quantityCol, detailsCol);
        loadDocuments();
    }

    private void showDocumentDetailsDialog(Document document) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource("/fxml/component/DocumentDetailModal.fxml"));
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Document Details");
            modalStage.setScene(new Scene(loader.load()));

            DocumentDetailController controller = loader.getController();
            controller.setDocument(document);
            controller.setOnDocumentUpdated(this::loadDocuments);

            modalStage.showAndWait();
        } catch (IOException e) {
            LoggerUtils.error("Error loading document detail modal", e);
        }
    }

    private void setupAuthorTable() {
        libraryTable.getColumns().clear();

        TableColumn<Object, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(
                data -> {
                    Author author = (Author) data.getValue();
                    return new SimpleStringProperty(String.valueOf(author.id()));
                });
        idCol.setPrefWidth(idCol.getMinWidth() + 15);

        TableColumn<Object, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(
                data -> {
                    Author author = (Author) data.getValue();
                    return new SimpleStringProperty(author.fullName());
                });

        TableColumn<Object, String> penNameCol = new TableColumn<>("Pen Name");
        penNameCol.setCellValueFactory(
                data -> {
                    Author author = (Author) data.getValue();
                    return new SimpleStringProperty(author.penName().orElse(""));
                });

        TableColumn<Object, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(
                data -> {
                    Author author = (Author) data.getValue();
                    return new SimpleStringProperty(author.email().orElse(""));
                });

        libraryTable.getColumns().addAll(idCol, nameCol, penNameCol, emailCol);
        loadAuthors();
    }

    private void setupPublisherTable() {
        libraryTable.getColumns().clear();

        TableColumn<Object, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(
                data -> {
                    Publisher publisher = (Publisher) data.getValue();
                    return new SimpleStringProperty(String.valueOf(publisher.id()));
                });
        idCol.setPrefWidth(idCol.getMinWidth() + 15);

        TableColumn<Object, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(
                data -> {
                    Publisher publisher = (Publisher) data.getValue();
                    return new SimpleStringProperty(publisher.name());
                });

        TableColumn<Object, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(
                data -> {
                    Publisher publisher = (Publisher) data.getValue();
                    return new SimpleStringProperty(publisher.email().orElse(""));
                });

        TableColumn<Object, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(
                data -> {
                    Publisher publisher = (Publisher) data.getValue();
                    return new SimpleStringProperty(publisher.phone().orElse(""));
                });

        libraryTable.getColumns().addAll(idCol, nameCol, emailCol, phoneCol);
        loadPublishers();
    }

    private void loadDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        libraryTable.setItems(FXCollections.observableArrayList(documents));
    }

    private void loadAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        libraryTable.setItems(FXCollections.observableArrayList(authors));
    }

    private void loadPublishers() {
        List<Publisher> publishers = publisherService.getAllPublishers();
        libraryTable.setItems(FXCollections.observableArrayList(publishers));
    }

    private void handleDocBtnClick() {
        setStyle("Documents");
        displayingWhatLabel.setText("Documents");
        setupDocumentTable();
    }

    private void handleAuthorBtnClick() {
        setStyle("Authors");
        displayingWhatLabel.setText("Authors");
        setupAuthorTable();
    }

    private void handlePublisherBtnClick() {
        setStyle("Publishers");
        displayingWhatLabel.setText("Publishers");
        setupPublisherTable();
    }

    private void handleAddNewBtnClick() {
        String currentDisplay = displayingWhatLabel.getText();
        try {
            FXMLLoader loader =
                    switch (currentDisplay) {
                        case "Documents" ->
                                new FXMLLoader(
                                        getClass()
                                                .getResource(
                                                        "/fxml/component/AddDocumentModal.fxml"));
                        case "Authors" ->
                                new FXMLLoader(
                                        getClass()
                                                .getResource(
                                                        "/fxml/component/AddAuthorModal.fxml"));
                        case "Publishers" ->
                                new FXMLLoader(
                                        getClass()
                                                .getResource(
                                                        "/fxml/component/AddPublisherModal.fxml"));
                        default ->
                                throw new IllegalStateException(
                                        "Unexpected value: " + currentDisplay);
                    };

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Add " + currentDisplay.substring(0, currentDisplay.length() - 1));
            modalStage.setScene(new Scene(loader.load()));
            modalStage.showAndWait();

            switch (currentDisplay) {
                case "Documents" -> loadDocuments();
                case "Authors" -> loadAuthors();
                case "Publishers" -> loadPublishers();
            }
        } catch (IOException e) {
            LoggerUtils.error("Error loading add modal", e);
        }
    }

    private void setStyle(String currentDisplay) {
        clearStyle();

        switch (currentDisplay) {
            case "Documents" -> {
                docBtn.getStyleClass().add("document-button-active");
                docSvg.getStyleClass().add("document-button-active");
            }
            case "Authors" -> {
                authorBtn.getStyleClass().add("document-button-active");
                authorSvg.getStyleClass().add("document-button-active");
            }
            case "Publishers" -> {
                publisherBtn.getStyleClass().add("document-button-active");
                publisherSvg.getStyleClass().add("document-button-active");
            }
        }
    }

    private void clearStyle() {
        docBtn.getStyleClass().remove("document-button-active");
        docSvg.getStyleClass().remove("document-button-active");
        authorBtn.getStyleClass().remove("document-button-active");
        authorSvg.getStyleClass().remove("document-button-active");
        publisherBtn.getStyleClass().remove("document-button-active");
        publisherSvg.getStyleClass().remove("document-button-active");
    }
}
