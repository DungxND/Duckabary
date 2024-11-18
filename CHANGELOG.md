# DEVELOPMENT PHASE:

## To-do
- [ ] Refactor the code
- [ ] Test app

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


- Separated UserSerivce for UI/CMD for future works.
- Now check if DB exist/ DB's table exist when running app.
- UID was wrong because array index start from 0, fixed.