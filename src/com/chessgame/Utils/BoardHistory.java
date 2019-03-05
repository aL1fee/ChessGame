package com.chessgame.Utils;

import com.chessgame.Piece;

import java.util.ArrayList;

public class BoardHistory {

    private ArrayList<Piece[][]> boards;
    private int index;

    public BoardHistory(Piece[][] boardArray) {
        boards = new ArrayList<>();
        boards.add(boardArray);
        index = 0;
    }

    public void addBoard(Piece[][] boardArray) {
        index++;
        boards.add(index, boardArray);
    }

    public Piece[][] getBoard(int i) {
        index = i;
        return boards.get(index);
    }

    public Piece[][] getPrevious() {
        if ((index - 1) >= 0) {
            index--;
            return boards.get(index);
        }
        System.out.println("No previous board exists.");
        return null;
    }

    public Piece[][] getNext() {
        if ((index + 1) < boards.size()) {
            index++;
            return boards.get(index);
        }
        System.out.println("No next board exists.");
        return null;
    }

    public void printAllBoards() {
        System.out.println();
        System.out.println("Board history:");
        System.out.println();
        int count = 0;
        for (Piece[][] p : boards) {
            System.out.println();
            System.out.println("BOARD # " + count);
            System.out.println();
            count++;
            for (int i = 8 - 1; i >= 0; i--) {
                System.out.println();
                for (int j = 0; j < 8; j++) {
                    if (p[i][j] != null) {
                        System.out.print(p[i][j].getFullName() + " ");
                    } else {
                        System.out.print("00 ");
                    }
                }
            }
        }
    }
}