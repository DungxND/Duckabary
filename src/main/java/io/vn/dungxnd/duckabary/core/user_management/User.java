package io.vn.dungxnd.duckabary.core.user_management;

import io.vn.dungxnd.duckabary.core.borrow_management.BorrowRecord;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final int id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private final List<BorrowRecord> borrowRecords;

    public User(
            int id,
            String username,
            String firstName,
            String lastName,
            String email,
            String phone,
            String address) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        borrowRecords = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<BorrowRecord> getBorrowRecords() {
        return borrowRecords;
    }

    public void addBorrowRecord(BorrowRecord borrowRecord) {
        this.borrowRecords.add(borrowRecord);
    }

    public void removeBorrowRecord(BorrowRecord borrowRecord) {
        this.borrowRecords.remove(borrowRecord);
    }
}
