package com.chessgame.pieces;

import com.chessgame.Piece;

import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(String name, String side, Boolean scalableMoves) {
        super(name, side, scalableMoves);
    }

    public ArrayList<String> getPlausibleMoves() {
        ArrayList<String> possibleMoves = new ArrayList<>();
        possibleMoves.add("fl");
        possibleMoves.add("fr");
        possibleMoves.add("br");
        possibleMoves.add("bl");
        return possibleMoves;
    }
}
