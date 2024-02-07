package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.layout.Pane;
import org.locationtech.jts.geom.Coordinate;

import java.util.*;
import java.util.stream.Collectors;

public class CellGenerator {
    private int numberOfCells;
    private int sceneWidth, sceneHeight;
    private double roomWidthThreshold, roomHeightThreshold;
    private List<Cell> cells;
    private Map<Coordinate, Cell> pointToCellMap;
    private Random random;

    public CellGenerator(int numberOfCells, int sceneWidth, int sceneHeight, double roomWidthThreshold, double roomHeightThreshold) {
        this.numberOfCells = numberOfCells;
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.roomWidthThreshold = roomWidthThreshold;
        this.roomHeightThreshold = roomHeightThreshold;
        this.cells = new ArrayList<>();
        this.pointToCellMap = new HashMap<>();
        this.random = new Random();
    }

    public List<Cell> generateCells(Pane root) {
        List<Cell> cells = new ArrayList<>();
        while (cells.size() < numberOfCells) {
            double width = getRandomDimension();
            double height = getRandomDimension();
            double x = getRandomPosition(width, sceneWidth);
            double y = getRandomPosition(height, sceneHeight);

            Cell cell = new Cell(x, y, width, height);
            if (!isOverlapping(cell, cells)) {
                cells.add(cell);
                cell.draw(root);
            }
        }
        return cells;
    }

    private double getRandomDimension() {
        double meanSize = (roomWidthThreshold + roomHeightThreshold) / 2;
        double sizeStandardDeviation = 15.0; // Adjust as needed
        return meanSize + (random.nextGaussian() * sizeStandardDeviation);
    }

    private double getRandomPosition(double dimension, double max) {
        return random.nextDouble() * (max - dimension);
    }

    private boolean isOverlapping(Cell newCell, List<Cell> cells) {
        for (Cell cell : cells) {
            if (newCell.overlaps(cell)) {
                return true;
            }
        }
        return false;
    }


    public List<Coordinate> getCellCenters() {
        // Convert cell positions to Coordinate objects for triangulation
        return cells.stream().map(cell -> new Coordinate(cell.getX() + cell.getWidth() / 2, cell.getY() + cell.getHeight() / 2)).collect(Collectors.toList());
    }

    public Map<Coordinate, Cell> getPointToCellMap() {
        // Fill the map linking cell center coordinates to cell objects
        for (Cell cell : cells) {
            Coordinate center = new Coordinate(cell.getX() + cell.getWidth() / 2, cell.getY() + cell.getHeight() / 2);
            pointToCellMap.put(center, cell);
        }
        return pointToCellMap;
    }

    public Set<Point> getObstacles() {
        // Define logic to determine obstacles based on cell properties or other criteria
        // This is just a placeholder implementation
        return cells.stream().filter(Cell::isObstacle).map(cell -> new Point((int)cell.getX(), (int)cell.getY())).collect(Collectors.toSet());
    }
}