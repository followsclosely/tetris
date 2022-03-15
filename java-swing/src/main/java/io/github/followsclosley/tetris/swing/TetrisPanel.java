package io.github.followsclosley.tetris.swing;

import io.github.followsclosley.tetris.Coordinate;
import io.github.followsclosley.tetris.Grid;
import io.github.followsclosley.tetris.Piece;
import io.github.followsclosley.tetris.TetrisEngine;

import javax.swing.*;
import java.awt.*;

public class TetrisPanel extends JPanel {

    private final TetrisEngine engine;
    private final Color[] COLORS = {new Color(100, 100, 100), new Color(150, 0, 0), new Color(0, 150, 0), new Color(0, 0, 150), new Color(150, 0, 0), new Color(50, 0, 90), new Color(50, 110, 50), new Color(150, 0, 140)};
    private final int PIECE_SIZE = 15;
    protected Dimension defaultDimension;

    public TetrisPanel(TetrisEngine engine) {
        this.engine = engine;
        this.defaultDimension = new Dimension(engine.getGrid().getWidth() * PIECE_SIZE + 1, engine.getGrid().getHeight() * PIECE_SIZE + 1);
        setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Grid grid = engine.getGrid();

        g.setColor(new Color(40,30,20));
        for (int x = 0, width = engine.getGrid().getWidth(); x <= width; x++) {
            g.drawLine(x * PIECE_SIZE, 0, x * PIECE_SIZE, grid.getHeight() * PIECE_SIZE);
        }
        for (int y = 0, height = engine.getGrid().getHeight(); y <= height; y++) {
            g.drawLine(0, y * PIECE_SIZE, grid.getWidth() * PIECE_SIZE, y * PIECE_SIZE);
        }

        //Paint the blocks on the grid
        for (int x = 0, width = engine.getGrid().getWidth(); x < width; x++) {
            for (int y = 0, height = engine.getGrid().getHeight(); y < height; y++) {
                int block = grid.getBlock(x, y);
                if (block > 0) {
                    g.setColor(COLORS[block]);
                    g.fill3DRect(x * PIECE_SIZE, y * PIECE_SIZE, PIECE_SIZE, PIECE_SIZE, true);
                }
            }
        }

        //Paint the current piece
        Piece current = engine.getCurrent();
        if (current != null) {
            for (Coordinate c : current.getCoordinates()) {
                g.setColor(COLORS[current.getIndex()]);
                g.fill3DRect(c.getX() * PIECE_SIZE, c.getY() * PIECE_SIZE, PIECE_SIZE, PIECE_SIZE, true);
            }
        }
    }

    @Override public Dimension getPreferredSize() {
        return defaultDimension;
    }
}
