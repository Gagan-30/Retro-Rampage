package com.base.game.retrorampage.LevelGeneration;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;
import java.util.ArrayList;
import java.util.List;

public class DelaunayTriangulator {
    private final GeometryFactory geometryFactory;

    public DelaunayTriangulator() {
        this.geometryFactory = new GeometryFactory();
    }

    public List<Coordinate[]> triangulate(List<Coordinate> points) {
        DelaunayTriangulationBuilder builder = new DelaunayTriangulationBuilder();
        builder.setSites(geometryFactory.createMultiPointFromCoords(points.toArray(new Coordinate[0])));
        Geometry triangles = builder.getTriangles(geometryFactory);

        List<Coordinate[]> triangleCoordinates = new ArrayList<>();
        for (int i = 0; i < triangles.getNumGeometries(); i++) {
            Geometry geometry = triangles.getGeometryN(i);
            if (geometry instanceof org.locationtech.jts.geom.Polygon) {
                org.locationtech.jts.geom.Polygon polygon = (org.locationtech.jts.geom.Polygon) geometry;
                triangleCoordinates.add(polygon.getExteriorRing().getCoordinates());
            }
        }

        return triangleCoordinates;
    }
}
