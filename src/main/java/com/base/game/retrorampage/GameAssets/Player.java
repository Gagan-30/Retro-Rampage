package com.base.game.retrorampage.GameAssets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player extends Sprite {
    private float speed; // Movement speed

    public Player(float x, float y, int width, int height, Texture texture, float speed) {
        super(x, y, width, height, texture);
        this.speed = speed;
    }

    @Override
    public void update() {
        // Implement movement logic here
        // Example: this.x += speed;
    }

    public void attack() {
        // Attack logic
    }

    public void render(GraphicsContext gc) {
            Image playerImage = this.getTexture().getImage();
            double scaleFactor = this.getTexture().getScaleFactor();
            double scaledWidth = playerImage.getWidth() * scaleFactor;
            double scaledHeight = playerImage.getHeight() * scaleFactor;
            gc.drawImage(playerImage, this.getX(), this.getY(), scaledWidth, scaledHeight);
    }
}
