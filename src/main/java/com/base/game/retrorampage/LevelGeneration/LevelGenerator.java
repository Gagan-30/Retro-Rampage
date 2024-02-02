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
        separateCells();
        identifyRooms();
        buildGraph();
        constructCorridors();
        return new Scene(root, 800, 600);
    }

    private void generateCells(Pane root) {
        final int sceneWidth = 800;
        final int sceneHeight = 600;
        final int padding = 10; // Padding to reduce likelihood of immediate overlap

        for (int i = 0; i < numberOfCells; i++) {
            double width = 20 + random.nextDouble() * 80; // Random width between 20 and 100
            double height = 20 + random.nextDouble() * 80; // Random height between 20 and 100

            // Attempt to place cells with some padding to reduce overlap
            double x = padding + random.nextDouble() * (sceneWidth - width - 2 * padding);
            double y = padding + random.nextDouble() * (sceneHeight - height - 2 * padding);

            // Simple check to avoid placing a new cell on top of an existing one
            boolean overlap;
            do {
                overlap = false;
                x = padding + random.nextDouble() * (sceneWidth - width - 2 * padding);
                y = padding + random.nextDouble() * (sceneHeight - height - 2 * padding);

                for (Cell cell : cells) {
                    if (x < cell.getX() + cell.getWidth() + padding && x + width + padding > cell.getX() &&
                            y < cell.getY() + cell.getHeight() + padding && y + height + padding > cell.getY()) {
                        overlap = true;
                        break;
                    }
                }
            } while (overlap);

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
