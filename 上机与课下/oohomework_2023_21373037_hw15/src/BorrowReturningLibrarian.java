import java.util.ArrayList;

public class BorrowReturningLibrarian { //可以用单例模式
    private final ArrayList<Book> books; //失败会进去
    private final Library library;
    private boolean statusB;
    private String school1B;
    private String school2B;

    public BorrowReturningLibrarian(Library library) {
        this.books = new ArrayList<>();
        this.library = library;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void borrowBBook(Person person, Book book) { //两个去向
        statusB = person.hasBBook(person.getBooks());
        if (!statusB) {
            System.out.println("[" + LibrarySystem.getTime() + "] borrowing and returning " +
                    "librarian lent " + book.getSchoolName() + "-" + book.getCategory() + "-" +
                    book.getName() + " to " + person.getSchoolName() + "-" + person.getId());
            System.out.println("[" + LibrarySystem.getTime() + "] " +
                    person.getSchoolName() + "-" + person.getId() + " borrowed "
                    + book.getSchoolName() + "-" + book.getCategory() + "-" + book.getName() +
                    " from borrowing and returning librarian");
            System.out.println("(State) [" + LibrarySystem.getTime() + "] " +
                    book.getCategory() + "-" + book.getName()
                        + " transfers from InOwnSchool to InOwnStudentHand");
            person.addBook(book);
            library.getOrderingLibrarian().clearBBook(person); //清除其他B类书的预定请求
        } else {
            System.out.println("[" + LibrarySystem.getTime() + "] borrowing and " +
                    "returning librarian refused lending " + book.getSchoolName()
                    + "-" + book.getCategory() + "-" + book.getName() + " to " +
                    person.getSchoolName() + "-" + person.getId());
            System.out.println("(State) [" + LibrarySystem.getTime() + "] " +
                    book.getCategory() + "-" + book.getName()
                    + " transfers from InOwnSchool to InOwnSchool");
            books.add(book);
        }
    }

    public void printReturnBBook(Person person, Book book) {
        System.out.println("[" + LibrarySystem.getTime() + "] " + person.getSchoolName() + "-" +
                person.getId() + " returned " + book.getSchoolName() + "-" + book.getCategory()
                + "-" + book.getName() + " to borrowing and returning librarian");
        System.out.println("[" + LibrarySystem.getTime() + "] borrowing and returning librarian "
                + "collected " + book.getSchoolName() + "-" + book.getCategory() + "-" +
                book.getName() + " from " + person.getSchoolName() + "-" + person.getId());
        school1B = person.getSchoolName();
        school2B = book.getSchoolName();
        if (school1B.equals(school2B)) {
            System.out.println("(State) [" + LibrarySystem.getTime() + "] " +
                    book.getCategory() + "-" + book.getName()
                    + " transfers from InOwnStudentHand to InOwnSchool");
        } else {
            System.out.println("(State) [" + LibrarySystem.getTime() + "] " +
                    book.getCategory() + "-" + book.getName()
                    + " transfers from InOtherStudentHand to InOtherSchool");
        }
    }

    public void returnBook(Person person, Book book) { //还B书 三种情况 损坏 正常归还 两个去向
        if (book.isSmeared()) {
            compensate(person); //先赔钱 再还书成功 再修复
            printReturnBBook(person, book);
            library.getLogisticsDivision().repairBook(book);
        } else {
            if (book.getTime() > 30) {
                compensate(person);
            }
            printReturnBBook(person, book);
            if (person.getSchoolName().equals(book.getSchoolName())) {
                books.add(book); //由借还处收集
            } else {
                library.getPurchasingDepartment().addCarryOut(book,
                        LibrarySystem.getLibrary(book.getSchoolName()));
            }
        }
        book.clearTime();
    }

    public void compensate(Person person) { //损坏和丢失共用
        System.out.println("[" + LibrarySystem.getTime() + "] " + person.getSchoolName()
                + "-" + person.getId() + " got punished by borrowing and returning librarian");
        System.out.println("[" + LibrarySystem.getTime() + "] borrowing and " +
                "returning librarian received " + person.getSchoolName() + "-" +
                person.getId() + "'s fine");
    }

}
