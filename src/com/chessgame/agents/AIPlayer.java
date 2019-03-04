package com.chessgame.agents;

import com.chessgame.Board;

public class AIPlayer extends Player {

    private Board board;

    public AIPlayer(String color, Board board) {
        super(color);
        this.board = board;
    }

    @Override
    public String makeMove(Board board) {
        return "NOT IMPLEMENTED YET.";
    }

}
