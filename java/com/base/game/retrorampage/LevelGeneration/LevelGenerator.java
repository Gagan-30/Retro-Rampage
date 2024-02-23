package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class LevelGenerator {
    private final Scene scene;

    // Initialize the managers
    private RoomManager roomManager;
    private GraphManager graphManager;
    private CorridorManager corridorManager;
    private VisualizationManager visualizationManager;

    public LevelGenerator(int numberOfCells) {
        Pane root = new Pane();
        int WIDTH = 1920;
        int HEIGHT = 1080;
        this.scene = new Scene(root, WIDTH, HEIGHT); // Assuming fixed dimensions for simplicity

        // Initialize managers with necessary parameters
        this.roomManager = new RoomManager(numberOfCells, root);
        this.graphManager = new GraphManager();
        this.corridorManager = new CorridorManager(root);
        this.visualizationManager = new VisualizationManager(root);
    }

    public Scene generateLevel() {
        // 1. Generate and identify rooms
        roomManager.generateRooms();
        List<Rectangle> roomRectangles = roomManager.getRoomRectangles();
        roomManager.disperseCells();
        corridorManager.setRooms(roomRectangles);

        // 2. Perform triangulation and generate MST
        var roomCenters = roomManager.getRoomCenters();
        var triangleEdges = graphManager.triangulateRooms(roomCenters);
        var mstEdges = graphManager.generateMST(triangleEdges);
        var loopedEdges = graphManager.reintroduceLoops(mstEdges, triangleEdges, 0.3);

        corridorManager.createHallways(loopedEdges, 10); // 2 is the hallway width

        // 3. Create corridors based on MST edges and additional logic for loops
        corridorManager.createCorridors(mstEdges);

        // 4. Draw Rooms and Triangulation
        roomManager.drawRooms();
        visualizationManager.drawDelaunayTriangulation(graphManager.getEdges()); // Ensure GraphManager exposes the edg
        corridorManager.createHallways(loopedEdges, 5);

        // Return the scene containing the generated level
        return this.scene;
    }
}
