package edu.eci.arsw.collabpaint.persistence;
public class PointPersistenceExeption extends Exception {
    public PointPersistenceExeption(String message) {
        super(message);
    }

    public PointPersistenceExeption(String message, Throwable cause) {
        super(message, cause);
    }
}