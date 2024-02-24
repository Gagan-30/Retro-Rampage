package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import java.util.List;
import javafx.scene.paint.Color;

public class VisualizationManager {
    private Pane root; // The pane where all visual elements are drawn

    public VisualizationManager(Pane root) {
        this.root = root;
    }

    public void drawDelaunayTriangulation(List<GraphManager.Edge> edges) {
        if (edges == null || edges.isEmpty()) {
            System.out.println("No edges to draw for Delaunay Triangulation.");
            return; // Early return if the edges list is null or empty
        }

        Group linesGroup = new Group(); // Create a group to hold all the lines
        for (GraphManager.Edge edge : edges) {
            Line line = new Line(
                    edge.start.x, edge.start.y,
                    edge.end.x, edge.end.y
            );
            line.setStroke(Color.LIGHTGRAY); // Set color for triangulation lines
            linesGroup.getChildren().add(line); // Add line to the group
        }

        root.getChildren().add(linesGroup); // Add the group to the root pane
    }
}
