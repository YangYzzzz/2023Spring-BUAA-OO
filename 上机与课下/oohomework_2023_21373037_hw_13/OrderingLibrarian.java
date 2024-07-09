import java.util.ArrayList;
import java.util.Iterator;

public class OrderingLibrarian {
    private final ArrayList<Pair> orderList;
    private final Library library;

    public OrderingLibrarian(Library library) {
        this.orderList = new ArrayList<>();
        this.library = library;
    }

    public ArrayList<Pair> getOrderList() {
        return orderList;
    }

    public boolean hasSameBook(Person person, Book book) {
        for (Pair pair : orderList) {
            if (pair.getPerson().equals(person) && pair.getBook().equals(book)) {
                return true;
            }
        }
        return false;
    }

    public void clearBBook(Person person) { //直接借到了B类书籍 清预定B的书
        Iterator<Pair> iterator = orderList.iterator();
        while (iterator.hasNext()) {  //将B的预定全部杀掉
            Pair pair = iterator.next();
            if (pair.getPerson().equals(person) && pair.getBook().getCategory() == 'B') {
                iterator.remove();
            }
        }
    }

    public void orderBook(Person person, Book book) { //成功or失败
        if (person.canOrderBook(book)) {
            System.out.println("[" + library.getTime() + "] " + person.getId() + " ordered " +
                    book.getCategory() + "-" + book.getName() + " from ordering librarian");
            orderList.add(new Pair(person, book));
            person.addOrderedBookTime();
        }
    }

    public void sendBook(Person person, Book book) {
        System.out.println("[" + library.getTime() + "] " + person.getId() + " borrowed "
                + book.getCategory() + "-" + book.getName() + " from ordering librarian");
        person.addBook(book);
    }
}
