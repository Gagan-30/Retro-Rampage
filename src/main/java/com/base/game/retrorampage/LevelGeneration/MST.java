package com.base.game.retrorampage.LevelGeneration;

import org.locationtech.jts.geom.Coordinate;

import java.util.*;

public class MST {
    private Map<Coordinate, Coordinate> parent = new HashMap<>();

    // Assuming `triangulateRooms` method returns a list of Edges for simplicity
    private List<Edge> generateAndVisualizeMST(List<Edge> edges) {
        // Initialize each node's parent to itself
        for (Edge edge : edges) {
            parent.putIfAbsent(edge.start, edge.start);
            parent.putIfAbsent(edge.end, edge.end);
        }

        // Kruskal's algorithm to construct MST
        for (Edge edge : edges) {
            Coordinate root1 = find(edge.start);
            Coordinate root2 = find(edge.end);

            if (!root1.equals(root2)) {
                //CorridorConstructor.createWideCorridor(edge.start, edge.end, 10); // Visualize the corridor
                union(root1, root2);
            }
        }
        return edges;
    }

    // Find set with path compression
    private Coordinate find(Coordinate i) {
        if (!parent.get(i).equals(i)) {
            parent.put(i, find(parent.get(i)));
        }
        return parent.get(i);
    }

    // Union by rank (simplified as naive union for this example)
    private void union(Coordinate x, Coordinate y) {
        Coordinate xRoot = find(x);
        Coordinate yRoot = find(y);
        parent.put(xRoot, yRoot);
    }
}
