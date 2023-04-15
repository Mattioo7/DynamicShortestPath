import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class DSPTest {
    public static void main(String[] args) {
        int numVertices = 10000;
        int numEdges = 20000;

        Map<Integer, Map<Integer, Integer>> graph = generateRandomGraph(numVertices, numEdges);
        DynamicShortestPath dsp = new DynamicShortestPath(graph);

        // Measure the time taken for initialization
        long startTime = System.nanoTime();
        dsp.initialize(0);
        long endTime = System.nanoTime();
        System.out.println("Initialization time: " + (endTime - startTime) / 1e6 + " ms");

        // Measure the time taken for edge insertions
        List<Edge> newEdges = generateRandomEdges(numVertices, 1000);
        startTime = System.nanoTime();
        for (Edge edge : newEdges) {
            dsp.insertEdge(edge.u, edge.v, edge.weight);
        }
        endTime = System.nanoTime();
        System.out.println("Edge insertions time: " + (endTime - startTime) / 1e6 + " ms");

        // Measure the time taken for edge deletions
        List<Edge> existingEdges = getExistingEdges(graph, 1000);
        startTime = System.nanoTime();
        for (Edge edge : existingEdges) {
            dsp.deleteEdge(edge.u, edge.v);
        }
        endTime = System.nanoTime();
        System.out.println("Edge deletions time: " + (endTime - startTime) / 1e6 + " ms");
    }

    static class Edge {
        int u, v, weight;

        Edge(int u, int v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }
    }

    private static Map<Integer, Map<Integer, Integer>> generateRandomGraph(int numVertices, int numEdges) {
        Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();
        for (int i = 0; i < numVertices; i++) {
            graph.put(i, new HashMap<>());
        }

        int count = 0;
        while (count < numEdges) {
            int u = ThreadLocalRandom.current().nextInt(numVertices);
            int v = ThreadLocalRandom.current().nextInt(numVertices);
            if (u == v || graph.get(u).containsKey(v)) {
                continue;
            }
            int weight = ThreadLocalRandom.current().nextInt(1, 101);
            graph.get(u).put(v, weight);
            count++;
        }

        return graph;
    }

    private static List<Edge> generateRandomEdges(int numVertices, int numEdges) {
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < numEdges; i++) {
            int u = ThreadLocalRandom.current().nextInt(numVertices);
            int v = ThreadLocalRandom.current().nextInt(numVertices);
            int weight = ThreadLocalRandom.current().nextInt(1, 101);
            edges.add(new Edge(u, v, weight));
        }
        return edges;
    }

    private static List<Edge> getExistingEdges(Map<Integer, Map<Integer, Integer>> graph, int numEdges) {
        List<Edge> edges = new ArrayList<>();
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : graph.entrySet()) {
            int u = entry.getKey();
            for (Map.Entry<Integer, Integer> subEntry : entry.getValue().entrySet()) {
                int v = subEntry.getKey();
                int weight = subEntry.getValue();
                edges.add(new Edge(u, v, weight));
            }
        }

        Collections.shuffle(edges);
        return edges.subList(0, Math.min(numEdges, edges.size()));
    }
}