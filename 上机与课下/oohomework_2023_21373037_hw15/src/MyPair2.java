public class MyPair2 {
    private final Book book;
    private final Library library;

    public MyPair2(Book book, Library library) {
        this.book = book;
        this.library = library;
    }

    public Book getBook() {
        return book;
    }

    public Library getLibrary() {
        return library;
    }
}
