package com.base.game.retrorampage.LevelGeneration;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Map;

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
        UnionFind unionFind = new UnionFind(edges.size());
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

        public UnionFind(int size) {
        }

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
