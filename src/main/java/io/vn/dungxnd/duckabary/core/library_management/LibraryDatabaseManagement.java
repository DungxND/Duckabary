package io.vn.dungxnd.duckabary.core.library_management;

import io.vn.dungxnd.duckabary.db.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class LibraryDatabaseManagement {
    private static final String DB_PATH =
            "src/main/resources/io/vn/dungxnd/duckabary/db/duckabary.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    public LinkedHashMap<Integer, Document> loadDocumentsFromDB() {
        LinkedHashMap<Integer, Document> documents = new LinkedHashMap<>();

        String sql = "SELECT * FROM documents";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.isClosed()) {
                System.out.println("No document data found!");
                return documents;
            }

            while (rs.next()) {
                int docId = rs.getInt("document_id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String description = rs.getString("description");
                String publisher = rs.getString("publisher");
                int publishYear = rs.getInt("publish_year");
                String genre = rs.getString("genre");
                String language = rs.getString("language");
                String ISBN = rs.getString("ISBN");
                int quantity = rs.getInt("quantity");

                Document document =
                        new Document(
                                docId,
                                title,
                                author,
                                new StringBuilder(description),
                                publisher,
                                publishYear,
                                genre,
                                language,
                                ISBN,
                                quantity);
                documents.put(docId, document);
            }
        } catch (SQLException e) {
            System.out.println("Error loading documents: " + e.getMessage());
        }
        System.out.println("Loaded " + documents.size() + " documents from database.");
        return documents;
    }

    public void addDocumentToDB(Document document) {
        String sql =
                "INSERT INTO documents (title, author, description, publisher, publish_year, genre, language, ISBN, quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, document.getTitle());
            pstmt.setString(2, document.getAuthor());
            pstmt.setString(3, document.getDescription().toString());
            pstmt.setString(4, document.getPublisher());
            pstmt.setInt(5, document.getPublishYear());
            pstmt.setString(6, document.getGenre());
            pstmt.setString(7, document.getLanguage());
            pstmt.setString(8, document.getISBN());
            pstmt.setInt(9, document.getQuantity());

            if (document.getTitle().isEmpty()) pstmt.setNull(1, java.sql.Types.VARCHAR);
            if (document.getAuthor().isEmpty()) pstmt.setNull(2, java.sql.Types.VARCHAR);
            if (document.getDescription().toString().isEmpty())
                pstmt.setNull(3, java.sql.Types.VARCHAR);
            if (document.getPublisher().isEmpty()) pstmt.setNull(4, java.sql.Types.VARCHAR);
            if (document.getGenre().isEmpty()) pstmt.setNull(6, java.sql.Types.VARCHAR);
            if (document.getLanguage().isEmpty()) pstmt.setNull(7, java.sql.Types.VARCHAR);
            if (document.getISBN().isEmpty()) pstmt.setNull(8, java.sql.Types.VARCHAR);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error adding document: " + e.getMessage());
        }
    }

    public void removeDocumentFromDB(int docId) {
        String sql = "DELETE FROM documents WHERE document_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, docId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error removing document: " + e.getMessage());
        }
    }

    public void updateDocumentIDInDB(int oldId, int newId) {
        String sql = "UPDATE documents SET document_id = ? WHERE document_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newId);
            pstmt.setInt(2, oldId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating document ID: " + e.getMessage());
        }
    }

    public void updateDocumentData(Document doc){
        String sql = "UPDATE documents SET title = ?, author = ?, description = ?, publisher = ?, publish_year = ?, genre = ?, language = ?, ISBN = ?, quantity = ? WHERE document_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, doc.getTitle());
            pstmt.setString(2, doc.getAuthor());
            pstmt.setString(3, doc.getDescription().toString());
            pstmt.setString(4, doc.getPublisher());
            pstmt.setInt(5, doc.getPublishYear());
            pstmt.setString(6, doc.getGenre());
            pstmt.setString(7, doc.getLanguage());
            pstmt.setString(8, doc.getISBN());
            pstmt.setInt(9, doc.getQuantity());
            pstmt.setInt(10, doc.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating document data: " + e.getMessage());
        }
    }
    public void updateDocumentQuantityInDB(int docId, int newQuantity) {
        String sql = "UPDATE documents SET quantity = ? WHERE document_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, docId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating document quantity: " + e.getMessage());
        }
    }
}
