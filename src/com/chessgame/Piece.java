package com.chessgame;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Piece implements Serializable {
    private final String name;
    private final String side;
    private final boolean scalableMoves;
    private boolean hasMoved;
    private ArrayList<String> plausibleMoves;

    public Piece(String name, String side, boolean scalableMoves) {
        this.name = name;
        this.side = side;
        this.scalableMoves = scalableMoves;
        this.hasMoved = false;
    }

    public abstract ArrayList<String> getPlausibleMoves();

    public String getName() {
        return name;
    }

    public String getFullName() {
        return side + name;
    }

    public String getSide() {
        return side;
    }

    public String getColor() {
        return side.equals("b") ? "black" : "white";
    }

    public void setHasMoved() {
        hasMoved = true;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public boolean isScalableMoves() {
        return scalableMoves;
    }
}
