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

    List<Coordinate> getCellCenters() {
        List<Coordinate> roomCenters = new ArrayList<>();
        for (Cell cell : cells) {
            double centerX = cell.getX() + cell.getWidth() / 2.0;
            double centerY = cell.getY() + cell.getHeight() / 2.0;
            Coordinate center = new Coordinate(centerX, centerY);
            roomCenters.add(center);
            pointToCellMap.put(center, cell); // Map the center Coordinate back to its cell
        }
        return roomCenters;
    }
}