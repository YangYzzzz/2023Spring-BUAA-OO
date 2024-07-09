public class Command {
    private final String time;
    private final String personId;
    private final char bookCategory;
    private final String bookName;
    private final Operation operation;

    public Command(String time, String personId, char bookCategory,
                   String bookName, Operation operation) {
        this.time = time;
        this.personId = personId;
        this.bookCategory = bookCategory;
        this.bookName = bookName;
        this.operation = operation;
    }

    public String getTime() {
        return time;
    }

    public String getPersonId() {
        return personId;
    }

    public char getBookCategory() {
        return bookCategory;
    }

    public String getBookName() {
        return bookName;
    }

    public Operation getOperation() {
        return operation;
    }
}
