import java.util.ArrayList;

public class Shelf {
    private ArrayList<Book> books;

    public Shelf() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public Book getBook(char category, String name) {
        for (Book book : books) {
            if (book.getName().equals(name) && book.getCategory() == category) {
                return book;
            }
        }
        return null;
    }

    public void removeBook(Book book) {
        books.remove(book);
    }
}
