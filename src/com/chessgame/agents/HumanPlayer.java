package com.chessgame.agents;

import com.chessgame.Board;

import java.util.Scanner;

public class HumanPlayer extends Player {

    private Scanner sc;

    public HumanPlayer(String color, Scanner sc) {
        super(color);
        this.sc = sc;
    }

    @Override
    public String  makeMove(Board board) {
        String str = sc.nextLine();
        return str;
    }
}
