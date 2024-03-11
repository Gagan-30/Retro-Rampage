package com.base.game.retrorampage.GameAssets;

import com.base.game.retrorampage.LevelGeneration.Cell;
import com.base.game.retrorampage.LevelGeneration.CorridorManager;
import javafx.geometry.Bounds;
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
    private long lastAttackTime = System.nanoTime();
    private long lastUpdateTime = System.nanoTime();
    private long playerCollisionCooldown = 2 * 1_000_000_000L; // 2 seconds in nanoseconds
    private long lastPlayerCollisionTime = 0;
    private boolean playerCollisionCooldownActive = false;

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
        long now = System.nanoTime();
        double elapsedSeconds = (now - lastUpdateTime) / 1e9; // Convert nanoseconds to seconds
        lastUpdateTime = now;

        double directionX = playerX - square.getX();
        double directionY = playerY - square.getY();
        double distanceToPlayer = Math.hypot(directionX, directionY);

        // Normalize the direction vector
        if (distanceToPlayer > 0) {
            directionX /= distanceToPlayer;
            directionY /= distanceToPlayer;

            // Calculate the movement based on elapsed time and speed
            double movementX = directionX * movementSpeed * elapsedSeconds;
            double movementY = directionY * movementSpeed * elapsedSeconds;

            // Calculate the new position
            double newX = square.getX() + movementX;
            double newY = square.getY() + movementY;

            // Check if the new position stays within the current cell
            if (corridorManager.isPositionWithinCell(newX, newY, square.getWidth(), square.getHeight())) {
                square.setX(newX);
                square.setY(newY);

                // Update the image position
                imageView.setX(newX + (square.getWidth() - width) / 2);
                imageView.setY(newY + (square.getHeight() - height) / 2);

                // Rotate the image to face the player
                double angleToPlayer = Math.toDegrees(Math.atan2(directionY, directionX));
                imageView.setRotate(angleToPlayer);

                // Set the last attack time to the current time
                lastAttackTime = System.nanoTime();
            }
        }
    }


    public boolean isCollidingPlayer(Player player) {
        Bounds playerBounds = player.getSquare().getBoundsInParent();
        Bounds enemyBounds = square.getBoundsInParent();

        if (playerBounds.intersects(enemyBounds)) {
            // Player and enemy are colliding
            handlePlayerCollision();
            return true;
        }

        return false;
    }

    public void handlePlayerCollision() {
        if (!playerCollisionCooldownActive && getHealth() > 0 && player != null) {
            // Set the player collision cooldown flag to true
            playerCollisionCooldownActive = true;

            // Reduce the player's health
            player.decreaseHealth(20); // Adjust the amount based on your game's logic

            // Check if the player's health is already zero or below
            if (player.getHealth() <= 0) {
                player.setHealth(0);
                System.out.println("Player-enemy collision! Player health: 0");
            } else {
                System.out.println("Player-enemy collision! Player health: " + player.getHealth());
            }

            // Start a timer to reset the collision cooldown after a certain delay
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            playerCollisionCooldownActive = false;
                        }
                    },
                    playerCollisionCooldown / 1_000_000 // Convert nanoseconds to milliseconds
            );
        }
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
