package com.base.game.retrorampage.GameAssets;

import com.base.game.retrorampage.LevelGeneration.Cell;
import com.base.game.retrorampage.LevelGeneration.CorridorManager;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy extends Sprite {
    private final Rectangle square;
    private static CorridorManager corridorManager;
    private int health;
    private double initialX;
    private double initialY;
    private Player player;

    public Enemy(double size, String imagePath, int health) {
        super(imagePath, size);
        this.square = new Rectangle(size, size);
        this.square.setFill(Color.TRANSPARENT);

        this.height = size;
        this.width = size;
        this.health = health;
    }


    public void setCorridorManager(CorridorManager corridorManager) {
        Enemy.corridorManager = corridorManager;
    }

    public static List<Enemy> spawnEnemies(int numEnemies, double size, String imagePath, int health, List<Cell> cells, Cell spawnCell, Pane root) {
        List<Enemy> enemies = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numEnemies; i++) {
            Enemy enemy = new Enemy(size, imagePath, health);
            enemy.setCorridorManager(corridorManager); // Set the corridorManager for the enemy

            Cell randomCell;
            do {
                randomCell = cells.get(random.nextInt(cells.size()));
            } while (randomCell.equals(spawnCell));

            double enemyX = randomCell.getCenterX() - size / 2;
            double enemyY = randomCell.getCenterY() - size / 2;

            // Debug information
            System.out.println("Enemy " + i + " spawned at: X=" + enemyX + ", Y=" + enemyY + " in Cell: " + randomCell);

            enemy.initialX = enemyX; // Set initialX
            enemy.initialY = enemyY; // Set initialY

            enemy.addToPane(root); // Ensure addToPane is called after updating the position
            enemies.add(enemy);
        }

        return enemies;
    }


    public void updatePosition(double playerX, double playerY, double movementSpeed) {
        double currentX = square.getX() + square.getWidth() / 2;
        double currentY = square.getY() + square.getHeight() / 2;

        // Calculate the distance between the enemy and the player
        double distanceToPlayer = Math.hypot(playerX - currentX, playerY - currentY);

        if (distanceToPlayer <= square.getWidth()) {
            // Player is within the same cell, move away from the player
            double directionX = currentX - playerX;
            double directionY = currentY - playerY;

            // Normalize the direction vector
            double distance = Math.sqrt(directionX * directionX + directionY * directionY);
            directionX /= distance;
            directionY /= distance;

            // Calculate the new position
            double newX = currentX + directionX * movementSpeed;
            double newY = currentY + directionY * movementSpeed;

            // Ensure the new position stays within the current cell
            if (corridorManager.isPositionWithinCell(newX, newY, square.getWidth(), square.getHeight())) {
                square.setX(newX - square.getWidth() / 2);
                square.setY(newY - square.getHeight() / 2);

                // Update the image position
                imageView.setX(square.getX() + (square.getWidth() - width) / 2);
                imageView.setY(square.getY() + (square.getHeight() - height) / 2);
            }

            // Take away player's health
            player.decreaseHealth(20); // Adjust the value based on your game's health system
        } else {
            // Player is not in the same cell, use the original behavior
            // Calculate the direction vector towards the player
            double directionX = playerX - currentX;
            double directionY = playerY - currentY;

            // Normalize the direction vector
            double distance = Math.sqrt(directionX * directionX + directionY * directionY);
            directionX /= distance;
            directionY /= distance;

            // Calculate the new position
            double newX = currentX + directionX * movementSpeed;
            double newY = currentY + directionY * movementSpeed;

            // Ensure the new position stays within the current cell
            if (corridorManager.isPositionWithinCell(newX, newY, square.getWidth(), square.getHeight())) {
                square.setX(newX - square.getWidth() / 2);
                square.setY(newY - square.getHeight() / 2);

                // Update the image position
                imageView.setX(square.getX() + (square.getWidth() - width) / 2);
                imageView.setY(square.getY() + (square.getHeight() - height) / 2);
            }
        }
    }


    public void resolveCollision(Rectangle obstacle, Player player) {
        double obstacleStartX = obstacle.getX();
        double obstacleEndX = obstacle.getX() + obstacle.getWidth();
        double obstacleStartY = obstacle.getY();
        double obstacleEndY = obstacle.getY() + obstacle.getHeight();

        double playerX = player.getX();
        double playerY = player.getY();
        double playerSize = player.getSize();

        if (playerX + playerSize > obstacleStartX && playerX < obstacleEndX &&
                playerY + playerSize > obstacleStartY && playerY < obstacleEndY &&
                square.getWidth() <= 40) {
            // Enemy square size is 25 pixels or less, and it collided with the player
            // Decrease player health by 20 (adjust this value based on your game's health system)
            player.decreaseHealth(20);
        }
    }

    public boolean isCollidingPlayer(Player player) {
        return false;
    }

    public boolean isCollidingRoom(Rectangle roomRectangle) {
        return false;
    }

    public void reduceHealth(int amount) {
        // Reduce the enemy's health by the specified amount
        health -= amount;
        System.out.println(health);

        // Optionally, you can add logic to handle the enemy's death when health reaches zero or below
        if (health <= 0) {
            // Handle enemy death, e.g., remove from the list, play death animation, etc.
            removeFromPane(); // Ensure the enemy is removed from the pane
        }
    }


    public Rectangle getSquare() {
        return square;
    }

    public int getHealth() {
        return health;
    }

    public void addToPane(Pane root) {
        this.root = root;

        imageView.setX(initialX);
        imageView.setY(initialY);

        // Center the square around the image
        square.setX(imageView.getX() - (square.getWidth() - width) / 2);
        square.setY(imageView.getY() - (square.getHeight() - height) / 2);

        // Add both square and image to the root
        root.getChildren().addAll(imageView, square);

        // Print the coordinates for debugging
        System.out.println("Enemy added to pane at: X=" + square.getX() + ", Y=" + square.getY());

        // Print the coordinates of imageView for debugging
        System.out.println("ImageView added to pane at: X=" + imageView.getX() + ", Y=" + imageView.getY());
    }


    public void removeFromPane() {
        if (root != null) {
            root.getChildren().removeAll(square, imageView);
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
