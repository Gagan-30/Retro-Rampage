package com.base.game.retrorampage.LevelGeneration;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CorridorConstructor {
    private Pane root;
    private Map<Coordinate, Cell> pointToCellMap;
    private Set<Point> obstacles;

    public CorridorConstructor(Pane root, Map<Coordinate, Cell> pointToCellMap, Set<Point> obstacles) {
        this.root = root;
        this.pointToCellMap = pointToCellMap;
        this.obstacles = obstacles;
    }

    public void constructCorridors(List<Edge> edges) {
        for (Edge edge : edges) {
            createWideCorridor(edge.start, edge.end, 10); // Example width
        }
    }

    public void createWideCorridor(Coordinate start, Coordinate end, double width) {
        Cell startCell = pointToCellMap.get(start);
        Cell endCell = pointToCellMap.get(end);

        if (startCell != null && endCell != null) {
            double angle = Math.atan2(endCell.getCenterY() - startCell.getCenterY(), endCell.getCenterX() - startCell.getCenterX());
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);

            Point2D[] corridorPoints = new Point2D[4];
            corridorPoints[0] = new Point2D(startCell.getCenterX() - sin * width / 2, startCell.getCenterY() + cos * width / 2);
            corridorPoints[1] = new Point2D(startCell.getCenterX() + sin * width / 2, startCell.getCenterY() - cos * width / 2);
            corridorPoints[2] = new Point2D(endCell.getCenterX() + sin * width / 2, endCell.getCenterY() - cos * width / 2);
            corridorPoints[3] = new Point2D(endCell.getCenterX() - sin * width / 2, endCell.getCenterY() + cos * width / 2);

            List<Point2D> adjustedPoints = adjustCorridorPointsForIntersections(corridorPoints);

            Polygon corridor = new Polygon();
            for (Point2D p : adjustedPoints) {
                corridor.getPoints().addAll(p.getX(), p.getY());
            }
            corridor.setFill(Color.LIGHTGRAY);

            root.getChildren().add(corridor);
        }
    }

    private List<Point2D> adjustCorridorPointsForIntersections(Point2D[] corridorPoints) {
        List<Point2D> adjustedPoints = new ArrayList<>();
        for (int i = 0; i < corridorPoints.length; i++) {
            Point2D start = corridorPoints[i];
            Point2D end = corridorPoints[(i + 1) % corridorPoints.length];
            Line corridorSegment = new Line(start.getX(), start.getY(), end.getX(), end.getY());

            boolean intersectionFound = false;
            for (Cell cell : pointToCellMap.values()) {
                Rectangle cellBounds = new Rectangle(cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                if (corridorSegment.getBoundsInLocal().intersects(cellBounds.getBoundsInLocal())) {
                    // Handle intersection case here. For simplicity, we're just marking intersections found
                    // You might want to adjust the corridor path, split it, or choose an alternative route
                    intersectionFound = true;
                    break;
                }
            }
            if (!intersectionFound) {
                adjustedPoints.add(start);
                // Optionally handle the case where you adjust the end point of the corridor segment
            }
        }
        return adjustedPoints;
    }
}
