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
    private int pieceSize;
    protected Dimension defaultDimension;

    public TetrisPanel(TetrisEngine engine) {
        this.engine = engine;
        this.init(30);
        setBackground(Color.BLACK);
    }
    public void init(int pieceSize){
        this.pieceSize = pieceSize;
        this.defaultDimension = new Dimension(engine.getGrid().getWidth() * pieceSize + 1, engine.getGrid().getHeight() * pieceSize + 1);
    }

    @Override
    public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);

        Grid grid = engine.getGrid();

        g.setColor(new Color(40,30,20));
        for (int x = 0, width = engine.getGrid().getWidth(); x <= width; x++) {
            g.drawLine(x * pieceSize, 0, x * pieceSize, grid.getHeight() * pieceSize);
        }
        for (int y = 0, height = engine.getGrid().getHeight(); y <= height; y++) {
            g.drawLine(0, y * pieceSize, grid.getWidth() * pieceSize, y * pieceSize);
        }

        //Paint the blocks on the grid
        for (int x = 0, width = engine.getGrid().getWidth(); x < width; x++) {
            for (int y = 0, height = engine.getGrid().getHeight(); y < height; y++) {
                int block = grid.getBlock(x, y);
                if (block > 0) {
                    g.setColor(COLORS[block]);
                    g.fill3DRect(x * pieceSize, y * pieceSize, pieceSize, pieceSize, true);
                }
            }
        }
        Piece current = engine.getCurrent();

        //Paint the current piece
        if (current != null) {
            Piece shadow = current.clone();
            while (shadow.translate(0,+1, grid));
            for (Coordinate c : shadow.getCoordinates()) {
                g.setColor(COLORS[shadow.getIndex()]);
                g.drawRect(c.getX() * pieceSize, c.getY() * pieceSize, pieceSize, pieceSize);
            }

            for (Coordinate c : current.getCoordinates()) {
                g.setColor(COLORS[current.getIndex()]);
                g.fill3DRect(c.getX() * pieceSize, c.getY() * pieceSize, pieceSize, pieceSize, true);
            }
        }



    }

    @Override public Dimension getPreferredSize() {
        return defaultDimension;
    }
}
