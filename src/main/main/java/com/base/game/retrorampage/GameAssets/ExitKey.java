package com.base.game.retrorampage.GameAssets;

import com.base.game.retrorampage.LevelGeneration.Cell;
import com.base.game.retrorampage.LevelGeneration.RoomManager;
import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

public class ExitKey extends Sprite {
    private final Player player;
    private final ImageView imageView;
    private final Pane root; // Add this field
    private boolean active;
    private boolean hasKey;
    private RoomManager roomManager;

    public ExitKey(double size, String imagePath, Player player, Pane root, RoomManager roomManager) {
        super(imagePath, size);
        this.player = player;
        this.root = root; // Initialize the root field
        this.imageView = new ImageView(imagePath);
        this.imageView.setFitWidth(size);
        this.imageView.setFitHeight(size);
        this.imageView.setPreserveRatio(true);
        this.active = false;
        this.hasKey = false;
        this.roomManager = roomManager; // Assign the roomManager parameter to the field
    }


    public void update() {
        if (isActive() && !hasKey) { // Check if the key is not collected yet
            // Check for collision with the player
            if (collidesWith(player.getBoundsInParent())) {
                // Increase player's health and deactivate the exit key
                hasKey = true; // Set the hasKey value to true when the player collects the key
                setActive(false);
                removeFromPane(); // Remove the exit key from the root
            }
        }
    }


    private boolean collidesWith(Bounds playerBounds) {
        Bounds healthItemBounds = getBoundsInParent();
        return healthItemBounds.intersects(playerBounds);
    }

    public void removeFromPane() {
        // Assuming you have a method to remove the health item from the root
        root.getChildren().remove(this.imageView);
    }

    private void draw() {
        // Assuming you have a method to add the health item to the root
        root.getChildren().add(this.imageView);
    }

    public void spawnRandomly() {
        draw();
        if (!isActive()) {
            Cell randomRoom = getRandomRoom(); // Get a random room from the roomManager
            if (randomRoom != null) {
                setRandomPosition(randomRoom); // Set the position randomly within the room
                setActive(true); // Activate the health item
            }
        }
    }

    private Cell getRandomRoom() {
        List<Cell> rooms = roomManager.getCells();
        if (!rooms.isEmpty()) {
            int randomIndex = (int) (Math.random() * rooms.size());
            return rooms.get(randomIndex);
        }
        return null;
    }

    private void setRandomPosition(Cell room) {
        double roomX = room.getX();
        double roomY = room.getY();
        double roomWidth = room.getWidth();
        double roomHeight = room.getHeight();

        // Calculate random position within the room, ensuring it stays within bounds
        double randomX = Math.max(roomX, Math.min(roomX + roomWidth - imageView.getFitWidth(), roomX + Math.random() * roomWidth));
        double randomY = Math.max(roomY, Math.min(roomY + roomHeight - imageView.getFitHeight(), roomY + Math.random() * roomHeight));

        // Set the position of the health item
        imageView.relocate(randomX, randomY);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean hasKey() {
        return hasKey;
    }

    public Bounds getBoundsInParent() {
        return imageView.getBoundsInParent();
    }

}
