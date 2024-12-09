package io.vn.dungxnd.duckabary.presentation.controller.component;

import static io.vn.dungxnd.duckabary.util.ValidationUtils.isValidISBN;
import static io.vn.dungxnd.duckabary.util.ValidationUtils.isValidISSN;

import io.vn.dungxnd.duckabary.domain.model.entity.Author;
import io.vn.dungxnd.duckabary.domain.model.library.Book;
import io.vn.dungxnd.duckabary.domain.model.library.Document;
import io.vn.dungxnd.duckabary.domain.model.library.Journal;
import io.vn.dungxnd.duckabary.domain.model.library.Thesis;
import io.vn.dungxnd.duckabary.domain.service.ServiceManager;
import io.vn.dungxnd.duckabary.domain.service.entity.AuthorService;
import io.vn.dungxnd.duckabary.domain.service.entity.PublisherService;
import io.vn.dungxnd.duckabary.domain.service.googleapi.GoogleBooksService;
import io.vn.dungxnd.duckabary.domain.service.library.DocumentService;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

public class AddDocumentController {

    private final GoogleBooksService googleBooksService;
    private final DocumentService documentService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final Image defaultCover;
    @FXML private TextField isbnInput;
    @FXML private TextField titleInput;
    @FXML private TextField authorInput;
    @FXML private TextField publisherInput;
    @FXML private TextField publishYearInput;
    @FXML private TextField quantityInput;
    @FXML private ImageView coverImage;
    @FXML private ChoiceBox<String> typeChoice;
    @FXML private TextArea descriptionInput;
    @FXML private Button lookupBtn;
    @FXML private Button saveBtn;
    @FXML private Label errorLabel;

    public AddDocumentController() {
        defaultCover =
                new Image(
                        Objects.requireNonNull(
                                getClass().getResourceAsStream("/image/default_cover.png")));
        this.googleBooksService = ServiceManager.getInstance().getGoogleBooksService();
        this.documentService = ServiceManager.getInstance().getDocumentService();
        this.authorService = ServiceManager.getInstance().getAuthorService();
        this.publisherService = ServiceManager.getInstance().getPublisherService();
    }

    public void initialize() {
        errorLabel.setVisible(false);
        typeChoice.getItems().addAll("Book", "Journal", "Thesis");
        saveBtn.setOnMouseClicked(event -> handleSave());
        lookupBtn.setOnMouseClicked(event -> handleLookup());
    }

    private void handleLookup() {
        String identifier = isbnInput.getText().trim();

        if (!identifier.isEmpty() && (isValidISBN(identifier) || isValidISSN(identifier))) {
            try {
                Document document = googleBooksService.getDocumentByIdentifier(identifier);
                Image cover = googleBooksService.getDocumentImage(identifier);
                if (cover != null) {
                    coverImage.setImage(cover);
                } else {
                    coverImage.setImage(defaultCover);
                }
                titleInput.setText(document.title());
                descriptionInput.setText(document.description());
                authorInput.setText(authorService.getAuthorById(document.author_id()).fullName());
                if (document instanceof Book) {
                    publisherInput.setText(
                            publisherService
                                    .getPublisherById(((Book) document).publisher_id())
                                    .name());
                    typeChoice.setValue("Book");
                } else if (document instanceof Journal) {
                    typeChoice.setValue("Journal");
                    publisherInput.setDisable(true);
                }
                typeChoice.setDisable(true);
                publishYearInput.setText(String.valueOf(document.publishYear()));
            } catch (Exception e) {
                errorLabel.setVisible(true);
                errorLabel.setText("Document not found with identifier: " + identifier);
                LoggerUtils.error("Error looking up doc: ", e);
            }
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("Invalid ISBN/ISSN");
        }
    }

    @FXML
    private void handleSave() {
        String identifier = isbnInput.getText().trim();
        String title = titleInput.getText().trim();
        String authorName = authorInput.getText().trim();
        String publisherName = publisherInput.getText().trim();
        String publishYear = publishYearInput.getText().trim();
        String quantity = quantityInput.getText().trim();
        String type = typeChoice.getValue();
        String description = descriptionInput.getText().trim();

        if (title.isEmpty() || authorName.isEmpty() || quantity.isEmpty() || type == null) {
            showError("Please fill in all required fields");
            return;
        }

        try {
            int year = publishYear.isEmpty() ? 0 : Integer.parseInt(publishYear);
            int qty = Integer.parseInt(quantity);

            if (qty <= 0) {
                showError("Quantity must be positive");
                return;
            }

            if (year < 0) {
                showError("Year must be positive");
                return;
            }

            if (documentService.isDocumentExist(isbnInput.getText())) {
                showError("Document already exists");
                return;
            }

            Author author =
                    authorService
                            .getAuthorByName(authorName)
                            .orElseGet(
                                    () ->
                                            authorService.saveAuthor(
                                                    Author.createAuthor(
                                                            null,
                                                            authorName,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null,
                                                            null)));

            Document document;
            switch (type) {
                case "Book" -> {
                    if (publisherName.isEmpty()) {
                        showError("Publisher is required for books");
                        return;
                    }
                    document =
                            new Book(
                                    null,
                                    title,
                                    author.id(),
                                    description,
                                    year,
                                    qty,
                                    "BOOK",
                                    identifier,
                                    null,
                                    "en",
                                    "UNKNOWN");
                    document =
                            documentService.saveBookWithPublisher((Book) document, publisherName);
                }
                case "Journal" -> {
                    document =
                            new Journal(
                                    null,
                                    title,
                                    author.id(),
                                    description,
                                    year,
                                    qty,
                                    "JOURNAL",
                                    identifier,
                                    "1",
                                    "1");
                }
                case "Thesis" -> {
                    document =
                            new Thesis(
                                    null,
                                    title,
                                    author.id(),
                                    description,
                                    year,
                                    qty,
                                    "THESIS",
                                    "",
                                    "",
                                    "",
                                    "",
                                    Optional.empty());
                }
                default -> {
                    showError("Invalid document type");
                    return;
                }
            }

            document = documentService.saveDocument(document);
            closeModal();

        } catch (NumberFormatException e) {
            showError("Invalid year or quantity format");
        } catch (Exception e) {
            LoggerUtils.error("Error saving document", e);
            showError("Failed to save document: " + e.getMessage());
        }
    }

    private void closeModal() {
        ((Stage) saveBtn.getScene().getWindow()).close();
    }

    private void showError(String message) {
        errorLabel.setVisible(true);
        errorLabel.setText(message);
        new Thread(
                        () -> {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                LoggerUtils.error("Error waiting for error message to hide", e);
                            }
                            errorLabel.setVisible(false);
                        })
                .start();
    }
}
