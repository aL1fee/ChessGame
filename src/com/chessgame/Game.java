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

/* Still need to implement:
    1. King castling, en passant moves.
    2. King castling, en passant moves, mate print notation.

   Areas to improve:
    1. Do a bitboard representation of the chess board for
    minMax to do considerably less copying of the Board class.
    2. Add AI strategies: use a number of good chess openings,
    improve piece development, add a center occupation priority,
    add strategies to hunt/save the queen, implement a priority
    to give a checkmate rather than gain material advantage,
    divide the usage of different strategies based on the timeline
    of the game (early-game, middle-game, end-game).
    3. Add multiple AI difficulty levels incorporating different
    AI strategies.
 */
public class Game {

    private boolean gameFinished;
    private boolean boardRotated;
    private ArrayList<String> historyOfMoves;
    private Board board;
    private BoardHistory boardHist;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player currentTurnPlayer;
    private Window window;
    private Timer timer;

    public Game() {
        gameFinished = false;
        boardRotated = false;
        historyOfMoves = new ArrayList<>();
        board = new Board();
        boardHist = new BoardHistory(board.getBoardCopy());
        whitePlayer = new HumanPlayer("white");
        blackPlayer = new AIPlayer("black", board);
        currentTurnPlayer = whitePlayer;
        window = new Window(this);
        timer = new Timer();
    }

    private void switchCurrentPlayer() {
        currentTurnPlayer = (currentTurnPlayer == whitePlayer) ? blackPlayer : whitePlayer;
    }

    public void parseCommand(int startX, int startY, int endX, int endY) {
        Piece currentPiece = board.pieceAt(startY, startX);
        String parsedCommand = Integer.toString(startY) + Integer.toString(startX) +
                Integer.toString(endY) + Integer.toString(endX);
        if (currentPiece != null && currentPiece.getColor().equals(currentTurnPlayer.getColor())) {
            if (board.isPossibleMove(startY, startX, parsedCommand, boardRotated)) {
                if (board.isPromotionMove(startY, startX, parsedCommand)) {
                    if (!currentTurnPlayer.isAI()) {
                        /* Pawn promotion move. */
                        String[] promotionChoices = {"Knight", "Bishop", "Rook", "Queen"};
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
                    } else {
                        String promotion = "Queen";
                        board.movePiece(startY, startX, endY, endX, promotion, boardRotated);
                    }
                }
                /* implement castling */
//                else if (board.isCastleMove(startY, startX, parsedCommand, boardRotated)) {
//                    board.castleMove(startY, startX, parsedCommand, boardRotated);
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
    }

    public void undoMove() {
        if (historyOfMoves.size() > 1) {
            String lastMove = "";
            /* Handling the moves text. */
            lastMove = historyOfMoves.get(historyOfMoves.size() - 2);
            /* Updating the state of the game. */
            this.board = new Board(boardHist.getPreviousTwo(), lastMove, board.getMoveNumber() - 2, false);
            historyOfMoves.remove(historyOfMoves.size() - 1);
            historyOfMoves.remove(historyOfMoves.size() - 1);
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

    public void makeRandomMove() {
        String move = board.getRandomMove(currentTurnPlayer.getColor(), boardRotated);
        if (move != null) {
            parseCommand(Character.getNumericValue(move.charAt(1)), Character.getNumericValue(move.charAt(0)),
                    Character.getNumericValue(move.charAt(3)), Character.getNumericValue(move.charAt(2)));
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

    public void setAIDifficulty(int i) {
        if (whitePlayer.isAI()) {
            whitePlayer.setDifficulty(i);
        } else if (blackPlayer.isAI()) {
            blackPlayer.setDifficulty(i);
        }
    }

    public Player getCurrentPlayer() {
        return currentTurnPlayer;
    }

    public boolean isBoardRotated() {
        return boardRotated;
    }

    public Board getBoard() {
        return board;
    }

    private boolean isGameFinished() {
        return gameFinished;
    }

    private Player getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    public static void main(String[] args) {
        /* Starting a new game. */
        new Game();
    }
}
