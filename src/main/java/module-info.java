module io.vn.dungxnd.duckabary {
    requires javafx.controls;
    requires javafx.fxml;
    requires password4j;
    requires org.jetbrains.annotations;
    requires io.github.cdimascio.dotenv.java;
    requires com.zaxxer.hikari;
    requires java.sql;
    requires org.slf4j;
    requires org.checkerframework.checker.qual;

    exports io.vn.dungxnd.duckabary.ui;

    opens io.vn.dungxnd.duckabary.ui to
            javafx.fxml;

    exports io.vn.dungxnd.duckabary.core.library_management;
    exports io.vn.dungxnd.duckabary.core.user_management;
    exports io.vn.dungxnd.duckabary.core.borrow_management;
    exports io.vn.dungxnd.duckabary;

    opens io.vn.dungxnd.duckabary to
            javafx.fxml;

    exports io.vn.dungxnd.duckabary.db;
}
