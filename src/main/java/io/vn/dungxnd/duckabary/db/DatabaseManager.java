package io.vn.dungxnd.duckabary.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseManager {
    private static final String DB_PATH =
            "src/main/resources/io/vn/dungxnd/duckabary/db/duckabary.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private static final String[] TABLES_CREATION_SQL_STATEMENTS = {
        "CREATE TABLE IF NOT EXISTS users ("
                + "    user_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    username text UNIQUE NOT NULL,"
                + "    firstname text,"
                + "    lastname text NOT NULL,"
                + "    email text NOT NULL,"
                + "    phone text,"
                + "    address text,"
                + "    registration_date datetime DEFAULT CURRENT_TIMESTAMP"
                + ");",
        "CREATE TABLE IF NOT EXISTS admins ("
                + "    admin_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    username text UNIQUE NOT NULL,"
                + "    email text NOT NULL,"
                + "    hashedPassword text NOT NULL"
                + ");",
        "CREATE TABLE IF NOT EXISTS documents ("
                + "    document_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    title text NOT NULL,"
                + "    author text,"
                + "    description text,"
                + "    publisher text,"
                + "    publish_year INTEGER,"
                + "    genre text,"
                + "    language text,"
                + "    ISBN text UNIQUE,"
                + "    quantity INTEGER DEFAULT 0"
                + ");",
        "CREATE TABLE IF NOT EXISTS borrow ("
                + "    borrow_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    user_id INTEGER NOT NULL,"
                + "    document_id INTEGER NOT NULL,"
                + "    borrow_date datetime DEFAULT CURRENT_TIMESTAMP,"
                + "    due_date datetime,"
                + "    return_date datetime,"
                + "    is_returned BOOLEAN DEFAULT 0,"
                + "    FOREIGN key (user_id) REFERENCES users (user_id),"
                + "    FOREIGN key (document_id) REFERENCES documents (document_id)"
                + ");",
        "CREATE index idx_user_email ON users (email);",
        "CREATE index idx_document_title ON documents (title);",
        "CREATE index idx_borrow_user ON borrow (user_id);",
        "CREATE index idx_borrow_document ON borrow (document_id);"
    };
    private static final String[] SQL_ALTER_TABLES = {
        "ALTER TABLE borrow RENAME TO _borrow_old;",
        "CREATE TABLE borrow ("
                + "    borrow_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    user_id INTEGER NOT NULL,"
                + "    document_id INTEGER NOT NULL,"
                + "    borrow_date datetime DEFAULT CURRENT_TIMESTAMP,"
                + "    due_date datetime,"
                + "    return_date datetime,"
                + "    FOREIGN key (user_id) REFERENCES users (user_id),"
                + "    FOREIGN key (document_id) REFERENCES documents (document_id)"
                + ");",
        "INSERT INTO borrow (borrow_id, user_id, document_id, borrow_date, due_date, return_date)"
                + "SELECT borrow_id, user_id, document_id, borrow_date, due_date, return_date FROM _borrow_old;",
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

    public static void createDatabaseIfNotExist() throws IOException {
        Path dbPath = Paths.get("src/main/resources/io/vn/dungxnd/duckabary/db/duckabary.db");
        createDBFileIfNotExist(dbPath);
        String url = "jdbc:sqlite:" + dbPath;

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {

            // check if tables already exist
            ResultSet userRs = conn.getMetaData().getTables(null, null, "users", null);
            ResultSet adminRs = conn.getMetaData().getTables(null, null, "admins", null);
            ResultSet docRs = conn.getMetaData().getTables(null, null, "documents", null);
            ResultSet borrowRs = conn.getMetaData().getTables(null, null, "borrow", null);

            if (userRs.next() && adminRs.next() && docRs.next() && borrowRs.next()) {
                return;
            } else {
                System.out.println("Database tables not found, creating new tables");
            }

            for (String sql : TABLES_CREATION_SQL_STATEMENTS) {
                stmt.execute(sql);
            }

            System.out.println("Tables created successfully!");

        } catch (SQLException e) {
            System.err.println("SQL error: " + e.getMessage());
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

    public static void createDBFileIfNotExist(Path dbPath) throws IOException {
        if (!dbPath.toFile().exists()) {
            System.out.println("Database file not found, creating a new one");
            boolean created = dbPath.toFile().createNewFile();
            if (created) {
                System.out.println("Database file created successfully!");
            } else {
                System.err.println("Failed to create the database file!");
            }
        }
    }

    public static void checkCorrectnessOfTablesColumns() {
        try (Connection conn = getConnection();
                ResultSet rs = conn.getMetaData().getColumns(null, null, "users", null)) {
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

    public static void checkAndInitDB() {
        Path dbPath = Paths.get(DB_PATH);
        try {
            createDatabaseIfNotExist();
        } catch (IOException e) {
            System.err.println("Error creating database file: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        alterTables();
    }
}
