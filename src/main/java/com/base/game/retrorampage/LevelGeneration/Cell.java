package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell {
    private double x, y; // Coordinates of the top-left corner of the cell
    private double width, height; // Size dimensions of the cell
    private boolean isRoom = false; // Flag indicating whether the cell is considered a room

    // Constructor initializes the cell with its position and size
    public Cell(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Draws the cell on the given pane using a rectangle shape
    public void draw(Pane root) {
        Rectangle rectangle = new Rectangle(this.x, this.y, this.width, this.height);
        rectangle.setStroke(Color.BLACK); // Sets the border color of the cell
        rectangle.setFill(Color.TRANSPARENT); // Sets the interior of the cell to be transparent
        root.getChildren().add(rectangle);
    }

    public boolean adjustIfOverlaps(Cell other) {
        if (this.overlaps(other)) {
            // Adjust this cell's position to resolve the overlap
            // This is a simplified example; you'll need a more sophisticated logic
            this.x += 10; // Move the cell to the right
            this.y += 10; // Move the cell down
            return true;
        }
        return false;
    }

    // Checks if this cell overlaps with another cell (cellB)
    public boolean overlaps(Cell cellB) {
        // Determines horizontal and vertical separations between the cells
        boolean horizontalSeparation = this.x + this.width < cellB.x || cellB.x + cellB.width < this.x;
        boolean verticalSeparation = this.y + this.height < cellB.y || cellB.y + cellB.height < this.y;

        // If there's no separation on either axis, the cells overlap
        return !(horizontalSeparation || verticalSeparation);
    }

    // Accessor and mutator methods for the cell's properties
    public double getX() {
        return x;
    }
    // Method to get the center X coordinate of the cell
    public double getCenterX() {
        return this.x + this.width / 2.0;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    // Method to get the center Y coordinate of the cell
    public double getCenterY() {
        return this.y + this.height / 2.0;
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

    // Determines if the cell is marked as a room
    public boolean isRoom() {
        return isRoom;
    }

    public void setRoom(boolean isRoom) {
        this.isRoom = isRoom;
    }
}
