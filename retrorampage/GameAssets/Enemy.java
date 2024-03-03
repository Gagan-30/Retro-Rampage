package com.base.game.retrorampage.GameAssets;

import com.base.game.retrorampage.LevelGeneration.Cell;
import com.base.game.retrorampage.LevelGeneration.CorridorManager;
import com.base.game.retrorampage.MainMenu.Config;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy extends Sprite {
    private final Rectangle square;
    private double prevEnemyX;
    private double prevEnemyY;
    private final long lastUpdateTime = System.nanoTime();
    private static CorridorManager corridorManager;

    public Enemy(double size, String imagePath) {
        super(imagePath, size);
        this.square = new Rectangle(size, size);
        this.square.setFill(Color.RED); // Set the enemy color to red for example
    }

    public void setCorridorManager(CorridorManager corridorManager) {
        Enemy.corridorManager = corridorManager;
    }

    public static List<Enemy> spawnEnemies(int numEnemies, double size, String imagePath, List<Cell> cells, Cell spawnCell, Pane root) {
        List<Enemy> enemies = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numEnemies; i++) {
            Enemy enemy = new Enemy(size, imagePath);
            enemy.setCorridorManager(corridorManager); // Set the corridorManager for the enemy

            Cell randomCell;
            do {
                randomCell = cells.get(random.nextInt(cells.size()));
            } while (randomCell.equals(spawnCell));

            enemy.square.setX(randomCell.getCenterX() - enemy.square.getWidth() / 2);
            enemy.square.setY(randomCell.getCenterY() - enemy.square.getHeight() / 2);

            enemy.setPosition(randomCell.getCenterX() - enemy.getX() / 2,
                    randomCell.getCenterY() - enemy.getY() / 2);

            enemy.addToPane(root);  // Ensure addToPane is called after updating the position

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
            // Player is within the same cell, move towards the player
            double directionX = playerX - currentX;
            double directionY = playerY - currentY;

            // Normalize the direction vector
            double distance = Math.sqrt(directionX * directionX + directionY * directionY);
            directionX /= distance;
            directionY /= distance;

            // Calculate the new position
            double newX = currentX + directionX * movementSpeed;
            double newY = currentY + directionY * movementSpeed;

            // Update the position
            square.setX(newX - square.getWidth() / 2);
            square.setY(newY - square.getHeight() / 2);
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
}
