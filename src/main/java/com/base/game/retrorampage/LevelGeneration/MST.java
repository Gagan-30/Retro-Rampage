package com.base.game.retrorampage.LevelGeneration;

import org.locationtech.jts.geom.Coordinate;
import java.util.*;

public class MST {
    private Map<Coordinate, Coordinate> parent = new HashMap<>();

    public MST(List<Coordinate[]> triangulationEdges) {
        // Constructor if needed
    }

    public List<Edge> generateMST(List<Coordinate[]> edges) {
        List<Edge> mstEdges = new ArrayList<>();

        // Initialize each node's parent to itself
        for (Edge edge : edges) {
            parent.putIfAbsent(edge.getStart(), edge.getStart());
            parent.putIfAbsent(edge.getEnd(), edge.getEnd());
        }

        // Sort edges based on their weight
        Collections.sort(edges);

        // Kruskal's algorithm
        for (Edge edge : edges) {
            Coordinate root1 = find(edge.getStart());
            Coordinate root2 = find(edge.getEnd());

            if (!root1.equals(root2)) {
                union(root1, root2);
                mstEdges.add(edge); // Add edge to MST
            }
        }

        return mstEdges;
    }

    private Coordinate find(Coordinate coordinate) {
        if (!parent.get(coordinate).equals(coordinate)) {
            parent.put(coordinate, find(parent.get(coordinate)));
        }
        return parent.get(coordinate);
    }

    private void union(Coordinate coordinate1, Coordinate coordinate2) {
        Coordinate root1 = find(coordinate1);
        Coordinate root2 = find(coordinate2);
        parent.put(root1, root2);
    }
}
