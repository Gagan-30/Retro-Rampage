package com.base.game.retrorampage.GameAssets;

import com.base.game.retrorampage.LevelGeneration.Cell;
import com.base.game.retrorampage.LevelGeneration.CorridorManager;
import com.base.game.retrorampage.MainMenu.Config;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player extends Sprite {
    private final Rectangle square;
    private double prevPlayerX;
    private double prevPlayerY;
    private long lastUpdateTime = System.nanoTime();
    private CorridorManager corridorManager;
    private int health;  // New field to store player health
    private final double size;

    public Player(double size, String imagePath, int initialHealth) {
        super(imagePath, size);
        this.square = new Rectangle(size, size);
        this.square.setFill(Color.RED);
        this.health = initialHealth;  // Initialize player health
        this.size = size;  // Initialize player size
    }


    public void setCorridorManager(CorridorManager corridorManager) {
        this.corridorManager = corridorManager;
    }

    public void drawInSpawn(Cell center, Pane root) {
        this.square.setX(center.getCenterX() - square.getWidth() / 2);
        this.square.setY(center.getCenterY() - square.getHeight() / 2);
        root.getChildren().add(this.square);

        this.setPosition(center.getCenterX() - this.size / 2,
                center.getCenterY() - this.size / 2);
        this.addToPane(root);
    }


    public void updatePosition(Input input, Config config, double movementSpeed) {
        // Update input state
        input.update();

        // Move the square based on arrow key input
        double speed = movementSpeed;

        long now = System.nanoTime();
        double dt = (now - lastUpdateTime) / 1e9; // Convert nanoseconds to seconds
        lastUpdateTime = now;
        double movement = speed * dt;

        String moveUpKey1 = config.getKeybind("MoveUp1");
        String moveUpKey2 = config.getKeybind("MoveUp2");
        String moveDownKey1 = config.getKeybind("MoveDown1");
        String moveDownKey2 = config.getKeybind("MoveDown2");
        String moveLeftKey1 = config.getKeybind("MoveLeft1");
        String moveLeftKey2 = config.getKeybind("MoveLeft2");
        String moveRightKey1 = config.getKeybind("MoveRight1");
        String moveRightKey2 = config.getKeybind("MoveRight2");

        // Handle key presses and update player's position
        if (input.isKeyPressed(moveUpKey1) || input.isKeyPressed(moveUpKey2)) {
            prevPlayerY = getY(); // Store previous position
            square.setY(square.getY() - movement);
            setPosition(getX(), getY() - movement);
        }
        if (input.isKeyPressed(moveDownKey1) || input.isKeyPressed(moveDownKey2)) {
            prevPlayerY = getY(); // Store previous position
            square.setY(square.getY() + movement);
            setPosition(getX(), getY() + movement);
        }
        if (input.isKeyPressed(moveLeftKey2) || input.isKeyPressed(moveLeftKey1)) {
            prevPlayerX = getX(); // Store previous position
            square.setX(square.getX() - movement);
            setPosition(getX() - movement, getY());
        }
        if (input.isKeyPressed(moveRightKey1) || input.isKeyPressed(moveRightKey2)) {
            prevPlayerX = getX(); // Store previous position
            square.setX(square.getX() + movement);
            setPosition(getX() + movement, getY());
        }

        // Update the player's looking direction based on mouse movement
        double mouseX = input.getMouseX();
        double mouseY = input.getMouseY();
        double angleToMouse = Math.toDegrees(Math.atan2(mouseY - getY(), mouseX - getX()));
        setRotation(angleToMouse);
    }

    public double getMouseLookingDirection(Input input) {
        double mouseX = input.getMouseX();
        double mouseY = input.getMouseY();

        // Calculate the angle between player and mouse position
        return Math.toDegrees(Math.atan2(mouseY - getY(), mouseX - getX()));
    }

    public void resolveCollision(Rectangle obstacle) {
        // Get the start and end coordinates for the width and height of the obstacle
        double obstacleStartX = obstacle.getX();
        double obstacleEndX = obstacle.getX() + obstacle.getWidth();
        double obstacleStartY = obstacle.getY();
        double obstacleEndY = obstacle.getY() + obstacle.getHeight();

        // Calculate the potential new position after collision resolution
        double newX = getX();
        double newY = getY();

        // Check if the new position is within the bounds of the room or hallway
        boolean withinRoomBounds = newX >= obstacleStartX && newX + square.getWidth() <= obstacleEndX &&
                newY >= obstacleStartY && newY + square.getHeight() <= obstacleEndY;

        // Check if the new position is within the bounds of any hallway
        boolean withinHallwayBounds = false;
        Rectangle[] hallwayBoundsList = corridorManager.getHallwayBounds();
        if (hallwayBoundsList != null) {
            for (Rectangle hallwayBounds : hallwayBoundsList) {
                if (newX >= hallwayBounds.getX()
                        && newX + square.getWidth() <= hallwayBounds.getX() + hallwayBounds.getWidth() &&
                        newY >= hallwayBounds.getY()
                        && newY + square.getHeight() <= hallwayBounds.getY() + hallwayBounds.getHeight()) {
                    withinHallwayBounds = true;
                    break; // No need to check further if within bounds of any hallway
                }
            }
        }

        // Adjust the position only if it's not within the bounds of the room
        if (!withinRoomBounds) {
            // If within the bounds of the hallway, allow movement; otherwise, prevent it
            if (withinHallwayBounds) {
                // Update the position
                square.setX(newX);
                square.setY(newY);

                // Update the player's image view position
                setPosition(getX() + (newX - square.getX()), getY() + (newY - square.getY()));
            }
        }
    }

    public double getSize() {
        return size;
    }

    // New method to decrease player health
    public void decreaseHealth(int amount) {
        health -= amount;
        System.out.println("health = " + health);
    }
}
