package src;

import src.ArrangingLibrarian;
import src.Book;
import src.BorrowReturningLibrarian;

import java.util.HashMap;

public class Library {
    private final String schoolName;
    private final ArrangingLibrarian arrangingLibrarian;
    private final BorrowReturningLibrarian borrowReturningLibrarian;
    private final LogisticsDivision logisticsDivision;
    private final OrderingLibrarian orderingLibrarian;
    private final Machine machine;
    private final Shelf shelf; //记录书架上的书 拿类和名来查找
    private final PurchasingDepartment purchasingDepartment;
    private final HashMap<Book, Integer> booksShadow;
    //记录所有存在的书 预定的时候在里面找到对应的书 然后往预定清单上写入人员
    // 再斟酌斟酌数据结构！！！如何得到不存在书架上的书 并进行预定？ 保留一个记录即可 随便一本书的引用应当都可以

    public Library(String schoolName) {
        this.schoolName = schoolName;
        this.purchasingDepartment = new PurchasingDepartment(this);
        this.arrangingLibrarian = new ArrangingLibrarian(this);
        this.borrowReturningLibrarian = new BorrowReturningLibrarian(this);
        this.logisticsDivision = new LogisticsDivision(this);
        this.orderingLibrarian = new OrderingLibrarian(this);
        this.machine = new Machine(this);
        this.shelf = new Shelf();
        this.booksShadow = new HashMap<>();
    }

    public PurchasingDepartment getPurchasingDepartment() {
        return purchasingDepartment;
    }

    public void addBooksShadow(Book book, int num) { //这里的Book用一个没有意义的引用即可
        booksShadow.put(book, num);
    }

    public void deleteBooksShadow(Book book) { //丢失时如此做 注意丢失的书来源
        if (booksShadow.containsKey(book)) {
            booksShadow.put(book, booksShadow.get(book) - 1);
        }
    }

    public ArrangingLibrarian getArrangingLibrarian() {
        return arrangingLibrarian;
    }

    public boolean hasRemainBook(char category, String name) {
        Book book = new Book(category, name, schoolName);
        return booksShadow.containsKey(book) && booksShadow.get(book) > 0;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public BorrowReturningLibrarian getBorrowReturningLibrarian() {
        return borrowReturningLibrarian;
    }

    public LogisticsDivision getLogisticsDivision() {
        return logisticsDivision;
    }

    public OrderingLibrarian getOrderingLibrarian() {
        return orderingLibrarian;
    }

    public Machine getMachine() {
        return machine;
    }

    public Shelf getShelf() {
        return shelf;
    }

}
