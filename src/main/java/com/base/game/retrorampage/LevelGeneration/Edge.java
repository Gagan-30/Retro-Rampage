package com.base.game.retrorampage.LevelGeneration;

import org.locationtech.jts.geom.Coordinate;

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

    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.weight, other.weight);
    }
}
