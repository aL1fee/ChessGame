package com.chessgame.Utils;

import com.chessgame.Piece;

import java.io.Serializable;
import java.util.ArrayList;

public class BoardHistory implements Serializable {

    private ArrayList<Piece[][]> boards;
    private int index;
    private int boardDim;

    public BoardHistory(Piece[][] boardArray) {
        index = 0;
        boardDim = 8;
        boards = new ArrayList<>();
        boards.add(boardArray);
    }

    public void addBoard(Piece[][] boardArray) {
        index++;
        boards.add(boardArray);
    }

    public Piece[][] getCurrent() {
        Piece[][] copyArray = new Piece[boardDim][boardDim];
        try {
            for (int i = 0; i < boardDim; i++) {
                for (int j = 0; j < boardDim; j++) {
                    if (boards.get(boards.size() - 1)[i][j] == null) {
                        boards.get(boards.size() - 1)[i][j] = null;
                    } else {
                        copyArray[i][j] = boards.get(boards.size() - 1)[i][j].clone();
                    }
                }
            }
        } catch (CloneNotSupportedException ex) {}
        return copyArray;
    }

    public Piece[][] getPrevious() {
        if ((index - 1) >= 0) {
            boards.remove(index);
            index--;
        }
        Piece[][] copyArray = new Piece[boardDim][boardDim];
        try {
            for (int i = 0; i < boardDim; i++) {
                for (int j = 0; j < boardDim; j++) {
                    if (boards.get(index)[i][j] == null) {
                        boards.get(index)[i][j] = null;
                    } else {
                        copyArray[i][j] = boards.get(index)[i][j].clone();
                    }
                }
            }
        } catch (CloneNotSupportedException ex) {}
        return copyArray;
    }

    public Piece[][] getPreviousTwo() {
        if ((index - 2) >= 0) {
            boards.remove(index);
            boards.remove(index - 1);
            index -= 2;
        }
        Piece[][] copyArray = new Piece[boardDim][boardDim];
        try {
            for (int i = 0; i < boardDim; i++) {
                for (int j = 0; j < boardDim; j++) {
                    if (boards.get(index)[i][j] == null) {
                        boards.get(index)[i][j] = null;
                    } else {
                        copyArray[i][j] = boards.get(index)[i][j].clone();
                    }
                }
            }
        } catch (CloneNotSupportedException ex) {}
        return copyArray;
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

    public ArrayList<Piece[][]> getAll() {
        return boards;
    }

    public void setBoards(ArrayList<Piece[][]> bds) {
        boards = bds;
    }
}