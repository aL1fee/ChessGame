package com.chessgame.pieces;

import com.chessgame.Piece;

import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(String name, String side, Boolean scalableMoves) {
        super(name, side, scalableMoves);
    }

    public ArrayList<String> getPlausibleMoves() {
        ArrayList<String> possibleMoves = new ArrayList<>();
        possibleMoves.add("f");
        possibleMoves.add("r");
        possibleMoves.add("b");
        possibleMoves.add("l");
        return possibleMoves;
    }
}