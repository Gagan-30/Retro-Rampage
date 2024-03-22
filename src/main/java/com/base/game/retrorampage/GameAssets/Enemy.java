package com.base.game.retrorampage.GameAssets;

import com.base.game.retrorampage.LevelGeneration.Cell;
import com.base.game.retrorampage.LevelGeneration.CorridorManager;
import com.base.game.retrorampage.MainMenu.Config;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class Enemy extends Sprite {
    private static CorridorManager corridorManager;
    private static int totalEnemiesKilled = 0;
    private final Rectangle square;
    private int health;
    private double initialX;
    private double initialY;
    private Player player;
    private long lastAttackTime = System.nanoTime();
    private long lastUpdateTime = System.nanoTime();
    private final long playerCollisionCooldown = 2 * 1_000_000_000L; // 2 seconds in nanoseconds
    private final long lastPlayerCollisionTime = 0;
    private boolean playerCollisionCooldownActive = false;
    private final Text damageLabel;
    private final FadeTransition fadeTransition;
    private final TranslateTransition translateTransition;
    private AudioClip audioClip;
    private URL playerDamagePath;
    private final Config config;

    public Enemy(double size, String imagePath, int health, Config config, Pane root) {
        super(imagePath, size);
        this.square = new Rectangle(size, size);
        this.square.setFill(Color.TRANSPARENT);
        this.config = config;

        this.height = size;
        this.width = size;
        this.health = health;

        // Initialize damage label
        damageLabel = new Text();
        damageLabel.setFont(new Font("Arial", 20));
        damageLabel.setFill(Color.RED);
        damageLabel.setVisible(false);

        // Initialize fade transition
        fadeTransition = new FadeTransition(Duration.seconds(0.5), damageLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        // Initialize translate transition
        translateTransition = new TranslateTransition(Duration.seconds(0.5), damageLabel);
        translateTransition.setFromY(0);
        translateTransition.setToY(-20);
        translateTransition.setCycleCount(1);

        this.root = root;
        root.getChildren().add(damageLabel);
    }

    public static List<Enemy> spawnEnemies(int numEnemies, double size, String imagePath, int health, List<Cell> cells, Cell spawnCell, Pane root) {
        List<Enemy> enemies = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numEnemies; i++) {
            Enemy enemy = new Enemy(size, imagePath, health, new Config("config.txt"), root);
            enemy.setCorridorManager(corridorManager); // Set the corridorManager for the enemy

            Cell randomCell;
            do {
                randomCell = cells.get(random.nextInt(cells.size()));
            } while (randomCell.equals(spawnCell));

            double enemyX = randomCell.getCenterX() - size / 2;
            double enemyY = randomCell.getCenterY() - size / 2;

            // Check if the enemy spawns on top of another enemy
            boolean overlaps = false;
            for (Enemy existingEnemy : enemies) {
                if (existingEnemy.getSquare().getBoundsInParent().intersects(enemyX, enemyY, size, size)) {
                    overlaps = true;
                    break;
                }
            }

            if (overlaps) {
                // Skip adding this enemy and move to the next iteration
                System.out.println("Enemy spawned on top of another enemy. Skipping...");
                continue;
            }

            // Debug information
            System.out.println("Enemy " + i + " spawned at: X=" + enemyX + ", Y=" + enemyY + " in Cell: " + randomCell);

            enemy.initialX = enemyX; // Set initialX
            enemy.initialY = enemyY; // Set initialY

            enemy.addToPane(root); // Ensure addToPane is called after updating the position
            enemies.add(enemy);
        }

        return enemies;
    }

    // Method to retrieve totalEnemiesKilled from outside the class
    public static int getTotalEnemiesKilled() {
        return totalEnemiesKilled;
    }

    public void setCorridorManager(CorridorManager corridorManager) {
        Enemy.corridorManager = corridorManager;
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
            // Load enemy damage audio
            playerDamagePath = getClass().getResource("/playerdamage.wav");
            audioClip = new AudioClip(playerDamagePath.toString());
            audioClip.setVolume(config.getVolume());
            audioClip.play(); // Play enemy damage audio

            // Check if the player's health is already zero or below
            if (player.getHealth() <= 0) {
                player.setHealth(0);
                System.out.println("Player-enemy collision! Player health: 0");
            } else {
                System.out.println("Player-enemy collision! Player health: " + player.getHealth());
            }

            // Start a timer to reset the collision cooldown after a certain delay
            new Timer().schedule(
                    new TimerTask() {
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

    // Method to increment totalEnemiesKilled when an enemy is killed
    private void incrementTotalEnemiesKilled() {
        totalEnemiesKilled++;
    }

    // Method to reduce enemy's health and handle death
    public void reduceHealth(int amount) {
        // Reduce the enemy's health by the specified amount
        health -= amount;
        System.out.println(health);

        // Check if the enemy has been killed
        if (health <= 0) {
            // Increment the totalEnemiesKilled counter
            incrementTotalEnemiesKilled();

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

    // Method to update enemy's damage label
    public void updateDamageLabel(int amount) {
        damageLabel.setText("-" + amount);
        damageLabel.setX(square.getX() - square.getWidth() / 2); // Adjust position to center the label horizontally
        damageLabel.setY(square.getY() - square.getHeight() / 2 - 30); // Adjust position to place the label above the enemy

        // Set initial opacity and translation
        damageLabel.setOpacity(1.0);
        damageLabel.setTranslateY(0);

        // Set an event handler to hide the label when the fade-out animation is finished
        fadeTransition.setOnFinished(event -> {
            damageLabel.setVisible(false);
            damageLabel.setOpacity(1.0); // Reset opacity after animation
            damageLabel.setTranslateY(0); // Reset translation after animation
        });

        // Set an event handler to reset translate transition after its completion
        translateTransition.setOnFinished(event -> {
            damageLabel.setTranslateY(0); // Reset translation after animation
        });

        // Ensure the label is in front of the enemy by setting its Z-order
        damageLabel.toFront();

        // Start the fade-out and translate animations
        fadeTransition.play();
        translateTransition.play();
        damageLabel.setVisible(true); // Make the label visible before starting the animations
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
