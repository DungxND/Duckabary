package io.vn.dungxnd.duckabary.domain.model.entity;

public record Author(
        Long id, String fullName, String penName, String email, String phone, String address) {}
