package io.github.followsclosley.tetris;

import lombok.Getter;

import java.util.Arrays;

public class Grid {
    int[][] blocks;
    @Getter private int width = 10;
    @Getter private int height = 20;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.blocks = new int[height][width];
    }

    public void addPiece(Piece p) {
        for (Coordinate c : p.getCoordinates()) {
            blocks[c.getY()][c.getX()] = p.getIndex();
        }
    }

    public int clearFinishedRows() {
        int count = 0;
        int[] counts = new int[height];

        for (int y = height - 1; y >= 0; y--) {
            counts[y] = count;
            boolean containsZero = Arrays.stream(blocks[y]).anyMatch(x -> x == 0);
            if (!containsZero) {
                count++;
            }
        }

        for (int y = height - 1; y >= 0; y--) {
            if (counts[y] > 0) {
                System.arraycopy(blocks[y], 0, blocks[y + counts[y]], 0, width);
            }
        }

        if (counts[0] > 0) {
            for (int x = 0; x < width; x++) {
                blocks[0][x] = 0;
            }
        }

        return count;
    }

    public int getBlock(Coordinate c) {
        return getBlock(c.getX(), c.getY());
    }

    public int getBlock(int x, int y) {
        return blocks[y][x];
    }
}