// package io.vn.dungxnd.duckabary.domain.service.googleapi;
//
// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import io.vn.dungxnd.duckabary.domain.model.library.Book;
//
// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.net.HttpURLConnection;
// import java.net.URL;
// import java.util.ArrayList;
// import java.util.List;
//
// public class GoogleBooksApiClient {
//    private static final String API_URL = "https://www.googleapis.com/books/v1/volumes?q=";
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    public Book searchBookByIsbn(String isbn) throws Exception {
//        String query = "isbn:" + isbn;
//        String jsonResponse = executeRequest(query);
//        return parseBookResponse(jsonResponse);
//    }
//
//    public List<Book> searchBooks(String title) throws Exception {
//        String query = "intitle:" + title.replace(" ", "+");
//        String jsonResponse = executeRequest(query);
//        return parseBookListResponse(jsonResponse);
//    }
//
//    private String executeRequest(String query) throws Exception {
//        URL url = new URL(API_URL + query);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//
//        try (BufferedReader reader = new BufferedReader(
//                new InputStreamReader(connection.getInputStream()))) {
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//            return response.toString();
//        }
//    }
//
//    private Book parseBookResponse(String jsonResponse) throws Exception {
//        JsonNode rootNode = mapper.readTree(jsonResponse);
//        JsonNode items = rootNode.path("items");
//        if (items.isEmpty()) return null;
//
//        JsonNode volumeInfo = items.get(0).path("volumeInfo");
//        return Book.createBook(
//            null,
//            volumeInfo.path("title").asText(),
//            null, // author_id will be set later
//            volumeInfo.path("description").asText(),
//            parsePublishYear(volumeInfo.path("publishedDate").asText()),
//            1, // default quantity
//            volumeInfo.path("industryIdentifiers")
//                    .findValue("identifier").asText(),
//            null, // publisher_id will be set later
//            volumeInfo.path("language").asText(),
//            volumeInfo.path("categories").isEmpty() ?
//                "UNKNOWN" : volumeInfo.path("categories").get(0).asText()
//        );
//    }
//
//    private List<Book> parseBookListResponse(String jsonResponse) throws Exception {
//        JsonNode rootNode = mapper.readTree(jsonResponse);
//        JsonNode items = rootNode.path("items");
//        List<Book> books = new ArrayList<>();
//
//        for (JsonNode item : items) {
//            JsonNode volumeInfo = item.path("volumeInfo");
//            books.add(Book.createBook(
//                null,
//                volumeInfo.path("title").asText(),
//                null,
//                volumeInfo.path("description").asText(),
//                parsePublishYear(volumeInfo.path("publishedDate").asText()),
//                1,
//                volumeInfo.path("industryIdentifiers")
//                        .findValue("identifier").asText(),
//                null,
//                volumeInfo.path("language").asText(),
//                volumeInfo.path("categories").isEmpty() ?
//                    "UNKNOWN" : volumeInfo.path("categories").get(0).asText()
//            ));
//        }
//        return books;
//    }
//
//    private int parsePublishYear(String dateStr) {
//        if (dateStr == null || dateStr.isEmpty()) return 0;
//        return Integer.parseInt(dateStr.substring(0, 4));
//    }
// }
