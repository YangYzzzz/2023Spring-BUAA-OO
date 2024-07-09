package src;

import src.Book;
import src.Command;
import src.Library;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LibrarySystem { //归还书籍未完成，整体流程启动未完成，继续优化！
    private static final Pattern BOOK_COMMAND = Pattern.compile("([ABC])-(\\d{4}) (\\d+) ([NY])");
    private static final Pattern PERSON_COMMAND =
            Pattern.compile("\\[(\\d{4}-\\d{2}-\\d{2})\\] (.+)-(.+) ([a-z]+) ([ABC])-(\\d{4})");
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final ArrayList<Library> LIBRARIES = new ArrayList<>();
    private static final ArrayList<Person> PEOPLE = new ArrayList<>();
    private static final ArrayList<MyPair> PREPARE_LIST = new ArrayList<>();
    private static int month = 1;
    private static int day = 0;

    public static void addPrepareList(char category, String name, Person person) {
        PREPARE_LIST.add(new MyPair(person, category, name));
    }

    public static void exePrepareList() { //一天结束时 遍历
        for (MyPair pair : PREPARE_LIST) { //对每一条指令几种可能 1 这个人不可以借这本书 pass; 2 可以从外校借书，从外校借；3 本校预定
            Person person = pair.getPerson();
            char category = pair.getCategory();
            String name = pair.getName();
            Library library = getLibrary(person.getSchoolName()); //先校级借阅
            if (person.canBorrowBook(pair.getCategory(), pair.getName(),
                    pair.getPerson().getBooks())) { //有问题
                if (canGetBookFromOutside(person, library, category, name) == 2) { //能借的话已经在之中完成了借
                    library.getOrderingLibrarian().orderBook(person, category, name);
                }
            }
        }
        PREPARE_LIST.clear();
        clearAllFutureBooks();
    }

    public static int canGetBookFromOutside(Person person, Library library,
                                            char category, String name) {
        if (!person.canBorrowBook(category, name, person.getFutureBooks())) {
            return 0;
        }
        for (Library library1 : LIBRARIES) {
            if (!library1.equals(library)) {
                Book book;
                if ((book = library1.getShelf().getBookOutside(category, name)) != null) {
                    library1.getShelf().removeBook(book);
                    library1.getPurchasingDepartment().addCarryOut(book, library); //两面注入
                    if (category == 'B') {
                        library.getOrderingLibrarian().clearBBook(person);
                    } else {
                        library.getOrderingLibrarian().clearCBook(person, name);
                    }
                    library.getPurchasingDepartment().addOrderOutsideList(person, category, name);
                    person.addFutureBooks(book); //添加到未来书单中
                    return 1;
                }
            }
        }
        return 2;
    }

    public static void clearAllFutureBooks() {
        for (Person person : PEOPLE) {
            person.clearFutureBooks();
        }
    }

    public static Library getLibrary(String schoolName) {
        for (Library library : LIBRARIES) {
            if (library.getSchoolName().equals(schoolName)) {
                return library;
            }
        }
        return null;
    }

    public static void updateTime() { //[2023-01-01] 至 [2023-12-31]
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
        } else {
            if (day == 28) {
                day = 1;
                month++;
            } else {
                day++;
            }
        }
    }

    public static Person getPerson(String id, String schoolName) { //没有便创建
        for (Person person : PEOPLE) {
            if (person.getId().equals(id) && person.getSchoolName().equals(schoolName)) {
                return person;
            }
        }
        Person person = new Person(id, getLibrary(schoolName), schoolName); //将对应的学校属性置入
        PEOPLE.add(person);
        return person;
    }

    public static ArrayList<Command> parseCmd() {
        int t = SCANNER.nextInt(); //有多少所学校
        SCANNER.nextLine();
        for (int j = 0; j < t; j++) {
            String schoolName = SCANNER.next();
            Library library = new Library(schoolName);
            LIBRARIES.add(library);
            int n = SCANNER.nextInt();
            SCANNER.nextLine();
            for (int i = 0; i < n; i++) {
                String bookCmd = SCANNER.nextLine();
                Matcher matcher = BOOK_COMMAND.matcher(bookCmd);
                if (matcher.find()) {
                    char category = matcher.group(1).charAt(0);
                    String name = matcher.group(2);
                    int num = Integer.parseInt(matcher.group(3));
                    library.addBooksShadow(new Book(category, name, schoolName,
                            matcher.group(4)), num);
                    for (int k = 0; k < num; k++) {
                        library.getShelf().addBook(new Book(category, name, schoolName,
                                matcher.group(4)));
                    }
                }
            }
        }
        int m = SCANNER.nextInt();
        SCANNER.nextLine();
        ArrayList<Command> commands = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            String personCmd = SCANNER.nextLine();
            Matcher matcher = PERSON_COMMAND.matcher(personCmd);
            if (matcher.find()) {
                Operation operation = null;
                switch (matcher.group(4)) {
                    case "borrowed":
                        operation = Operation.BORROW;
                        break;
                    case "smeared":
                        operation = Operation.SMEAR;
                        break;
                    case "lost":
                        operation = Operation.LOST;
                        break;
                    case "returned":
                        operation = Operation.RETURN;
                        break;
                    default:
                        break;
                }
                commands.add(new Command(matcher.group(1), matcher.group(3),
                        matcher.group(5).charAt(0), matcher.group(6), operation, matcher.group(2)));
            }
        }
        return commands;
    }

    public static void cleanAllOrderedBookTime() {
        for (Person person : PEOPLE) {
            person.clearOrderedBookTime();
        }
    }

    public static String getTime() {
        return 2023 + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
    }

    public static void receiveAllBook() {
        for (Library library : LIBRARIES) {
            library.getPurchasingDepartment().receiveBook();
        }
    }

    public static void deliverAllBook() {
        for (Library library : LIBRARIES) {
            library.getPurchasingDepartment().deliverBook();
        }
    }

    public static void sendOutsideAllBook() {
        for (Library library : LIBRARIES) {
            library.getPurchasingDepartment().sendCarryOut();
        }
    }

    public static void arrangeAllBook() {
        System.out.println("[" + getTime() + "] arranging librarian arranged all the books");
        for (Library library : LIBRARIES) {
            library.getArrangingLibrarian().arrangeBook();
        }
    }

    public static void purchaseAllBook() {
        for (Library library : LIBRARIES) {
            library.getPurchasingDepartment().buyBook();
        }
    }

    public static void successAllOrder() {
        for (Library library : LIBRARIES) {
            library.getOrderingLibrarian().successOrder();
        }
    }

    public static void main(String[] args) { //从202310起开始运行
        ArrayList<Command> commands = parseCmd();
        int pointer = 0; //指示运行到第几条指令
        int cnt = 0; //用来判断第几天需要收拾
        int len = commands.size();
        while (pointer < len) {
            //System.out.println(pointer);
            updateTime();
            cleanAllOrderedBookTime(); //清除所有人标记
            receiveAllBook(); //接受发放从外校过来的书
            deliverAllBook();
            if (cnt % 3 == 0) {
                purchaseAllBook(); //买书
                arrangeAllBook(); //整理书
            }
            cnt++;
            while (pointer < len && commands.get(pointer).getTime().equals(getTime())) {
                Command command = commands.get(pointer);
                Person person = getPerson(command.getPersonId(), command.getSchoolName());
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
            exePrepareList();
            successAllOrder();
            sendOutsideAllBook();
        }
    }
}
