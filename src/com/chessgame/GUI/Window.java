package com.chessgame.GUI;

import com.chessgame.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JPanel {

    private JMenuBar menuBar;
    private Game game;
    private MyPanel myPanel;
    private JFrame frame;
    private String gameSavesPath;

    public Window(Game game) {
        this.game = game;

        gameSavesPath = "././res/";

        frame = new JFrame("Chess Game");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myPanel = new MyPanel(this.game, frame);
        frame.add(myPanel);






        createMenu();
        frame.setJMenuBar(menuBar);

        frame.validate();

        frame.pack();



        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(true);
    }

    private void createMenu() {
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem m1_1 = new JMenuItem("New game");
        JMenuItem m1_2 = new JMenuItem("Load");
        JMenuItem m1_3 = new JMenuItem("Save");
        JMenuItem m1_4 = new JMenuItem("Quit");

        m1_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.newGame();
                myPanel.repaint();
            }
        });

        m1_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = JOptionPane.showInputDialog(frame, "Enter the file name.",
                        "Loading the game.", JOptionPane.INFORMATION_MESSAGE);
                game.deserialize(gameSavesPath + message);

                myPanel.repaint();
                myPanel.updateHistoryOfMovesText();


            }
        });

        m1_3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String message = JOptionPane.showInputDialog(frame, "Enter the file name.",
                        "Saving the game.", JOptionPane.INFORMATION_MESSAGE);
                System.out.println(message);
                // check for filename!
                game.serialize(gameSavesPath + message);





            }
        });
        m1_4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menu.add(m1_1);
        menu.add(m1_2);
        menu.add(m1_3);
        menu.add(m1_4);
        menuBar.add(menu);

        JMenu menu2 = new JMenu("Settings");
        JMenuItem m2_1 = new JMenuItem("AI difficulty");
        JMenuItem m2_2 = new JMenuItem("Rotate the board");
        menu2.add(m2_1);
        menu2.add(m2_2);

        m2_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Not implemented yet.");
            }
        });

        m2_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Not implemented yet.");
            }
        });

        menuBar.add(menu2);

    }
}
