package io.vn.dungxnd.duckabary.presentation.controller.component;

import static io.vn.dungxnd.duckabary.util.ValidationUtils.isValidISBN;
import static io.vn.dungxnd.duckabary.util.ValidationUtils.isValidISSN;

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

public class DocumentDetailController {
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
    @FXML private Button saveBtn;
    @FXML private Label errorLabel;

    private Document document;
    private Runnable onDocumentUpdated;

    public DocumentDetailController() {
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
        typeChoice.setDisable(true);

        saveBtn.setOnAction(event -> handleSave());
    }

    private void getDocumentDetail() {
        isbnInput.setText(
                document instanceof Book
                        ? ((Book) document).isbn()
                        : document instanceof Journal ? ((Journal) document).issn() : "");
        titleInput.setText(document.title());
        descriptionInput.setText(document.description());
        authorInput.setText(authorService.getAuthorById(document.author_id()).fullName());
        authorInput.setDisable(true);
        publishYearInput.setText(String.valueOf(document.publishYear()));
        quantityInput.setText(String.valueOf(document.quantity()));

        if (document instanceof Book) {
            typeChoice.setValue("Book");
            publisherInput.setText(
                    publisherService.getPublisherById(((Book) document).publisher_id()).name());
            publisherInput.setDisable(true);
        } else if (document instanceof Journal) {
            typeChoice.setValue("Journal");
            publisherInput.setDisable(true);
        } else {
            typeChoice.setValue("Thesis");
            publisherInput.setDisable(true);
        }

        try {
            Image cover = googleBooksService.getDocumentImage(isbnInput.getText());
            coverImage.setImage(cover != null ? cover : defaultCover);
        } catch (Exception e) {
            coverImage.setImage(defaultCover);
        }
    }

    private void handleLookup() {
        String identifier = isbnInput.getText().trim();
        if (!identifier.isEmpty() && (isValidISBN(identifier) || isValidISSN(identifier))) {
            try {
                Document fetchedDoc = googleBooksService.getDocumentByIdentifier(identifier);
                Image cover = googleBooksService.getDocumentImage(identifier);

                coverImage.setImage(cover != null ? cover : defaultCover);
                titleInput.setText(fetchedDoc.title());
                descriptionInput.setText(fetchedDoc.description());
                authorInput.setText(authorService.getAuthorById(fetchedDoc.author_id()).fullName());
                publishYearInput.setText(String.valueOf(fetchedDoc.publishYear()));

                if (fetchedDoc instanceof Book) {
                    publisherInput.setText(
                            publisherService
                                    .getPublisherById(((Book) fetchedDoc).publisher_id())
                                    .name());
                }
            } catch (Exception e) {
                LoggerUtils.error("Error looking up document", e);
                showError("Failed to lookup document: " + e.getMessage());
            }
        } else {
            showError("Invalid ISBN/ISSN");
        }
    }

    @FXML
    private void handleSave() {
        try {
            String quantity = quantityInput.getText().trim();
            if (quantity.isEmpty()) {
                showError("Quantity is required");
                return;
            }

            int qty = Integer.parseInt(quantity);
            if (qty < 0) {
                showError("Quantity must not be negative");
                return;
            }

            Document updatedDoc =
                    switch (document) {
                        case Book book ->
                                new Book(
                                        book.id(),
                                        titleInput.getText(),
                                        book.author_id(),
                                        descriptionInput.getText(),
                                        Integer.parseInt(publishYearInput.getText()),
                                        qty,
                                        "BOOK",
                                        book.isbn(),
                                        book.publisher_id(),
                                        book.language(),
                                        book.genre());
                        case Journal journal ->
                                new Journal(
                                        journal.id(),
                                        titleInput.getText(),
                                        journal.author_id(),
                                        descriptionInput.getText(),
                                        Integer.parseInt(publishYearInput.getText()),
                                        qty,
                                        "JOURNAL",
                                        journal.issn(),
                                        journal.volume(),
                                        journal.issue());
                        case Thesis thesis ->
                                new Thesis(
                                        thesis.id(),
                                        titleInput.getText(),
                                        thesis.author_id(),
                                        descriptionInput.getText(),
                                        Integer.parseInt(publishYearInput.getText()),
                                        qty,
                                        "THESIS",
                                        thesis.university(),
                                        thesis.department(),
                                        thesis.supervisor(),
                                        thesis.degree(),
                                        thesis.defenseDate());
                        default -> throw new IllegalStateException("Unexpected document type");
                    };

            documentService.saveDocument(updatedDoc);

            if (onDocumentUpdated != null) {
                onDocumentUpdated.run();
            }
            closeModal();

        } catch (NumberFormatException e) {
            showError("Invalid number format");
        } catch (Exception e) {
            LoggerUtils.error("Error saving document", e);
            showError("Failed to save document: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setVisible(true);
        errorLabel.setText(message);
    }

    private void closeModal() {
        ((Stage) saveBtn.getScene().getWindow()).close();
    }

    public void setDocument(Document document) {
        this.document = document;
        getDocumentDetail();
    }

    public void setOnDocumentUpdated(Runnable callback) {
        this.onDocumentUpdated = callback;
    }
}
