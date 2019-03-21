package com.chessgame.agents;

import com.chessgame.Board;
import com.chessgame.Game;
import com.chessgame.Piece;
import com.chessgame.Utils.MoveValue;

public class AIPlayer extends Player {

    private int minMaxDepth;
    private boolean isAI;

    public AIPlayer(String color, Board board) {
        super(color);
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
        Board boardCopy = new Board(boardCopyArray, "", 0, false);

        int currentDepth = game.getBoard().isBeginningOfTheGame() ? 2 : minMaxDepth;

        long startTime = System.currentTimeMillis();

        MoveValue bestMoveValue = minMaxMove(boardCopy, game.getCurrentPlayer().getColor(), "", Integer.MIN_VALUE,
                Integer.MAX_VALUE, game.isBoardRotated(), 0, currentDepth, 0);

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Time taken: " + elapsedTime);

        String bestMove = bestMoveValue.getMove();

        System.out.println("Minmax final move: " + bestMove + "; Value: " + bestMoveValue.getValue());

        /* Inevitable mate. */
        if (bestMove.equals("")) {
            game.makeRandomMove();
        } else {
            game.parseCommand(Character.getNumericValue(bestMove.charAt(1)), Character.getNumericValue(bestMove.charAt(0)),
                    Character.getNumericValue(bestMove.charAt(3)), Character.getNumericValue(bestMove.charAt(2)));
        }
    }


    public MoveValue minMaxMove(Board board, String currentPlayer, String moveSequence, double alpha, double beta,
                                boolean isBoardRotated, int depth, int maxDepth, double extraValue) {
        if (depth == maxDepth) {
            return getMoveValueOfTheBoard(board, moveSequence, isBoardRotated, extraValue);
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
                            Board boardCopy = new Board(boardCopyArray, str, board.getMoveNumber(), false);
                            boardCopy.makeMoveForAI(Character.getNumericValue(str.charAt(1)),
                                    Character.getNumericValue(str.charAt(0)), Character.getNumericValue(str.charAt(3)),
                                    Character.getNumericValue(str.charAt(2)), currentPlayer, isBoardRotated);
                            if (boardCopy.isKingAttacked(currentPlayer, isBoardRotated)) {
                                continue;
                            }

                            double newExtraValue = 0;

                            /* Beginning of the game little strategies s.a. partial debuts,
                               piece development, closeness to the enemy king. */
                            if (boardCopy.isBeginningOfTheGame()) {
                                if (depth == 0 && boardCopy.isDebutMove(str)) {
                                    newExtraValue += Math.random() / 2;
                                }
                                if (!boardCopy.hasPieceMoved(str)) {
                                    newExtraValue += .4;
                                }
                            /* Middle of the game strategies. */
                            } else if (boardCopy.isMiddleOfTheGame()) {
                                if (!boardCopy.hasPieceMoved(str)) {
                                    newExtraValue += .3;
                                }
                                newExtraValue += boardCopy.closenessToEnemyKing(str);
                            }
                            newExtraValue += boardCopy.closenessToEnemyKing(str);

                            MoveValue moveVal;

                            /* Give a check as a current move. */
                            if (boardCopy.isKingAttacked(otherPlayer(currentPlayer), isBoardRotated) && depth == 0) {
                                newExtraValue += .5;
                                moveVal = minMaxMove(boardCopy, otherPlayer(currentPlayer),
                                    moveSequence + str, alpha, beta,
                                        isBoardRotated, depth + 1, maxDepth, extraValue + newExtraValue);
                            } else {
                                moveVal = minMaxMove(boardCopy, otherPlayer(currentPlayer),
                                        moveSequence + str, alpha, beta,
                                        isBoardRotated, depth + 1, maxDepth, extraValue + newExtraValue);
                            }

                            if (bestMoveValue.getValue() < moveVal.getValue()) {
                                bestMoveValue = moveVal;
                            }
                            alpha = Math.max(alpha, bestMoveValue.getValue());
                            if (alpha >= beta) {
                                break;
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
                    if (currentPlayer.equals(board.pieceAt(i, j).getColor())) {
                        for (String str : board.getPossibleMoves(i, j, isBoardRotated)) {
                            Piece[][] boardCopyArray = board.getBoardCopy();
                            Board boardCopy = new Board(boardCopyArray, str, board.getMoveNumber(), false);
                            boardCopy.makeMoveForAI(Character.getNumericValue(str.charAt(1)),
                                    Character.getNumericValue(str.charAt(0)), Character.getNumericValue(str.charAt(3)),
                                    Character.getNumericValue(str.charAt(2)), currentPlayer, isBoardRotated);
                            if (boardCopy.isKingAttacked(currentPlayer, isBoardRotated)) {
                                continue;
                            }
                            double newExtraValue = 0;

                            /* Beginning of the game little strategies s.a.
                               piece development, closeness to the enemy king. */
                            if (boardCopy.isBeginningOfTheGame()) {
                                if (depth == 0 && boardCopy.isDebutMove(str)) {
                                    newExtraValue -= Math.random() / 2;
                                }
                                if (!boardCopy.hasPieceMoved(str)) {
                                    newExtraValue -= .4;
                                }
                                /* Middle of the game strategies. */
                            } else if (boardCopy.isMiddleOfTheGame()) {
                                if (!boardCopy.hasPieceMoved(str)) {
                                    newExtraValue -= .3;
                                }
                                newExtraValue -= boardCopy.closenessToEnemyKing(str);
                            }
                            newExtraValue -= boardCopy.closenessToEnemyKing(str);

                            MoveValue moveVal;
                            /* Give a check as a current move. */
                            if (boardCopy.isKingAttacked(otherPlayer(currentPlayer), isBoardRotated) && depth == 0) {
                                newExtraValue -= .5;
                                moveVal = minMaxMove(boardCopy, otherPlayer(currentPlayer),
                                        moveSequence + str, alpha, beta,
                                        isBoardRotated, depth + 1, maxDepth, extraValue + newExtraValue);
                            } else {
                                moveVal = minMaxMove(boardCopy, otherPlayer(currentPlayer),
                                        moveSequence + str, alpha, beta,
                                        isBoardRotated, depth + 1, maxDepth, extraValue + newExtraValue);
                            }

                            if (moveVal.getValue() < bestMoveValue.getValue()) {
                                bestMoveValue = moveVal;
                            }
                            beta = Math.min(beta, bestMoveValue.getValue());
                            if (alpha >= beta) {
                                break;
                            }
                        }
                    }
                }
            }
            return bestMoveValue;
        }
        return null;
    }

    private MoveValue getMoveValueOfTheBoard(Board board, String moveSequence, boolean isBoardRotated, double extraValue) {
        return new MoveValue(board.getBoardValue(isBoardRotated) + extraValue, moveSequence);
    }

    public void setDifficulty(int i) {
        minMaxDepth = i;
    }

    private String otherPlayer(String str) {
        return (str.equals("white")) ? "black" : "white";
    }

    @Override
    public boolean isAI() {
        return isAI;
    }
}