package com.base.game.retrorampage.LevelGeneration;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Geometry;

import java.util.*;

public class GraphManager {
    private GeometryFactory geometryFactory = new GeometryFactory();
    private List<Edge> edges;

    public List<Edge> triangulateRooms(List<Coordinate> roomCenters) {
        DelaunayTriangulationBuilder builder = new DelaunayTriangulationBuilder();
        builder.setSites(roomCenters);
        Geometry triangulated = builder.getTriangles(geometryFactory);

        edges = new ArrayList<>(); // Initialize the edges list
        for (int i = 0; i < triangulated.getNumGeometries(); i++) {
            Geometry triangle = triangulated.getGeometryN(i);
            Coordinate[] coordinates = triangle.getCoordinates();
            // Each triangle will have 3 vertices and the first is repeated at the end
            for (int j = 0; j < coordinates.length - 1; j++) {
                Coordinate start = coordinates[j];
                Coordinate end = coordinates[(j + 1) % coordinates.length];
                edges.add(new Edge(start, end));
            }
        }
        return edges;
    }

    public List<Edge> generateMST(List<Edge> edges) {
        // Implementing a simple version of Kruskal's algorithm for MST generation
        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>(edges);
        UnionFind unionFind = new UnionFind();
        List<Edge> mstEdges = new ArrayList<>();

        while (!edgeQueue.isEmpty() && mstEdges.size() < edges.size() - 1) {
            Edge edge = edgeQueue.poll();
            if (unionFind.find(edge.start) != unionFind.find(edge.end)) {
                unionFind.union(edge.start, edge.end);
                mstEdges.add(edge);
            }
        }
        return mstEdges;
    }

    // Utility class for Union-Find operations
    private static class UnionFind {
        private Map<Coordinate, Coordinate> parent = new HashMap<>();

        public Coordinate find(Coordinate x) {
            parent.putIfAbsent(x, x);
            if (!x.equals(parent.get(x))) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        public void union(Coordinate x, Coordinate y) {
            Coordinate xRoot = find(x);
            Coordinate yRoot = find(y);
            if (!xRoot.equals(yRoot)) {
                parent.put(xRoot, yRoot);
            }
        }
    }

    public List<Edge> reintroduceLoops(List<Edge> mstEdges, List<Edge> allEdges, double loopFactor) {
        // loopFactor should be between 0 and 1, representing the percentage of additional edges to add

        // Copy the MST edges to avoid modifying the original list
        List<Edge> updatedEdges = new ArrayList<>(mstEdges);

        // Create a copy of all edges and shuffle it to get a random order
        List<Edge> shuffledAllEdges = new ArrayList<>(allEdges);
        Collections.shuffle(shuffledAllEdges, new Random());

        // Calculate the number of loops (additional edges) to add
        int loopsToAdd = (int)(loopFactor * mstEdges.size());

        // Iterate over the shuffled edges and add them if they don't already exist in the MST
        for (Edge edge : shuffledAllEdges) {
            if (loopsToAdd <= 0) {
                break; // Stop if we've added the desired number of loops
            }

            // Check if the edge is not already part of the MST
            if (!updatedEdges.contains(edge)) {
                updatedEdges.add(edge); // Add the edge to introduce a loop
                loopsToAdd--; // Decrement the number of loops left to add
            }
        }

        // Return the updated list of edges with the loops reintroduced
        return updatedEdges;
    }

    // Edge class to represent connections between points
    public static class Edge implements Comparable<Edge> {
        Coordinate start;
        Coordinate end;
        double weight;

        public Edge(Coordinate start, Coordinate end) {
            this.start = start;
            this.end = end;
            this.weight = start.distance(end);
        }

        // Getters might be necessary for other parts of your application
        public Coordinate getStart() {
            return start;
        }

        @Override
        public int compareTo(Edge other) {
            return Double.compare(this.weight, other.weight);
        }
    }

    // This method might be used to get the edges for drawing
    public List<Edge> getEdges() {
        return edges;
    }

}
