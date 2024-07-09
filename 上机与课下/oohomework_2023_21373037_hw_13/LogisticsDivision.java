import java.util.ArrayList;

public class LogisticsDivision {
    private final ArrayList<Book> books;
    private final Library library;

    public LogisticsDivision(Library library) {
        this.books = new ArrayList<>();
        this.library = library;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void repairBook(Book book) {
        book.setSmeared(false);
        books.add(book);
        System.out.println("[" + library.getTime() + "] " + book.getCategory() + "-" +
                book.getName() + " got repaired by logistics division");
    }
}
