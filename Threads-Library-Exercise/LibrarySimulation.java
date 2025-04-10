import java.util.HashMap;
import java.util.Map;
import java.util.Random;

class Library {
    private final Map<String, Integer> booksInventory = new HashMap<>();

    public synchronized void addBook(String bookName, int quantity) {
        booksInventory.put(bookName, booksInventory.getOrDefault(bookName, 0) + quantity);
        System.out.println(Thread.currentThread().getName() + " added " + quantity + " copies of " + bookName);
    }

    public synchronized void borrowBook(String bookName) {
        if (booksInventory.getOrDefault(bookName, 0) > 0) {
            booksInventory.put(bookName, booksInventory.get(bookName) - 1);
            System.out.println(Thread.currentThread().getName() + " borrowed " + bookName);
        } else {
            System.out.println(Thread.currentThread().getName() + " tried to borrow " + bookName + " but none available");
        }
    }

    public synchronized void printInventory() {
        System.out.println("\nFinal inventory:");
        for (Map.Entry<String, Integer> entry : booksInventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " copies");
        }
    }
}

class Patron implements Runnable {
    private final Library library;
    private final Random random = new Random();
    private final String[] bookList;

    public Patron(Library library, String[] bookList) {
        this.library = library;
        this.bookList = bookList;
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            String chosenBook = bookList[random.nextInt(bookList.length)];
            int amount = random.nextInt(3) + 1; // 1 to 3 copies

            if (random.nextBoolean()) {
                library.addBook(chosenBook, amount);
            } else {
                library.borrowBook(chosenBook);
            }

            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class LibrarySimulation {
    public static void main(String[] args) {
        Library library = new Library();

        String[] bookTitles = {
            "Harry Potter", "The Hobbit", "1984", 
            "To Kill a Mockingbird", "The Great Gatsby"
        };

        library.addBook("Harry Potter", 5);
        library.addBook("The Hobbit", 3);
        library.addBook("1984", 2);
        library.addBook("To Kill a Mockingbird", 4);
        library.addBook("The Great Gatsby", 1);

        Thread user1 = new Thread(new Patron(library, bookTitles), "Patron 1");
        Thread user2 = new Thread(new Patron(library, bookTitles), "Patron 2");
        Thread user3 = new Thread(new Patron(library, bookTitles), "Patron 3");

        user1.start();
        user2.start();
        user3.start();

        try {
            user1.join();
            user2.join();
            user3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        library.printInventory();
    }
}
