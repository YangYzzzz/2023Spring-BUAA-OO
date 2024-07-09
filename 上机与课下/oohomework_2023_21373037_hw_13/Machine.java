import java.util.ArrayList;

public class Machine {
    private final ArrayList<Book> books;
    private final Library library;

    public Machine(Library library) {
        this.books = new ArrayList<>();
        this.library = library;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void searchBook(char category, String name, Person person) { //借书起始点在此
        System.out.println("[" + library.getTime() + "] " + person.getId() + " queried " +
                category + "-" + name + " from self-service machine");
        Book book = library.getShelf().getBook(category, name); //若能得到 则已经在书架上删除完
        if (book != null) {
            if (category == 'A') {
                return;
            } else if (category == 'B') {
                library.getShelf().removeBook(book);
                library.getBorrowReturningLibrarian().borrowBook(person, book);
                return;
            } else {
                library.getShelf().removeBook(book);
                borrowBook(person, book);
                return;
            }
        }
        //预定 保证能预定
        for (Book book1 : library.getBooksShadow()) { //从整体找
            if (book1.getName().equals(name) && book1.getCategory() == category) {
                library.getOrderingLibrarian().orderBook(person, book1);
                return;
            }
        }
    }

    public void borrowBook(Person person, Book book) { //借C类书
        if (!person.hasCSameBook(book)) { //成功
            System.out.println("[" + library.getTime() + "] " + person.getId() + " borrowed " +
                    book.getCategory() + "-" + book.getName() + " from self-service machine");
            person.addBook(book);
        } else { //失败
            books.add(book);
        }
    }

    public void returnBook(Person person, Book book) {
        //还C类书 两种情况 损坏 交给借还处 并成功归还 与 正常归还
        if (book.isSmeared()) {
            library.getBorrowReturningLibrarian().compensate(person);
            System.out.println("[" + library.getTime() + "] " + person.getId() + " returned " +
                    book.getCategory() + "-" + book.getName() + " to self-service machine");
            library.getLogisticsDivision().repairBook(book);
        } else {
            System.out.println("[" + library.getTime() + "] " + person.getId() + " returned " +
                    book.getCategory() + "-" + book.getName() + " to self-service machine");
            books.add(book); //由借还处收集
        }
    }
}
