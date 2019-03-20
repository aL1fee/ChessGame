package com.chessgame.Utils;

public class MoveValue {
    private double value;
    private String move;

    public MoveValue(double value, String move) {
        this.value = value;
        this.move = move;
    }

    public void addValue(double x) {
        value += x;
    }

    public double getValue() {
        return value;
    }

    public String getMove() {
        return move;
    }
}