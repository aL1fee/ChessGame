package com.chessgame.agents;

import com.chessgame.Board;

import java.io.Serializable;

public abstract class Player implements Serializable {

    private final String color;

    public abstract String makeMove(Board board);

    public Player(String color) {
        this.color = color;
    }

    public String getColor() {
            return color;
    }
}