module io.vn.dungxnd.duckabary {
    requires javafx.controls;
    requires javafx.fxml;
    requires password4j;
    requires org.jetbrains.annotations;
    requires io.github.cdimascio.dotenv.java;
    requires com.zaxxer.hikari;
    requires org.slf4j;
    requires org.checkerframework.checker.qual;
    requires java.prefs;
    requires commons.logging;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires opencensus.api;

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
    exports io.vn.dungxnd.duckabary.presentation.controller.auth;

    opens io.vn.dungxnd.duckabary.presentation.controller.auth to
            javafx.fxml;

    exports io.vn.dungxnd.duckabary.presentation.controller.modal;

    opens io.vn.dungxnd.duckabary.presentation.controller.modal to
            javafx.fxml;
}
