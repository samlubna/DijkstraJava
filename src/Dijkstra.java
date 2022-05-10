import java.io.File;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public final class Dijkstra {
    // the priority queue
    private static HeapQueue queue;
    // The node size
    private static int size = 0;
    // The actual array capacity
    private static int capacity = 0;
    // the labels for the locations in the graph
    private static String [] labels;
    // the adjacency matrix
    private static int [][] matrix;
    // An input buffer in the form of a dynamic array.
    private final static ArrayList<String> inputStream = new ArrayList<>();
    // A max value denoting infinity
    public static int MAX_VALUE = 1000;
    // A path matrix to store the weights and the parents of a node
    private static int [][] path;
    // A visited matrix to keep track of the nodes
    private static int [] visited;
    // A private constructor as this is a utility class
    private  Dijkstra () {}
    /*
    * This function accepts a file path for the input data which
    * is used to form the graph. It employs some rudimentary validation
    * logic to ensure that the file abides by the following structure:
    * N (Is an integer that denotes the number of nodes within the graph)
    *
    * The following N lines are treated as Strings which makeup the labels for the nodes.
    * Label - 1
    * Label - 2
    * Label - 3
    * ETC.
    *
    * M (Is an integer for the number of links within the graph)
    *
    * The following M entries are comma-separated tuples connecting v1 to v2 with wight w.
    * v1 and v2 are the indexes of the nodes according to their appearance in the label section
    * v1,v2,w1
    * v2,v3,w2
    * ETC
    *
    * Example input
    * ^ Not part of the input scheme,
    *   provided only as a reference on how to read said input.
    *   Remember to remove these annotations when copying the example.
    * 5   (N)^
    * A   (1)^
    * B   (2)^
    * C   (3)^
    * D   (4)^
    * E   (5)^
    * 7   (M)^
    * 1,2,6 (read as A is connected to B with weight 6)^
    * 1,4,1 (read as A is connected to D with wight 1)^
    * 2,4,2 (read as B is connected to D with weight 2)^
    * 2,5,2 (read as B is connected to E with weight 2)^
    * 5,4,1 (read as E is connected D with weight 1)^
    * 5,3,5 (read as E is connected to C with weight 5)^
    * 3,2,5 (read as C is connected to B with weight 5)^
    *
    *
    * */
    public static void readFile (String filePath) {
        // Clear the input stream
        inputStream.clear();
        try {
            // Start reading the file
            File inputFile = new File(filePath);
            Scanner reader =  new Scanner(inputFile);
            // Nodes is the number of locations in the graph
            int nodes;
            // Links is the number of connections between any two given nodes
            int links = 0;
            // Input validation is done alongside file input.
            if (reader.hasNext())
                nodes = Integer.parseInt(reader.nextLine());
            else throw new InputMismatchException("Empty file");
            // Read in all the locations
            for (int i = 0; i < nodes; i++) {
                if (!reader.hasNext()) break;
                inputStream.add(reader.nextLine());
            }
            // Check for inconsistencies
            if (inputStream.size() != nodes)
                throw new InputMismatchException("The number of nodes isn't correct");
            // Read in the number of links
            if (reader.hasNext())
                links = Integer.parseInt(reader.nextLine());
            for (int i = 0; i < links; i++) {
                if (!reader.hasNext()) break;
                inputStream.add(reader.nextLine());
            }
            // Check if the numbers match all the nodes and links combined
            if (inputStream.size() != nodes+links)
                throw new InputMismatchException("The number of links isn't correct");
            // Validating the links for the following format A,B,W - > A is linked to B with wight W
            for (int i = 0; i < links; i++) {
                String [] l = inputStream.get(nodes + i).split(",");
                if (l.length != 3) throw new InputMismatchException("Malformed link");
                for (int j = 0; j < 2; j++) {
                    int value = Integer.parseInt(l[j]);
                    if (value < 1 || value > nodes)
                        throw new InputMismatchException("Link index out of range");
                } Integer.parseInt(l[2]);
            }
            // Allocating memory for the acquired and validated data.
            // Endeavouring to use memory pooling where possible
            if (capacity < nodes) {
                labels = new String[nodes];
                queue = new HeapQueue(nodes);
                matrix = new int[nodes][nodes];
                path = new int[nodes][2];
                visited = new int[nodes];
                capacity = nodes;
            }
            size = nodes;
            // Storing the names
            for (int i = 0; i < size; i++)
                labels[i] = inputStream.get(i);
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    // -1 means not connected.
                    matrix[i][j] = -1;
            // Fill the adjacency matrix
            for (int i = 0; i < links; i++) {
                String [] temp = inputStream.get(i + nodes).split(",");
                int a = Integer.parseInt(temp[0])-1;
                int b = Integer.parseInt(temp[1])-1;
                int w = Integer.parseInt(temp[2]);
                // the connections are bidirectional.
                matrix [a][b] = w;
                // Comment out the following to make it unidirectional.
                matrix [b][a] = w;
            }
            reader.close();
            System.out.println("File Loaded Successfully");
        } catch (Exception e) {
            System.out.println("Error : "+e.getMessage());
        }
    }
    /*
    * A utility method to print the adjacency matrix
    * */
    public static void printAdj () {
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                System.out.print(i + " - "+labels[i] + "'s  Neighbours --> ");
                for (int j = 0; j < size; j++)
                    System.out.printf("(%s,%d) ",labels[j], matrix[i][j]);
                System.out.println();
            }
        } else System.out.println("Nothing to print");
    }
    /*
    * A utility function to print the shortest path between two nodes
    * */
    private static void printPath (int from, int to) {
        if (to == from)
            System.out.print(labels[from] + " ----> ");
        else {
            printPath(from, path[to][0]);
            System.out.print("(" + labels[to] + ',' + path[to][1] + ")" + " ----> ");
        }
    }
    /*
    * Dijkstra's algorithm constructs a minimum spanning tree, which is a trimmed
    * version of the original graph with the minimum possible edge weights, to construct
    * the shortest path between a starting point and all of its direct and indirect neighbours.
    * Online resource: https://www.youtube.com/watch?v=GazC3A4OQTE&t=5s
    * */
    public static void run (int from, int to) throws IllegalAccessException {
        if (queue == null) {
            System.out.println("No input file was provided");
            return;
        }
        // clear the queue for the next iteration
        queue.clear();
        // Initialize the visited array
        for (int i = 0; i< size; i++)
            visited[i] = 0;
        // initializing the path
        path[from][0] = from;
        path[from][1] = 0;
        for (int i = 0;  i < size; i++ )
            if (i != from) {
                path[i][1] = MAX_VALUE;
                path[i][0] = -1;
            }
        queue.insert(0, from);
        // Running the algorithm
        while (!queue.isEmpty()) {
            int current = queue.extractMin();
            if (current == to) break;
            // Is it visited.
            if (visited[current] == 0) {
                visited[current] = 1;
                for (int i = 0; i < size; i++) {
                    if (visited[i] == 0 && matrix[current][i] != -1) {
                        int new_weight = path[current][1] + matrix[current][i];
                        // Update path
                        if (new_weight <= path[i][1]) {
                            path[i][1] = new_weight;
                            path[i][0] = current;
                            queue.insert(path[i][1],i);
                        }
                     }
                   }
                }
             }
    }
    public static void getPath (int from, int to) {
        if (path[to][0] == -1)
            System.out.printf("Can't reach %s from %s%n", labels[to],labels[from]);
        else {
            printPath(from, to);
            System.out.printf("%s%d%c%s\n",ANSI.ESC, 6, ANSI.LEFT, ANSI.ClEAR_LINE);
        }
    }
    public static int getSize () {
        return size;
    }
}

