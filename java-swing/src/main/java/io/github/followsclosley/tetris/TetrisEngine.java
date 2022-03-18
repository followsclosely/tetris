package io.github.followsclosley.tetris;

import lombok.Getter;
import lombok.Setter;

public class TetrisEngine implements Runnable {

    @Getter private final Grid grid;
    @Getter private final PieceFactory factory;
    private final boolean running = true;
    @Getter @Setter private Runnable component;
    @Getter private Piece current;

    public TetrisEngine(Grid grid, PieceFactory factory) {
        this.grid = grid;
        this.factory = factory;
    }

    public boolean swapHold() {
        current = factory.swapHold(current);
        return true;
    }

    @Override
    public void run() {
        current = factory.get();
        current.translate(3, 1, grid);
        while (running) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignore) {
            }

            if (current.translate(0, 1, grid)) {

            } else {
                grid.addPiece(current);
                grid.clearFinishedRows();

                current = factory.get();
                current.translate(3, 1, grid);
            }

            component.run();

        }
    }
}
