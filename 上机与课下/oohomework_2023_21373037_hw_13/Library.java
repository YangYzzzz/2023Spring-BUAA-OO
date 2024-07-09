import java.util.ArrayList;

public class Library {
    private final ArrangingLibrarian arrangingLibrarian;
    private final BorrowReturningLibrarian borrowReturningLibrarian;
    private final LogisticsDivision logisticsDivision;
    private final OrderingLibrarian orderingLibrarian;
    private final Machine machine;
    private final ArrayList<Person> people;
    private final Shelf shelf; //记录书架上的书 拿类和名来查找
    private ArrayList<Book> booksShadow = new ArrayList<>();
    //记录所有存在的书 预定的时候在里面找到对应的书 然后往预定清单上写入人员
    // 再斟酌斟酌数据结构！！！如何得到不存在书架上的书 并进行预定？ 保留一个记录即可 随便一本书的引用应当都可以
    private int month;
    private int day;

    public Library() {
        this.arrangingLibrarian = new ArrangingLibrarian(this);
        this.borrowReturningLibrarian = new BorrowReturningLibrarian(this);
        this.logisticsDivision = new LogisticsDivision(this);
        this.orderingLibrarian = new OrderingLibrarian(this);
        this.machine = new Machine(this);
        this.people = new ArrayList<>();
        this.shelf = new Shelf();
        this.booksShadow = new ArrayList<>();
        this.month = 1;
        this.day = 0;
    }

    public void updateTime() { //[2023-01-01] 至 [2023-12-31]
        if (month == 1 || month == 3 || month == 5 || month == 7
                || month == 8 || month == 10 || month == 12) {
            if (day == 31) {
                day = 1;
                month++;
            } else {
                day++;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            if (day == 30) {
                day = 1;
                month++;
            } else {
                day++;
            }
        } else  {
            if (day == 28) {
                day = 1;
                month++;
            } else {
                day++;
            }
        }
    }

    public String getTime() {
        return 2023 + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
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

    public ArrayList<Book> getBooksShadow() {
        return booksShadow;
    }

    public void addBooksShadow(Book book) {
        booksShadow.add(book);
    }

    public Person getPerson(String id) { //没有便创建
        for (Person person : people) {
            if (person.getId().equals(id)) {
                return person;
            }
        }
        Person person = new Person(id, this);
        people.add(person);
        return person;
    }

    public void cleanAllOrderedBookTime() {
        for (Person person : people) {
            person.clearOrderedBookTime();
        }
    }

    public void run(ArrayList<Command> commands) { //从202310起开始运行
        int pointer = 0; //指示运行到第几条指令
        int cnt = 0; //用来判断第几天需要收拾
        int len = commands.size();
        while (pointer < len) {
            //System.out.println(pointer);
            updateTime();
            cleanAllOrderedBookTime(); //清除所有人标记
            if (cnt % 3 == 0) {
                arrangingLibrarian.arrangeBook();
            }
            cnt++;
            while (pointer < len && commands.get(pointer).getTime().equals(getTime())) {
                Command command = commands.get(pointer);
                Person person = getPerson(command.getPersonId());
                switch (command.getOperation()) {
                    case BORROW:
                        person.borrowBook(command.getBookCategory(),
                                command.getBookName());
                        break;
                    case SMEAR:
                        person.smearBook(command.getBookCategory(), command.getBookName());
                        break;
                    case LOST:
                        person.lostBook(command.getBookCategory(),
                                command.getBookName());
                        break;
                    case RETURN:
                        person.returnBook(command.getBookCategory(),
                                command.getBookName());
                        break;
                    default:
                        break;
                }
                pointer++;
            }
        }
    }
}
