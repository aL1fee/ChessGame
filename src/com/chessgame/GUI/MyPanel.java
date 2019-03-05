package com.chessgame.GUI;

import com.chessgame.Board;

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
    private String figuresPngFilepath;
    private Board board;
    private int initialFigurePosX;
    private int initialFigurePosY;
    private int squareSize;

    public MyPanel(Board board) {
        setBorder(BorderFactory.createLineBorder(Color.black));
        width = 700;
        height = 500;
        squareSize = 50;
        this.board = board;
        boardDim = board.getBoardDim();
        figuresPngFilepath = "././res/chess_pieces.png";

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

                System.out.println("initialFigurePosX: " + initialFigurePosX);
                System.out.println("initialFigurePosY: " + initialFigurePosY);
                System.out.println("X: " + x + " and Y: " + y);

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

                System.out.println("finalFigurePosX: " + finalFigurePosX);
                System.out.println("finalFigurePosY: " + finalFigurePosY);
                System.out.println("X: " + x + " and Y: " + y);

                if (initialFigurePosX >= 0 && initialFigurePosX < boardDim &&
                        initialFigurePosY >= 0 && initialFigurePosY < boardDim &&
                        finalFigurePosX >= 0 && finalFigurePosX < boardDim &&
                        finalFigurePosY >= 0 && finalFigurePosY < boardDim &&
                        x > offsetX && x < offsetX + 400 && y > offsetY && y < offsetY + 400) {
                    board.movePiece(initialFigurePosY, initialFigurePosX, finalFigurePosY, finalFigurePosX);
                    repaint();
                }
            }
        });



    }

    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawFigures(g);
//        g.drawString("This is my custom Panel!", 10, 20);
    }

    private void drawBoard(Graphics g) {
        System.out.println("ha");
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

//        for (int i = boardDim - 1; i >= 0; i--) {
        for (int i = 0; i < boardDim; i++) {
            offsetY += squareSize;
            offsetX = 25;
            for (int j = 0; j < boardDim; j++) {
                offsetX += squareSize;
                switch (board.pieceNameAt(i, j)) {
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
    }
}