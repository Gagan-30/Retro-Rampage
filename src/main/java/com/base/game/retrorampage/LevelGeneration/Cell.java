package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell {
    private double x, y; // Position of the cell
    private double width, height; // Dimensions of the cell

    public Cell(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Pane root) {
        Rectangle rectangle = new Rectangle(x, y, width, height);
        rectangle.setStroke(Color.BLACK); // Outline color
        rectangle.setFill(Color.TRANSPARENT); // Fill color
        root.getChildren().add(rectangle);
    }

    // Getters and Setters
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean overlaps(Cell cellB) {
        // Check if this cell is to the right of cellB or cellB is to the right of this cell
        boolean horizontalSeparation = this.x + this.width < cellB.x || cellB.x + cellB.width < this.x;

        // Check if this cell is above cellB or cellB is above this cell
        boolean verticalSeparation = this.y + this.height < cellB.y || cellB.y + cellB.height < this.y;

        // If there is horizontal or vertical separation, they don't overlap; otherwise, they do
        return !(horizontalSeparation || verticalSeparation);
    }
}
