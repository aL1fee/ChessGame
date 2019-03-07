package com.chessgame;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Piece implements Serializable, Cloneable {
    private final String name;
    private final String side;
    private final boolean scalableMoves;
    private boolean hasMoved;

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

    @Override
    public Piece clone() throws CloneNotSupportedException {
        Piece result = (Piece) super.clone();
        return result;
    }

    public String getSide() {
        return side;
    }

    public String getEnemySideFull() {
        return side.equals("w") ? "black" : "white";
    }

    public String getColor() {
        return side.equals("b") ? "black" : "white";
    }

    public void setHasMoved(boolean bool) {
        hasMoved = bool;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public boolean isScalableMoves() {
        return scalableMoves;
    }
}
