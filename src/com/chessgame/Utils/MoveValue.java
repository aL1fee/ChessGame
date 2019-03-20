package com.chessgame.Utils;

public class MoveValue {
    private int value;
    private String move;

    public MoveValue(int value, String move) {
        this.value = value;
        this.move = move;
    }

    public int getValue() {
        return value;
    }

    public String getMove() {
        return move;
    }
}