package io.vn.dungxnd.duckabary.domain.service.user.impl;

import static io.vn.dungxnd.duckabary.util.ValidationUtils.*;

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
        if (isValidCredentials(username, password)) {
            return searchByUsername(username);
        }
        return Optional.empty();
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return managerRepository.searchByUsername(username).isEmpty();
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
