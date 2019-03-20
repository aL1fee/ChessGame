package com.chessgame;

import com.chessgame.GUI.Window;
import com.chessgame.Utils.BoardHistory;
import com.chessgame.Utils.GameState;
import com.chessgame.agents.AIPlayer;
import com.chessgame.agents.HumanPlayer;
import com.chessgame.agents.Player;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game {

    private Board board;
    private Player currentTurnPlayer;
    private Player whitePlayer;
    private Player blackPlayer;
    private BoardHistory boardHist;
    private ArrayList<String> historyOfMoves;
    private Window window;

    private Timer timer;

    private boolean gameFinished;
//    private LinkedList<GameState> gameStateHistory;
    private boolean boardRotated;

    public Game() {
        board = new Board();
        boardHist = new BoardHistory(board.getBoardCopy());
        whitePlayer = new HumanPlayer("white");
        blackPlayer = new AIPlayer("black", board);
        currentTurnPlayer = whitePlayer;
        historyOfMoves = new ArrayList<>();
        gameFinished = false;

        boardRotated = false;

//        gameStateHistory = new LinkedList<>();


        timer = new Timer();

        window = new Window(this);
    }

    private void switchCurrentPlayer() {
        currentTurnPlayer = (currentTurnPlayer == whitePlayer) ? blackPlayer : whitePlayer;
    }

    public void parseCommand(int startX, int startY, int endX, int endY) {
//        System.out.println("Z: " + startX + "" + startY + "" + endX + "" + endY);
        Piece currentPiece = board.pieceAt(startY, startX);
        String parsedCommand = Integer.toString(startY) + Integer.toString(startX) +
                Integer.toString(endY) + Integer.toString(endX);
        if (currentPiece != null && currentPiece.getColor().equals(currentTurnPlayer.getColor())) {
            if (board.isPossibleMove(startY, startX, parsedCommand, boardRotated)) {
                if (board.isPromotionMove(startY, startX, parsedCommand)) {
                    /* Pawn promotion move. */
                    String[] promotionChoices = { "Knight", "Bishop", "Rook", "Queen" };
                    String promotion = (String) JOptionPane.showInputDialog(window.getFrame(),
                            "Choose your choice of promotion.",
                            "Pawn promotion",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            promotionChoices,
                            promotionChoices[0]);
                    if (promotion == null) {
                        return;
                    }
                    board.movePiece(startY, startX, endY, endX, promotion, boardRotated);
                }

//                else if (board.isCastleMove(startY, startX, parsedCommand, boardRotated)) {
//                    board.castleMove(startY, startX, parsedCommand, boardRotated);
//
//
//                    //implement
//
//                } else if (board.isIllegalKingMove(startY, startX, parsedCommand, boardRotated)) {
//                    return;
                else {
                    board.movePiece(startY, startX, endY, endX, null, boardRotated);
                }
                historyOfMoves.add(board.getLastMove());
                /* Update the board history. */
                boardHist.addBoard(board.getBoardCopy());
                /* Checking if the king is under attack. */
                if (board.isKingAttacked(currentTurnPlayer.getColor(), boardRotated)) {
                    board.setBoard(boardHist.getPrevious());
                    historyOfMoves.remove(historyOfMoves.size() - 1);
                    board.setMoveNumber(board.getMoveNumber() - 1);
                    System.out.println("The king is under attack, try another move.");
                } else {
                    switchCurrentPlayer();
                    if (currentTurnPlayer.isAI()) {
                        Game g = this;
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                currentTurnPlayer.makeMove(g);
                                window.getFrame().repaint();
                            }
                        }, 0);

                    }
                }
            } else {
                System.out.println("Incorrect move, try again.");
            }
        }



//        if (currentTurnPlayer.isAI()) {
//            currentTurnPlayer.makeMove(this);
//            switchCurrentPlayer();
//        }

//        printHistoryOfMoves();
    }

