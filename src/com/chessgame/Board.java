package com.chessgame;

import com.chessgame.pieces.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Board {

    private Piece[][] board;
    public static int boardDim;

    public Board() {
        boardDim = 8;
        board = new Piece[boardDim][boardDim];
        initializeBoard();
    }

    private void initializeBoard() {
        File initFile = new File("././res/initialPosition.txt");
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
                            board[i][j] = new King("k", "w", false);
                            break;
                        case "bk":
                            board[i][j] = new King("k", "b", false);
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

    public void flipBoard() {
        Piece[][] oldBoard = getBoardCopy();
        Piece[][] newBoard = new Piece[boardDim][boardDim];
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                newBoard[i][j] = oldBoard[boardDim - i - 1][boardDim - j - 1];
            }
        }
        board = newBoard;
    }

    public boolean isPossibleMove(int i1, int j1, String command) {
        return getPossibleMoves(i1, j1).contains(command);
    }

    public ArrayList<String> getPossibleMoves(int i1, int j1) {
        ArrayList<String> listOfAllMoves = new ArrayList<>();
        Piece movingPiece = board[i1][j1];
        /* No piece at this location. */
        if (movingPiece == null) {
            return listOfAllMoves;
        }
        ArrayList<String> possibleMovesMoves = movingPiece.getPlausibleMoves();
        for (String str : possibleMovesMoves) {
            int newMoveX = 0;
            int newMoveY = 0;
            for (char j : str.toCharArray()) {
                switch (j) {
                    case 'f':
                        /* Pawn direction check #1. */
                        if (movingPiece.getColor().equals("white")) {
                            newMoveY += 1;
                        } else {
                            newMoveY -= 1;
                        }
                        break;
                    case 'r':
                        newMoveX += 1;
                        break;
                    case 'b':
                        /* Pawn direction check #2. */
                        if (movingPiece.getColor().equals("white")) {
                            newMoveY -= 1;
                        } else {
                            newMoveY += 1;
                        }
                        break;
                    case 'l':
                        newMoveX -= 1;
                        break;
                }
            }
            int newScaX = newMoveX;
            int newScaY = newMoveY;
            while ((j1 + newScaX) >= 0 && (j1 + newScaX) < boardDim &&
                    (i1 + newScaY) >= 0 && (i1 + newScaY) < boardDim) {



                /* Another piece occupies the intended location. */
                if (board[i1 + newScaY][j1 + newScaX] != null) {
                    if (movingPiece.getSide().equals(board[i1 + newScaY][j1 + newScaX].getSide())) {
                        break;
                    }
                /* Pawn eating. */
                } else if (movingPiece.getName().equals("p") && (Math.abs(newScaY) == 1 && Math.abs(newScaX) == 1)) {
                    break;
                }

                listOfAllMoves.add(Integer.toString(i1) + Integer.toString(j1) +
                        Integer.toString(i1 + newScaY) + Integer.toString(j1 + newScaX));
                if (movingPiece.isScalableMoves()) {
                    newScaX += newMoveX;
                    newScaY += newMoveY;
                } else {
                    break;
                }
            }
        }
        return listOfAllMoves;
    }

    public boolean isKingAttacked(String color) {
        String kingNotation = (color.equals("white") ? "w" : "b") + "k";
        System.out.println(kingNotation);
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                if (board[i][j] != null && board[i][j].getFullName().equals(kingNotation)) {
                    return isPositionAttacked(i, j);
                }
            }
        }
        /* The king is not on the board. Error trigger. */
        return false;
    }

    public boolean isPositionAttacked(int i, int j) {
        for (int k = 0; k < boardDim; k++) {
            for (int l = 0; l < boardDim; l++) {
                String possibleAttack = Integer.toString(k) + Integer.toString(l) +
                        Integer.toString(i) + Integer.toString(j);
                if (getPossibleMoves(k, l).contains(possibleAttack)) {
                    /* If the attacking piece is a pawn. */
                    if (pieceAt(k, l).getName().equals("p")) {
                        if (Math.abs(i - k) + Math.abs(j - l) == 2) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void movePiece(int i1, int j1, int i2, int j2) {
        if (board[i1][j1] != null && ((i1 != i2) || (j1 != j2))) {
            board[i2][j2] = board[i1][j1];
            board[i1][j1] = null;
            board[i2][j2].setHasMoved();
        }
    }

    public Piece[][] getBoardCopy() {
        Piece[][] array = new Piece[boardDim][];
        for (int i = 0; i < boardDim; i++) {
            array[i] = board[i].clone();
        }
        return array;

    }

    public void setBoard(Piece[][] array) {
        board = array;
    }

    public int getBoardDim() {
        return boardDim;
    }

    public Piece pieceAt(int i, int j) {
        return board[i][j];
    }

    public String pieceNameAt(int i, int j) {
        if (board[i][j] != null) {
            return board[i][j].getFullName();
        }
        return "-";
    }

    public void printBoard() {
        for (int i = boardDim - 1; i >= 0; i--) {
            System.out.println();
            for (int j = 0; j < boardDim; j++) {
                if (board[i][j] != null) {
                    System.out.print(board[i][j].getFullName() + " ");
                } else {
                    System.out.print("00 ");
                }
            }
        }
    }
}
