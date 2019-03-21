package com.chessgame.agents;

import com.chessgame.Game;

import java.io.Serializable;

public abstract class Player implements Serializable {

    private final String color;

    public abstract void makeMove(Game game, int startX, int startY, int endX, int endY);

    public abstract void makeMove(Game game);

    public abstract boolean isAI();

    public abstract void setDifficulty(int i);

    public Player(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}