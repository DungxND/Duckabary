package io.vn.dungxnd.duckabary.domain.service.user.impl;

import static io.vn.dungxnd.duckabary.util.ValidationUtils.*;

import io.vn.dungxnd.duckabary.domain.model.user.Manager;
import io.vn.dungxnd.duckabary.domain.service.user.ManagerService;
import io.vn.dungxnd.duckabary.exception.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.user.ManagerRepository;
import io.vn.dungxnd.duckabary.util.LoggerUtils;

import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

public class ManagerServiceImpl implements ManagerService {
    private static final String AVATAR_DIR = "avatars/managers/";
    private final ManagerRepository managerRepository;

    public ManagerServiceImpl(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    public List<Manager> getAllManagers() {
        return managerRepository.getAll();
    }

    @Override
    public Manager getManagerById(int id) throws DatabaseException {
        return managerRepository
                .searchById(id)
                .orElseThrow(() -> new DatabaseException("Manager not found with id: " + id));
    }

    @Override
    public Manager saveManager(Manager manager) throws DatabaseException {
        validateManager(manager);

        if (manager.managerId() == 0) {
            if (!isUsernameAvailable(manager.username())) {
                throw new DatabaseException("Username already taken: " + manager.username());
            }
        }

        return managerRepository.save(manager);
    }

    @Override
    public void deleteManager(int id) throws DatabaseException {
        managerRepository.delete(id);
    }

    @Override
    public Optional<Manager> searchByUsername(String username) {
        return managerRepository.searchByUsername(username);
    }

    @Override
    public Optional<Manager> searchByEmail(String email) {
        return managerRepository.searchByEmail(email);
    }

    @Override
    public boolean isValidCredentials(String username, String password) {
        return managerRepository.validateCredentials(username, password);
    }

    @Override
    public Optional<Manager> authenticate(String username, String password) {
        try {
            if (isValidCredentials(username, password)) {
                return searchByUsername(username);
            }
        } catch (DatabaseException e) {
            LoggerUtils.error("Error authenticating manager " + username, e);
        }
        return Optional.empty();
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return managerRepository.searchByUsername(username).isEmpty();
    }

    @Override
    public Manager updateManagerAvatar(int managerId, File imageFile) throws DatabaseException {
        try {
            Files.createDirectories(Paths.get(AVATAR_DIR));

            String fileName = managerId + "_" + UUID.randomUUID() + ".png";
            Path destinationPath = Paths.get(AVATAR_DIR, fileName);

            BufferedImage originalImage = ImageIO.read(imageFile);
            BufferedImage resizedImage = resizeImage(originalImage, 200, 200);
            ImageIO.write(resizedImage, "png", destinationPath.toFile());

            Manager manager = getManagerById(managerId);
            manager = manager.withAvatarPath(destinationPath.toString());
            return saveManager(manager);

        } catch (IOException e) {
            throw new DatabaseException("Failed to save avatar image", e);
        }
    }

    @Override
    public Image getAvatarImage(int managerId) {
        try {
            Manager manager = getManagerById(managerId);
            if (manager.avatarPath() == null) {
                return null;
            }
            return new Image(new FileInputStream(manager.avatarPath()));
        } catch (FileNotFoundException | DatabaseException e) {
            LoggerUtils.error("Error loading avatar for manager " + managerId, e);
            return null;
        }
    }

    private BufferedImage resizeImage(
            BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage =
                new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    private void validateManager(Manager manager) {
        try {
            validateHashedPassword(manager.hashedPassword());
            validateRequiredEmail(manager.email());
            validateRequiredUsername(manager.username());
            validateRawPassword(manager.hashedPassword());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
