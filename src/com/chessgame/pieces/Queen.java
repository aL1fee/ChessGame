package com.chessgame.pieces;

import com.chessgame.Piece;

import java.util.ArrayList;

public class Queen extends Piece {
    public Queen(String name, String side, Boolean scalableMoves) {
        super(name, side, scalableMoves);
    }

    public ArrayList<String> getPossibleMoves() {
        ArrayList<String> possibleMoves = new ArrayList<>();
        possibleMoves.add("l");
        possibleMoves.add("fl");
        possibleMoves.add("f");
        possibleMoves.add("fr");
        possibleMoves.add("r");
        possibleMoves.add("br");
        possibleMoves.add("b");
        possibleMoves.add("bl");
        return possibleMoves;
    }
}
