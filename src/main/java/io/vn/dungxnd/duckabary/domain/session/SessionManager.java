package io.vn.dungxnd.duckabary.domain.session;

import io.vn.dungxnd.duckabary.domain.model.user.Manager;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SessionManager {
    private static final StringProperty currentUsername = new SimpleStringProperty();
    private static Manager currentManager;

    public static StringProperty currentUsernameProperty() {
        return currentUsername;
    }

    public static Manager getCurrentManager() {
        return currentManager;
    }

    public static void setCurrentManager(Manager manager) {
        currentManager = manager;
        currentUsername.set(manager.username());
    }

    public static void clearSession() {
        currentManager = null;
    }

    public static boolean isLoggedIn() {
        return currentManager != null;
    }
}
