package library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Library {
    private List<Book> books;
    private List<Member> members;
    private FileHandler fileHandler;
    
    // Configurable loan period and fine rate
    private static final int LOAN_WEEKS = 2;
    private static final double FINE_PER_DAY = 1.50; // $1.50 fine per overdue day
    
    public Library() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
        this.fileHandler = new FileHandler();
        loadData();
    }
    
    private void loadData() {
        books = fileHandler.loadBooks();
        members = fileHandler.loadMembers();
        System.out.println("Loaded " + books.size() + " books and " + members.size() + " members.");
    }
    
    public void saveData() {
        fileHandler.saveBooks(books);
        fileHandler.saveMembers(members);
    }
    
    public boolean exportBooksToCsv(String filename) {
        return fileHandler.exportToCSV(books, filename);
    }
    
    // --- Book Management ---
    public void addBook(Book book) {
        if (findBookByIsbn(book.getIsbn()) != null) {
            System.out.println("Error: A book with ISBN " + book.getIsbn() + " already exists.");
            return;
        }
        books.add(book);
        saveData();
        System.out.println("Book added successfully: " + book.getTitle());
    }
    
    public void removeBook(String isbn) {
        Book bookToRemove = findBookByIsbn(isbn);
        if (bookToRemove != null) {
            if (!bookToRemove.isAvailable()) {
                System.out.println("Error: Cannot remove book. It is currently borrowed by member: " + bookToRemove.getBorrowedBy());
                return;
            }
            books.remove(bookToRemove);
            saveData();
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Error: Book not found with ISBN: " + isbn);
        }
    }
    
    public Book findBookByIsbn(String isbn) {
        return books.stream()
            .filter(book -> book.getIsbn().equals(isbn))
            .findFirst()
            .orElse(null);
    }
    
    public List<Book> searchBooks(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return books.stream()
            .filter(book -> book.getTitle().toLowerCase().contains(lowerKeyword) ||
                           book.getAuthor().toLowerCase().contains(lowerKeyword) ||
                           book.getIsbn().contains(keyword))
            .collect(Collectors.toList());
    }
    
    public void displayAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }
        
        System.out.println("\n=== ALL BOOKS ===");
        System.out.println("Total books: " + books.size());
        System.out.println("-".repeat(80));
        
        for (int i = 0; i < books.size(); i++) {
            System.out.println((i + 1) + ". " + books.get(i));
        }
    }
    
    // --- Member Management ---
    public void registerMember(Member member) {
        if (findMemberById(member.getId()) != null) {
            System.out.println("Error: A member with ID " + member.getId() + " already exists.");
            return;
        }
        members.add(member);
        saveData();
        System.out.println("Member registered successfully: " + member.getName());
    }
    
    public Member findMemberById(String id) {
        return members.stream()
            .filter(member -> member.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
    
    public void displayAllMembers() {
        if (members.isEmpty()) {
            System.out.println("No registered members.");
            return;
        }
        
        System.out.println("\n=== REGISTERED MEMBERS ===");
        System.out.println("Total members: " + members.size());
        System.out.println("-".repeat(80));
        
        for (int i = 0; i < members.size(); i++) {
            System.out.println((i + 1) + ". " + members.get(i));
        }
    }
    
    public void payFine(String memberId, double amount) {
        Member member = findMemberById(memberId);
        if (member == null) {
             System.out.println("Error: Member not found.");
             return;
        }
        
        if (!member.hasFines()) {
            System.out.println("Member has no outstanding fines.");
            return;
        }
        
        if (amount <= 0) {
            System.out.println("Invalid amount.");
            return;
        }
        
        member.payFine(amount);
        saveData();
        System.out.printf("Payment of $%.2f accepted. Remaining fines: $%.2f\n", amount, member.getTotalFines());
    }
    
    // --- Borrowing Operations ---
    public void borrowBook(String isbn, String memberId) {
        Book book = findBookByIsbn(isbn);
        Member member = findMemberById(memberId);
        
        if (book == null) {
            System.out.println("Error: Book not found!");
            return;
        }
        
        if (member == null) {
            System.out.println("Error: Member not found!");
            return;
        }
        
        if (!book.isAvailable()) {
            System.out.println("Error: Book is already borrowed by member " + book.getBorrowedBy() + ".");
            return;
        }
        
        // Process borrowing based on reservations
        if (book.hasReservations()) {
            String nextInLine = book.getReservationQueue().peek();
            if (!nextInLine.equals(memberId)) {
                System.out.println("Error: Book is reserved for member " + nextInLine + ".");
                System.out.println("Please ask them to cancel their reservation or wait for your turn.");
                return;
            } else {
                // Member is next in line, remove their reservation
                book.getNextReservation();
            }
        }
        
        // Members with excessive fines cannot borrow
        if (member.getTotalFines() > 10.0) {
            System.out.println("Error: Member " + member.getName() + " has excessive fines outstanding ($" + 
                String.format("%.2f", member.getTotalFines()) + "). Please pay fines before borrowing.");
            return;
        }
        
        book.setAvailable(false);
        book.setBorrowedBy(memberId);
        book.setDueDate(LocalDate.now().plusWeeks(LOAN_WEEKS));
        
        member.borrowBook(isbn);
        
        saveData();
        
        System.out.println("Book borrowed successfully by " + member.getName() + "!");
        System.out.println("Due date: " + book.getDueDate());
    }
    
    public void returnBook(String isbn) {
         Book book = findBookByIsbn(isbn);
         if (book == null) {
             System.out.println("Error: Book not found.");
             return;
         }
         
         if (book.isAvailable()) {
             System.out.println("Error: Book is not currently borrowed.");
             return;
         }
         
         String memberId = book.getBorrowedBy();
         Member member = findMemberById(memberId);
         
         // Calculate fines if overdue
         if (book.isOverdue()) {
             long daysOverdue = ChronoUnit.DAYS.between(book.getDueDate(), LocalDate.now());
             double fine = daysOverdue * FINE_PER_DAY;
             
             if (member != null) {
                 member.addFine(fine);
                 System.out.printf("Warning: Book is %d days overdue. A fine of $%.2f has been added to member %s's account.\n", 
                     daysOverdue, fine, member.getName());
             }
         }
         
         book.setAvailable(true);
         book.setBorrowedBy(null);
         book.setDueDate(null);
         
         if (member != null) {
             member.returnBook(isbn);
         }
         
         saveData();
         System.out.println("Book returned successfully.");
         
         // Notify if reserved
         if (book.hasReservations()) {
             String nextMemberId = book.getReservationQueue().peek();
             Member nextMember = findMemberById(nextMemberId);
             String nextMemberName = (nextMember != null) ? nextMember.getName() : nextMemberId;
             System.out.println("*** Notification: This book is reserved for " + nextMemberName + " ***");
         }
    }
    
    public void reserveBook(String isbn, String memberId) {
        Book book = findBookByIsbn(isbn);
        Member member = findMemberById(memberId);
        
        if (book == null) {
            System.out.println("Error: Book not found!");
            return;
        }
        
        if (member == null) {
            System.out.println("Error: Member not found!");
            return;
        }
        
        if (book.isAvailable() && !book.hasReservations()) {
             System.out.println("Book is currently available. You can borrow it directly.");
             return;
        }
        
        if (memberId.equals(book.getBorrowedBy())) {
             System.out.println("Error: You have already borrowed this book.");
             return;
        }
        
        if (book.getReservationQueue().contains(memberId)) {
             System.out.println("Error: You have already reserved this book.");
             return;
        }
        
        book.addReservation(memberId);
        saveData();
        
        System.out.println("Book reserved successfully. You are position " + book.getReservationQueue().size() + " in the queue.");
    }
    
    // --- Statistics ---
    public void displayStatistics() {
        System.out.println("\n=== LIBRARY STATISTICS ===");
        
        if (books.isEmpty()) {
             System.out.println("The library has no books.");
             return;
        }
        
        long availableBooks = books.stream()
            .filter(Book::isAvailable)
            .count();
        
        long borrowedBooks = books.size() - availableBooks;
        
        System.out.println("Total Books: " + books.size());
        System.out.println("Available Books: " + availableBooks);
        System.out.println("Borrowed Books: " + borrowedBooks);
        System.out.println("Registered Members: " + members.size());
        
        if (borrowedBooks > 0) {
            long overdueBooks = books.stream()
                .filter(book -> !book.isAvailable() && book.isOverdue())
                .count();
            System.out.println("Overdue Books: " + overdueBooks);
            
            if (overdueBooks > 0) {
                System.out.println("\n--- Overdue Books List ---");
                books.stream()
                     .filter(book -> !book.isAvailable() && book.isOverdue())
                     .forEach(book -> {
                         Member m = findMemberById(book.getBorrowedBy());
                         String borrowerName = m != null ? m.getName() : book.getBorrowedBy();
                         System.out.printf("- %s by %s (Due: %s, Borrowed by: %s)\n", 
                             book.getTitle(), book.getAuthor(), book.getDueDate(), borrowerName);
                     });
            }
        }
        
        double totalOutstandingFines = members.stream()
            .mapToDouble(Member::getTotalFines)
            .sum();
            
        System.out.printf("\nTotal Outstanding Fines: $%.2f\n", totalOutstandingFines);
    }
}
