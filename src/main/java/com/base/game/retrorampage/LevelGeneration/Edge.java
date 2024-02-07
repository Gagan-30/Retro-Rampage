package com.base.game.retrorampage.LevelGeneration;

import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Edge implements Comparable<Edge> {
    Coordinate start;
    Coordinate end;
    double weight;

    public Edge(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
        // Euclidean distance as weight
        this.weight = Math.sqrt(Math.pow(start.x - end.x, 2) + Math.pow(start.y - end.y, 2));
    }

    public Coordinate getStart() {
        return start;
    }

    public void setStart(Coordinate start) {
        this.start = start;
    }

    public Coordinate getEnd() {
        return end;
    }

    public void setEnd(Coordinate end) {
        this.end = end;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.weight, other.weight);
    }

    private List<Edge> convertTrianglesToEdges(List<Coordinate[]> triangleCoordinates) {
        Set<Edge> edges = new HashSet<>(); // Use a set to automatically avoid duplicate edges

        for (Coordinate[] triangle : triangleCoordinates) {
            for (int i = 0; i < triangle.length; i++) {
                Coordinate start = triangle[i];
                Coordinate end = triangle[(i + 1) % triangle.length]; // Ensure wrapping to the first coordinate
                Edge edge = new Edge(start, end);
                edges.add(edge);
            }
        }

        return new ArrayList<>(edges); // Convert the set back to a list
    }
}
