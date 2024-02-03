package com.base.game.retrorampage.LevelGeneration;

import javafx.application.Application;
import javafx.stage.Stage;
import org.poly2tri.Poly2Tri;
import org.poly2tri.geometry.polygon.Polygon;
import org.poly2tri.geometry.polygon.PolygonPoint;
import org.poly2tri.triangulation.delaunay.DelaunayTriangle;

import java.util.Arrays;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Level Generator");
        LevelGenerator levelGenerator = new LevelGenerator(15);
        primaryStage.setScene(levelGenerator.generateLevel());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}