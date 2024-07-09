package src;

import src.Book;

import java.util.ArrayList;

public class Shelf {
    private final ArrayList<Book> books;

    public Shelf() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public Book getBookOutside(char category, String name) {
        for (Book book : books) {
            if (book.getName().equals(name) && book.getCategory() == category
                                    && !book.isPrivateOwned()) { //非私有
                return book;
            }
        }
        return null;
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
