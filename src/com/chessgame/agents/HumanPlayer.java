package com.chessgame.agents;

import com.chessgame.Game;

public class HumanPlayer extends Player {

    private boolean isAI;

    public HumanPlayer(String color) {
        super(color);
        isAI = false;
    }

    @Override
    public void makeMove(Game game, int startX, int startY, int endX, int endY) {
        game.parseCommand(startX, startY, endX, endY);
    }

    @Override
    public boolean isAI() {
        return isAI;
    }

    @Override
    public void makeMove(Game game) {}

    @Override
    public void setDifficulty(int i) {}
}
