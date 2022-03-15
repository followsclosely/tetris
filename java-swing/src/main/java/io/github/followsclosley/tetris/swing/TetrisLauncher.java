package io.github.followsclosley.tetris.swing;

import io.github.followsclosley.tetris.Grid;
import io.github.followsclosley.tetris.PieceFactory;
import io.github.followsclosley.tetris.TetrisEngine;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class TetrisLauncher {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        TetrisEngine engine = new TetrisEngine(new Grid(10, 20), new PieceFactory(5));
        TetrisLauncher.show(engine);
    }

    public static void show(TetrisEngine engine) {
        TetrisPanel tetrisPanel = new TetrisPanel(engine);
        tetrisPanel.init(25);

        engine.setComponent(tetrisPanel::repaint);

        JFrame frame = new JFrame("Tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(tetrisPanel);
        frame.pack();
        frame.setVisible(true);

        Map<Integer, KeyPressedAction> keys = new HashMap<>();
        keys.put(KeyEvent.VK_RIGHT, (e) -> engine.getCurrent().translate(+1, 0, engine.getGrid()));
        keys.put(KeyEvent.VK_LEFT, (e) -> engine.getCurrent().translate(-1, 0, engine.getGrid()));
        keys.put(KeyEvent.VK_UP, (e) -> engine.getCurrent().rotate(-1, engine.getGrid()));
        keys.put(KeyEvent.VK_SPACE, (e) -> {
            boolean repaintNeeded = false;
            while (engine.getCurrent().translate(0, +1, engine.getGrid())) {
                repaintNeeded = true;
            }
            return repaintNeeded;
        });

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                KeyPressedAction action = keys.get(e.getKeyCode());
                if (action != null) {
                    boolean repaint = action.perform(e);
                    if (repaint) {
                        SwingUtilities.invokeLater(tetrisPanel::repaint);
                    }
                }
            }
        });

        new Thread(engine).start();
    }

    public interface KeyPressedAction {
        boolean perform(KeyEvent e);
    }
}