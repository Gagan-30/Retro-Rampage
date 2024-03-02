package com.base.game.retrorampage.GameAssets;

import javafx.scene.image.Image;

public class Enemy extends Sprite {
    private double speed;

    public Enemy(Image image, double x, double y, double speed) {
        super(image, x, y);
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}