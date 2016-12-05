import java.util.Scanner;

public class Main {
    /**
     * Driver
     */
    public static void main(String[] args) {
        // instantiate a new database
        Db db = new Db();
        // create a scanner
        Scanner sc = new Scanner(System.in);
        // delimit by space
        sc.useDelimiter("\\s+");
        String line; // [cmd] [key] [value]
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            // tokenize line into String array
            String[] tokens = line.split("\\s+");
            // get the command
            String cmd = tokens[0];
            String name;
            Integer value;
            try {
                // case sensitive
                switch (cmd.toLowerCase()) {
                    case "get":
                        name = tokens[1];
                        if (db.get(name) != null) {
                            System.out.printf("%s\n", db.get(name));
                        } else {
                            System.out.printf("%s\n", "NULL");
                        }
                        break;
                    case "set":
                        name = tokens[1];
                        value = Integer.parseInt(tokens[2]);
                        db.set(name, value);
                        break;
                    case "unset": // UNSET is treated as if a SET command setting a name to null value.
                        name = tokens[1];
                        db.set(name, null);
                        break;
                    case "numequalto":
                        value = Integer.parseInt(tokens[1]);
                        System.out.println(db.getCount(value));
                        break;
                    case "begin":
                        db.begin();
                        break;
                    case "rollback":
                        if (!db.rollBack()) {
                            System.out.printf("%s\n", "No Transaction");
                        }
                        break;
                    case "commit":
                        if (!db.commit()) {
                            System.out.printf("%s\n", "No Transaction");
                        }
                        break;
                    case "end": // END : terminates database (eliminates all variables stored)
                        return;
                    case "":
                        break;
                    default:
                        System.out.printf("%s: %s\n", "Invalid Command", cmd);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format: " + line );
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Possibly missing operand: " + line );
            }
        }
        sc.close();
    }
}
