package io.github.followsclosley.tetris;

import lombok.Getter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class PieceFactory {

    private final Queue<Piece> queue = new LinkedList<>();
    private final Random random = new Random();
    @Getter private int queueSize = 5;
    @Getter private Piece hold;

    public PieceFactory(int queueSize) {
        this.queueSize = queueSize;
        for (int i = 0; i < queueSize; i++) {
            queue.add(getRandomPiece());
        }
    }

    public Piece get() {
        Piece p = queue.remove();
        queue.add(getRandomPiece());
        return p;
    }

    public Piece swapHold(Piece piece) {
        Piece p = (hold != null) ? hold : getRandomPiece();
        hold = getPiece(piece.getIndex());
        return p;
    }

    private Piece getRandomPiece() {
        return getPiece(random.nextInt(7) + 1);
    }

    private Piece getPiece(int index) {
        return switch (index) {
            // -X--
            // XXX-
            case 1 -> new Piece(index, new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(2, 0), new Coordinate(1, 1));
            // -XX-
            // -XX-
            case 2 -> new Piece(index, false, new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(2, 0), new Coordinate(2, 1));
            // X---
            // XXX-
            case 3 -> new Piece(index, new Coordinate(1, 1), new Coordinate(0, 1), new Coordinate(2, 1), new Coordinate(0, 0));
            // ---X
            // -XXX
            case 4 -> new Piece(index, new Coordinate(2, 1), new Coordinate(1, 1), new Coordinate(3, 1), new Coordinate(3, 0));
            // XX--
            // -XX-
            case 5 -> new Piece(index, new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 1));
            // --XX
            // -XX-
            case 6 -> new Piece(index, new Coordinate(2, 0), new Coordinate(3, 0), new Coordinate(1, 1), new Coordinate(2, 1));
            // XXXX
            // ----
            default -> new Piece(index, new Coordinate(1, 0), new Coordinate(0, 0), new Coordinate(2, 0), new Coordinate(3, 0));
        };
    }
}
