package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.shape.Rectangle;
import org.locationtech.jts.geom.Coordinate;

public class RoomManager {
    private final int numberOfCells;
    private final Pane root; // The pane on which rooms will be drawn
    private final List<Cell> cells = new ArrayList<>();
    private final Random random = new Random();
    private final double minDisperseWidth = 60.0;
    private final double minDisperseHeight = 60.0;

    public RoomManager(int numberOfCells, Pane root) {
        this.numberOfCells = numberOfCells;
        this.root = root;
    }

    public void generateRooms() {
        cells.clear(); // Clear previous cells if any
        double centerX = root.getWidth() / 2;
        double centerY = root.getHeight() / 2;

        for (int i = 0; i < numberOfCells; i++) {
            double width = 50 + random.nextDouble() * (250 - 50); // Random width between 50 and 250
            double height = 50 + random.nextDouble() * (250 - 50); // Random height between 50 and 250

            // Position the cell's center at the center of the pane
            double x = centerX - width / 2;
            double y = centerY - height / 2;

            Cell cell = new Cell(x, y, width, height);

            // Determine if the cell should be considered a room based on size thresholds
            // Define size thresholds for what constitutes a room
            double roomWidthThreshold = 60.0;
            double roomHeightThreshold = 60.0;
            if (cell.getWidth() >= roomWidthThreshold && cell.getHeight() >= roomHeightThreshold) {
                cell.setRoom(true);
            }

            cells.add(cell);
        }

        disperseCells(); // Disperse cells after they are created at the center
    }

    public List<Coordinate> getRoomCenters() {
        List<Coordinate> roomCenters = new ArrayList<>();
        for (Cell cell : cells) {
            if (cell.isRoom()) {
                double centerX = cell.getX() + cell.getWidth() / 2.0;
                double centerY = cell.getY() + cell.getHeight() / 2.0;
                roomCenters.add(new Coordinate(centerX, centerY));
            }
        }
        return roomCenters;
    }

    // Optional: Method to visualize rooms on the Pane for debugging or visualization purposes
    public void drawRooms() {
        for (Cell cell : cells) {
            if (cell.isRoom()) {
                cell.draw(root);
            }
        }
    }

    public void disperseCells() {
        boolean moved;
        do {
            moved = false;
            for (int i = 0; i < cells.size(); i++) {
                for (int j = i + 1; j < cells.size(); j++) {
                    Cell cellA = cells.get(i);
                    Cell cellB = cells.get(j);
                    if (cellA.overlaps(cellB)) {
                        // Adjust positions to reduce overlap
                        adjustPositions(cellA, cellB);
                        moved = true;
                    }
                }
            }
        } while (moved);
    }

    private void adjustPositions(Cell cellA, Cell cellB) {
        // Calculate the overlap offsets for X and Y axes
        double overlapX = (cellA.getWidth() / 2 + cellB.getWidth() / 2 + minDisperseWidth * 1.15)
                - Math.abs(cellA.getCenterX() - cellB.getCenterX());
        double overlapY = (cellA.getHeight() / 2 + cellB.getHeight() / 2 + minDisperseHeight * 1.15)
                - Math.abs(cellA.getCenterY() - cellB.getCenterY());

        // Ensure there's a minimum separation distance
        if (overlapX < minDisperseWidth) {
            overlapX += minDisperseWidth - overlapX;
        }
        if (overlapY < minDisperseHeight) {
            overlapY += minDisperseHeight - overlapY;
        }

        // Calculate the direction to move each cell
        double moveAX = overlapX * (cellA.getCenterX() < cellB.getCenterX() ? -1 : 1);
        double moveAY = overlapY * (cellA.getCenterY() < cellB.getCenterY() ? -1 : 1);
        double moveBX = -moveAX;
        double moveBY = -moveAY;

        // Apply the calculated positions, ensuring cells stay within the pane's bounds and maintain minimum separation
        cellA.setX(clamp(cellA.getX() + moveAX, root.getWidth() - cellA.getWidth()));
        cellA.setY(clamp(cellA.getY() + moveAY, root.getHeight() - cellA.getHeight()));
        cellB.setX(clamp(cellB.getX() + moveBX, root.getWidth() - cellB.getWidth()));
        cellB.setY(clamp(cellB.getY() + moveBY, root.getHeight() - cellB.getHeight()));
    }

    private double clamp(double value, double max) {
        return Math.max(0, Math.min(max, value));
    }

    public List<Rectangle> getRoomRectangles() {
        List<Rectangle> roomRectangles = new ArrayList<>();
        for (Cell cell : cells) {
            if (cell.isRoom()) {
                // Create a new Rectangle based on the cell's position and size
                Rectangle roomRectangle = new Rectangle(cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                roomRectangles.add(roomRectangle);
            }
        }
        return roomRectangles;
    }
}
