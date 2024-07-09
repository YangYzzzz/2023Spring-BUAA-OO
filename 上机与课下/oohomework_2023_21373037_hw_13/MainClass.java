import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    private static Pattern bookCommand = Pattern.compile("([ABC])-(\\d{4}) (\\d+)");
    private static Pattern personCommand =
            Pattern.compile("\\[(\\d{4}-\\d{2}-\\d{2})\\] (.+) ([a-z]+) ([ABC])-(\\d{4})");
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Library library = new Library();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String bookCmd = scanner.nextLine();
            Matcher matcher = bookCommand.matcher(bookCmd);
            if (matcher.find()) {
                char category = matcher.group(1).charAt(0);
                String name = matcher.group(2);
                int num = Integer.parseInt(matcher.group(3));
                library.addBooksShadow(new Book(category, name));
                for (int j = 0; j < num; j++) {
                    library.getShelf().addBook(new Book(category, name));
                }
            }
        }
        int m = scanner.nextInt();
        scanner.nextLine();
        ArrayList<Command> commands = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            String personCmd = scanner.nextLine();
            Matcher matcher = personCommand.matcher(personCmd);
            if (matcher.find()) {
                Operation operation = null;
                switch (matcher.group(3)) {
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
                commands.add(new Command(matcher.group(1), matcher.group(2),
                        matcher.group(4).charAt(0), matcher.group(5), operation));
            }
        }
        library.run(commands);
    }
}
