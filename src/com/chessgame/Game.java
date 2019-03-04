package com.chessgame;

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
    private HashMap<Character, Integer> notationMap;
    private Scanner sc;

    private boolean gameFinished;

    public Game() {
        board = new Board();
        sc = new Scanner(System.in);
        whitePlayer = new HumanPlayer("white", sc);
        blackPlayer = new HumanPlayer("black", sc);
        currentTurnPlayer = whitePlayer;
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
                        if (board.isKingAttacked(currentTurnPlayer.getColor())) {
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
            System.out.println("Invalid command, try again.");
        }

        board.printBoard();
    }

    public void startTheGame() {
        board.printBoard();
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public Player getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    public Board getBoard() {
        return board;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.startTheGame();
        while (!game.isGameFinished()) {
            System.out.println();
            System.out.println(game.getCurrentTurnPlayer().getColor() + " player's turn");

            game.parseCommand(game.getCurrentTurnPlayer().makeMove(game.getBoard()));
        }
    }
}
