package com.base.game.retrorampage.LevelGeneration;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;

import java.util.*;

/**
 * Manages the creation and manipulation of graphs for level generation.
 */
public class GraphManager {
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private List<Edge> edges;

    /**
     * Triangulates the given room centers to form a graph of triangles.
     *
     * @param roomCenters The coordinates of room centers.
     * @return The list of edges representing the triangulated graph.
     */
    public List<Edge> triangulateRooms(List<Coordinate> roomCenters) {
        // Use Delaunay triangulation to generate triangles connecting room centers
        DelaunayTriangulationBuilder builder = new DelaunayTriangulationBuilder();
        builder.setSites(roomCenters);
        Geometry triangulated = builder.getTriangles(geometryFactory);

        edges = new ArrayList<>();
        // Convert triangles into edges
        for (int i = 0; i < triangulated.getNumGeometries(); i++) {
            Geometry triangle = triangulated.getGeometryN(i);
            Coordinate[] coordinates = triangle.getCoordinates();
            for (int j = 0; j < coordinates.length - 1; j++) {
                Coordinate start = coordinates[j];
                Coordinate end = coordinates[(j + 1) % coordinates.length];
                edges.add(new Edge(start, end));
            }
        }
        return edges;
    }

    /**
     * Generates a minimum spanning tree (MST) from the given edges using Kruskal's algorithm.
     *
     * @param edges The list of edges to generate the MST from.
     * @return The list of edges representing the MST.
     */
    public List<Edge> generateMST(List<Edge> edges) {
        // Priority queue for sorting edges by weight
        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>(edges);
        UnionFind unionFind = new UnionFind(); // Union-Find data structure
        List<Edge> mstEdges = new ArrayList<>();

        // Iterate through edges and build MST
        while (!edgeQueue.isEmpty() && mstEdges.size() < edges.size() - 1) {
            Edge edge = edgeQueue.poll(); // Get edge with minimum weight
            // Check if adding this edge forms a cycle
            if (unionFind.find(edge.start) != unionFind.find(edge.end)) {
                unionFind.union(edge.start, edge.end); // Union vertices
                mstEdges.add(edge); // Add edge to MST
            }
        }
        return mstEdges;
    }

    /**
     * Reintroduces loops into the MST edges to create additional connections.
     *
     * @param mstEdges   The list of edges representing the MST.
     * @param allEdges   The list of all edges available for reintroduction.
     * @param loopFactor The percentage of additional edges to add as loops.
     * @return The updated list of edges with reintroduced loops.
     */
    public List<Edge> reintroduceLoops(List<Edge> mstEdges, List<Edge> allEdges, double loopFactor) {
        List<Edge> updatedEdges = new ArrayList<>(mstEdges);
        List<Edge> shuffledAllEdges = new ArrayList<>(allEdges);
        Collections.shuffle(shuffledAllEdges, new Random());

        // Calculate the number of loops to add based on loopFactor
        int loopsToAdd = (int) (loopFactor * mstEdges.size());

        // Add additional edges as loops
        for (Edge edge : shuffledAllEdges) {
            if (loopsToAdd <= 0) {
                break;
            }
            if (!updatedEdges.contains(edge)) {
                updatedEdges.add(edge);
                loopsToAdd--;
            }
        }
        return updatedEdges;
    }

    /**
     * Retrieves the edges representing the graph.
     *
     * @return The list of edges representing the graph.
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * Utility class for Union-Find operations.
     */
    private static class UnionFind {
        private final Map<Coordinate, Coordinate> parent = new HashMap<>();

        // Find operation to determine the root of a vertex
        public Coordinate find(Coordinate x) {
            parent.putIfAbsent(x, x);
            if (!x.equals(parent.get(x))) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        // Union operation to merge two disjoint sets
        public void union(Coordinate x, Coordinate y) {
            Coordinate xRoot = find(x);
            Coordinate yRoot = find(y);
            if (!xRoot.equals(yRoot)) {
                parent.put(xRoot, yRoot);
            }
        }
    }

    /**
     * Represents an edge connecting two points in a graph.
     */
    public static class Edge implements Comparable<Edge> {
        Coordinate start;
        Coordinate end;
        double weight;

        /**
         * Constructs an edge between two points and calculates its weight.
         *
         * @param start The starting point of the edge.
         * @param end   The ending point of the edge.
         */
        public Edge(Coordinate start, Coordinate end) {
            this.start = start;
            this.end = end;
            this.weight = start.distance(end);
        }

        /**
         * Retrieves the starting point of the edge.
         *
         * @return The starting point.
         */
        public Coordinate getStart() {
            return start;
        }

        /**
         * Compares this edge with another based on their weights.
         *
         * @param other The other edge to compare to.
         * @return A negative integer, zero, or a positive integer as this edge is less than, equal to, or greater than the other edge.
         */
        @Override
        public int compareTo(Edge other) {
            return Double.compare(this.weight, other.weight);
        }
    }


}
