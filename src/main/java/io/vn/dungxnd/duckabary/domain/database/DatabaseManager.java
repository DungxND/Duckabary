package io.vn.dungxnd.duckabary.domain.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseManager {
    private static final String DB_PATH = "db/duckabary.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private static final String[] TABLES_CREATION_SQL_STATEMENTS = {
        "CREATE TABLE IF NOT EXISTS user ("
                + "    user_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    username TEXT UNIQUE NOT NULL,"
                + "    firstname TEXT,"
                + "    lastname TEXT NOT NULL,"
                + "    email TEXT NOT NULL,"
                + "    phone TEXT,"
                + "    address TEXT,"
                + "    registration_date datetime DEFAULT CURRENT_TIMESTAMP"
                + ");",
        "CREATE TABLE IF NOT EXISTS manager ("
                + "    manager_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    username TEXT UNIQUE NOT NULL,"
                + "    email TEXT NOT NULL,"
                + "    hashedPassword TEXT NOT NULL,"
                + "    avatarPath TEXT"
                + ");",
        "CREATE TABLE IF NOT EXISTS author ("
                + "    author_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    fullName TEXT NOT NULL,"
                + "    penName TEXT,"
                + "    email TEXT,"
                + "    phone TEXT,"
                + "    address TEXT,"
                + "    birthDate datetime,"
                + "    deathDate datetime"
                + ");",
        "CREATE TABLE IF NOT EXISTS document ("
                + "    document_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    title TEXT NOT NULL,"
                + "    author_id INTEGER NOT NULL,"
                + "    description TEXT,"
                + "    publish_year INTEGER,"
                + "    quantity INTEGER DEFAULT 0,"
                + "    type TEXT NOT NULL CHECK (type IN ('BOOK', 'JOURNAL', 'THESIS')),"
                + "    created_at datetime DEFAULT CURRENT_TIMESTAMP,"
                + "    updated_at datetime DEFAULT CURRENT_TIMESTAMP,"
                + "    FOREIGN key (author_id) REFERENCES author (author_id) ON DELETE RESTRICT"
                + ");",
        "CREATE TABLE IF NOT EXISTS publisher ("
                + "    publisher_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    name TEXT NOT NULL,"
                + "    email TEXT,"
                + "    phone TEXT,"
                + "    address TEXT"
                + ");",
        "CREATE TABLE IF NOT EXISTS book ("
                + "    document_id INTEGER PRIMARY KEY,"
                + "    isbn TEXT NOT NULL,"
                + "    publisher_id INT,"
                + "    language TEXT,"
                + "    genre TEXT,"
                + "    FOREIGN key (document_id) REFERENCES document (document_id)"
                + "    FOREIGN key (publisher_id) REFERENCES publisher_id (publisher_id)"
                + ");",
        "CREATE TABLE IF NOT EXISTS journal ("
                + "    document_id INTEGER PRIMARY KEY,"
                + "    issn TEXT NOT NULL,"
                + "    volume TEXT,"
                + "    issue TEXT,"
                + "    FOREIGN key (document_id) REFERENCES document (document_id)"
                + ");",
        "CREATE TABLE IF NOT EXISTS thesis ("
                + "    document_id INTEGER PRIMARY KEY,"
                + "    university TEXT,"
                + "    department TEXT,"
                + "    supervisor TEXT,"
                + "    degree TEXT,"
                + "    defense_date datetime,"
                + "    FOREIGN key (document_id) REFERENCES document (document_id)"
                + ");",
        "CREATE TABLE IF NOT EXISTS borrow ("
                + "    borrow_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    user_id INTEGER NOT NULL,"
                + "    document_id INTEGER NOT NULL,"
                + "    borrow_quantity INTEGER NOT NULL,"
                + "    borrow_date datetime DEFAULT CURRENT_TIMESTAMP,"
                + "    due_date datetime,"
                + "    return_date datetime,"
                + "    FOREIGN key (user_id) REFERENCES user (user_id),"
                + "    FOREIGN key (document_id) REFERENCES document (document_id)"
                + ");",
        "CREATE INDEX IF NOT EXISTS idx_manager_username ON manager(username);",
        "CREATE INDEX IF NOT EXISTS idx_manager_email ON manager(email);",
        "CREATE INDEX IF NOT EXISTS idx_document_title ON document(title);",
        "CREATE INDEX IF NOT EXISTS idx_borrow_user ON borrow(user_id);"
    };
    private static final String[] SQL_ALTER_TABLES = {};
    private static volatile HikariDataSource dataSource;

    // https://gpcoder.com/6257-gioi-thieu-jdbc-connection-pool/

    private static HikariDataSource getDataSource() {
        if (dataSource == null) {
            synchronized (DatabaseManager.class) {
                if (dataSource == null) {
                    try {
                        createDatabaseIfNotExist();
                        HikariConfig config = new HikariConfig();
                        config.setJdbcUrl(DB_URL);
                        config.setMaximumPoolSize(10);
                        config.setMinimumIdle(5);
                        config.setIdleTimeout(100000);
                        config.setConnectionTimeout(20000);
                        config.setLeakDetectionThreshold(60000);
                        config.setKeepaliveTime(60000);
                        dataSource = new HikariDataSource(config);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to initialize database", e);
                    }
                }
            }
        }
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public static void close() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }

    private static File getDatabaseFile() {
        File dbFile = new File(DB_PATH);
        if (!dbFile.exists()) {
            dbFile.getParentFile().mkdirs();
        }
        return dbFile;
    }

    public static void createDatabaseIfNotExist() throws IOException {
        File dbFile = getDatabaseFile();
        if (!dbFile.exists()) {
            System.out.println("Database file not found, creating a new one...");
            try {
                dbFile.getParentFile().mkdirs();
                dbFile.createNewFile();

                try (Connection conn = DriverManager.getConnection(DB_URL);
                        Statement stmt = conn.createStatement()) {
                    for (String sql : TABLES_CREATION_SQL_STATEMENTS) {
                        stmt.execute(sql);
                    }
                }
                System.out.println("Database created successfully!");
            } catch (IOException | SQLException e) {
                throw new RuntimeException("Failed to initialize database", e);
            }
        }
    }

    public static void alterTables() {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {
            for (String sql : SQL_ALTER_TABLES) {
                stmt.execute(sql);
            }
            System.out.println("Tables altered successfully!");
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

    public static void checkCorrectnessOfTablesColumns() {
        try (Connection conn = getConnection();
                ResultSet rs = conn.getMetaData().getColumns(null, null, "user", null)) {
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                String columnType = rs.getString("TYPE_NAME");
                System.out.println("Column name: " + columnName + ", type: " + columnType);
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
        try {
            Connection conn = getConnection();
            ResultSet rs = conn.getMetaData().getColumns(null, null, "admins", null);
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                String columnType = rs.getString("TYPE_NAME");
                System.out.println("Column name: " + columnName + ", type: " + columnType);
            }
        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        createDatabaseIfNotExist();
    }
}
