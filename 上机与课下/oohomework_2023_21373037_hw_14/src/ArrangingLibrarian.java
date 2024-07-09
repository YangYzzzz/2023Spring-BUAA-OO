package src;

import src.Book;

import java.util.ArrayList;
import java.util.Iterator;

public class ArrangingLibrarian {
    private final ArrayList<Book> books;
    private final Library library;

    public ArrangingLibrarian(Library library) {
        this.books = new ArrayList<>();
        this.library = library;
    }

    public void collectBook() {
        for (Book book : library.getBorrowReturningLibrarian().getBooks()) {
            books.add(book);
        }
        library.getBorrowReturningLibrarian().getBooks().clear();
        for (Book book : library.getLogisticsDivision().getBooks()) {
            books.add(book);
        }
        library.getLogisticsDivision().getBooks().clear();
        for (Book book : library.getMachine().getBooks()) {
            books.add(book);
        }
        library.getMachine().getBooks().clear();
        for (Book book : library.getPurchasingDepartment().getBooks()) {
            books.add(book); //保证都是本图书管里的书
        }
        library.getPurchasingDepartment().getBooks().clear();
    }

    public void sendBook() {
        //派送算法 先按orderList遍历 寻找有了的书籍 若为B再清除orderList 要维护两个容器 对于书容器 删除即可
        ArrayList<MyPair> orderList = library.getOrderingLibrarian().getOrderList();
        for (int i = 0; i < orderList.size(); i++) {
            MyPair pair = orderList.get(i);
            if (!pair.isVis()) { //未被设置标记才可以进行操作
                for (int j = 0; j < books.size(); j++) {
                    Book book = books.get(j);
                    if (pair.getCategory() == book.getCategory() &&
                            pair.getName().equals(book.getName())) { //可以派发
                        library.getOrderingLibrarian().sendBook(pair.getPerson(), book); //派发
                        books.remove(book); //将书在收集管理员移除
                        pair.setVis(true); //标注该请求做完
                        if (book.getCategory() == 'B') { ////清除该人所有B类预定
                            for (MyPair pair1 : orderList) {
                                if (pair1.getPerson().equals(pair.getPerson()) &&
                                        pair1.getCategory() == 'B') {
                                    library.getPurchasingDepartment().removePurchaseList(pair1);
                                    pair1.setVis(true);
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        Iterator<MyPair> iterator = orderList.iterator();
        while (iterator.hasNext()) { //统一清除预定清单
            MyPair pair = iterator.next();
            if (pair.isVis()) {
                iterator.remove();
            }
        }
        for (Book book : books) { //将剩下的全部填到书架上
            library.getShelf().addBook(book);
        }
        books.clear();
    }

    public void arrangeBook() {
        collectBook();
        sendBook();
    }
}
