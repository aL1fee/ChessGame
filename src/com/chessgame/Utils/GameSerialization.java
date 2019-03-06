package com.chessgame.Utils;

import com.chessgame.Board;
import com.chessgame.agents.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class GameSerialization implements Serializable {

    private Board board;
    private Player currentTurnPlayer;
    private Player whitePlayer;
    private Player blackPlayer;
    private BoardHistory boardHist;
    private ArrayList<String> historyOfMoves;
    private boolean gameFinished;

    public GameSerialization(Board board, Player currentTurnPlayer, Player whitePlayer, Player blackPlayer,
                             BoardHistory boardHist, ArrayList<String> historyOfMoves, boolean gameFinished) {
        this.board = board;
        this.currentTurnPlayer = currentTurnPlayer;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.boardHist = boardHist;
        this.historyOfMoves = historyOfMoves;
        this.gameFinished = gameFinished;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentTurnPlayer() {
        return currentTurnPlayer;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public BoardHistory getBoardHist() {
        return boardHist;
    }

    public ArrayList<String> getHistoryOfMoves() {
        return historyOfMoves;
    }

    public Player getBlackPlayer() {
        return blackPlayer;
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }
}
