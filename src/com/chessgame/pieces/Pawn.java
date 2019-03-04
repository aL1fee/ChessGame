package com.chessgame.pieces;

import com.chessgame.Piece;

import java.util.ArrayList;

public class Pawn extends Piece {

    public Pawn(String name, String side, Boolean scalableMoves) {
        super(name, side, scalableMoves);

    }

    public ArrayList<String> getPlausibleMoves() {
        ArrayList<String> possibleMoves = new ArrayList<>();
        possibleMoves.add("f");
        possibleMoves.add("fr");
        possibleMoves.add("fl");
        if (!isHasMoved()) {
            possibleMoves.add("ff");
        }
        return possibleMoves;
    }
}
