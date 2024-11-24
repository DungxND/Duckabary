package io.vn.dungxnd.duckabary.domain.service.user.impl;

import io.vn.dungxnd.duckabary.domain.model.library.BorrowRecord;
import io.vn.dungxnd.duckabary.domain.model.user.User;
import io.vn.dungxnd.duckabary.domain.service.user.UserService;
import io.vn.dungxnd.duckabary.exeption.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.UserRepository;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.BorrowRecordRepository;

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
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int id) throws DatabaseException {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new DatabaseException("User not found with id: " + id));
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
        // Check if user has any active borrows
        List<BorrowRecord> activeBorrows =
                borrowRepository.findByUserId(id).stream()
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
        return userRepository.findByName(name.trim());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.username() == null || user.username().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (user.email() == null || user.email().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (user.lastName() == null || user.lastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (!user.email().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}
