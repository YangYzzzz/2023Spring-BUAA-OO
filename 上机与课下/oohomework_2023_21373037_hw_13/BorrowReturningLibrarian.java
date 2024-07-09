import java.util.ArrayList;

public class BorrowReturningLibrarian { //可以用单例模式
    private final ArrayList<Book> books; //失败会进去
    private final Library library;

    public BorrowReturningLibrarian(Library library) {
        this.books = new ArrayList<>();
        this.library = library;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void borrowBook(Person person, Book book) { //两个去向
        if (!person.hasBBook()) {
            System.out.println("[" + library.getTime() + "] " + person.getId() + " borrowed "
                    + book.getCategory() + "-" + book.getName() +
                    " from borrowing and returning librarian");
            person.addBook(book);
            library.getOrderingLibrarian().clearBBook(person); //清除其他B类书的预定请求
        } else {
            books.add(book);
        }
    }

    public void returnBook(Person person, Book book) { //三种情况 损坏 正常归还 两个去向
        if (book.isSmeared()) {
            compensate(person); //先赔钱 再还书成功 再修复
            System.out.println("[" + library.getTime() + "] " + person.getId() + " returned " +
                    book.getCategory() + "-" + book.getName() +
                    " to borrowing and returning librarian");
            library.getLogisticsDivision().repairBook(book);
        } else {
            System.out.println("[" + library.getTime() + "] " + person.getId() + " returned " +
                    book.getCategory() + "-" + book.getName() +
                    " to borrowing and returning librarian");
            books.add(book); //由借还处收集
        }
    }

    public void compensate(Person person) { //损坏和丢失共用
        System.out.println("[" + library.getTime() + "] " + person.getId() +
                " got punished by borrowing and returning librarian");
    }

}
