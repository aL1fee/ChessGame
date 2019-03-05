package com.chessgame.GUI;

import com.chessgame.Board;

import javax.swing.*;

public class Window extends JPanel {

    public Window(Board board) {
        JFrame frame = new JFrame("Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new MyPanel(board));
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(true);
    }
}
