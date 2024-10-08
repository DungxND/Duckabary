package io.vn.dungxnd.duckabary.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class CSVImporter {
    public static void main(String[] args) {
        String url = "jdbc:duckdb:libsql://duckabary-db-dungxnd.turso.io";
        String authToken = System.getenv("TURSODB_TOKEN");
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            // Load CSV files into tables
            stmt.execute("COPY users FROM '../../fakeDataGen/datas/users.csv' (HEADER, DELIMITER ',');");
            stmt.execute("COPY admins FROM '../../fakeDataGen/datas/admins.csv' (HEADER, DELIMITER ',');");
            stmt.execute("COPY documents FROM '../../fakeDataGen/datas/documents.csv' (HEADER, DELIMITER ',');");
            stmt.execute("COPY borrow FROM '../../fakeDataGen/datas/borrows.csv' (HEADER, DELIMITER ',');");

            System.out.println("CSV files imported successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}