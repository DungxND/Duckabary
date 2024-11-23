# Duckabary Library Management System

## Introduction

This library management system is a project for the OOP course at VNU-UET.

## Features detailed description

### General

- **UI**:
    - All text:
      - Font: Montserrat

### Admin User

- **Enforced login**
    - Admin user must login to access the system and database. If not logged in, show login form. No one can access the
      db (Not implemented)
- **Input validation** (Not implemented)
    - General: All fields must not be empty
    - Username: Must be unique, do not match with any exist username. Must contain only letters, numbers, and
      underscores,
      no special characters.
    - Email: Must be in the format of [username]@[domain].[extension].
    - Password: Must contain at least 8 characters, at least one uppercase letter, one lowercase letter, one number, and
      one
      special character.

- **Register**
    - Admin user can register with username, email, password.
    - If admin user is successfully registered, it should be added to local UserManagement AdminUser array and local
      database (admin table). Password should be hashed before saving to database.

- **Login**
    - Admin user can login with username and password. If incorrect, tell user that login failed. Else, login
      successfully.
    - Remember login status. If user is logged in, show user's username and logout button. Else, show login form. (Not
      implemented)

### User management

- **Input validation**
    - General: All fields must not be empty
    - Username: Must be unique, do not match with any exist username. Must contain at least 5 characters and 16
      characters maximum. Must contain only letters, numbers, and underscores, no special characters.
    - Email: Don't have to be unique. Must be in the format of [username]@[domain].[extension].
    - Phone number: Don't have to be unique. Must be Vietnamese phone number, must be 8-11 digits. Must not contain
      any characters else than number.
    - Address: Don't have to be unique. Must not be empty.
- **Add user**
    - If incorrect input validation, tell user which input is wrong. New user not created. Else, user is created
      with a message: User [username] created with [id].
    - If user is successfully created, it should be added to local UserManagement User array and local database (
      user table).
- **Edit user**
    - Still check if new info satisfy validation. If not, tell user which input is wrong. User info not changed.
      Else, user info is changed, in local UserManagement User array and local database.
- **Delete user** (Not implemented)
- **Search user** (Not implemented)
    - Search by username, email, phone number, address. Return a list of users that satisfy the search query.

### Library management

- **Document management**
    - **Input validation**
        - Title: Must not be empty.
        - Description: No validation. Empty is allowed.
        - Author: No validation. Empty is allowed.
        - Publisher: No validation. Empty is allowed.
        - Publish Year: Must be a number. If empty, set to 0.
        - Type: Must be one of the following: Book, Journal, Magazine, Newspaper. If empty, set to Book. (Not
          implemented)
        - ISBN: Must be unique, do not match with any exist ISBN. Must contain 10 or 13 digits. Empty allowed.
        - Quantity: Must be a number. If empty, set to 0.

    - **Add document**
        - If incorrect input validation, tell user which input is wrong. New document not created. Else, document is
          created with a message: Document [title] created with [id].
        - If document is successfully created, it should be added to local LibraryManagement Document array and
          local database (document table).

- **Author management** (Not implemented)
- **Publisher management** (Not implemented)

### Borrow management

- Input validation (Not implemented)
    - User ID: Must be a number. Must be a valid user ID. (Exist in local UserManagement User array)
    - Document ID: Must be a number. Must be a valid document ID. (Exist in local LibraryManagement Document array)
    - Date time: Must be in the format of yyyy-MM-dd HH:mm:ss (1975-04-30 10:45:00). Must be a valid date time. If only
      date is inputted, set time to 00:00:00, if no seconds, set to 00. If no minutes, set to 00. If no hours, set to
        00. Else, return error.
    - Due time must be after borrow time. (Not implemented)
    - Borrowed quantity: Must be a number and stock must be enough.
- Borrow document
    - If incorrect input validation, tell user which input is wrong. Document not borrowed. Else, document is borrowed
      with a message: Document [title] borrowed [n] copy(ies) by user [username] with [id].
    - If document is successfully borrowed, it should be added to local BorrowManagement Borrow array, User's self array
      and local database (borrow table). Document quantity should be decreased by borrowed quantity.
- Return document
    - Check if user exist, document exist, and document is borrowed by user. If not, return error. Else, document is
      returned with a message: Document [title] returned [n] copy(ies) by user [username] with [id].
    - If document is successfully returned, it should **not** be removed from any local BorrowManagement Borrow array,
      User's self array and local database (borrow table) but returned date is now set (current time or custom input
      time (not implemented)) . Document quantity should be increased by returned quantity.