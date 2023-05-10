import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BatchLoader {
    HashMap<Integer, Map<Integer, Integer>> graph;
    DynamicShortestPath dynamicShortestPath;

    public BatchLoader(String[] args) {

    }

    public void loadFromStdio() {
        loadFromStream(System.in);
    }

    public void loadFromStream(InputStream stream) {
        Scanner scanner = new Scanner(stream);

        loadGraph(scanner);

        this.dynamicShortestPath = new DynamicShortestPath(graph);
        this.dynamicShortestPath.initialize(0);

        loadAndPerformOperations(scanner);
    }

    public void loadGraph(Scanner scanner) {
        int numVertices = scanner.nextInt();
        int numEdges = scanner.nextInt();
        this.graph = new HashMap<>();

        for (int i = 0; i < numVertices; i++) {
            graph.put(i, new HashMap<>());
        }

        for (int i = 0; i < numEdges; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int weight = scanner.nextInt();
            graph.get(u).put(v, weight);
        }
    }

    public void loadAndPerformOperations(Scanner scanner) {
        int numOperations = scanner.nextInt();

        for (int i = 0; i < numOperations; i++) {
            String operation = scanner.next();
            int u = scanner.nextInt();
            int v = scanner.nextInt();

            switch (operation) {
                case "insert" -> {
                    int weight = scanner.nextInt();
                    dynamicShortestPath.insertEdge(u, v, weight);
                }
                case "delete" -> dynamicShortestPath.deleteEdge(u, v);
                default -> System.out.println("Invalid operation: " + operation);
            }
        }
    }
}
