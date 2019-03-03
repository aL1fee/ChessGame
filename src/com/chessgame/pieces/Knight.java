package com.chessgame.pieces;

import com.chessgame.Piece;

import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(String name, String side, Boolean scalableMoves) {
        super(name, side, scalableMoves);
    }

    public ArrayList<String> getPossibleMoves() {
        ArrayList<String> possibleMoves = new ArrayList<>();
        possibleMoves.add("fll");
        possibleMoves.add("ffl");
        possibleMoves.add("ffr");
        possibleMoves.add("frr");
        possibleMoves.add("brr");
        possibleMoves.add("bbr");
        possibleMoves.add("bbl");
        possibleMoves.add("bll");
        return possibleMoves;
    }
}
