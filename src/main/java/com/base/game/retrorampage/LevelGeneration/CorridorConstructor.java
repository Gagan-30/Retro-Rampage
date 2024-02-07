package com.base.game.retrorampage.LevelGeneration;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import org.locationtech.jts.geom.Coordinate;

import java.util.*;

public class CorridorConstructor {
    private Pane root;
    private static Map<Coordinate, Cell> pointToCellMap;

    void constructCorridors(List<Coordinate[]> triangleCoordinates) {
        Set<Edge> edges = new HashSet<>();

        for (Coordinate[] triangle : triangleCoordinates) {
            for (int i = 0; i < triangle.length - 1; i++) {
                Coordinate start = triangle[i];
                Coordinate end = triangle[(i + 1) % triangle.length];
                edges.add(new Edge(start, end));
            }
        }

        for (Edge edge : edges) {
            createCorridor(edge.start, edge.end);
        }
    }

    private void createCorridor(Coordinate start, Coordinate end) {
        Cell startCell = pointToCellMap.get(start);
        Cell endCell = pointToCellMap.get(end);

        if (startCell != null && endCell != null) {
            Line corridor = new Line(startCell.getCenterX(), startCell.getCenterY(), endCell.getCenterX(), endCell.getCenterY());
            corridor.setStrokeWidth(2);
            root.getChildren().add(corridor);
        }
    }
}
