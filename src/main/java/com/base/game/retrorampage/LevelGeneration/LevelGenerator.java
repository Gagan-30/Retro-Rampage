package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.Random;

public class LevelGenerator {
    private int numberOfCells;
    private ArrayList<Cell> cells = new ArrayList<>();
    private Random random = new Random();

    public LevelGenerator(int numberOfCells) {
        this.numberOfCells = numberOfCells;
    }

    public Scene generateLevel() {
        Pane root = new Pane();
        generateCells(root);
        generateCells(root);
        separateCells();
        identifyRooms();
        buildGraph();
        constructCorridors();
        return new Scene(root, 800, 600);
    }

    private void generateCells(Pane root) {
        for (int i = 0; i < numberOfCells; i++) {
            double width = 20 + random.nextDouble() * 80; // Random width between 20 and 100
            double height = 20 + random.nextDouble() * 80; // Random height between 20 and 100
            double x = random.nextDouble() * (800 - width); // Ensure cell fits within the scene
            double y = random.nextDouble() * (600 - height); // Ensure cell fits within the scene
            Cell cell = new Cell(x, y, width, height);
            cells.add(cell);
            cell.draw(root);
        }
    }

    private void separateCells() {
        // Implement logic to separate overlapping cells
    }

    private void identifyRooms() {
        // Implement logic to identify which cells are large enough to be rooms
    }

    private void buildGraph() {
        // Implement Delaunay Triangulation and Minimal Spanning Tree construction
    }

    private void constructCorridors() {
        // Implement corridor construction between rooms
    }
}
