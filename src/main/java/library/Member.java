package library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Member implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String name;
    private List<String> borrowedBookIsbns;
    private double totalFines;
    
    public Member(String id, String name) {
        this.id = id;
        this.name = name;
        this.borrowedBookIsbns = new ArrayList<>();
        this.totalFines = 0.0;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    
    public List<String> getBorrowedBooks() {
        if (borrowedBookIsbns == null) {
            borrowedBookIsbns = new ArrayList<>();
        }
        return borrowedBookIsbns;
    }
    
    public void borrowBook(String isbn) {
        getBorrowedBooks().add(isbn);
    }
    
    public void returnBook(String isbn) {
        getBorrowedBooks().remove(isbn);
    }
    
    public double getTotalFines() { return totalFines; }
    
    public void addFine(double amount) {
        this.totalFines += amount;
    }
    
    public void payFine(double amount) {
        this.totalFines = Math.max(0, this.totalFines - amount);
    }
    
    public boolean hasFines() {
        return totalFines > 0;
    }
    
    @Override
    public String toString() {
        String finesInfo = hasFines() ? String.format(" | Fines Due: $%.2f", totalFines) : "";
        return String.format("ID: %s | Name: %s | Borrowed Books: %d%s", 
            id, name, getBorrowedBooks().size(), finesInfo);
    }
}
