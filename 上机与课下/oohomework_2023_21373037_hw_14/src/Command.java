package src;

public class Command {
    private final String time;
    private final String personId;
    private final char bookCategory;
    private final String bookName;
    private final Operation operation;
    private final String schoolName;

    public Command(String time, String personId, char bookCategory,
                   String bookName, Operation operation, String schoolName) {
        this.schoolName = schoolName;
        this.time = time;
        this.personId = personId;
        this.bookCategory = bookCategory;
        this.bookName = bookName;
        this.operation = operation;
    }

    public String getSchoolName() {
        return schoolName;
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
