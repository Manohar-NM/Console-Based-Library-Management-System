# Console-Based Library Management System

## Project Description
A Java console application for managing library operations including book tracking, member management, and borrowing system with file-based data persistence.

## Features
- Add, remove, and search for books
- Register and manage library members
- Borrow and return books with due dates
- Calculate overdue fines
- File-based data persistence
- Comprehensive input validation
- Library statistics generation

## How to Run
```bash
# Compile and run
javac -d bin src/main/java/library/*.java
java -cp bin library.Main
```

## Sample Menu
``` text
=== LIBRARY MANAGEMENT SYSTEM ===
1. Add New Book
2. View All Books
3. Search Books
4. Register Member
5. Borrow Book
6. Return Book
7. View Library Statistics
8. Exit
Enter your choice:
```

---

## Project Overview and Objectives
This project simulates a real-world library management system through an interactive command-line interface. The objective is to provide a robust, persistent solution for tracking book inventory, managing library members, and processing borrow/return transactions. The system is designed following core Object-Oriented principles.

## Setup and Installation Instructions
1. Ensure you have the Java Development Kit (JDK) installed on your system.
2. Clone this repository to your local machine:
   ```bash
   git clone https://github.com/Manohar-NM/Console-Based-Library-Management-System.git
   ```
3. Navigate to the project directory:
   ```bash
   cd Console-Based-Library-Management-System
   ```
4. Compile the application:
   ```bash
   javac -d bin src/main/java/library/*.java
   ```
5. Run the application:
   ```bash
   java -cp bin library.Main
   ```

## Code Structure Explanation
The project is strictly organized following Java standard directory structures (`src/main/java/library/`) and contains the following components:

- `Main.java`: The entry point of the application containing the console UI loop, interactive menu options, and input validation handling.
- `Library.java`: The core management class which orchestrates all internal operations like modifying lists, validating book availability, checking reservations, and executing search parameters.
- `Book.java`: Data model representing the properties of a book (ISBN, title, author, year) and its current state (availability, due date, queue).
- `Member.java`: Data model representing a member entity tracking their profile, an array of borrowed books, and any accumulated fines.
- `FileHandler.java`: Responsible for I/O data persistence mechanisms; primarily executing Java Object Serialization for `.txt` records and outputting `.csv` files.

## How Technical Requirements Were Met
- **Java classes with proper encapsulation:** Core models (`Book`, `Member`, `Library`) strictly enforce `private` access modifiers on fields and expose state mutations solely through secure `public` getter and setter methods.
- **File I/O operations for data persistence:** Implemented via `FileHandler.java` seamlessly capturing data into `books.txt` and `members.txt` via `ObjectOutputStream`, and converting binary back onto the heap via `ObjectInputStream` on initialization.
- **ArrayLists for storing collections:** Both the total inventory of books and the list of registered members are stored using dynamic `ArrayList`s within the `Library` framework class.
- **Console-based menu system:** Provided cohesively in `Main.java` inside an infinitely running `while` loop with a `switch` router.
- **Exception handling for errors:** Strategically applied `try-catch` blocks secure file operations (`IOException`, `ClassNotFoundException`) and prevent system crashes from malformed dynamic keyboard inputs (`InputMismatchException`, `NoSuchElementException`).
- **Input validation and user feedback:** Scanner routines inherently refuse progression until formatting correctness is achieved, providing informative recovery texts.
- **Search and filter functionality:** Powered by Java Streams in `Library.java`, enabling instantaneous substring and case-insensitive keyword filtering mapped across title, author, and ISBN parameters.

## Screenshots
> *(Please replace this section with active screenshots of the running platform inside your terminal before final submission)*

![Running Application](https://via.placeholder.com/800x400.png?text=Take+a+screenshot+of+the+terminal+and+place+it+here)
