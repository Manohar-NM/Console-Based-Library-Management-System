package library;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
  private static Library library;
  private static Scanner scanner;

  public static void main(String[] args) {
    System.out.println("Initializing Library Management System...");
    library = new Library();
    scanner = new Scanner(System.in);

    boolean running = true;
    while (running) {
      displayMenu();
      int choice = getIntInput("Enter your choice: ");

      try {
        switch (choice) {
          case 1:
            addBook();
            break;
          case 2:
            removeBook();
            break;
          case 3:
            searchBooks();
            break;
          case 4:
            library.displayAllBooks();
            break;
          case 5:
            registerMember();
            break;
          case 6:
            library.displayAllMembers();
            break;
          case 7:
            borrowBook();
            break;
          case 8:
            returnBook();
            break;
          case 9:
            reserveBook();
            break;
          case 10:
            payFine();
            break;
          case 11:
            library.displayStatistics();
            break;
          case 12:
            exportData();
            break;
          case 0:
            running = false;
            System.out.println("Saving data and exiting...");
            library.saveData();
            System.out.println("Goodbye!");
            break;
          default:
            System.out.println("Invalid choice. Please try again.");
        }
      } catch (Exception e) {
        System.out.println("An error occurred: " + e.getMessage());
      }

      // Add a small pause for readability
      if (running) {
        System.out.println("\nPress Enter to continue...");
        if (scanner.hasNextLine()) {
          scanner.nextLine();
        } else {
          running = false; // Exit if no more input
        }
      }
    }
    scanner.close();
  }

  private static void displayMenu() {
    System.out.println("\n" + "=".repeat(40));
    System.out.println("📚 LIBRARY MANAGEMENT SYSTEM 📚");
    System.out.println("=".repeat(40));
    System.out.println("--- Book Management ---");
    System.out.println("1. Add a Book");
    System.out.println("2. Remove a Book");
    System.out.println("3. Search Books");
    System.out.println("4. Display All Books");
    System.out.println("\n--- Member Management ---");
    System.out.println("5. Register Member");
    System.out.println("6. Display All Members");
    System.out.println("10. Pay Fine");
    System.out.println("\n--- Borrowing Operations ---");
    System.out.println("7. Borrow a Book");
    System.out.println("8. Return a Book");
    System.out.println("9. Reserve a Book");
    System.out.println("\n--- System ---");
    System.out.println("11. View Statistics");
    System.out.println("12. Export Books to CSV");
    System.out.println("0. Exit");
    System.out.println("=".repeat(40));
  }

  // --- Helper Methods ---
  private static String getStringInput(String prompt) {
    System.out.print(prompt);
    if (scanner.hasNextLine()) {
      return scanner.nextLine().trim();
    }
    return "";
  }

  private static int getIntInput(String prompt) {
    while (true) {
      System.out.print(prompt);
      if (!scanner.hasNext())
        return -1;
      try {
        int value = scanner.nextInt();
        if (scanner.hasNextLine())
          scanner.nextLine(); // Consume newline
        return value;
      } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a number.");
        if (scanner.hasNextLine())
          scanner.nextLine(); // Clear invalid input
      }
    }
  }

  private static double getDoubleInput(String prompt) {
    while (true) {
      System.out.print(prompt);
      if (!scanner.hasNext())
        return -1.0;
      try {
        double value = scanner.nextDouble();
        if (scanner.hasNextLine())
          scanner.nextLine(); // Consume newline
        return value;
      } catch (InputMismatchException e) {
        System.out.println("Invalid input. Please enter a valid amount.");
        if (scanner.hasNextLine())
          scanner.nextLine(); // Clear invalid input
      }
    }
  }

  // --- Menu Actions ---
  private static void addBook() {
    System.out.println("\n=== ADD NEW BOOK ===");
    String isbn = getStringInput("Enter ISBN: ");
    if (isbn.isEmpty()) {
      System.out.println("ISBN cannot be empty.");
      return;
    }

    String title = getStringInput("Enter Title: ");
    if (title.isEmpty()) {
      System.out.println("Title cannot be empty.");
      return;
    }

    String author = getStringInput("Enter Author: ");
    if (author.isEmpty()) {
      System.out.println("Author cannot be empty.");
      return;
    }

    int year = getIntInput("Enter Publication Year: ");

    Book newBook = new Book(isbn, title, author, year);
    library.addBook(newBook);
  }

  private static void removeBook() {
    System.out.println("\n=== REMOVE BOOK ===");
    String isbn = getStringInput("Enter ISBN of the book to remove: ");
    library.removeBook(isbn);
  }

  private static void searchBooks() {
    System.out.println("\n=== SEARCH BOOKS ===");
    String keyword = getStringInput("Enter search keyword (title, author, or ISBN): ");
    System.out.println("Searching for: " + keyword);

    List<Book> results = library.searchBooks(keyword);

    if (results.isEmpty()) {
      System.out.println("No books found matching '" + keyword + "'.");
    } else {
      System.out.println("\nSearch Results: " + results.size() + " book(s) found");
      System.out.println("-".repeat(80));
      int i = 1;
      for (Book book : results) {
        System.out.println(i++ + ". " + book);
      }
    }
  }

  private static void registerMember() {
    System.out.println("\n=== REGISTER NEW MEMBER ===");
    String id = getStringInput("Enter Member ID: ");
    if (id.isEmpty()) {
      System.out.println("Member ID cannot be empty.");
      return;
    }

    String name = getStringInput("Enter Member Name: ");
    if (name.isEmpty()) {
      System.out.println("Member Name cannot be empty.");
      return;
    }

    Member newMember = new Member(id, name);
    library.registerMember(newMember);
  }

  private static void borrowBook() {
    System.out.println("\n=== BORROW BOOK ===");
    String isbn = getStringInput("Enter Book ISBN: ");
    String memberId = getStringInput("Enter Member ID: ");

    library.borrowBook(isbn, memberId);
  }

  private static void returnBook() {
    System.out.println("\n=== RETURN BOOK ===");
    String isbn = getStringInput("Enter Book ISBN: ");
    library.returnBook(isbn);
  }

  private static void reserveBook() {
    System.out.println("\n=== RESERVE BOOK ===");
    String isbn = getStringInput("Enter Book ISBN: ");
    String memberId = getStringInput("Enter Member ID: ");

    library.reserveBook(isbn, memberId);
  }

  private static void payFine() {
    System.out.println("\n=== PAY FINE ===");
    String memberId = getStringInput("Enter Member ID: ");
    double amount = getDoubleInput("Enter payment amount: $");

    library.payFine(memberId, amount);
  }

  private static void exportData() {
    System.out.println("\n=== EXPORT TO CSV ===");
    String filename = getStringInput("Enter output filename (e.g., library_inventory.csv): ");
    if (filename.isEmpty()) {
      filename = "library_inventory.csv";
    } else if (!filename.toLowerCase().endsWith(".csv")) {
      filename += ".csv";
    }

    boolean success = library.exportBooksToCsv(filename);
    if (success) {
      System.out.println("Export successful. Data saved to " + filename);
    } else {
      System.out.println("Export failed. Please check the console output for errors.");
    }
  }
}
