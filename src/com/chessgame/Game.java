package com.chessgame;

import com.chessgame.GUI.Window;
import com.chessgame.Utils.BoardHistory;
import com.chessgame.agents.HumanPlayer;
import com.chessgame.agents.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Game {

    private Board board;
    private Player currentTurnPlayer;
    private Player whitePlayer;
    private Player blackPlayer;
    private BoardHistory boardHist;
    private Window window;
    private HashMap<Character, Integer> notationMap;
    private Scanner sc;

    private boolean gameFinished;

    public Game() {
        board = new Board();
        sc = new Scanner(System.in);
        boardHist = new BoardHistory(board.getBoardCopy());
        whitePlayer = new HumanPlayer("white", sc);
        blackPlayer = new HumanPlayer("black", sc);
        currentTurnPlayer = whitePlayer;
        window = new Window(board);
        gameFinished = false;
        notationMap = new HashMap<>();
        initNotationMap();
    }

    private void switchCurrentPlayer() {
        currentTurnPlayer = (currentTurnPlayer == whitePlayer) ? blackPlayer : whitePlayer;
    }

    private void initNotationMap() {
        notationMap.put('a', 0);
        notationMap.put('b', 1);
        notationMap.put('c', 2);
        notationMap.put('d', 3);
        notationMap.put('e', 4);
        notationMap.put('f', 5);
        notationMap.put('g', 6);
        notationMap.put('h', 7);
    }

    public void parseCommand(String command) {
        if (command.length() == 4) {
            int j1 = notationMap.get(command.charAt(0));
            int i1 = Character.getNumericValue(command.charAt(1)) - 1;
            int j2 = notationMap.get(command.charAt(2));
            int i2 = Character.getNumericValue(command.charAt(3)) - 1;
            String parsedCommand = Integer.toString(i1) + Integer.toString(j1) +
                    Integer.toString(i2) + Integer.toString(j2);
            int dim = board.getBoardDim();
            if ((i1 >= 0 && i1 < dim) && (j1 >= 0 && j1 < dim) &&
                    (i2 >= 0 && i2 < dim) && (j2 >= 0 && j2 < dim)) {

                System.out.println(Arrays.toString(board.getPossibleMoves(i1, j1).toArray()));
                Piece currentPiece = board.pieceAt(i1, j1);
                if (currentPiece != null && currentPiece.getColor().equals(currentTurnPlayer.getColor())) {
                    if (board.isPossibleMove(i1, j1, parsedCommand)) {
                        board.movePiece(i1, j1, i2, j2);
                        /* Update the board history. */
                        boardHist.addBoard(board.getBoardCopy());
                        /* Checking if the king is under attack. */
                        if (board.isKingAttacked(currentTurnPlayer.getColor())) {
                            board.setBoard(boardHist.getPrevious());
                            System.out.println("The king is under attack, try another move.");
                        }
                        switchCurrentPlayer();
                    } else {
                        System.out.println("Incorrect move, try again.");
                    }
                }
            } else {
                System.out.println("Incorrect indexes, try again.");
            }
        } else {
            switch (command) {
                /* Load the previous board. */
                case "b":
                    if (boardHist.getPrevious() != null) {
                        board.setBoard(boardHist.getPrevious());
                    }
                    break;
                /* Load the next board if such exists. */
                case "f":
                    if (boardHist.getNext() != null) {
                        board.setBoard(boardHist.getNext());
                    }
                    break;
                case "p":
                    boardHist.printAllBoards();
                    break;
                case "flip_board":
                    board.flipBoard();
                    board.printBoard();
                default:
                    System.out.println();
                    System.out.println("Invalid command, try again.");
                    break;
            }
        }

        board.printBoard();
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

    private Board getBoard() {
        return board;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.startTheGame();
        while (!game.isGameFinished()) {
            System.out.println();
            System.out.println();
            System.out.println(game.getCurrentTurnPlayer().getColor() + " player's turn");

            game.parseCommand(game.getCurrentTurnPlayer().makeMove(game.getBoard()));
        }
    }
}
