package com.chessgame.agents;

import com.chessgame.Board;
import com.chessgame.Game;
import com.chessgame.Piece;
import com.chessgame.Utils.MoveValue;

public class AIPlayer extends Player {

    private Board board;
    public int minMaxDepth;
    private boolean isAI;
//    public HashMap<String, Integer> valueSystem;

    public AIPlayer(String color, Board board) {
        super(color);
        this.board = board;
        minMaxDepth = 3;
        isAI = true;
    }

    @Override
    public void makeMove(Game game,  int startX, int startY, int endX, int endY) {
        makeMove(game);
    }

    @Override
    public void makeMove(Game game) {

        Piece[][] boardCopyArray = game.getBoard().getBoardCopy();
        Board boardCopy = new Board(boardCopyArray, "", false);


        String bestMove = minMaxMove(boardCopy, game.getCurrentPlayer().getColor(), "",
                game.isBoardRotated(), 0, minMaxDepth).getMove();

        System.out.println("MINMAX FINAL MOVE: " + bestMove);

        /* Inevitable mate. */
        if (bestMove.equals("")) {
            game.makeRandomMove();
        } else {
            game.parseCommand(Character.getNumericValue(bestMove.charAt(1)), Character.getNumericValue(bestMove.charAt(0)),
                    Character.getNumericValue(bestMove.charAt(3)), Character.getNumericValue(bestMove.charAt(2)));
        }
    }




    /*  1. while within depth:
            3. Makes a move for AI but doesn't paint it.
            4. Evaluates a board and records the move with its score.
            5. Makes a move for a player but doesn't paint it.
            6. Evaluates a board and records the move with its score.
     */

    public MoveValue minMaxMove(Board board, String currentPlayer, String moveSequence,
                                boolean isBoardRotated, int depth, int maxDepth) {
        if (depth == maxDepth) {
            return getMoveValueOfTheBoard(board, moveSequence, isBoardRotated);
        }
        if (currentPlayer.equals("white")) {
            MoveValue bestMoveValue = new MoveValue(Integer.MIN_VALUE, moveSequence);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board.pieceAt(i, j) == null) {
                        continue;
                    }
                    if (currentPlayer.equals(board.pieceAt(i, j).getColor())) {
                        for (String str : board.getPossibleMoves(i, j, isBoardRotated)) {
                            Piece[][] boardCopyArray = board.getBoardCopy();
                            Board boardCopy = new Board(boardCopyArray, str, false);
                            boardCopy.makeMoveForAI(Character.getNumericValue(str.charAt(1)),
                                    Character.getNumericValue(str.charAt(0)), Character.getNumericValue(str.charAt(3)),
                                    Character.getNumericValue(str.charAt(2)), currentPlayer, isBoardRotated);
                            if (boardCopy.isKingAttacked(currentPlayer, isBoardRotated)) {
                                continue;
                            }

                            MoveValue moveVal = minMaxMove(boardCopy, otherPlayer(currentPlayer),
                                    moveSequence + str, isBoardRotated, depth + 1, maxDepth);
//                            System.out.println("Stringmove! : " + bestMoveValue.getMove());
                            if (bestMoveValue.getValue() < moveVal.getValue()) {
                                bestMoveValue = moveVal;
                            }
                        }
                    }
                }
            }
            return bestMoveValue;
        } else if (currentPlayer.equals("black")) {
            MoveValue bestMoveValue = new MoveValue(Integer.MAX_VALUE, moveSequence);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board.pieceAt(i, j) == null) {
                        continue;
                    }
//                    System.out.println("HEY!!!!!!!!!!!!!!!! : " + bestMoveValue.getValue());
                    if (currentPlayer.equals(board.pieceAt(i, j).getColor())) {
                        for (String str : board.getPossibleMoves(i, j, isBoardRotated)) {
                            Piece[][] boardCopyArray = board.getBoardCopy();
                            Board boardCopy = new Board(boardCopyArray, str, false);
                            boardCopy.makeMoveForAI(Character.getNumericValue(str.charAt(1)),
                                    Character.getNumericValue(str.charAt(0)), Character.getNumericValue(str.charAt(3)),
                                    Character.getNumericValue(str.charAt(2)), currentPlayer, isBoardRotated);
                            if (boardCopy.isKingAttacked(currentPlayer, isBoardRotated)) {
                                continue;
                            }

                            MoveValue moveVal = minMaxMove(boardCopy, otherPlayer(currentPlayer),
                                    moveSequence + str, isBoardRotated, depth + 1, maxDepth);
                            if (moveVal.getValue() < bestMoveValue.getValue()) {
//                                System.out.println("Bestmoveval: " + bestMoveValue.getValue());
//                                System.out.println("moveval: " + moveVal.getValue());
                                bestMoveValue = moveVal;
                            }
                        }
                    }
                }
            }
            return bestMoveValue;
        }
        return null;
    }

    private String otherPlayer(String str) {
        return (str.equals("white")) ? "black" : "white";
    }

    private int getValueOfTheBoard(Game game) {
        return 0;
    }

    private MoveValue getMoveValueOfTheBoard(Board board, String moveSequence, boolean isBoardRotated) {
        return new MoveValue(board.getBoardValue(isBoardRotated), moveSequence);
    }

    @Override
    public boolean isAI() {
        return isAI;
    }
}
