package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.locationtech.jts.geom.Coordinate;
import java.util.List;

public class CorridorManager {
    private Pane root; // The JavaFX pane on which corridors will be drawn

    public CorridorManager(Pane root) {
        this.root = root;
    }

    public void createCorridors(List<GraphManager.Edge> edges) {
        for (GraphManager.Edge edge : edges) {
            drawCorridor(edge.start, edge.end);
        }
    }

    private void drawCorridor(Coordinate start, Coordinate end) {
        // Convert the coordinates to Line objects for JavaFX visualization
        Line line = new Line(start.x, start.y, end.x, end.y);
        line.setStroke(Color.RED);
        line.setStrokeWidth(2); // Set the corridor width, adjust as needed
        root.getChildren().add(line);
    }

    // This method assumes that the 'edges' list contains edges connecting the centers
    // of rooms that are to be connected by hallways.
    public void createHallways(List<GraphManager.Edge> edges, double hallwayWidth) {
        for (GraphManager.Edge edge : edges) {
            // Calculate the direction and length of the hallway
            // This assumes that room centers are directly connectable without intersecting other rooms
            double dx = edge.end.x - edge.start.x;
            double dy = edge.end.y - edge.start.y;

            // First segment is horizontal
            double x1 = edge.start.x;
            double y1 = edge.start.y - hallwayWidth / 2; // Offset by half the hallway width
            double w1 = dx;
            double h1 = hallwayWidth;

            // Second segment is vertical
            double x2 = edge.end.x - hallwayWidth / 2; // Offset by half the hallway width
            double y2 = edge.start.y;
            double w2 = hallwayWidth;
            double h2 = dy;

            // Adjust the length and position if dx or dy is negative
            if (dx < 0) {
                x1 = edge.end.x;
                w1 = -dx;
            }
            if (dy < 0) {
                y2 = edge.end.y;
                h2 = -dy;
            }

            // Create rectangles for the hallway segments
            Rectangle hallwayHorizontal = new Rectangle(x1, y1, w1, h1);
            Rectangle hallwayVertical = new Rectangle(x2, y2, w2, h2);

            // Set the fill color to transparent and the stroke to a visible color
            hallwayHorizontal.setFill(null);
            hallwayHorizontal.setStroke(Color.BLACK);
            hallwayVertical.setFill(null);
            hallwayVertical.setStroke(Color.BLACK);

            // Add the hallway segments to the root pane
            root.getChildren().addAll(hallwayHorizontal, hallwayVertical);
        }
    }
}
