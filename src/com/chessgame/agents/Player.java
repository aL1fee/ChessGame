package com.chessgame.agents;

import com.chessgame.Board;

public abstract class Player {

    private final String color;

    public abstract String makeMove(Board board);

    public Player(String color) {
        this.color = color;
    }

    public String getColor() {
            return color;
    }
}