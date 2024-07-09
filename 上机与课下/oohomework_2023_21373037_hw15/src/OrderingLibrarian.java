import java.util.ArrayList;
import java.util.Iterator;

public class OrderingLibrarian {
    private final ArrayList<MyPair> orderList;
    private final ArrayList<MyPair> buffer;
    private final Library library;
    private String school1O;
    private String school2O;

    public OrderingLibrarian(Library library) {
        this.buffer = new ArrayList<>();
        this.orderList = new ArrayList<>();
        this.library = library;
    }

    public ArrayList<MyPair> getOrderList() {
        return orderList;
    }

    public void successOrder() {
        for (MyPair pair : buffer) {
            Person person = pair.getPerson();
            char category = pair.getCategory();
            String name = pair.getName();
            System.out.println("[" + LibrarySystem.getTime() + "] " + person.getSchoolName()
                    + "-" + person.getId() + " ordered " + library.getSchoolName() + "-" +
                    category + "-" + name + " from ordering librarian");
            System.out.println("[" + LibrarySystem.getTime() + "] ordering librarian recorded "
                    + person.getSchoolName() + "-" + person.getId() + "'s order of " +
                    library.getSchoolName() + "-" + category + "-" + name);
            orderList.add(pair);
        }
        buffer.clear();
    }

    public boolean hasSameOrder(Person person, char category, String name) {
        for (MyPair pair : orderList) {
            if (pair.getPerson().equals(person) && pair.getName().equals(name)
                    && pair.getCategory() == category) {
                return true;
            }
        }
        return false;
    }

    public void clearCBook(Person person, String name) {
        for (MyPair pair : orderList) {
            if (pair.getPerson().equals(person) && pair.getCategory() == 'C'
                    && pair.getName().equals(name)) {
                library.getPurchasingDepartment().removePurchaseList(pair);
                orderList.remove(pair);
                return;
            }
        }
    }

    public void clearBBook(Person person) { //直接借到了B类书籍 清预定B的书
        Iterator<MyPair> iterator = orderList.iterator();
        while (iterator.hasNext()) {  //将B的预定全部杀掉
            MyPair pair = iterator.next();
            if (pair.getPerson().equals(person) && pair.getCategory() == 'B') {
                library.getPurchasingDepartment().removePurchaseList(pair);
                iterator.remove();
            }
        }
    }

    public void orderNewBook(Person person, char category, String name) { //成功or失败
        if (person.canOrderBook(category, name)) {
            buffer.add(new MyPair(person, category, name));
            person.addOrderedBookTime();
            if (!library.hasRemainBook(category, name)) { //若无剩本
                library.getPurchasingDepartment().addPurchaseList(category, name, person);
            }
        }
    }

    public void sendBook(Person person, Book book) {
        school1O = person.getSchoolName();
        school2O = book.getSchoolName();
        if (school2O.equals(school1O)) {
            System.out.println("[" + LibrarySystem.getTime() + "] ordering librarian lent "
                    + book.getSchoolName() + "-" + book.getCategory() + "-" + book.getName() +
                    " to " + person.getSchoolName() + "-" + person.getId());
            System.out.println("[" + LibrarySystem.getTime() + "] " +
                    person.getSchoolName() + "-" + person.getId() + " borrowed "
                    + book.getSchoolName() + "-" + book.getCategory() + "-" + book.getName() +
                    " from ordering librarian");
            System.out.println("(State) [" + LibrarySystem.getTime() + "] " +
                    book.getCategory() + "-" + book.getName()
                    + " transfers from InOwnSchool to InOwnStudentHand");
            person.addBook(book);
        }
    }
}
