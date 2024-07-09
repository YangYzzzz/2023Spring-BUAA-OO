import java.util.ArrayList;

public class Person {
    private String id; //学号
    private ArrayList<Book> books;
    private int orderedTime;
    private Library library;

    public Person(String id, Library library) {
        this.id = id;
        this.books = new ArrayList<>();
        this.orderedTime = 0;
        this.library = library;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            return ((Person) obj).getId().equals(this.getId());
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

    public boolean hasBBook() {
        for (Book book : books) {
            if (book.getCategory() == 'B') {
                return true;
            }
        }
        return false;
    }

    public boolean hasCSameBook(Book book) {
        for (Book book1 : books) {
            if (book1.getName().equals(book.getName())) {
                return true;
            }
        }
        return false;
    }

    public void addOrderedBookTime() { //相互加清单
        this.orderedTime++;
    }

    public boolean canOrderBook(Book book) { //能否预定 book都是从allBook得到的引用
        if (orderedTime >= 3) {
            return false;
        }
        if ((hasBBook() && book.getCategory() == 'B') ||
                (hasCSameBook(book) && book.getCategory() == 'C')) {
            return false;
        }
        return !library.getOrderingLibrarian().hasSameBook(this, book);
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
        getBook(category, name).setSmeared(true);
    }

    public void lostBook(char category, String name) {
        Book book = getBook(category, name);
        this.books.remove(book); //该书彻底再见 也许发生这种书再也没有了的情况 但是这只影响预约 永远拿不到了罢了
        library.getBorrowReturningLibrarian().compensate(this);
    }

    public void returnBook(char category, String name) {
        Book book = getBook(category, name);
        books.remove(book); //先将拥有的这本书清除
        if (category == 'B') {
            library.getBorrowReturningLibrarian().returnBook(this, book);
        } else {
            library.getMachine().returnBook(this, book);
        }
    }
}
