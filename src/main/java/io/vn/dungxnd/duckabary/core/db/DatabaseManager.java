package io.vn.dungxnd.duckabary.core.db;

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
        "CREATE TABLE IF NOT EXISTS borrows ("
                + "    borrow_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "    user_id INTEGER NOT NULL,"
                + "    document_id INTEGER NOT NULL,"
                + "    borrow_quantity INTEGER NOT NULL,"
                + "    borrow_date datetime DEFAULT CURRENT_TIMESTAMP,"
                + "    due_date datetime,"
                + "    return_date datetime,"
                + "    FOREIGN key (user_id) REFERENCES users (user_id),"
                + "    FOREIGN key (document_id) REFERENCES documents (document_id)"
                + ");"
    };
    private static final String[] SQL_ALTER_TABLES = {
        "ALTER TABLE borrows RENAME TO _borrow_old;",
        "CREATE TABLE borrows ("
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
    private static volatile HikariDataSource dataSource;

    // https://gpcoder.com/6257-gioi-thieu-jdbc-connection-pool/
    private static synchronized HikariDataSource getDataSource() {
        if (dataSource == null) {
            try {
                createDatabaseIfNotExist();

                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(DB_URL);
                config.setMaximumPoolSize(10);
                config.setMinimumIdle(2);
                config.setIdleTimeout(30000);
                config.setConnectionTimeout(30000);
                config.setLeakDetectionThreshold(2000);

                dataSource = new HikariDataSource(config);
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize database", e);
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

    public static void main(String[] args) throws IOException {
        alterTables();
    }
}
