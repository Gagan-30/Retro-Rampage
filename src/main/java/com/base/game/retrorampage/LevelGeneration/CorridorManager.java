package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
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
        line.setStrokeWidth(2); // Set the corridor width, adjust as needed
        root.getChildren().add(line);
    }
}
