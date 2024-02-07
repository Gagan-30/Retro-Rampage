package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import org.locationtech.jts.geom.Coordinate;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class LevelGenerator {
    private final int numberOfCells;
    private final int sceneWidth;
    private final int sceneHeight;
    private final Pane root;

    // Thresholds could be constants or configurable parameters
    private final double roomWidthThreshold = 50.0;
    private final double roomHeightThreshold = 50.0;
    private GraphicsContext gc;

    public LevelGenerator(int numberOfCells, int sceneWidth, int sceneHeight) {
        this.numberOfCells = numberOfCells;
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.root = new Pane();
    }

        public Scene generateLevel() {
            // Initialize CellGenerator with the proper arguments.
            CellGenerator cellGenerator = new CellGenerator(numberOfCells, sceneWidth, sceneHeight, roomWidthThreshold, roomHeightThreshold);

            // Generate cells and retrieve the needed data structures.
            List<Cell> cells = cellGenerator.generateCells(root);
            List<Coordinate> cellCenters = cellGenerator.getCellCenters();

            /*
            List<Coordinate[]> triangleGeometries = triangulateRooms(roomCenters); // Use JTS Geometry objects
            constructCorridors(triangleGeometries); // Pass JTS Geometry objects
            */

            // DelaunayTriangulation and MST creation.
            DelaunayTriangulator triangulator = new DelaunayTriangulator();
            List<Coordinate[]> triangleGeometries = triangulator.triangulate(cellCenters);

            // Corridor construction.
            CorridorConstructor corridorConstructor = new CorridorConstructor();
            corridorConstructor.constructCorridors(triangleGeometries);

            // Return the Scene.
            return new Scene(root, sceneWidth, sceneHeight);
        }
    }
