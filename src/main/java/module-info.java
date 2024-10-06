module io.vn.dungxnd.duckabary {
    requires javafx.controls;
    requires javafx.fxml;


    opens io.vn.dungxnd.duckabary to javafx.fxml;
    exports io.vn.dungxnd.duckabary.core;
    exports io.vn.dungxnd.duckabary.ui;
    opens io.vn.dungxnd.duckabary.ui to javafx.fxml;
}