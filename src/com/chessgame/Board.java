package com.chessgame;

import com.chessgame.pieces.*;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Board {

    private Piece[][] board;
    private int boardDim;

    public Board() {
        board = new Piece[8][8];
        boardDim = 8;
        initializeBoard();
    }

    private void initializeBoard() {
        File initFile  = new File("././res/initialPosition.txt");
        try {
            Scanner sc = new Scanner(initFile);
            for (int i = 0; i < boardDim; i++) {
                for (int j = 0; j < boardDim; j++) {
                    switch (sc.next()) {
                        case "wp":
                            board[i][j] = new Pawn("p", "w", false);
                            break;
                        case "bp":
                            board[i][j] = new Pawn("p", "b", false);
                            break;
                        case "wn":
                            board[i][j] = new Knight("n", "w", false);
                            break;
                        case "bn":
                            board[i][j] = new Knight("n", "b", false);
                            break;
                        case "wr":
                            board[i][j] = new Rook("r", "w", true);
                            break;
                        case "br":
                            board[i][j] = new Rook("r", "b", true);
                            break;
                        case "wb":
                            board[i][j] = new Bishop("b", "w", true);
                            break;
                        case "bb":
                            board[i][j] = new Bishop("b", "b", true);
                            break;
                        case "wq":
                            board[i][j] = new Queen("q", "w", true);
                            break;
                        case "bq":
                            board[i][j] = new Queen("q", "b", true);
                            break;
                        case "wk":
                            board[i][j] = new Queen("k", "w", false);
                            break;
                        case "bk":
                            board[i][j] = new Queen("k", "b", false);
                            break;
                        case "00":
                            board[i][j] = null;
                            break;
                    }
                }
            }
            sc.close();
        } catch (IOException | InputMismatchException ex) {
            ex.printStackTrace();
        }
    }

    public void printBoard() {
        for (int i = boardDim - 1; i >= 0; i--) {
            System.out.println();
            for (int j = 0; j < boardDim; j++) {
                if (board[i][j] != null) {
                    System.out.print(board[i][j].getName() + " ");
                } else {
                    System.out.print("00 ");
                }
            }
        }
    }


}
