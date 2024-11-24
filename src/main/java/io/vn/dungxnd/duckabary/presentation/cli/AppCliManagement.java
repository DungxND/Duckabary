package io.vn.dungxnd.duckabary.presentation.cli;

import io.vn.dungxnd.duckabary.domain.service.borrow.BorrowService;
import io.vn.dungxnd.duckabary.domain.service.borrow.impl.BorrowServiceImpl;
import io.vn.dungxnd.duckabary.domain.service.library.DocumentService;
import io.vn.dungxnd.duckabary.domain.service.library.impl.DocumentServiceImpl;
import io.vn.dungxnd.duckabary.domain.service.user.ManagerService;
import io.vn.dungxnd.duckabary.domain.service.user.UserService;
import io.vn.dungxnd.duckabary.domain.service.user.impl.ManagerServiceImpl;
import io.vn.dungxnd.duckabary.domain.service.user.impl.UserServiceImpl;
import io.vn.dungxnd.duckabary.infrastructure.repository.*;
import io.vn.dungxnd.duckabary.infrastructure.repository.impl.*;
import io.vn.dungxnd.duckabary.infrastructure.repository.impl.library.*;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.*;

public class AppCliManagement {
    private final DocumentRepository documentRepository;
    private final BookRepository bookRepository;
    private final JournalRepository journalRepository;
    private final ThesisRepository thesisRepository;
    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRepository;
    private final ManagerRepository managerRepository;

    private final DocumentService documentService;
    private final UserService userService;
    private final BorrowService borrowService;
    private final ManagerService managerService;

    public AppCliManagement() {
        documentRepository = new DocumentRepositoryImpl();
        bookRepository = new BookRepositoryImpl();
        journalRepository = new JournalRepositoryImpl();
        thesisRepository = new ThesisRepositoryImpl();
        userRepository = new UserRepositoryImpl();
        borrowRepository = new BorrowRecordRepositoryImpl();
        managerRepository = new ManagerRepositoryImpl();

        documentService =
                new DocumentServiceImpl(
                        documentRepository, bookRepository, journalRepository, thesisRepository);

        userService = new UserServiceImpl(userRepository, borrowRepository);

        borrowService = new BorrowServiceImpl(borrowRepository, documentService, userService);

        managerService = new ManagerServiceImpl(managerRepository);
    }

    public DocumentService getDocumentService() {
        return documentService;
    }

    public UserService getUserService() {
        return userService;
    }

    public BorrowService getBorrowService() {
        return borrowService;
    }

    public ManagerService getManagerService() {
        return managerService;
    }
}
