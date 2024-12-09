# DEVELOPMENT PHASE:

## To-do
- [ ] Refactor the code
- [ ] Test app

- [ ] User borrows list (UI)
- [ ] Library's document, author, publisher list

## 04 Dec 2024 (DungxND)

### Added

- Added services functions description.
- Added remember username when login.

### Changed

- Reworked all UIs
- Reduced Argon2id RAM usage to 64MB (from 128MB)

### Fixed

- Fixed unable to add new user

## 29 Nov 2024 #2 (DucLH)

### Changed

- Improved main menu UI (still need a lot more improvement)

## 29 Nov 2024 #1 (DungxND)

### Added

- Register now works.
- Show password in login/register UI

### Changed

- Better email validation
- Reworked the login/register UI

## 28 Nov 2024 (DungxND)

### Added

- Improve UI
- Login now works. (Register not yet)

## 27 Nov 2024 (DucLH)

### Added

- More app's UIs, controller base.

## 26 Nov 2024 (DungxND)

### Added

- Added Author and Publisher management.

## 24 Nov 2024 (DungxND)

### Added

- Use of LoggerUtils to log app's activities.
- Added repository for objects.

### Changed

- Mass refactor/rewrite the code.
- Restructured the project. Migrate to use Database's data only instead of both local data and database data.

## 23 Nov 2024 #2 (DungxND)

### Added

- Added quick first README.md for tester's testing purpose. (Need to be refined and updated)

## 23 Nov 2024 #1 (DucLH)

### Added

- Added app main menu UI, register UI, login UI, user management UI, document management UI, borrow management UI.

## 22 Nov 2024 #2 (DungxND)

### Added

- Added borrow quantity to borrow record.

### Fixed

- Fixed that JavaFX app cannot be run with error "Missing JavaFX runtime components".

## 22 Nov 2024 #1 (DucLD)

### Added

- Added UIs 

## 21 Nov 2024 #2 (DungxND)

### Added

- Added LoggerUtils to log app's activities.

### Changed

- Improved gradle.build configuration.

### Fixed

- Fixed problem syncing userId between local and database.
- Fixed problem returning document cause err in BorrowDatabaseManager.

## 21 Nov 2024 #1 (DungxND)

### Added

- Added Gradle .jar building configuration.
- Added Generational Z Garbage Collector ([ZGC](https://docs.oracle.com/en/java/javase/23/gctuning/z-garbage-collector.html)) to the project. (Java 23)
- Added validation for user creation.

### Changed

- Use of record instead of class BorrowRecord, User, AdminUser (reduced a lot of code and may improve performance) (**NEED TO CONDUCT TESTS TO MAKE SURE NEW SYSTEM WORKS**)
- Better handling of borrow/return document
- Better initialization/handling of the database creation, connection
  - Stop using static block to initialize the database, replace with synchronized method to prevent many creation.
  - Using volatile keyword to make sure other threads see the updated value of the database connection.
- Using relative path of the database file, app can now run in Debug/Release env without path/file error.
- Update Java version 21 to 23 (to use ZGC and compatible with JavaFX 24+ea15)
- Update deps

### Fixed

- Fixed if the option input is string, the app will crash

## 19 Nov 2024 (DucLH)

### Added

- Added pp main menu UI.

## 18 Nov 2024 (DungxND):

### Added

- Check for correct date time format when inputting date time.

### Changed

- Update deps

### Fixed

- Unable/wrongly borrow&return document.
 

## 15 Nov 2024 (DungxND):

### Added

- Create app management class to handle all app's managers/services.
- Create borrow package to manage borrow/return services.
- Check if username is already taken
- Search document(s) by genre

### Changed

- Complete (maybe) the borrow/return document func
    - Fix problem that borrow/return document request is duplicated
- Relocate the UI folder/files

### Fixed

- Correctly handle if input is empty
- Fix problem that might make the publishing year goes wrong

## 15 Oct 2024 (DucLH):

### Added

- App login UI.

## 14 Oct 2024 (DungxND):


- Separated UserService for UI/CMD for future works.
- Now check if DB exist/ DB's table exist when running app.
- UID was wrong because array index start from 0, fixed.