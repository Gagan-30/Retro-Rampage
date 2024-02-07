package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.Scene;
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

    public LevelGenerator(int numberOfCells, int sceneWidth, int sceneHeight) {
        this.numberOfCells = numberOfCells;
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.root = new Pane();
    }

    public Scene generateLevel() {
        // Step 1: Generate cells
        CellGenerator cellGenerator = new CellGenerator(numberOfCells, sceneWidth, sceneHeight, roomWidthThreshold, roomHeightThreshold);
        List<Cell> cells = cellGenerator.generateCells(root); // Assume generateCells now correctly takes root as a parameter if needed for drawing

        // Convert cells to coordinates for triangulation
        List<Coordinate> cellCenters = cellGenerator.getCellCenters(); // Assume getCellCenters now exists and is called correctly

        // Step 2: Triangulate cells to create a Delaunay Triangulation
        DelaunayTriangulator triangulator = new DelaunayTriangulator();
        List<Coordinate[]> triangulationEdges = triangulator.triangulate(cellCenters); // Assume triangulate now correctly takes cellCenters as input and returns List<Edge>

        // Step 3: Generate the MST from the triangulation
        MST mst = new MST(triangulationEdges);
        List<Edge> mstEdges = mst.generateMST(triangulationEdges);

        // Step 4: Construct corridors using the MST (with or without loops)
        Map<Coordinate, Cell> pointToCellMap = cellGenerator.getPointToCellMap(); // Assume getPointToCellMap now exists
        Set<Point> obstacles = cellGenerator.getObstacles(); // Assume getObstacles now exists
        CorridorConstructor corridorConstructor = new CorridorConstructor(root, pointToCellMap, obstacles); // Assume constructor now matches this signature
        corridorConstructor.constructCorridors(mstEdges);

        // Optional: Use AStarPathFinder for pathfinding if needed for NPCs or player movement

        return new Scene(root, sceneWidth, sceneHeight);
    }
}
