package io.vn.dungxnd.duckabary.infrastructure.repository.library;

import io.vn.dungxnd.duckabary.domain.model.library.BorrowRecord;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository {
    List<BorrowRecord> findAll();

    Optional<BorrowRecord> findById(int id);

    BorrowRecord save(BorrowRecord record);

    void delete(int id);

    List<BorrowRecord> findByUserId(int userId);

    List<BorrowRecord> findByDocumentId(Long documentId);

    List<BorrowRecord> findOverdueRecords();

    List<BorrowRecord> findActiveRecords();
}
