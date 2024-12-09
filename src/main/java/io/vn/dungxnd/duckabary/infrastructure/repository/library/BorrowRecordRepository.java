package io.vn.dungxnd.duckabary.infrastructure.repository.library;

import io.vn.dungxnd.duckabary.domain.model.library.BorrowRecord;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository {
    List<BorrowRecord> findAll();

    Optional<BorrowRecord> searchById(Connection conn, int id);

    Optional<BorrowRecord> searchById(int id);

    BorrowRecord save(BorrowRecord record);

    void delete(int id);

    List<BorrowRecord> searchByUserId(int userId);

    List<BorrowRecord> searchByDocumentId(Long documentId);

    List<BorrowRecord> findOverdueRecords();

    List<BorrowRecord> findActiveRecords();

    void deleteRecordWithUserId(int userId);
}
