import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class PurchasingDepartment { //在这个模块中完成与其他学校的交互
    private final HashMap<Book, HashSet<Person>> purchaseList; //购买清单, Person不能重复
    private final ArrayList<MyPair> orderOutsideList; //校外借书清单 书一定都来自外校 假书
    private final ArrayList<Book> books; //该送进来的书 这一天凌晨送进来 其中包含 还过来的书和校外借的书
    private final ArrayList<Book> carryIn;
    private final ArrayList<MyPair2> carryOut; //该送出去的书 前一天晚上送出去 书和送往学校的名字 两本书可能一样
    private final Library library;
    private String school1PI;
    private String school2PI;
    private String school3PI;
    private String school1PO;
    private String school2PO;
    private String school3PO;
    private boolean statusP;

    public PurchasingDepartment(Library library) {
        this.purchaseList = new HashMap<>();
        this.orderOutsideList = new ArrayList<>();
        this.books = new ArrayList<>();
        this.carryIn = new ArrayList<>();
        this.carryOut = new ArrayList<>();
        this.library = library;
    }

    public void removePurchaseList(MyPair pair) {
        Book book = new Book(pair.getCategory(), pair.getName(), library.getSchoolName());
        if (purchaseList.containsKey(book)) {
            purchaseList.get(book).remove(pair.getPerson());
        }
    }

    public void addCarryIn(Book book) {
        carryIn.add(book);
    }

    public void addPurchaseList(char category, String name, Person person) {
        Book book = new Book(category, name, library.getSchoolName()); //假的
        if (purchaseList.containsKey(book)) {
            purchaseList.get(book).add(person);
        } else {
            HashSet<Person> hashSet = new HashSet<>();
            hashSet.add(person);
            purchaseList.put(book, hashSet);
        }
    }

    public void buyBook() {
        for (Map.Entry<Book, HashSet<Person>> entry : purchaseList.entrySet()) {
            if (entry.getValue().size() > 0) {
                int num = Math.max(entry.getValue().size(), 3);
                Book book = entry.getKey();
                System.out.println("[" + LibrarySystem.getTime() + "] " + book.getSchoolName() +
                        "-" + book.getCategory() + "-" + book.getName() +
                        " got purchased by purchasing department in " + library.getSchoolName());
                for (int i = 0; i < num; i++) {
                    books.add(new Book(book.getCategory(), book.getName(), book.getSchoolName()));
                }
                library.addBooksShadow(book, num);
            }
        }
        purchaseList.clear();
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void addOrderOutsideList(Person person, char category, String name) {
        orderOutsideList.add(new MyPair(person, category, name));
    }

    public void addCarryOut(Book book, Library library) { //注意library是发送到的图书馆
        carryOut.add(new MyPair2(book, library));
    }

    public void sendCarryOut() {
        for (MyPair2 pair : carryOut) {
            Book book = pair.getBook();
            Library libraryTo = pair.getLibrary();
            libraryTo.getPurchasingDepartment().addCarryIn(book);
            school1PO = book.getSchoolName();
            school2PO = library.getSchoolName();
            school3PO = library.getSchoolName();
            System.out.println("[" + LibrarySystem.getTime() + "] " + book.getSchoolName() +
                    "-" + book.getCategory() + "-" + book.getName() +
                    " got transported by purchasing department in " + library.getSchoolName());
            if (school1PO.equals(school2PO)) {
                System.out.println("(State) [" + LibrarySystem.getTime() + "] " +
                        book.getCategory() + "-" + book.getName()
                        + " transfers from InOwnSchool to InRoad");
            } else if (!school1PO.equals(school3PO)) {
                System.out.println("(State) [" + LibrarySystem.getTime() + "] " +
                        book.getCategory() + "-" + book.getName()
                        + " transfers from InOtherSchool to InRoad");
            }
        }
        carryOut.clear();
    }

    public void deliverBook() {
        Iterator<Book> iterator = carryIn.iterator();
        while (iterator.hasNext()) {
            Book book = iterator.next();
            for (MyPair pair : orderOutsideList) {
                statusP = (pair.getCategory() == book.getCategory()) &&
                        pair.getName().equals(book.getName());
                if (statusP) {
                    Person person = pair.getPerson();
                    orderOutsideList.remove(pair); //删除清单内容
                    System.out.println("[" + LibrarySystem.getTime() + "] purchasing department" +
                            " lent " + book.getSchoolName() + "-" + book.getCategory() + "-" +
                            book.getName() + " to " +
                            person.getSchoolName() + "-" + person.getId());
                    System.out.println("[" + LibrarySystem.getTime() + "] " + person.getSchoolName()
                            + "-" + person.getId() + " borrowed " + book.getSchoolName() + "-" +
                            book.getCategory() + "-" + book.getName() +
                            " from purchasing department");
                    System.out.println("(State) [" + LibrarySystem.getTime() + "] " +
                            book.getCategory() + "-" + book.getName()
                            + " transfers from InOtherSchool to InOtherStudentHand");
                    person.addBook(book);
                    if (pair.getCategory() == 'B') {
                        library.getOrderingLibrarian().clearBBook(person);
                    } else { //c类
                        library.getOrderingLibrarian().clearCBook(person, pair.getName());
                    }
                    iterator.remove(); //删除对应运入内容
                    break;
                }
            }
        }
        for (Book book : carryIn) {
            books.add(book);
        }
        carryIn.clear();
    }

    public void receiveBook() { //收书加发书
        for (Book book : carryIn) {
            school1PI = book.getSchoolName();
            school2PI = library.getSchoolName();
            school3PI = library.getSchoolName();
            System.out.println("[" + LibrarySystem.getTime() + "] " + book.getSchoolName() + "-"
                    + book.getCategory() + "-" + book.getName() +
                    " got received by purchasing department in " + library.getSchoolName());
            if (school1PI.equals(school2PI)) {
                System.out.println("(State) [" + LibrarySystem.getTime() + "] " +
                        book.getCategory() + "-" + book.getName()
                        + " transfers from InRoad to InOwnSchool");
            } else if (!school1PI.equals(school3PI)) {
                System.out.println("(State) [" + LibrarySystem.getTime() + "] " +
                        book.getCategory() + "-" + book.getName()
                        + " transfers from InRoad to InOtherSchool");
            }
        }
    }
}
