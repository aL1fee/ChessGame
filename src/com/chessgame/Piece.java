package com.chessgame;

import java.util.ArrayList;

public abstract class Piece {
    private final String name;
    private final String side;
    private final boolean scalableMoves;

    public Piece(String name, String side, boolean scalableMoves) {
        this.name = name;
        this.side = side;
        this.scalableMoves = scalableMoves;
    }

    public abstract ArrayList<String> getPossibleMoves();

    public String getName() {
        return side + name;
    }

    public boolean isScalableMoves() {
        return scalableMoves;
    }
}
