package io.vn.dungxnd.duckabary.presentation.controller.page;

import io.vn.dungxnd.duckabary.domain.model.user.User;
import io.vn.dungxnd.duckabary.domain.service.ServiceManager;
import io.vn.dungxnd.duckabary.domain.service.borrow.BorrowService;
import io.vn.dungxnd.duckabary.domain.service.user.UserService;
import io.vn.dungxnd.duckabary.presentation.controller.modal.AddUserController;
import io.vn.dungxnd.duckabary.presentation.controller.modal.UserDetailController;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class UserMgmtController {
    private final UserService userService;
    private final BorrowService borrowService;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> uidColumn;
    @FXML private TableColumn<User, String> fullNameColumn;
    @FXML private TableColumn<User, String> addressColumn;
    @FXML private TableColumn<User, String> phoneColumn;
    @FXML private TableColumn<User, Void> borrowColumn;
    @FXML private TableColumn<User, Void> detailsColumn;
    @FXML private TextField searchInput;
    @FXML private Button searchBtn;
    @FXML private Button addUserBtn;

    public UserMgmtController() {
        this.userService = ServiceManager.getInstance().getUserService();
        this.borrowService = ServiceManager.getInstance().getBorrowService();
    }

    public void initialize() {
        searchBtn.setOnMouseClicked(event -> search());
        addUserBtn.setOnMouseClicked(event -> handleAddUser());
        searchInput.setOnKeyPressed(
                event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        search();
                    }
                });
        searchInput.textProperty().addListener((observable, oldValue, newValue) -> search());
        setupColumns();
        loadUsers();
    }

    private void setupColumns() {
        uidColumn.setCellValueFactory(
                data -> new SimpleStringProperty(String.valueOf(data.getValue().id())));
        fullNameColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue().firstName() + " " + data.getValue().lastName()));
        addressColumn.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().address()));
        phoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().phone()));

        detailsColumn.setCellFactory(
                column ->
                        new TableCell<>() {
                            private final Button detailsButton = new Button("Details");

                            {
                                detailsButton.setOnAction(
                                        event -> {
                                            User user = getTableRow().getItem();
                                            if (user != null) {
                                                showUserDetailsDialog(user);
                                            }
                                        });
                            }

                            @Override
                            protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(detailsButton);
                                }
                            }
                        });
    }

    private void loadUsers() {
        List<User> users = userService.getAllUsers();
        userTable.setItems(FXCollections.observableArrayList(users));
    }

    private void search() {
        String query = searchInput.getText();
        if (query.isEmpty()) {
            loadUsers();
            return;
        }

        try {
            if (query.matches("\\d+")) {
                // Search by ID
                int id = Integer.parseInt(query);
                User user = userService.getUserById(id);
                if (user != null) {
                    userTable.setItems(FXCollections.observableArrayList(List.of(user)));
                } else {
                    LoggerUtils.info("No user found with ID: " + id);
                    userTable.setItems(FXCollections.observableArrayList());
                }
            } else {
                // Search by name/username
                List<User> users = userService.searchByName(query);
                users.addAll(userService.searchByUsername(query).map(List::of).orElse(List.of()));
                userTable.setItems(FXCollections.observableArrayList(users));

                if (users.isEmpty()) {
                    LoggerUtils.info("No users found matching: " + query);
                }
            }
        } catch (Exception e) {
            LoggerUtils.info("Error searching for users: " + e.getMessage());
            userTable.setItems(FXCollections.observableArrayList());
        }
    }

    private void showUserDetailsDialog(User user) {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/component/UserDetailModal.fxml"));
            Parent root = loader.load();

            UserDetailController controller = loader.getController();
            controller.setUser(user);
            controller.setOnUserUpdated(this::loadUsers);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("User Details");
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (IOException e) {
            LoggerUtils.error("Error loading user details dialog", e);
        }
    }

    private void showAddUserModal() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/fxml/component/AddUserModal.fxml"));
            Parent root = loader.load();

            AddUserController controller = loader.getController();
            controller.setOnUserAdded(this::loadUsers);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Add User");
            dialog.setScene(new Scene(root));
            dialog.showAndWait();
        } catch (IOException e) {
            LoggerUtils.error("Error loading add user dialog", e);
        }
    }

    @FXML
    private void handleAddUser() {
        showAddUserModal();
    }
}
