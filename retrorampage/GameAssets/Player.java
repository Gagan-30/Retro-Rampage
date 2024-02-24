package com.base.game.retrorampage.GameAssets;

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
}
