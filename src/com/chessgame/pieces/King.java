package com.chessgame.pieces;

import com.chessgame.Piece;

import java.util.ArrayList;

public class King extends Piece {
    public King(String name, String side, Boolean scalableMoves) {
        super(name, side, scalableMoves);
    }

    public ArrayList<String> getPlausibleMoves() {
        ArrayList<String> possibleMoves = new ArrayList<>();
        possibleMoves.add("l");
        possibleMoves.add("fl");
        possibleMoves.add("f");
        possibleMoves.add("fr");
        possibleMoves.add("r");
        possibleMoves.add("br");
        possibleMoves.add("b");
        possibleMoves.add("bl");
//        possibleMoves.add("ll");
//        possibleMoves.add("rr");
        return possibleMoves;
    }
}
