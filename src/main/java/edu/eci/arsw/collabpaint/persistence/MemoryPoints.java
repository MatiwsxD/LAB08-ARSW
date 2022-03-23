package edu.eci.arsw.collabpaint.persistence.impl;


import edu.eci.arsw.collabpaint.model.Point;
import edu.eci.arsw.collabpaint.persistence.PointPersistenceExeption;
import edu.eci.arsw.collabpaint.persistence.PointsPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class MemoryPoints implements PointsPersistence{

    ConcurrentHashMap<String,List<Point>> puntos = new ConcurrentHashMap<>();

    @Override
    public List<Point> getPolygon(String numdibujo, Point pt) throws PointPersistenceExeption {
        if(!puntos.containsKey(numdibujo)) {
            puntos.put(numdibujo, new ArrayList<>());
        }
        puntos.get(numdibujo).add(pt);
        if(!(puntos.get(numdibujo).size()>=3)) {
            throw new PointPersistenceExeption("there aren't polygons");
        }
        return puntos.get(numdibujo);
    }

}