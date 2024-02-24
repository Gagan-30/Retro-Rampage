package com.base.game.retrorampage.GameAssets;

import javafx.scene.image.Image;

public class Texture {

    protected Image image;
    private double scaleFactor;

    public Texture(String imagePath, double scaleFactor) {
        this.scaleFactor = scaleFactor;
        this.image = new Image(getClass().getResourceAsStream(imagePath));
    }


    public Image getImage() {
        return image;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }
}
