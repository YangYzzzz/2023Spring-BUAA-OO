import java.util.ArrayList;

public class LogisticsDivision {
    private final ArrayList<Book> books;
    private final Library library;
    private String school1R;
    private String school2R;

    public LogisticsDivision(Library library) {
        this.books = new ArrayList<>();
        this.library = library;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void repairBook(Book book) {
        book.setSmeared(false);
        if (book.getSchoolName().equals(library.getSchoolName())) {
            books.add(book);
        } else {
            library.getPurchasingDepartment().addCarryOut(book,
                    LibrarySystem.getLibrary(book.getSchoolName()));
        }
        school1R = book.getSchoolName();
        school2R = library.getSchoolName();
        System.out.println("[" + LibrarySystem.getTime() + "] " + book.getSchoolName() +
                "-" + book.getCategory() + "-" + book.getName() + " got repaired by logistics " +
                "division in " + library.getSchoolName()); //读者的学校修复
        if (school1R.equals(school2R)) {
            System.out.println("(State) [" + LibrarySystem.getTime() + "] " +
                    book.getCategory() + "-" + book.getName()
                    + " transfers from InOwnSchool to InOwnSchool");
        } else {
            System.out.println("(State) [" + LibrarySystem.getTime() + "] " +
                    book.getCategory() + "-" + book.getName()
                    + " transfers from InOtherSchool to InOtherSchool");
        }
    }
}
