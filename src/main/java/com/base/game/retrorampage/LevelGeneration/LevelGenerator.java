package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Random;

public class LevelGenerator {
    private final int numberOfCells;
    private final ArrayList<Cell> cells = new ArrayList<>();
    private final Random random = new Random();
    private final Pane root = new Pane();
    private final int sceneWidth = 800;
    private final int sceneHeight = 600;
    private final int padding = 10; // Padding to reduce likelihood of immediate overlap
    private final int maxIterations = 1000; // Example value
    private final double roomWidthThreshold = 50; // Example threshold value
    private final double roomHeightThreshold = 50; // Example threshold value
    private double sceneCenterX = sceneWidth / 2.0;
    private double sceneCenterY = sceneHeight / 2.0;
    private double positionStandardDeviation = 100.0; // Adjust based on scene size


    private double meanSize = 50.0; // Average size for width and height
    private double sizeStandardDeviation = 15.0; // Standard deviation to control the spread of sizes

    public LevelGenerator(int numberOfCells) {
        this.numberOfCells = numberOfCells;
    }

    public Scene generateLevel() {
        generateCells();
        separateCells();
        identifyRooms();
        buildGraph();
        constructCorridors();
        return new Scene(root, sceneWidth, sceneHeight);
    }

    private void generateCells() {
        for (int i = 0; i < numberOfCells; i++) {
            double width = getRandomDimension();
            double height = getRandomDimension();
            double x = getRandomPosition(positionStandardDeviation, sceneWidth - width); // Adjusted for cell width
            double y = getRandomPosition(positionStandardDeviation, sceneHeight - height); // Adjusted for cell height

            Cell cell = new Cell(x, y, width, height);
            if (!isOverlapping(cell)) {
                cells.add(cell);
                cell.draw(root);
            } else {
                i--; // Retry this cell generation if overlap detected
            }
        }
    }

    private double getRandomDimension() {
        // Generate a normally distributed value centered around meanSize
        double dimension = meanSize + (random.nextGaussian() * sizeStandardDeviation);
        // Ensure the dimension falls within your desired range
        return Math.max(20, Math.min(100, dimension));
    }

    private double getRandomPosition(double standardDeviation, double sceneSize) {
        // Generate a normally distributed position around the center of the scene
        double position = (sceneSize / 2.0) + (random.nextGaussian() * standardDeviation);
        // Clamp the position to ensure it's within the scene bounds
        return Math.max(padding, Math.min(sceneSize - padding, position));
    }


    private boolean isOverlapping(Cell newCell) {
        for (Cell cell : cells) {
            if (newCell.overlaps(cell)) {
                return true;
            }
        }
        return false;
    }

    private void separateCells() {
        int iterations = 0;
        boolean overlapExists;
        do {
            overlapExists = adjustOverlappingCells();
            iterations++;
        } while (overlapExists && iterations < maxIterations);
    }

    private boolean adjustOverlappingCells() {
        for (int i = 0; i < cells.size(); i++) {
            for (int j = i + 1; j < cells.size(); j++) {
                if (cells.get(i).adjustIfOverlaps(cells.get(j))) {
                    return true; // Overlap found and adjustments made
                }
            }
        }
        return false; // No overlaps found
    }

    private void identifyRooms() {
        cells.forEach(cell -> {
            if (cell.getWidth() >= roomWidthThreshold && cell.getHeight() >= roomHeightThreshold) {
                cell.setRoom(true);
            }
        });
    }

    private void constructCorridors() {
    }

    private void buildGraph() {
    }
}
