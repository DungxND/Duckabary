package io.vn.dungxnd.duckabary.presentation.cli;

import io.vn.dungxnd.duckabary.domain.service.borrow.BorrowService;
import io.vn.dungxnd.duckabary.domain.service.borrow.impl.BorrowServiceImpl;
import io.vn.dungxnd.duckabary.domain.service.entity.AuthorService;
import io.vn.dungxnd.duckabary.domain.service.entity.PublisherService;
import io.vn.dungxnd.duckabary.domain.service.entity.impl.AuthorServiceImpl;
import io.vn.dungxnd.duckabary.domain.service.entity.impl.PublisherServiceImpl;
import io.vn.dungxnd.duckabary.domain.service.library.DocumentService;
import io.vn.dungxnd.duckabary.domain.service.library.impl.DocumentServiceImpl;
import io.vn.dungxnd.duckabary.domain.service.user.ManagerService;
import io.vn.dungxnd.duckabary.domain.service.user.UserService;
import io.vn.dungxnd.duckabary.domain.service.user.impl.ManagerServiceImpl;
import io.vn.dungxnd.duckabary.domain.service.user.impl.UserServiceImpl;
import io.vn.dungxnd.duckabary.infrastructure.repository.entity.AuthorRepository;
import io.vn.dungxnd.duckabary.infrastructure.repository.entity.PublisherRepository;
import io.vn.dungxnd.duckabary.infrastructure.repository.entity.impl.AuthorRepositoryImpl;
import io.vn.dungxnd.duckabary.infrastructure.repository.entity.impl.PublisherRepositoryImpl;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.*;
import io.vn.dungxnd.duckabary.infrastructure.repository.library.impl.*;
import io.vn.dungxnd.duckabary.infrastructure.repository.user.ManagerRepository;
import io.vn.dungxnd.duckabary.infrastructure.repository.user.UserRepository;
import io.vn.dungxnd.duckabary.infrastructure.repository.user.impl.ManagerRepositoryImpl;
import io.vn.dungxnd.duckabary.infrastructure.repository.user.impl.UserRepositoryImpl;

public class AppCliManagement {
    private final DocumentRepository documentRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;
    private final JournalRepository journalRepository;
    private final ThesisRepository thesisRepository;
    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRepository;
    private final ManagerRepository managerRepository;

    private final DocumentService documentService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final UserService userService;
    private final BorrowService borrowService;
    private final ManagerService managerService;

    public AppCliManagement() {
        documentRepository = new DocumentRepositoryImpl();
        authorRepository = new AuthorRepositoryImpl();
        publisherRepository = new PublisherRepositoryImpl();
        bookRepository = new BookRepositoryImpl();
        journalRepository = new JournalRepositoryImpl();
        thesisRepository = new ThesisRepositoryImpl();
        userRepository = new UserRepositoryImpl();
        borrowRepository = new BorrowRecordRepositoryImpl();
        managerRepository = new ManagerRepositoryImpl();

        authorService = new AuthorServiceImpl(authorRepository, documentRepository);
        publisherService = new PublisherServiceImpl(publisherRepository, bookRepository);

        documentService =
                new DocumentServiceImpl(
                        authorService,
                        publisherService,
                        documentRepository,
                        bookRepository,
                        journalRepository,
                        thesisRepository);

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

    public AuthorService getAuthorService() {
        return authorService;
    }

    public PublisherService getPublisherService() {
        return publisherService;
    }
}
