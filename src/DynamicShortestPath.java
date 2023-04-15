import java.util.*;

public class DynamicShortestPath {
    private Map<Integer, Map<Integer, Integer>> graph; // adjacency list
    private Map<Integer, Integer> dist; // shortest path distances
    private Map<Integer, Integer> parent; // parent pointers for shortest path tree
    private Set<Integer> affectedRegion; // affected region vertices

    public DynamicShortestPath(Map<Integer, Map<Integer, Integer>> graph) {
        this.graph = graph;
        this.dist = new HashMap<>();
        this.parent = new HashMap<>();
        this.affectedRegion = new HashSet<>();
    }

    public void initialize(int source) {
        for (int vertex : graph.keySet()) {
            dist.put(vertex, Integer.MAX_VALUE);
            parent.put(vertex, -1);
        }
        dist.put(source, 0);
        affectedRegion.add(source);
    }

    public void insertEdge(int u, int v, int weight) {
        graph.computeIfAbsent(u, k -> new HashMap<>()).put(v, weight);
        updateAffectedRegion(u, v, weight);
        recomputeDistances();
    }

    public void deleteEdge(int u, int v) {
        if (graph.get(u) != null) {
            graph.get(u).remove(v);
        }
        updateAffectedRegion(u, v, -1);
        recomputeDistances();
    }

    private void updateAffectedRegion(int u, int v, int weight) {
        if (dist.get(u) + weight < dist.get(v)) {
            affectedRegion.add(v);
        }
    }

    private void recomputeDistances() {
        PriorityQueue<Vertex> pq = new PriorityQueue<>();
        for (int vertex : affectedRegion) {
            pq.add(new Vertex(vertex, dist.get(vertex)));
        }
        affectedRegion.clear();

        while (!pq.isEmpty()) {
            Vertex current = pq.poll();
            int u = current.id;
            int d = current.dist;

            if (d > dist.get(u)) continue;

            for (Map.Entry<Integer, Integer> entry : graph.getOrDefault(u, new HashMap<>()).entrySet()) {
                int v = entry.getKey();
                int w = entry.getValue();

                if (dist.get(u) + w < dist.get(v)) {
                    dist.put(v, dist.get(u) + w);
                    parent.put(v, u);
                    pq.add(new Vertex(v, dist.get(v)));
                }
            }
        }
    }

    public Map<Integer, Integer> getShortestPathDistances() {
        return dist;
    }

    static class Vertex implements Comparable<Vertex> {
        int id;
        int dist;

        Vertex(int id, int dist) {
            this.id = id;
            this.dist = dist;
        }

        @Override
        public int compareTo(Vertex other) {
            return Integer.compare(this.dist, other.dist);
        }
    }
}
