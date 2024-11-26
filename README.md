# Duckabary Library Management System

## Introduction

This library management system is a project for the OOP course at VNU-UET.

## Features detailed description

### General

- **UI**:
    - All text:
        - Font: Montserrat

### Manager

- **Enforced login**
    - Manager must login to access the system. If not, show login/register form.
    - If not logged in, db file is encrypted. If logged in, db file is decrypted. (Not implemented)
- **Input validation**
    - General: All fields must not be empty
    - Username: Must be unique, do not match with any exist username. Must contain only letters, numbers, and
      underscores, no special characters.
    - Email: Must be in the format of [username]@[domain].[extension].
    - Password: Must contain at least 8 characters.

- **Register**
    - Manager can register with username, email, password.
    - If manager is successfully registered, it should be added to local database (manager table). Password should be
      hashed before saving to database. A message should be shown: Manager [username] registered successfully with [id].
    - After registering, if CLI, user not logged in. If GUI, user is logged in automatically.

- **Login**
    - Manager can log in with username/email and password. If incorrect, tell user that login failed. Else, login
      successfully.
    - Remember login status. (Not implemented)

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
        - All type:
            - Type: Must be one of the following: Book, Journal, Magazine, Newspaper. Error if not.
            - Title: Must not be empty.
            - Description: No validation. Empty is allowed.
            - Author name: Required. If empty, return error. If author name not exist in database, add author to
              database
              and ask if user has more author info (Pen name, Phone, Email, Address) print: "Author [author name] ([pen
              name if exist]) added to database with author id: [id]". If author's full name/pen name match with any
              exist author in database, print these author's info and ask user if they want to use any of these author
              or
              create a new one (Must be different with existing records).
            - Publish Year: Must be a number. If empty, set to 0.
            - Quantity: Must be a positive number or 0. If empty/negative, set to 0.
        - Book type:
            - ISBN: Must be unique, do not match with any existing ISBN in DB. Must contain 10 or 13 digits. Empty
              allowed.
            - Publisher: Must not be empty. If exist in database, print publisher's info and ask user if they want to
              use this publisher or create a new one (Must be different with existing records).
            - Language: Must be in [ISO-639](https://en.wikipedia.org/wiki/List_of_ISO_639_language_codes) Set 1 format.
              Empty is allowed.
        - Journal type:
            - ISSN: Must be unique, do not match with any exist ISSN. Must contain 8 digits. Empty allowed.
            - Volume: String. Empty allowed.
            - Issue: String. Empty allowed.
        - Thesis type:
            - University: No validation. Empty is allowed.
            - Major: No validation. Empty is allowed.
            - Supervisor: No validation. Empty is allowed.
            - Defense date: Must be in the format of yyyy-MM-dd HH:mm (Ex: 1975-04-30 11:30). Must be a valid date. If
              only date is
              inputted, set time to 00:00:00. If no minutes, set to HH:00 . If no hours, set to 00:00. Different format
              return error. Empty is allowed.
    - **Add document**
        - If incorrect input validation, tell user which input is wrong. New document not created. Else, document is
          created with a message: Document [title] (type: [type]) created with [id].
        - When adding Document, if Author fullname/pen name not exist in database, add author to database and ask if
          user has more author info (Pen name, Phone, Email, Address) print: "Author [author name] ([pen name if exist])
          added to database with author id: [id]". If author's full name/pen name match with any exist author in
          database, print
          these author's info and ask user if they want to use any of these author or create a new one (Must be
          different with existing records).
        - When adding Book, Publisher name is required, if not exist in database, add publisher to database and
          print: "Publisher [publisher name] added to database with publisher id: [id]". If publisher's name match with
          any exist publisher in database, print publisher's info and ask user if they want to use this publisher or
          create a new one (Must be different with existing records).
        - If document is successfully created, it should be added to local database (document table and document
          specific type table (thesis/book/journal)).
    - **Edit document** (Not implemented)
    - **Delete document**
        - If document is being borrowed, return error. Else, document is deleted with a message: Document [title]
          deleted
          successfully.
        - If document is successfully deleted, it should be removed from local database (document table and document
          specific type table (thesis/book/journal)).

- **Author management**
    - **Input validation:**
        - Full name: Required. If empty, return error.
        - Pen name: No validation. Empty is allowed.
        - Phone: Must be a Vietnamese phone number, must be 8-11 digits. Must not contain any characters else than
          number. Empty is allowed.
        - Email: Must be in the format of [username]@[domain].[extension]. Empty is allowed.
        - Address: No validation. Empty is allowed.
    - **Add author**
        - If incorrect input validation, tell user which input is wrong. New author not created. Else, author is created
          with a message: Author [author name] ([pen name if exist]) added to database with author id: [id].
        - If author is successfully created, it should be added to local database (author table).
    - **Edit author** (Not implemented)
    - **Delete author**
        - If author is being used in any document, return error. Else, author is deleted with a message:
          Author [author name] deleted successfully.
        - If author is successfully deleted, it should be removed from local database (author table).
- **Publisher management**
- **Input validation**
    - Name: Required. If empty, return error.
    - Phone: Must be a Vietnamese phone number, must be 8-11 digits. Must not contain any characters else than number.
      Empty is allowed.
    - Email: Must be in the format of [username]@[domain].[extension]. Empty is allowed.
    - Address: No validation. Empty is allowed.
- **Add publisher**
    - If incorrect input validation, tell user which input is wrong. New publisher not created. Else, publisher is
      created
      with a message: Publisher [publisher name] added to database with publisher id: [id].
    - If publisher is successfully created, it should be added to local database (publisher table).
- **Edit publisher** (Not implemented)

### Borrow management

- **Input validation**
    - User ID: Must be a number. Must be a valid user ID. (Exist in database's user table)
    - Document ID: Must be a number. Must be a valid document ID. (Exist in database's document table)
    - Date time: Must be in the format of yyyy-MM-dd HH:mm:ss (1975-04-30 10:45:00). Must be a valid date time. If only
      date is inputted, set time to 00:00:00, if no seconds, set to HH:mm:00. If no minutes, set to HH:00:00. If no
      hours, set to
      00:00:00, Else, return error.
    - Due time must be after borrow time. (Not implemented)
    - Borrowed quantity: Must be a number and stock must be enough. If not, return error.
- **Borrow document**
    - If incorrect input validation, tell user which input is wrong. Document not borrowed. Else, document is borrowed
      with a message: Document [title] borrowed [n] copy(ies) by user [username] with record ID: [id].
    - If document is successfully borrowed, it should be added to local BorrowManagement Borrow array, User's self array
      and local database (borrow table). Document quantity should be decreased by borrowed quantity.
- **Return document**
    - Check if user exist, document exist, and document is borrowed by user. If not, return error. Else, document is
      returned with a message: Document [title] returned successfully [n] copy(ies) by user [username]. Return date
      is [current time]
    - If document is successfully returned, it should **not** be removed from local database (borrow table) but returned
      date is now set (current time or custom input
      time (not implemented)). Document quantity should be increased by returned quantity.