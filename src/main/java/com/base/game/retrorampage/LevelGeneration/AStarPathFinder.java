package com.base.game.retrorampage.LevelGeneration;

import java.util.*;

public class AStarPathFinder {

    public static List<Point> findPath(Point start, Point goal, Set<Point> obstacles) {
        PriorityQueue<Point> openSet = new PriorityQueue<>(Comparator.comparingDouble(Point::getFScore));
        Map<Point, Point> cameFrom = new HashMap<>();
        Map<Point, Double> gScore = new HashMap<>();
        Map<Point, Double> fScore = new HashMap<>();

        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, goal));

        openSet.add(start);

        while (!openSet.isEmpty()) {
            Point current = openSet.poll();
            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }

            for (Point neighbor : getNeighbors(current, obstacles)) {
                double tentativeGScore = gScore.getOrDefault(current, Double.MAX_VALUE) + distance(current, neighbor);
                if (tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + heuristic(neighbor, goal));
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return Collections.emptyList(); // Path not found
    }

    private static double heuristic(Point p1, Point p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }

    private static double distance(Point p1, Point p2) {
        return Math.hypot(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    private static List<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
        List<Point> totalPath = new ArrayList<>();
        totalPath.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(current);
        }
        Collections.reverse(totalPath);
        return totalPath;
    }

    private static Set<Point> getNeighbors(Point current, Set<Point> obstacles) {
        Set<Point> neighbors = new HashSet<>();
        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}}; // 4-directional movement
        for (int[] dir : directions) {
            Point neighbor = new Point(current.getX() + dir[0], current.getY() + dir[1]);
            if (!obstacles.contains(neighbor)) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }
}
