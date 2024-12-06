package io.vn.dungxnd.duckabary.domain.service;

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

public class ServiceManager {

    private static volatile ServiceManager instance;

    private final ManagerService managerService;
    private final UserService userService;
    private final DocumentService documentService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final BorrowService borrowService;

    private ServiceManager() {

        ManagerRepository managerRepository = new ManagerRepositoryImpl();
        UserRepository userRepository = new UserRepositoryImpl();
        DocumentRepository documentRepository = new DocumentRepositoryImpl();
        BookRepository bookRepository = new BookRepositoryImpl();
        ThesisRepository thesisRepository = new ThesisRepositoryImpl();
        JournalRepository journalRepository = new JournalRepositoryImpl();
        AuthorRepository authorRepository = new AuthorRepositoryImpl();
        PublisherRepository publisherRepository = new PublisherRepositoryImpl();
        BorrowRecordRepository borrowRecordRepository = new BorrowRecordRepositoryImpl();

        this.managerService = new ManagerServiceImpl(managerRepository);
        this.userService = new UserServiceImpl(userRepository, borrowRecordRepository);
        this.authorService = new AuthorServiceImpl(authorRepository, documentRepository);
        this.publisherService = new PublisherServiceImpl(publisherRepository, bookRepository);
        this.documentService =
                new DocumentServiceImpl(
                        authorService,
                        publisherService,
                        documentRepository,
                        bookRepository,
                        journalRepository,
                        thesisRepository);
        this.borrowService =
                new BorrowServiceImpl(borrowRecordRepository, documentService, userService);
    }

    public static ServiceManager getInstance() {
        return InstanceHolder.instance;
    }

    public ManagerService getManagerService() {
        return managerService;
    }

    public UserService getUserService() {
        return userService;
    }

    public DocumentService getDocumentService() {
        return documentService;
    }

    public AuthorService getAuthorService() {
        return authorService;
    }

    public PublisherService getPublisherService() {
        return publisherService;
    }

    public BorrowService getBorrowService() {
        return borrowService;
    }

    private static final class InstanceHolder {
        private static final ServiceManager instance = new ServiceManager();
    }
}
