package io.vn.dungxnd.duckabary.domain.service.borrow.impl;

import io.vn.dungxnd.duckabary.domain.model.library.BorrowRecord;
import io.vn.dungxnd.duckabary.domain.service.borrow.BorrowService;
import io.vn.dungxnd.duckabary.domain.service.library.DocumentService;
import io.vn.dungxnd.duckabary.domain.service.user.UserService;
import io.vn.dungxnd.duckabary.exeption.DatabaseException;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.BorrowRecordRepository;

import java.time.LocalDateTime;
import java.util.List;

public class BorrowServiceImpl implements BorrowService {
    private final BorrowRecordRepository borrowRepository;
    private final DocumentService documentService;
    private final UserService userService;

    public BorrowServiceImpl(
            BorrowRecordRepository borrowRepository,
            DocumentService documentService,
            UserService userService) {
        this.borrowRepository = borrowRepository;
        this.documentService = documentService;
        this.userService = userService;
    }

    @Override
    public List<BorrowRecord> getAllBorrows() {
        return borrowRepository.findAll();
    }

    @Override
    public BorrowRecord getBorrowById(int id) throws DatabaseException {
        return borrowRepository
                .findById(id)
                .orElseThrow(() -> new DatabaseException("Borrow record not found with id: " + id));
    }

    @Override
    public BorrowRecord borrowDocument(
            int userId, Long documentId, int quantity, LocalDateTime dueDate)
            throws DatabaseException {

        if (userService.getUserById(userId) == null) {
            throw new DatabaseException("User not found");
        }

        if (documentService.getDocumentById(documentId) == null) {
            throw new DatabaseException("Document not found");
        }

        if (!isDocumentAvailableForBorrow(documentId, quantity)) {
            throw new DatabaseException("Document not available in requested quantity");
        }

        BorrowRecord borrowRecord =
                BorrowRecord.createBorrowRecord(
                        0, userId, documentId, quantity, LocalDateTime.now(), dueDate);

        documentService.updateQuantity(documentId, -quantity);

        return borrowRepository.save(borrowRecord);
    }

    @Override
    public BorrowRecord returnDocument(int borrowId) throws DatabaseException {
        BorrowRecord borrowRecord = getBorrowById(borrowId);

        if (borrowRecord.isReturned()) {
            throw new DatabaseException("Document already returned");
        }

        documentService.updateQuantity(borrowRecord.documentId(), borrowRecord.quantity());

        return borrowRepository.save(borrowRecord.returnDocument());
    }

    @Override
    public List<BorrowRecord> getBorrowsByUser(int userId) {
        return borrowRepository.findByUserId(userId);
    }

    @Override
    public List<BorrowRecord> getBorrowsByDocument(Long documentId) {
        return borrowRepository.findByDocumentId(documentId);
    }

    @Override
    public List<BorrowRecord> getOverdueBorrows() {
        return borrowRepository.findOverdueRecords();
    }

    @Override
    public List<BorrowRecord> getActiveBorrows() {
        return borrowRepository.findActiveRecords();
    }

    @Override
    public boolean isDocumentAvailableForBorrow(Long documentId, int quantity) {
        return documentService.canBeBorrowed(documentId, quantity);
    }
}
