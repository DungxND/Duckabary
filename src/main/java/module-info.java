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

    exports io.vn.dungxnd.duckabary.domain.service.user;
    exports io.vn.dungxnd.duckabary.domain.model.user;
    exports io.vn.dungxnd.duckabary.domain.model.library;
    exports io.vn.dungxnd.duckabary.domain.database;
    exports io.vn.dungxnd.duckabary.presentation.controller;

    opens io.vn.dungxnd.duckabary.presentation.controller to
            javafx.fxml;

    exports io.vn.dungxnd.duckabary.presentation.ui;

    opens io.vn.dungxnd.duckabary.presentation.ui to
            javafx.fxml;

    exports io.vn.dungxnd.duckabary.presentation.cli;

    opens io.vn.dungxnd.duckabary.presentation.cli to
            javafx.fxml;

    exports io.vn.dungxnd.duckabary.domain.service.library;
    exports io.vn.dungxnd.duckabary.exception;
    exports io.vn.dungxnd.duckabary.domain.service.borrow;
}
