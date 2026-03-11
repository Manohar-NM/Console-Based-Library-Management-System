package library;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String BOOKS_FILE = "books.txt";
    private static final String MEMBERS_FILE = "members.txt";

    @SuppressWarnings("unchecked")
    public List<Book> loadBooks() {
        File file = new File(BOOKS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKS_FILE))) {
            return (List<Book>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading books: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveBooks(List<Book> books) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKS_FILE))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public List<Member> loadMembers() {
        File file = new File(MEMBERS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MEMBERS_FILE))) {
            return (List<Member>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading members: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveMembers(List<Member> members) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MEMBERS_FILE))) {
            oos.writeObject(members);
        } catch (IOException e) {
            System.err.println("Error saving members: " + e.getMessage());
        }
    }
    
    public boolean exportToCSV(List<Book> books, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("ISBN,Title,Author,Year,Status,Borrower,Due Date");
            
            for (Book book : books) {
                String dueDateStr = book.getDueDate() != null ? book.getDueDate().toString() : "";
                String borrowerStr = book.getBorrowedBy() != null ? book.getBorrowedBy() : "";
                String statusStr = book.isAvailable() ? "Available" : "Borrowed";
                
                // Escape quotes and wrap in quotes to handle commas in title/author
                String title = "\"" + book.getTitle().replace("\"", "\"\"") + "\"";
                String author = "\"" + book.getAuthor().replace("\"", "\"\"") + "\"";
                
                writer.printf("%s,%s,%s,%d,%s,%s,%s%n",
                    book.getIsbn(),
                    title,
                    author,
                    book.getYear(),
                    statusStr,
                    borrowerStr,
                    dueDateStr
                );
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting to CSV: " + e.getMessage());
            return false;
        }
    }
}
