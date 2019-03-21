package com.chessgame;

import com.chessgame.pieces.*;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Board implements Serializable {

    private int boardDim;
    private int moveNumber;
    private String lastMove;
    private String initialPositionFile;
    private String debutFileLocation;
    private Piece[][] board;
    private HashMap<Integer, Character> notationMap;
    private ArrayList<String> debutMoves;

    public Board() {
        this(new Piece[8][8], "", 0, true);
    }

    public Board(Piece[][] board, String lastMove, int moveNumber, boolean needInitTheBoard) {
        boardDim = 8;
        notationMap = new HashMap<>();
        this.moveNumber = moveNumber;
        this.board = board;
        this.lastMove = lastMove;
        debutFileLocation = "././res/debut_moves.txt";
        initialPositionFile = "././res/initialPosition.txt";
//        initialPositionFile = "././res/test_positon.txt";

        initDebutMoves();
        if (needInitTheBoard) {
            initializeBoard();
        }
        initNotationMap();
    }

    private void initDebutMoves() {
        debutMoves = new ArrayList<>();
        File initFile = new File(debutFileLocation);
        try {
            Scanner sc = new Scanner(initFile);
            while (sc.hasNextLine()) {
                debutMoves.add(sc.nextLine());
            }
            sc.close();
        } catch (IOException | InputMismatchException ex) {
            ex.printStackTrace();
        }
    }

    private void initializeBoard() {
        File initFile = new File(initialPositionFile);
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

    public boolean isPossibleMove(int i1, int j1, String command, boolean boardRotated) {
        return getPossibleMoves(i1, j1, boardRotated).contains(command);
    }

    public ArrayList<String> getPossibleMoves(int i1, int j1, boolean boardRotated) {
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
                            newMoveY = (boardRotated) ? newMoveY + 1 : newMoveY - 1;
//                            newMoveY -= 1;
                        } else {
                            newMoveY = (boardRotated) ? newMoveY - 1 : newMoveY + 1;
//                            newMoveY += 1;
                        }
                        break;
                    case 'r':
                        newMoveX += 1;
                        break;
                    case 'b':
                        /* Pawn direction check #2. */
                        if (movingPiece.getColor().equals("white")) {
                            newMoveY = (boardRotated) ? newMoveY - 1 : newMoveY + 1;
//                            newMoveY += 1;
                        } else {
                            newMoveY = (boardRotated) ? newMoveY + 1 : newMoveY - 1;
//                            newMoveY -= 1;
                        }
                        break;
                    case 'l':
                        newMoveX -= 1;
                        break;
                }
            }
            int newScaX = newMoveX;
            int newScaY = newMoveY;
            boolean scalPieceEncounteredFirstEnemy = false;
            while ((j1 + newScaX) >= 0 && (j1 + newScaX) < boardDim &&
                    (i1 + newScaY) >= 0 && (i1 + newScaY) < boardDim) {
                /* Making sure a piece doesn't jump over enemy pieces. */
                if (scalPieceEncounteredFirstEnemy) {
                    break;
                }
                /* Another piece occupies the intended location. */
                if (board[i1 + newScaY][j1 + newScaX] != null) {
                    if (movingPiece.getSide().equals(board[i1 + newScaY][j1 + newScaX].getSide())) {
                        break;
                    } else {
                        scalPieceEncounteredFirstEnemy = true;
                    }
                    /* Pawn can't pass forward if there is another piece. . */
                    if (movingPiece.getName().equals("p") && ((Math.abs(newScaY) == 1 ||  Math.abs(newScaY) == 2) &&
                            Math.abs(newMoveX) == 0)) {
                        break;
                    }
                }
                /* Pawn eating. */
                else if (movingPiece.getName().equals("p") && (Math.abs(newScaY) == 1 && Math.abs(newScaX) == 1)) {
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

    public void movePiece(int i1, int j1, int i2, int j2, String promotion, boolean boardRotated) {
        if (board[i1][j1] != null && ((i1 != i2) || (j1 != j2))) {
            /* Setting the last move string. */
            String entireMoveString;

            if (!boardRotated) {
                String pos1Y = Integer.toString(boardDim - i1);
                String pos2Y = Integer.toString(boardDim - i2);
                entireMoveString = String.valueOf(notationMap.get(j1)) + pos1Y;
                if (board[i2][j2] != null) {
                    entireMoveString += "x";
                } else {
                    entireMoveString += "-";
                }
                entireMoveString += String.valueOf(notationMap.get(j2)) + pos2Y;
            } else {
                String pos1Y = Integer.toString(i1 + 1);
                String pos2Y = Integer.toString(i2 + 1);
                entireMoveString = String.valueOf(notationMap.get(boardDim - j1 - 1)) + pos1Y;
                if (board[i2][j2] != null) {
                    entireMoveString += "x";
                } else {
                    entireMoveString += "-";
                }
                entireMoveString += String.valueOf(notationMap.get(boardDim - j2 - 1)) + pos2Y;
            }

            /* Making the move. */
            if (promotion != null) {
                String side = board[i1][j1].getSide();
                board[i1][j1] = null;
                switch (promotion) {
                    case "Knight":
                        board[i2][j2] = new Knight("n", side, false);
                        break;
                    case "Bishop":
                        board[i2][j2] = new Bishop("b", side, true);
                        break;
                    case "Rook":
                        board[i2][j2] = new Rook("r", side, true);
                        break;
                    case "Queen":
                        board[i2][j2] = new Queen("q", side, true);
                        break;
                }
            } else {
                board[i2][j2] = board[i1][j1];
                board[i1][j1] = null;
                board[i2][j2].setHasMoved(true);
                moveNumber++;
            }

            /* Seeing if that was a check. */
            if (isKingAttacked(pieceAt(i2, j2).getEnemySideFull(), boardRotated)) {
//                if (isMate(pieceAt(i2, j2).getEnemySideFull(), boardRotated) ||
//                        isMate(pieceAt(i2, j2).getColor(), boardRotated)) {
//                    entireMoveString += "#";
//                }
                entireMoveString += "+";
            }
            lastMove = entireMoveString;
        }
    }

    public void makeMoveForAI(int startX, int startY, int endX, int endY, String color, boolean boardRotated) {
        Piece currentPiece = pieceAt(startY, startX);
        String parsedCommand = Integer.toString(startY) + Integer.toString(startX) +
                Integer.toString(endY) + Integer.toString(endX);
        if (currentPiece != null && currentPiece.getColor().equals(color)) {
            if (isPossibleMove(startY, startX, parsedCommand, boardRotated)) {
                if (isPromotionMove(startY, startX, parsedCommand)) {
                    String side = board[startY][startX].getSide();
                    board[startY][startX] = null;
                    board[endY][endX] = new Queen("q", side, true);
                }
                /* Handling castling. */
//                else if (board.isCastleMove(startY, startX, parsedCommand, boardRotated)) {
//                    board.castleMove(startY, startX, parsedCommand, boardRotated);
//
//                    /* implement . */
//
//                } else if (board.isIllegalKingMove(startY, startX, parsedCommand, boardRotated)) {
//                    return;
                else if (board[startY][startX] != null && ((startY != endY) || (startX != endX))) {
                    board[endY][endX] = board[startY][startX];
                    board[startY][startX] = null;
                    lastMove = lastMove + Integer.toString(startY) + Integer.toString(startX) +
                            Integer.toString(endY) + Integer.toString(endX);
                }

                /* Checking if the king is under attack. */
                if (isKingAttacked(color, boardRotated)) {
                    /* implement */
                }
            } else {
                /* implement an incorrect move. */
            }
        }
    }

    public boolean isPromotionMove(int i1, int j1, String command) {
        Piece movingPiece = board[i1][j1];
        if (movingPiece.getName().equals("p") &&
                (Integer.parseInt(String.valueOf(command.charAt(2))) == 7 ||
                        Integer.parseInt(String.valueOf(command.charAt(2))) == 0)) {
            return true;
        }
        return false;
    }

    public void castleMove(int i1, int j1, String command, boolean boardRotated) {

        /* implement */
    }

    public boolean isCastleMove(int i1, int j1, String command, boolean boardRotated) {

        /* implement */

        return false;
    }

    public boolean isIllegalKingMove(int i1, int j1, String command, boolean boardRotated) {
        int newY = Integer.parseInt(String.valueOf(command.charAt(2)));
        int newX = Integer.parseInt(String.valueOf(command.charAt(3)));
        Piece movingPiece = board[i1][j1];
        if (movingPiece.getName().equals("k") && (Math.abs(newY - i1) + Math.abs(newX - j1)) > 1) {
            return true;
        }
        return false;
    }

    private boolean isMate(String color, boolean boardRotated) {

        /* implement */

        return false;
    }

    public boolean isKingAttacked(String color, boolean boardRotated) {
        String kingNotation = (color.equals("white") ? "w" : "b") + "k";
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                if (board[i][j] != null && board[i][j].getFullName().equals(kingNotation)) {
                    return isPositionAttacked(i, j, boardRotated);
                }
            }
        }
        /* The king is not on the board. Error trigger. */
        return false;
    }

    public boolean isPositionAttacked(int i, int j, boolean boardRotated) {
        for (int k = 0; k < boardDim; k++) {
            for (int l = 0; l < boardDim; l++) {
                String possibleAttack = Integer.toString(k) + Integer.toString(l) +
                        Integer.toString(i) + Integer.toString(j);
                if (getPossibleMoves(k, l, boardRotated).contains(possibleAttack)) {
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

    /* Evaluates the board: positive for white, negative for black. */
    public double getBoardValue(boolean isBoardRotated) {
        double score = 0;
        /* Strategy #1: the quantity of pieces. */
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                if (pieceAt(i, j) != null) {
                    switch (pieceAt(i, j).getFullName()) {
                        case "wp": score += 1; break;
                        case "bp": score -= 1; break;
                        case "wn": score += 3; break;
                        case "bn": score -= 3; break;
                        case "wr": score += 5; break;
                        case "br": score -= 5; break;
                        case "wb": score += 3; break;
                        case "bb": score -= 3; break;
                        case "wq": score += 9; break;
                        case "bq": score -= 9; break;
                    }
                }
            }
        }
        /* Strategy #2: if king is attacked. */
//        if (isKingAttacked("white", isBoardRotated)) {
//            score = score - .5;
//        } else if (isKingAttacked("black", isBoardRotated)) {
//            score = score + .5;
//        }
        return score;
    }

    public double closenessToEnemyKing(String str) {
        int i2 = Character.getNumericValue(str.charAt(2));
        int j2 = Character.getNumericValue(str.charAt(3));
        String kingNotation = (pieceAt(i2, j2).getEnemySideFull().equals("white") ? "w" : "b") + "k";
        double result = 0;
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                if (board[i][j] != null && board[i][j].getFullName().equals(kingNotation)) {
                    double distance = Math.pow((i2 - i),2) + Math.pow((j2 - j), 2);
                    if (isBetween(distance, 0, 15)) {
                        result += .5;
                    } else if (isBetween(distance, 15, 30)) {
                        result += .25;
                    } else if (isBetween(distance, 20, 40)) {
                        result += .1;
                    }
                }
            }
        }
        return result;
    }

    public String getRandomMove(String color, boolean boardRotated) {
        ArrayList<String> possibleMoves = new ArrayList<>();
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                if (pieceAt(i, j) != null && pieceAt(i, j).getColor().equals(color)) {
                    possibleMoves.addAll(getPossibleMoves(i, j, boardRotated));
                }
            }
        }
        if (possibleMoves.size() == 0) {
            System.out.println("No moves whatsoever -> mate.");
            return null;
        } else {
            return possibleMoves.get(0);
        }
    }

    public Piece[][] getBoardCopy() {
        Piece[][] array = new Piece[boardDim][boardDim];
        try {
            for (int i = 0; i < boardDim; i++) {
                for (int j = 0; j < boardDim; j++) {
                    if (board[i][j] == null) {
                        array[i][j] = null;
                    } else {
                        array[i][j] = board[i][j].clone();
                    }
                }
            }
        } catch (CloneNotSupportedException ex) {}
        return array;
    }

    public boolean hasPieceMoved(String str) {
        for (int i = 0; i < boardDim; i++) {
            for (int j = 0; j < boardDim; j++) {
                if (pieceAt(i, j) != null) {
                    if (pieceAt(i, j).getFullName().equals(str) && pieceAt(i, j).isHasMoved()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void initNotationMap() {
        notationMap.put(0, 'a');
        notationMap.put(1, 'b');
        notationMap.put(2, 'c');
        notationMap.put(3, 'd');
        notationMap.put(4, 'e');
        notationMap.put(5, 'f');
        notationMap.put(6, 'g');
        notationMap.put(7, 'h');
    }

    public String pieceNameAt(int i, int j) {
        if (board[i][j] != null) {
            return board[i][j].getFullName();
        }
        return "-";
    }

    public void printDebutMoves() {
        System.out.println();
        for (String str : debutMoves) {
            System.out.print(str + ", ");
        }
    }

    public void printBoard() {
        for (int i = 0; i < boardDim; i++) {
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

    public String getLastMove() {
        return lastMove;
    }

    private static boolean isBetween(double num, int x, int y) {
        return num > x && num < y;
    }

    public boolean isBeginningOfTheGame() {
        return moveNumber < 5;
    }

    public boolean isMiddleOfTheGame() {
        return moveNumber > 5 && moveNumber < 20;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int x) {
        moveNumber = x;
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

    public boolean isDebutMove(String str) {
        return debutMoves.contains(str);
    }
}
