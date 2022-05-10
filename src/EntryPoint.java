import java.util.Scanner;

public class EntryPoint {
    public static String prompt (String message) {
        return ANSI.START_COLOR+message+ANSI.END_COLOR ;
    }
    public static void clearLines (int l) {
        System.out.print(ANSI.RESET_CURSOR);
        for (int i = 0; i < l; i++)
            System.out.print(ANSI.ESC+1+ANSI.UP+ANSI.ClEAR_LINE);
    }
    public static void main(String[] args) throws IllegalAccessException {
        String [] labels = {
                "\ta - Load file",
                "  b - Find path between points ",
                "q - quit"
        };
        System.out.println("---------------------------------------------------------------------------------");
        System.out.printf("\tAlgorithm : %s\tProgram Author : %s\n","Dijkstra's Shortest Path", "Samuel Lubna");
        System.out.println("---------------------------------------------------------------------------------");
        for (String l : labels)
            System.out.printf("%s    ",l);
        System.out.println();
        System.out.print(prompt("Choose an option: "));
        // The main loop
        boolean run = true;
        String userInput;
        int prevLines = 1;
        Scanner input = new Scanner(System.in);
        while (run) {
            userInput = input.next();
            switch (userInput) {
                case "a":
                    clearLines(prevLines);
                    System.out.print(prompt("Enter File name: "));
                    userInput = input.next();
                    System.out.print(ANSI.ESC+ANSI.UP+ANSI.RESET_CURSOR+ANSI.ClEAR_LINE);
                    System.out.print(prompt("Loading..."));
                    System.out.print(ANSI.RESET_CURSOR);
                    Dijkstra.readFile(userInput);
                    System.out.print(prompt("Choose an option: "));
                    prevLines = 2;
                    break;
                case "b":
                    clearLines(prevLines);
                    if (Dijkstra.getSize() == 0)
                        System.out.println("No file provided");
                    else {
                        Dijkstra.printAdj();
                        int to, from, limit = Dijkstra.getSize();
                        while (true) {
                            System.out.print(prompt("From A to B as A,B: "));
                            String [] v = input.next().split(",");
                            System.out.print(ANSI.ESC+ANSI.UP+ANSI.RESET_CURSOR+ANSI.ClEAR_LINE);
                            System.out.print(prompt("Computing..."));
                            System.out.print(ANSI.RESET_CURSOR);
                            try {
                                 from = Integer.parseInt(v[0]);
                                 to = Integer.parseInt(v[1]);
                                 if (to < 0 || from < 0 || to >= limit || from >= limit)
                                     throw new IllegalArgumentException("Index must be between 0 and "+limit);
                                 Dijkstra.run(from,to);
                                 Dijkstra.getPath(from,to);
                            } catch (Exception e) {
                                System.out.println("Error "+e.getMessage());
                            }
                            System.out.print(prompt("Again? (y): "));
                            String ch = input.next();
                            if (ch.charAt(0) != 'y') {
                                clearLines(2 + Dijkstra.getSize());
                                prevLines = 1;
                                break;
                            } clearLines(2);
                        }
                    }
                    System.out.print(prompt("Choose an option: "));
                    break;
                case "q":
                    clearLines(prevLines+5);
                    run = false;
                    input.close();
                    break;
                default:
                    clearLines(prevLines);
                    System.out.print("Error: Invalid input\n");
                    System.out.print(prompt("Choose an option: "));
                    prevLines = 2;
            }
        }
    }
}