//    private void startTheGame() {
//        board.printBoard();
//    }

    private boolean isGameFinished() {
        return gameFinished;
    }

    private Player getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    private void printHistoryOfMoves() {
        int temp = 0;
        int count = 1;
        System.out.println();
        System.out.println();
        for (String i : historyOfMoves) {
            if (temp % 2 == 0) {
                System.out.print(count + ". " + i + " ");
                count++;
            } else {
                System.out.print(" " + i + " ");
            }
            temp++;
        }
    }

    public String getHistoryOfMoves() {
        String allMoves = "";
        int count = 1;
        int temp = 1;
        System.out.println();
        System.out.println();
        for (String i : historyOfMoves) {
            if (temp % 2 != 0) {
                allMoves += count + ". " + i + " ";
            } else {
                allMoves += " " + i + " ";
                count++;
            }
            if (temp % 3 == 0 && temp != 0) {
                allMoves += "\n";
            }
            temp++;
        }
        return allMoves;
    }

    public Board getBoard() {
        return board;
    }

    public void rotateTheBoard() {
        int boardDim = 8;
        ArrayList<Piece[][]> newBoardHist = new ArrayList<>();
        for (Piece[][] b : boardHist.getAll()) {
            Piece[][] newBoard = new Piece[boardDim][boardDim];
            for (int i = 0; i < boardDim; i++) {
                for (int j = 0; j < boardDim; j++) {
                    newBoard[i][j] = b[boardDim - i - 1][boardDim - j - 1];
                }
            }
            newBoardHist.add(newBoard);
        }

        boardHist.setBoards(newBoardHist);
        boardRotated = !boardRotated;

        String lastMove = "";
        if (historyOfMoves.size() != 0) {
            lastMove = historyOfMoves.get(historyOfMoves.size() - 1);
        }
        this.board = new Board(boardHist.getCurrent(), lastMove, board.getMoveNumber(), false);

//        board.rotateBoard();
    }

    public void undoMove() {
        if (historyOfMoves.size() > 0) {
            String lastMove = "";
            /* Handling the moves text. */
            if (historyOfMoves.size() != 0) {
                lastMove = historyOfMoves.get(historyOfMoves.size() - 1);
            }
            /* Updating the state of the game. */
            this.board = new Board(boardHist.getPrevious(), lastMove, board.getMoveNumber() - 1, false);
            historyOfMoves.remove(historyOfMoves.size() - 1);
            switchCurrentPlayer();
        }
    }

    public void newGame() {
        board = new Board();
        boardHist = new BoardHistory(board.getBoardCopy());
        whitePlayer = new HumanPlayer("white");
        blackPlayer = new HumanPlayer("black");
        currentTurnPlayer = whitePlayer;
        historyOfMoves = new ArrayList<>();
        gameFinished = false;
    }

    public void newGame(String color) {

        // implement AI Colors

        board = new Board();
        boardHist = new BoardHistory(board.getBoardCopy());
        if (color.equals("white")) {
            whitePlayer = new HumanPlayer("white");
            blackPlayer = new AIPlayer("black", board);
        } else {
            whitePlayer = new AIPlayer("white", board);
            blackPlayer = new HumanPlayer("black");
        }
        currentTurnPlayer = whitePlayer;
        historyOfMoves = new ArrayList<>();
        gameFinished = false;
        if (currentTurnPlayer.isAI()) {
            Game g = this;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    currentTurnPlayer.makeMove(g);
                    window.getFrame().repaint();
                }
            }, 1500);
        }
    }

    public void serialize(String filePath) {

        GameState gS = new GameState(board, currentTurnPlayer, whitePlayer, blackPlayer,
                boardHist, historyOfMoves, gameFinished);
        try {
            FileOutputStream file = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(gS);
            out.close();
            file.close();
            System.out.println("Object has been serialized");
        } catch (IOException ex) {
            System.out.println("IOException is caught");
        }
    }

    public void deserialize(String filePath) {
        GameState gS = null;
        try {
            FileInputStream file = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(file);

            gS = (GameState) in.readObject();

            in.close();
            file.close();

            System.out.println("Object has been deserialized ");
        } catch(IOException ex) {
            System.out.println("IOException is caught");
        } catch(ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }

        if (gS != null) {
            setGameState(gS);
        }
    }

    private void setGameState(GameState gS) {
        this.board = gS.getBoard();
        this.currentTurnPlayer = gS.getCurrentTurnPlayer();
        this.whitePlayer = gS.getWhitePlayer();
        this.blackPlayer = gS.getBlackPlayer();
        this.boardHist = gS.getBoardHist();
        this.historyOfMoves = gS.getHistoryOfMoves();
        this.gameFinished = gS.isGameFinished();
    }

    public Player getCurrentPlayer() {
        return currentTurnPlayer;
    }

    public boolean isBoardRotated() {
        return boardRotated;
    }

    public void makeRandomMove() {
        String move = board.getRandomMove(currentTurnPlayer.getColor(), boardRotated);
        if (move != null) {
            parseCommand(Character.getNumericValue(move.charAt(1)), Character.getNumericValue(move.charAt(0)),
                    Character.getNumericValue(move.charAt(3)), Character.getNumericValue(move.charAt(2)));
        }
    }



    public static void main(String[] args) {
        /* Starting a new game. */
        Game game = new Game();
    }
}
