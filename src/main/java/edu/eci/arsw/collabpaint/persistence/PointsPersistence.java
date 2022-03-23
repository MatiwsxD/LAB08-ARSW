package edu.eci.arsw.collabpaint.persistence;

import java.util.List;

import edu.eci.arsw.collabpaint.model.Point;

public interface PointsPersistence {

    public List<Point> getPolygon(String numdibujo, Point pt) throws PointPersistenceExeption ;
}