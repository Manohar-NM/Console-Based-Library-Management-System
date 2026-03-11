package library;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String isbn;
    private String title;
    private String author;
    private int year;
    private boolean available;
    private String borrowedBy;
    private LocalDate dueDate;
    private Queue<String> reservationQueue;
    
    public Book(String isbn, String title, String author, int year) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
        this.available = true;
        this.borrowedBy = null;
        this.dueDate = null;
        this.reservationQueue = new LinkedList<>();
    }
    
    // Getters and setters
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public boolean isAvailable() { return available; }
    
    public void setAvailable(boolean available) { 
        this.available = available; 
    }
    
    public String getBorrowedBy() { return borrowedBy; }
    public void setBorrowedBy(String borrowedBy) { 
        this.borrowedBy = borrowedBy; 
    }
    
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { 
        this.dueDate = dueDate; 
    }
    
    public Queue<String> getReservationQueue() {
        if (reservationQueue == null) {
            reservationQueue = new LinkedList<>();
        }
        return reservationQueue;
    }
    
    public void addReservation(String memberId) {
        getReservationQueue().offer(memberId);
    }
    
    public String getNextReservation() {
        return getReservationQueue().poll();
    }
    
    public boolean hasReservations() {
        return !getReservationQueue().isEmpty();
    }
    
    public boolean isOverdue() {
        if (dueDate == null) return false;
        return LocalDate.now().isAfter(dueDate);
    }
    
    @Override
    public String toString() {
        String status = available ? "Available" : "Borrowed by: " + borrowedBy;
        if (!available && isOverdue()) {
             status += " (OVERDUE)";
        }
        String reservations = hasReservations() ? " | Reserved by: " + getReservationQueue().size() + " member(s)" : "";
        
        return String.format("ISBN: %s | Title: %s | Author: %s | Year: %d | %s%s",
            isbn, title, author, year, status, reservations);
    }
}
