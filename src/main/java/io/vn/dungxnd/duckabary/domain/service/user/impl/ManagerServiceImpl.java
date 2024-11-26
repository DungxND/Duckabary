package io.vn.dungxnd.duckabary.domain.service.user.impl;

import io.vn.dungxnd.duckabary.domain.model.user.Manager;
import io.vn.dungxnd.duckabary.domain.service.user.ManagerService;
import io.vn.dungxnd.duckabary.exception.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.user.ManagerRepository;

import java.util.List;
import java.util.Optional;

public class ManagerServiceImpl implements ManagerService {
    private final ManagerRepository managerRepository;

    public ManagerServiceImpl(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public Manager getManagerById(int id) throws DatabaseException {
        return managerRepository
                .findById(id)
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
    public Optional<Manager> findByUsername(String username) {
        return managerRepository.findByUsername(username);
    }

    @Override
    public Optional<Manager> findByEmail(String email) {
        return managerRepository.findByEmail(email);
    }

    @Override
    public boolean validateCredentials(String username, String password) {
        return managerRepository.validateCredentials(username, password);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return managerRepository.findByUsername(username).isEmpty();
    }

    private void validateManager(Manager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Manager cannot be null");
        }
        if (manager.username() == null || manager.username().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (manager.email() == null || manager.email().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!manager.email().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (manager.hashedPassword() == null || manager.hashedPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }
}
