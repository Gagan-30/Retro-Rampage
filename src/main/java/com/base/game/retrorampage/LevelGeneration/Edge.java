package com.base.game.retrorampage.LevelGeneration;

import org.locationtech.jts.geom.Coordinate;

class Edge {
    Coordinate start;
    Coordinate end;
    double weight;

    public Edge(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
        this.weight = calculateWeight(start, end);
    }

    private double calculateWeight(Coordinate start, Coordinate end) {
        // Calculate Euclidean distance between start and end points
        double dx = start.x - end.x;
        double dy = start.y - end.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
