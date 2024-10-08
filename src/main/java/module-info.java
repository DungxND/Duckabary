module io.vn.dungxnd.duckabary {
    requires javafx.controls;
    requires javafx.fxml;
    requires password4j;
    requires org.jetbrains.annotations;
    requires java.sql;


    exports io.vn.dungxnd.duckabary.ui;
    opens io.vn.dungxnd.duckabary.ui to javafx.fxml;
    exports io.vn.dungxnd.duckabary.core.library_management;
}