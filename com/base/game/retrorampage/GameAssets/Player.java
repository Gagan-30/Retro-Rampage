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

    private Pane root;  // Add the root field

    public Player(double size, String imagePath, int health, Pane root) {
        super(imagePath, size);
        this.square = new Rectangle(size, size);
        this.square.setFill(Color.RED);
        this.health = health;  // Initialize player health
        this.size = size;  // Initialize player size
        this.root = root;  // Initialize the root field
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
        double newX = getX();
        double newY = getY();

        // Calculate the potential new position after collision resolution
        double playerLeft = newX;
        double playerRight = newX + square.getWidth();
        double playerTop = newY;
        double playerBottom = newY + square.getHeight();

        double obstacleLeft = obstacle.getX();
        double obstacleRight = obstacle.getX() + obstacle.getWidth();
        double obstacleTop = obstacle.getY();
        double obstacleBottom = obstacle.getY() + obstacle.getHeight();

        // Check for overlap in the X-axis
        if (playerRight > obstacleLeft && playerLeft < obstacleRight) {
            // Check for overlap in the Y-axis
            if (playerBottom > obstacleTop && playerTop < obstacleBottom) {
                // Calculate the overlap in both axes
                double overlapX = Math.min(playerRight - obstacleLeft, obstacleRight - playerLeft);
                double overlapY = Math.min(playerBottom - obstacleTop, obstacleBottom - playerTop);

                // Resolve the collision based on the smaller overlap
                if (overlapX < overlapY) {
                    // Resolve in the X-axis
                    if (playerRight > obstacleLeft && newX < obstacleLeft) {
                        newX = obstacleLeft - square.getWidth();
                    } else if (playerLeft < obstacleRight && newX > obstacleRight) {
                        newX = obstacleRight;
                    }
                } else {
                    // Resolve in the Y-axis
                    if (playerBottom > obstacleTop && newY < obstacleTop) {
                        newY = obstacleTop - square.getHeight();
                    } else if (playerTop < obstacleBottom && newY > obstacleBottom) {
                        newY = obstacleBottom;
                    }
                }

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

        if (health <= 0) {
            // Player is dead, handle death logic
            die();
        }
    }

    // Method to handle player death
    private void die() {
        // Optionally, you can add death animation or other effects here

        // Remove the player's square from the pane
        removeFromPane();

        // Remove the player's square from the scene
        root.getChildren().remove(square);

        // Remove the player's imageView from the pane
        removeFromPane();

        health = 100;  // Reset health for demonstration, adjust as needed
    }


    // New method to set the root
    public void setRoot(Pane root) {
        this.root = root;
    }
}
