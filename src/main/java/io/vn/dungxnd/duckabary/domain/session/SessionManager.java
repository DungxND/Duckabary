package io.vn.dungxnd.duckabary.domain.session;

import io.vn.dungxnd.duckabary.domain.model.user.Manager;
import io.vn.dungxnd.duckabary.domain.service.ServiceManager;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class SessionManager {
    private static final StringProperty currentUsername = new SimpleStringProperty();
    private static final ObjectProperty<Image> currentAvatar = new SimpleObjectProperty<>();
    private static final StringProperty currentEmail = new SimpleStringProperty();
    private static Manager currentManager;

    public static StringProperty currentUsernameProperty() {
        return currentUsername;
    }

    public static ObjectProperty<Image> currentAvatarProperty() {
        return currentAvatar;
    }

    public static StringProperty currentEmailProperty() {
        return currentEmail;
    }

    public static Manager getCurrentManager() {
        return currentManager;
    }

    public static void setCurrentManager(Manager manager) {
        currentManager = manager;
        if (manager != null) {
            currentUsername.set(manager.username());
            currentEmail.set(manager.email());
            try {
                Image avatarImage =
                        ServiceManager.getInstance()
                                .getManagerService()
                                .getAvatarImage(manager.managerId());
                currentAvatar.set(avatarImage);
            } catch (Exception e) {
                LoggerUtils.error("Error loading avatar", e);
                currentAvatar.set(null);
            }
        } else {
            clearSession();
        }
    }

    public static void clearSession() {
        currentManager = null;
        currentUsername.set(null);
        currentEmail.set(null);
        currentAvatar.set(null);
    }

    public static boolean isLoggedIn() {
        return currentManager != null;
    }
}
