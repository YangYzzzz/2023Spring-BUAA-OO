package src;

import src.Book;
import src.Library;
import src.LibrarySystem;

import java.util.ArrayList;

public class Machine {
    private final ArrayList<Book> books;
    private final Library library;
    private boolean statusM;
    private String school1M;
    private String school2M;

    public Machine(Library library) {
        this.books = new ArrayList<>();
        this.library = library;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void searchBook(char category, String name, Person person) { //借书起始点在此
        System.out.println("[" + LibrarySystem.getTime() + "] " + person.getSchoolName()
                + "-" + person.getId() + " queried " + category + "-" + name +
                " from self-service machine");
        System.out.println("[" + LibrarySystem.getTime() + "] self-service machine " +
                "provided information of " + category + "-" + name);
        Book book = library.getShelf().getBook(category, name); //若能得到 则已经在书架上删除完
        if (book != null) {
            if (category == 'A') {
                return;
            } else if (category == 'B') {
                library.getShelf().removeBook(book);
                library.getBorrowReturningLibrarian().borrowBBook(person, book);
                return;
            } else {
                library.getShelf().removeBook(book);
                borrowCBook(person, book);
                return;
            }
        } //整个LibrarySystem应该有一个统一的清单
        if (category != 'A') { //除A类 先记录进清单中
            LibrarySystem.addPrepareList(category, name, person);
        }
    }

    public void borrowCBook(Person person, Book book) { //借C类书
        statusM = person.hasCSameBook(book.getCategory(), book.getName(), person.getBooks());
        if (!statusM) {
            System.out.println("[" + LibrarySystem.getTime() + "] self-service machine lent "
                    + book.getSchoolName() + "-" + book.getCategory() + "-" + book.getName() +
                    " to " + person.getSchoolName() + "-" + person.getId());
            System.out.println("[" + LibrarySystem.getTime() + "] " + person.getSchoolName() +
                    "-" + person.getId() + " borrowed " + book.getSchoolName() + "-" +
                    book.getCategory() + "-" + book.getName() + " from self-service machine");
            System.out.println("(State) [" + LibrarySystem.getTime() + "] " +
                    book.getCategory() + "-" + book.getName()
                    + " transfers from InOwnSchool to InOwnStudentHand");
            person.addBook(book);
        } else { //失败
            System.out.println("[" + LibrarySystem.getTime() +
                    "] self-service machine refused lending " + book.getSchoolName() + "-"
                    + book.getCategory() + "-" + book.getName() + " to " +
                    person.getSchoolName() + "-" + person.getId());
            System.out.println("(State) [" + LibrarySystem.getTime() + "] " +
                    book.getCategory() + "-" + book.getName()
                    + " transfers from InOwnSchool to InOwnSchool");
            books.add(book);
        }
    }

    public void printReturnCBook(Person person, Book book) {
        System.out.println("[" + LibrarySystem.getTime() + "] " + person.getSchoolName() + "-" +
                person.getId() + " returned " + book.getSchoolName() + "-" + book.getCategory() +
                "-" + book.getName() + " to self-service machine");
        System.out.println("[" + LibrarySystem.getTime() + "] self-service machine collected " +
                book.getSchoolName() + "-" + book.getCategory() + "-" + book.getName() + " from " +
                person.getSchoolName() + "-" + person.getId());
        school1M = person.getSchoolName();
        school2M = book.getSchoolName();
        if (school2M.equals(school1M)) {
            System.out.println("(State) [" + LibrarySystem.getTime() + "] " +
                    book.getCategory() + "-" + book.getName()
                    + " transfers from InOwnStudentHand to InOwnSchool");
        } else {
            System.out.println("(State) [" + LibrarySystem.getTime() + "] " +
                    book.getCategory() + "-" + book.getName()
                    + " transfers from InOtherStudentHand to InOtherSchool");
        }
    }

    public void returnBook(Person person, Book book) {
        //还C类书 两种情况 损坏 交给借还处 并成功归还 与 正常归还
        if (book.isSmeared()) {
            library.getBorrowReturningLibrarian().compensate(person);
            printReturnCBook(person, book);
            library.getLogisticsDivision().repairBook(book);
        } else {
            printReturnCBook(person, book);
            if (book.getSchoolName().equals(person.getSchoolName())) {
                books.add(book);
            } else {
                library.getPurchasingDepartment().addCarryOut(book,
                        LibrarySystem.getLibrary(book.getSchoolName()));
            }
        }
    }
}
