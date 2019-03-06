package com.chessgame;

import com.chessgame.GUI.Window;
import com.chessgame.Utils.BoardHistory;
import com.chessgame.Utils.GameSerialization;
import com.chessgame.agents.HumanPlayer;
import com.chessgame.agents.Player;

import java.io.*;
import java.util.ArrayList;

public class Game {

    private Board board;
    private Player currentTurnPlayer;
    private Player whitePlayer;
    private Player blackPlayer;
    private BoardHistory boardHist;
    private ArrayList<String> historyOfMoves;

    private boolean gameFinished;

    public Game() {
        board = new Board();
        boardHist = new BoardHistory(board.getBoardCopy());
        whitePlayer = new HumanPlayer("white");
        blackPlayer = new HumanPlayer("black");
        currentTurnPlayer = whitePlayer;
        historyOfMoves = new ArrayList<>();
        gameFinished = false;

        new Window(this);
    }

    private void switchCurrentPlayer() {
        currentTurnPlayer = (currentTurnPlayer == whitePlayer) ? blackPlayer : whitePlayer;
    }

    public void parseCommand(int startX, int startY, int endX, int endY) {
        Piece currentPiece = board.pieceAt(startY, startX);
        String parsedCommand = Integer.toString(startY) + Integer.toString(startX) +
                Integer.toString(endY) + Integer.toString(endX);
        if (currentPiece != null && currentPiece.getColor().equals(currentTurnPlayer.getColor())) {
            if (board.isPossibleMove(startY, startX, parsedCommand)) {
                board.movePiece(startY, startX, endY, endX);
                historyOfMoves.add(board.getLastMove());
                /* Update the board history. */
                boardHist.addBoard(board.getBoardCopy());
                /* Checking if the king is under attack. */
//                board.printBoard();
                if (board.isKingAttacked(currentTurnPlayer.getColor())) {
                    board.setBoard(boardHist.getPrevious());
                    historyOfMoves.remove(historyOfMoves.size() - 1);
                    System.out.println("The king is under attack, try another move.");
                } else {
                    switchCurrentPlayer();
                }
            } else {
                System.out.println("Incorrect move, try again.");
            }
        }
        printHistoryOfMoves();
//        System.out.println("============================");
//        board.printBoard();
//        System.out.println();
//        System.out.println("============================");
    }

    private void startTheGame() {
        board.printBoard();
    }

    private boolean isGameFinished() {
        return gameFinished;
    }

    private Player getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    private void printHistoryOfMoves() {
        int count = 1;
        System.out.println();
        System.out.println();
        for (String i : historyOfMoves) {
            System.out.print(count + ". " + i + " ");
            count++;
        }
    }

    public String getHistoryOfMoves() {
        String allMoves = "";
        int count = 1;
        System.out.println();
        System.out.println();
        for (String i : historyOfMoves) {
            allMoves += count + ". " + i + " ";
            count++;
        }
        return allMoves;
    }

    public Board getBoard() {
        return board;
    }

    public void setNextLineHistoryOfMoves() {
        historyOfMoves.add("\n");
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

    public void serialize(String filePath) {

        GameSerialization gS = new GameSerialization(board, currentTurnPlayer, whitePlayer, blackPlayer,
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
        GameSerialization gS = null;
        try {
            FileInputStream file = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(file);

            gS = (GameSerialization) in.readObject();

            in.close();
            file.close();

            System.out.println("Object has been deserialized ");
        } catch(IOException ex) {
            System.out.println("IOException is caught");
        } catch(ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }

        if (gS != null) {
            this.board = gS.getBoard();
            this.currentTurnPlayer = gS.getCurrentTurnPlayer();
            this.whitePlayer = gS.getWhitePlayer();
            this.blackPlayer = gS.getBlackPlayer();
            this.boardHist = gS.getBoardHist();
            this.historyOfMoves = gS.getHistoryOfMoves();
            this.gameFinished = gS.isGameFinished();
        }


    }

    public static void main(String[] args) {
        Game game = new Game();
        game.startTheGame();
//        while (!game.isGameFinished()) {
//
//        }
    }
}
