package io.vn.dungxnd.duckabary.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_PATH =
            "src/main/resources/io/vn/dungxnd/duckabary/db/duckabary.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private static final String[] SQL_STATEMENTS = {
        "CREATE TABLE IF NOT EXISTS users ("
                + "    user_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    username text UNIQUE NOT NULL,"
                + "    firstname text,"
                + "    lastname text NOT NULL,"
                + "    email text UNIQUE NOT NULL,"
                + "    phone text,"
                + "    address text,"
                + "    registration_date datetime DEFAULT CURRENT_TIMESTAMP"
                + ");",
        "CREATE TABLE IF NOT EXISTS admins ("
                + "    admin_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    username text UNIQUE NOT NULL,"
                + "    email text UNIQUE NOT NULL,"
                + "    password text NOT NULL"
                + ");",
        "CREATE TABLE IF NOT EXISTS documents ("
                + "    document_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    title text NOT NULL,"
                + "    author text NOT NULL,"
                + "    description text,"
                + "    publisher text,"
                + "    publication_date DATE,"
                + "    quantity INTEGER DEFAULT 1"
                + ");",
        "CREATE TABLE IF NOT EXISTS borrow ("
                + "    borrow_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    user_id INTEGER NOT NULL,"
                + "    document_id INTEGER NOT NULL,"
                + "    borrow_date datetime DEFAULT CURRENT_TIMESTAMP,"
                + "    due_date datetime NOT NULL,"
                + "    return_date datetime,"
                + "    FOREIGN key (user_id) REFERENCES users (user_id),"
                + "    FOREIGN key (document_id) REFERENCES documents (document_id)"
                + ");",
        "CREATE index idx_user_email ON users (email);",
        "CREATE index idx_document_title ON documents (title);",
        "CREATE index idx_borrow_user ON borrow (user_id);",
        "CREATE index idx_borrow_document ON borrow (document_id);"
    };
    private static final HikariDataSource dataSource;

    // https://gpcoder.com/6257-gioi-thieu-jdbc-connection-pool/
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);

        config.setConnectionTimeout(30000);
        config.setLeakDetectionThreshold(2000);

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public static void createTables() throws IOException {
        Path dbPath = Paths.get("src/main/resources/io/vn/dungxnd/duckabary/db/duckabary.db");
        if (!dbPath.toFile().exists()) {
            System.out.println("Database file not found, creating a new one");
            dbPath.toFile().createNewFile();
        }
        String url = "jdbc:sqlite:" + dbPath;

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {

            for (String sql : SQL_STATEMENTS) {
                stmt.execute(sql);
            }

            System.out.println("Tables created successfully!");

        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
    }

    public static void deleteDatabase() {
        Path dbPath = Paths.get(DB_PATH);
        if (dbPath.toFile().delete()) {
            System.out.println("Database deleted successfully!");
        } else {
            System.err.println("Failed to delete the database file!");
        }
    }

    public static void createDBFile(Path dbPath) throws IOException {
        if (!dbPath.toFile().exists()) {
            System.out.println("Database file not found, creating a new one");
            dbPath.toFile().createNewFile();
        }
    }

    public static void main(String[] args) throws IOException {
        createTables();
    }
}
