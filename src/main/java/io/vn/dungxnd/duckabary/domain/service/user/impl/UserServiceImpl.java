package io.vn.dungxnd.duckabary.domain.service.user.impl;

import io.vn.dungxnd.duckabary.domain.model.library.BorrowRecord;
import io.vn.dungxnd.duckabary.domain.model.user.User;
import io.vn.dungxnd.duckabary.domain.service.user.UserService;
import io.vn.dungxnd.duckabary.exception.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.BorrowRecordRepository;
import io.vn.dungxnd.duckabary.infrastructure.repository.user.UserRepository;
import io.vn.dungxnd.duckabary.util.LoggerUtils;
import io.vn.dungxnd.duckabary.util.ValidationUtils;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRepository;

    public UserServiceImpl(UserRepository userRepository, BorrowRecordRepository borrowRepository) {
        this.userRepository = userRepository;
        this.borrowRepository = borrowRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    @Override
    public User getUserById(int id) throws DatabaseException {

        try {
            return userRepository
                    .searchById(id)
                    .orElseThrow(() -> new DatabaseException("User not found with id: " + id));
        } catch (Exception e) {
            throw new DatabaseException("User not found with id: " + id);
        }
    }

    @Override
    public User saveUser(User user) throws DatabaseException {
        validateUser(user);

        if (user.id() == 0 && !isUsernameAvailable(user.username())) {
            throw new DatabaseException("Username already taken: " + user.username());
        }

        if (user.id() == 0 && !isEmailAvailable(user.email())) {
            throw new DatabaseException("Email already in use: " + user.email());
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(int id) throws DatabaseException {
        List<BorrowRecord> activeBorrows =
                borrowRepository.searchByUserId(id).stream()
                        .filter(record -> !record.isReturned())
                        .toList();

        if (!activeBorrows.isEmpty()) {
            throw new DatabaseException("Cannot delete user with active borrows");
        }

        userRepository.delete(id);
    }

    @Override
    public List<User> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Search name cannot be empty");
        }
        return userRepository.searchByName(name.trim());
    }

    @Override
    public Optional<User> searchByUsername(String username) {
        return userRepository.searchByUsername(username);
    }

    @Override
    public Optional<User> searchByEmail(String email) {
        return userRepository.searchByEmail(email);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return userRepository.searchByUsername(username).isEmpty();
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return userRepository.searchByEmail(email).isEmpty();
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (user.lastName() == null || user.lastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }

        try {
            ValidationUtils.validateRequiredUsername(user.username());
            ValidationUtils.validateEmail(user.email());
            ValidationUtils.validatePhone(user.phone());
        } catch (IllegalArgumentException e) {
            LoggerUtils.error("Error validating user: " + user.username(), e);
        }
    }
}
