package io.vn.dungxnd.duckabary.core.library_management;

import java.util.Scanner;

public class AppCommandline {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();
        while (true) {
            System.out.println("1. Add document");
            System.out.println("2. Remove document");
            System.out.println("3. Get document");
            System.out.println("4. Exit");
            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.print("Enter id: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter desc: ");
                    String desc = scanner.nextLine();
                    Document doc = new Document(id, title, author, "", new StringBuilder(desc), 1);
                    library.addDocument(doc);
                    break;
                case 2:
                    System.out.print("Enter id: ");
                    int removeId = scanner.nextInt();
                    library.removeDocument(removeId);
                    break;
                case 3:
                    System.out.print("Enter id: ");
                    int getId = scanner.nextInt();
                    Document document = library.getDocument(getId);
                    if (document != null) {
                        System.out.println("Title: " + document.getTitle());
                        System.out.println("Author: " + document.getAuthor());
                        System.out.println("Desc: " + document.getDescription());
                    } else {
                        System.out.println("Document not found");
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }


}
