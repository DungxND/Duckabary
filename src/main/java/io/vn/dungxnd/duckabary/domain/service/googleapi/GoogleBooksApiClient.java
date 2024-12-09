package io.vn.dungxnd.duckabary.domain.service.googleapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.vn.dungxnd.duckabary.domain.model.entity.Author;
import io.vn.dungxnd.duckabary.domain.model.entity.Publisher;
import io.vn.dungxnd.duckabary.domain.model.library.Book;
import io.vn.dungxnd.duckabary.domain.model.library.Document;
import io.vn.dungxnd.duckabary.domain.model.library.Journal;
import io.vn.dungxnd.duckabary.domain.service.entity.AuthorService;
import io.vn.dungxnd.duckabary.domain.service.entity.PublisherService;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GoogleBooksApiClient {
    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private final ObjectMapper mapper = new ObjectMapper();
    private final AuthorService authorService;
    private final PublisherService publisherService;

    public GoogleBooksApiClient(AuthorService authorService, PublisherService publisherService) {
        this.authorService = authorService;
        this.publisherService = publisherService;
    }

    public Document searchDocumentByIdentifier(String identifier) throws Exception {
        // Check if ISBN or ISSN
        String type = identifier.length() == 8 ? "JOURNAL" : "BOOK";
        String query = type.equals("JOURNAL") ? "issn:" : "isbn:";
        String jsonResponse = executeRequest(query + identifier);
        return parseDocumentResponse(jsonResponse, type);
    }

    public Image fetchDocumentImage(String identifier) {
        try {
            String jsonResponse =
                    executeRequest((identifier.length() == 8 ? "issn:" : "isbn:") + identifier);
            JsonNode rootNode = mapper.readTree(jsonResponse);
            JsonNode items = rootNode.path("items");

            if (items.isEmpty()) {
                return null;
            }

            JsonNode imageLinks = items.get(0).path("volumeInfo").path("imageLinks");

            String imageUrl =
                    imageLinks.path("extraLarge").asText("").isEmpty()
                            ? imageLinks.path("large").asText("").isEmpty()
                                    ? imageLinks.path("medium").asText("").isEmpty()
                                            ? imageLinks.path("thumbnail").asText("").isEmpty()
                                                    ? null
                                                    : imageLinks.path("thumbnail").asText()
                                            : imageLinks.path("medium").asText()
                                    : imageLinks.path("large").asText()
                            : imageLinks.path("extraLarge").asText();

            return imageUrl != null ? new Image(imageUrl) : null;

        } catch (Exception e) {
            LoggerUtils.error("Error fetching document image", e);
            return null;
        }
    }

    private Document parseDocumentResponse(String jsonResponse, String type) throws Exception {
        JsonNode rootNode = mapper.readTree(jsonResponse);
        JsonNode items = rootNode.path("items");
        if (items.isEmpty()) return null;

        JsonNode volumeInfo = items.get(0).path("volumeInfo");

        String authorName = volumeInfo.path("authors").get(0).asText("Unknown Author");
        Author author = authorService.getAuthorByName(authorName).orElse(null);
        if (author == null) {
            author = Author.createAuthor(null, authorName, null, null, null, null, null, null);
            author = authorService.saveAuthor(author);
        }

        // Get publisher info and create if not exists
        String publisherName = volumeInfo.path("publisher").asText("Unknown Publisher");
        Publisher publisher = publisherService.getPublisherByName(publisherName).orElse(null);
        if (publisher == null) {
            publisher = Publisher.createPublisher(null, publisherName, null, null, null);
            publisher = publisherService.savePublisher(publisher);
        }

        if (type.equals("JOURNAL")) {
            return new Journal(
                    null,
                    volumeInfo.path("title").asText(),
                    author.id(),
                    volumeInfo.path("description").asText(),
                    parsePublishYear(volumeInfo.path("publishedDate").asText()),
                    1,
                    type,
                    volumeInfo.path("industryIdentifiers").findValue("identifier").asText(),
                    volumeInfo.path("volumeNumber").asText("1"),
                    volumeInfo.path("issueNumber").asText("1"));
        } else {
            return new Book(
                    null,
                    volumeInfo.path("title").asText(),
                    author.id(),
                    volumeInfo.path("description").asText(),
                    parsePublishYear(volumeInfo.path("publishedDate").asText()),
                    1,
                    type,
                    volumeInfo.path("industryIdentifiers").findValue("identifier").asText(),
                    publisher.id(),
                    volumeInfo.path("language").asText("en"),
                    volumeInfo.path("categories").isEmpty()
                            ? "UNKNOWN"
                            : volumeInfo.path("categories").get(0).asText());
        }
    }

    public Book searchBookByIsbn(String isbn) throws Exception {
        String query = "isbn:" + isbn;
        String jsonResponse = executeRequest(query);
        return parseBookResponse(jsonResponse);
    }

    public List<Book> searchBooks(String title) throws Exception {
        String query = "intitle:" + title.replace(" ", "+");
        String jsonResponse = executeRequest(query);
        return parseBookListResponse(jsonResponse);
    }

    private String executeRequest(String query) throws Exception {
        URL url = new URI(API_URL + query).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader =
                new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    private Book parseBookResponse(String jsonResponse) throws Exception {
        JsonNode rootNode = mapper.readTree(jsonResponse);
        JsonNode items = rootNode.path("items");
        if (items.isEmpty()) return null;

        JsonNode volumeInfo = items.get(0).path("volumeInfo");
        return Book.createBook(
                null,
                volumeInfo.path("title").asText(),
                null,
                volumeInfo.path("description").asText(),
                parsePublishYear(volumeInfo.path("publishedDate").asText()),
                1,
                volumeInfo.path("industryIdentifiers").findValue("identifier").asText(),
                null,
                volumeInfo.path("language").asText(),
                volumeInfo.path("categories").isEmpty()
                        ? "UNKNOWN"
                        : volumeInfo.path("categories").get(0).asText());
    }

    private List<Book> parseBookListResponse(String jsonResponse) throws Exception {
        JsonNode rootNode = mapper.readTree(jsonResponse);
        JsonNode items = rootNode.path("items");
        List<Book> books = new ArrayList<>();

        for (JsonNode item : items) {
            JsonNode volumeInfo = item.path("volumeInfo");
            books.add(
                    Book.createBook(
                            null,
                            volumeInfo.path("title").asText(),
                            null,
                            volumeInfo.path("description").asText(),
                            parsePublishYear(volumeInfo.path("publishedDate").asText()),
                            1,
                            volumeInfo.path("industryIdentifiers").findValue("identifier").asText(),
                            null,
                            volumeInfo.path("language").asText(),
                            volumeInfo.path("categories").isEmpty()
                                    ? "UNKNOWN"
                                    : volumeInfo.path("categories").get(0).asText()));
        }
        return books;
    }

    private int parsePublishYear(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return 0;
        return Integer.parseInt(dateStr.substring(0, 4));
    }
}
