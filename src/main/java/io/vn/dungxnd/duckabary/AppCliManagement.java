package io.vn.dungxnd.duckabary;

import io.vn.dungxnd.duckabary.core.borrow_management.BorrowCmdService;
import io.vn.dungxnd.duckabary.core.borrow_management.BorrowManagement;
import io.vn.dungxnd.duckabary.core.library_management.LibraryCmdService;
import io.vn.dungxnd.duckabary.core.library_management.LibraryManagement;
import io.vn.dungxnd.duckabary.core.user_management.UserCmdService;
import io.vn.dungxnd.duckabary.core.user_management.UserManagement;

public class AppCliManagement {
    private final UserManagement userManagement;
    private final UserCmdService userCmdService;
    private final LibraryManagement libraryManagement;
    private final LibraryCmdService libraryCmdService;
    private final BorrowManagement borrowManagement;
    private final BorrowCmdService borrowCmdService;

    public AppCliManagement() {
        userManagement = new UserManagement();
        userCmdService = new UserCmdService(userManagement);
        libraryManagement = new LibraryManagement();
        libraryCmdService = new LibraryCmdService(libraryManagement, userCmdService);
        borrowManagement = new BorrowManagement(libraryCmdService);
        borrowCmdService = new BorrowCmdService(borrowManagement);
    }

    public UserManagement getUserManagement() {
        return userManagement;
    }

    public LibraryManagement getLibraryManagement() {
        return libraryManagement;
    }

    public UserCmdService getUserCmdService() {
        return userCmdService;
    }

    public LibraryCmdService getLibraryCmdService() {
        return libraryCmdService;
    }

    public BorrowManagement getBorrowManagement() {
        return borrowManagement;
    }

    public BorrowCmdService getBorrowCmdService() {
        return borrowCmdService;
    }
}
