import java.util.ArrayList;

public class Person {
    private final String id; //学号
    private final ArrayList<Book> books;
    private final ArrayList<Book> futureBooks;
    private final String schoolName; //八成没用 再议
    private int orderedTime;
    private final Library library;

    public Person(String id, Library library, String schoolName) {
        this.futureBooks = new ArrayList<>();
        this.schoolName = schoolName;
        this.id = id;
        this.books = new ArrayList<>();
        this.orderedTime = 0;
        this.library = library;
    }

    public void addBookTime() {
        for (Book book : books) {
            book.addTime();
        }
    }

    public void addFutureBooks(Book book) {
        futureBooks.add(book);
    }

    public void clearFutureBooks() {
        futureBooks.clear();
    }

    public String getSchoolName() {
        return schoolName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            return ((Person) obj).getId().equals(this.id) &&
                    ((Person) obj).getSchoolName().equals(this.schoolName);
        } else {
            return false;
        }
    }

    public String getId() {
        return id;
    }

    public void addBook(Book book) {
        this.books.add(book);
    }

    public boolean hasBBook(ArrayList<Book> books) {
        for (Book book : books) {
            if (book.getCategory() == 'B') {
                return true;
            }
        }
        return false;
    }

    public boolean hasCSameBook(char category, String name, ArrayList<Book> books) {
        for (Book book : books) {
            if (book.getName().equals(name) && book.getCategory() == category) {
                return true;
            }
        }
        return false;
    }

    public void addOrderedBookTime() { //相互加清单
        this.orderedTime++;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public ArrayList<Book> getFutureBooks() {
        return futureBooks;
    }

    public boolean canBorrowBook(char category, String name, ArrayList<Book> books) {
        if (category == 'B' && hasBBook(books)) {
            return false;
        } else if (category == 'C' && hasCSameBook(category, name, books)) {
            return false;
        } else {
            return true;
        }
    }

    public void getOrderedBook() {

    }

    public boolean canOrderBook(char category, String name) { //能否预定 book都是从allBook得到的引用
        if (orderedTime >= 3) {
            return false;
        }
        return !library.getOrderingLibrarian().hasSameOrder(this, category, name);
    }

    public Book getBook(char category, String name) {
        for (Book book : books) {
            if (book.getCategory() == category && book.getName().equals(name)) {
                return book;
            }
        }
        return null;
    }

    public void clearOrderedBookTime() {
        this.orderedTime = 0;
    }

    public void borrowBook(char category, String name) {
        library.getMachine().searchBook(category, name, this);
    }

    public void smearBook(char category, String name) { //与时间无关
        Book book = getBook(category, name);
        book.setSmeared(true);
    }

    public void lostBook(char category, String name) {
        Book book = getBook(category, name);
        this.books.remove(book);
        LibrarySystem.getLibrary(book.getSchoolName()).deleteBooksShadow(book);
        library.getBorrowReturningLibrarian().compensate(this);
    }

    public void returnBook(char category, String name) { //key
        Book book = getBook(category, name);
        books.remove(book); //先将拥有的这本书清除
        if (category == 'B') {
            library.getBorrowReturningLibrarian().returnBook(this, book);
        } else {
            library.getMachine().returnBook(this, book);
        }
    }
}
