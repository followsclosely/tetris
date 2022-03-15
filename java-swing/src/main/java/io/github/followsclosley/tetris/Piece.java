package io.github.followsclosley.tetris;

import lombok.Getter;

import java.util.Arrays;

public class Piece {
    @Getter private int moveCounter = 0;
    @Getter private final int index;
    private final boolean rotates;
    @Getter private Coordinate[] coordinates;

    public Piece(int index, Coordinate... coordinates) {
        this(index, true, coordinates);
    }

    public Piece(int index, boolean rotates, Coordinate... coordinates) {
        this.index = index;
        this.rotates = rotates;
        this.coordinates = coordinates;
    }

    public Piece clone(){
        return new Piece(index, coordinates.clone());
    }

    public boolean translate(int dx, int dy, Grid grid) {
        int length = coordinates.length;
        Coordinate[] translatedCoordinates = new Coordinate[length];

        for (int i = 0; i < length; i++) {
            translatedCoordinates[i] = new Coordinate(coordinates[i].getX() + dx, coordinates[i].getY() + dy);
        }

        return verify(translatedCoordinates, grid);
    }

    public boolean rotate(int direction, Grid grid) {
        if (!rotates) return false;

        int length = coordinates.length;
        Coordinate centroid = coordinates[0];
        Coordinate[] translatedCoordinates = new Coordinate[length];

        for (int i = 0; i < length; i++) {
            translatedCoordinates[i] = new Coordinate(coordinates[i].getY() + (centroid.getX() - centroid.getY()), centroid.getX() + (centroid.getY() - coordinates[i].getX()));
        }

        return verify(translatedCoordinates, grid);
    }

    private boolean verify(Coordinate[] translatedCoordinates, Grid grid) {

        boolean isGood = !Arrays.stream(translatedCoordinates).anyMatch(c ->
                c.getX() < 0 || c.getX() >= grid.getWidth()
                        || c.getY() < 0 || c.getY() >= grid.getHeight()
                        || grid.getBlock(c) != 0);

        if (isGood) {
            synchronized (this) {
                moveCounter++;
            }
            this.coordinates = translatedCoordinates;
        }

        return isGood;
    }
}
