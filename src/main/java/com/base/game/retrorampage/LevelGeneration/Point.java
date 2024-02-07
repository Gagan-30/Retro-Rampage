package com.base.game.retrorampage.LevelGeneration;

import java.util.Objects;

public class Point {
    int x, y;
    double gScore; // Cost from the start node
    double hScore; // Heuristic cost to the goal node
    double fScore; // Total cost (gScore + hScore)
    Point parent; // For path reconstruction

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        this.gScore = Double.MAX_VALUE;
        this.hScore = Double.MAX_VALUE;
        this.fScore = Double.MAX_VALUE;
        this.parent = null;
    }

    public Point getParent() {
        return parent;
    }

    public void setParent(Point parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public double getFScore() {
        return  fScore;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
