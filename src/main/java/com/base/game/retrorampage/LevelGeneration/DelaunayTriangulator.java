package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DelaunayTriangulator {
    private List<Edge> edges;
    private final GeometryFactory geometryFactory;

    public DelaunayTriangulator() {
        this.geometryFactory = new GeometryFactory();
        edges = new ArrayList<>();
    }

    public List<Coordinate[]> triangulate(List<Coordinate> roomCenters) {
        // Create an instance of GeometryFactory, which is a utility class to create geometry objects.
        GeometryFactory geometryFactory = new GeometryFactory();

        // Instantiate DelaunayTriangulationBuilder, a class that builds the Delaunay triangulation for a set of points.
        DelaunayTriangulationBuilder builder = new DelaunayTriangulationBuilder();

        // Convert the list of room center points into a MultiPoint geometry and set it as the input for triangulation.
        builder.setSites(geometryFactory.createMultiPointFromCoords(roomCenters.toArray(new Coordinate[0])));

        // Perform the triangulation and retrieve a Geometry collection representing the triangles.
        org.locationtech.jts.geom.Geometry triangles = builder.getTriangles(geometryFactory);

        // Prepare a list to hold the coordinates of each triangle's vertices.
        List<Coordinate[]> triangleCoordinates = new ArrayList<>();

        // Iterate through each geometry in the collection, which represents a triangle.
        for (int i = 0; i < triangles.getNumGeometries(); i++) {
            // Cast the geometry to a Polygon object to access its vertices (each triangle is represented as a polygon).
            org.locationtech.jts.geom.Polygon triangle = (org.locationtech.jts.geom.Polygon) triangles.getGeometryN(i);

            // Extract the coordinates of the triangle's vertices and add them to the list.
            triangleCoordinates.add(triangle.getCoordinates());
        }

        // Return the list of triangles represented by arrays of coordinates.
        return triangleCoordinates;
    }
}
