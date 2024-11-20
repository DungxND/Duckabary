package io.vn.dungxnd.duckabary.core.user_management;

import io.vn.dungxnd.duckabary.core.borrow_management.BorrowRecord;

import java.util.ArrayList;
import java.util.List;

public record User(
        int id,
        String username,
        String firstName,
        String lastName,
        String email,
        String phone,
        String address,
        List<BorrowRecord> borrowRecords) {

    public static User createUser(
            int id,
            String username,
            String firstName,
            String lastName,
            String email,
            String phone,
            String address) {
        return new User(
                id, username, firstName, lastName, email, phone, address, new ArrayList<>());
    }

    public User addBorrowRecord(BorrowRecord borrowRecord) {
        List<BorrowRecord> updatedBorrowRecords = new ArrayList<>(borrowRecords);
        updatedBorrowRecords.add(borrowRecord);
        return new User(
                id, username, firstName, lastName, email, phone, address, updatedBorrowRecords);
    }

    public User removeBorrowRecord(BorrowRecord borrowRecord) {
        List<BorrowRecord> updatedBorrowRecords = new ArrayList<>(borrowRecords);
        updatedBorrowRecords.remove(borrowRecord);
        return new User(
                id, username, firstName, lastName, email, phone, address, updatedBorrowRecords);
    }

    public User returnDocument(BorrowRecord record) {
        int index = borrowRecords.indexOf(record);
        if (index != -1) {
            List<BorrowRecord> updatedBorrowRecords = new ArrayList<>(borrowRecords);
            updatedBorrowRecords.set(index, record.returnDocument());
            return new User(
                    id, username, firstName, lastName, email, phone, address, updatedBorrowRecords);
        }
        return this;
    }
}
