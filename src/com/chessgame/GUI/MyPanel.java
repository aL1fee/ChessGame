package com.chessgame.GUI;

import com.chessgame.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class MyPanel extends JPanel {

    private int width;
    private int height;
    private int boardDim;
    private int initialFigurePosX;
    private int initialFigurePosY;
    private int squareSize;
    private JFrame frame;
    private String figuresPngFilepath;
    private Game game;

    public MyPanel(Game game, JFrame frame) {
        width = 700;
        height = 500;
        squareSize = 50;
        boardDim = game.getBoard().getBoardDim();
        figuresPngFilepath = "././res/chess_pieces.png";

        this.frame = frame;
        this.game = game;

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {}

            @Override
            public void mouseMoved(MouseEvent e) {}
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                int x = e.getX();
                int y = e.getY();
                int offsetX = 75;
                int offsetY = 50;
                initialFigurePosX = (x - offsetX) / squareSize;
                initialFigurePosY = (y - offsetY) / squareSize;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                int x = e.getX();
                int y = e.getY();
                int offsetX = 75;
                int offsetY = 50;
                int finalFigurePosX = (x - offsetX) / squareSize;
                int finalFigurePosY  = (y - offsetY) / squareSize;

                if (initialFigurePosX >= 0 && initialFigurePosX < boardDim &&
                        initialFigurePosY >= 0 && initialFigurePosY < boardDim &&
                        finalFigurePosX >= 0 && finalFigurePosX < boardDim &&
                        finalFigurePosY >= 0 && finalFigurePosY < boardDim &&
                        x > offsetX && x < offsetX + 400 && y > offsetY && y < offsetY + 400 &&
                        !game.getCurrentPlayer().isAI()) {
                    game.getCurrentPlayer().makeMove(game, initialFigurePosX, initialFigurePosY, finalFigurePosX, finalFigurePosY);
                    repaint();
                }
            }
        });
    }

    private void drawBoard(Graphics g) {
        int offsetX;
        int offsetY = 0;
        for (int i = 0; i < boardDim; i++) {
            offsetY += squareSize;
            offsetX = 25;
            for (int j = 0; j < boardDim; j++) {
                offsetX += squareSize;
                if ((i + j) % 2 == 0) {
                    g.setColor(new Color(0xeeeed2));
                } else {
                    g.setColor(new Color(0x769656));
                }
                g.fillRect(offsetX, offsetY, squareSize, squareSize);
            }
        }
    }

    private void drawFigures(Graphics g) {
        BufferedImage image = null;
        try{
            image = ImageIO.read(new File(figuresPngFilepath));
        } catch (IOException ex) {}
        if (image == null) {
            System.out.println("Pieces image is not found.");
            return;
        }

        int offsetY = 0;
        int offsetX;

        for (int i = 0; i < boardDim; i++) {
            offsetY += squareSize;
            offsetX = 25;
            for (int j = 0; j < boardDim; j++) {
                offsetX += squareSize;
                switch (game.getBoard().pieceNameAt(i, j)) {
                    case "wk":
                        g.drawImage(image, offsetX, offsetY, offsetX + squareSize, offsetY + squareSize,
                                    0, 0, 333, 333, null);
                        break;
                    case "wq":
                        g.drawImage(image, offsetX, offsetY, offsetX + squareSize, offsetY + squareSize,
                                333, 0, 666, 333, null);
                        break;
                    case "wb":
                        g.drawImage(image, offsetX, offsetY, offsetX + squareSize, offsetY + squareSize,
                                666, 0, 999, 333, null);
                        break;
                    case "wn":
                        g.drawImage(image, offsetX, offsetY, offsetX + squareSize, offsetY + squareSize,
                                999, 0, 1332, 333, null);
                        break;
                    case "wr":
                        g.drawImage(image, offsetX, offsetY, offsetX + squareSize, offsetY + squareSize,
                                1332, 0, 1666, 333, null);
                        break;
                    case "wp":
                        g.drawImage(image, offsetX, offsetY, offsetX + squareSize, offsetY + squareSize,
                                1666, 0, 2000, 333, null);
                        break;
                    case "bk":
                        g.drawImage(image, offsetX, offsetY, offsetX + squareSize, offsetY + squareSize,
                                0, 333, 333, 666, null);
                        break;
                    case "bq":
                        g.drawImage(image, offsetX, offsetY, offsetX + squareSize, offsetY + squareSize,
                                333, 333, 666, 666, null);
                        break;
                    case "bb":
                        g.drawImage(image, offsetX, offsetY, offsetX + squareSize, offsetY + squareSize,
                                666, 333, 999, 666, null);
                        break;
                    case "bn":
                        g.drawImage(image, offsetX, offsetY, offsetX + squareSize, offsetY + squareSize,
                                999, 333, 1332, 666, null);
                        break;
                    case "br":
                        g.drawImage(image, offsetX, offsetY, offsetX + squareSize, offsetY + squareSize,
                                1332, 333, 1666, 666, null);
                        break;
                    case "bp":
                        g.drawImage(image, offsetX, offsetY, offsetX + squareSize, offsetY + squareSize,
                                1666, 333, 2000, 666, null);
                        break;
                }
            }
        }
        /* Adding a board border. */
        Graphics2D g2 = (Graphics2D) g;
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(1));
        g.setColor(Color.BLACK);
        g2.drawRect(75, 50, 400, 400);
        g2.setStroke(oldStroke);
    }

    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawString("Moves:", 560, 60);
        drawBoard(g);
        drawFigures(g);
        drawHistoryOfMoves(g);
    }

    public void drawHistoryOfMoves(Graphics g) {
        String movesString = game.getHistoryOfMoves();
        System.out.println(movesString);

        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.BOLD, 14));

        int x = 505;
        int y = 75;
        for (String line : movesString.split("\n")) {
            g.drawString(line, x, y += g.getFontMetrics().getHeight());
        }
    }
}