package org.flipdot.ninarow.games;

import org.flipdot.ninarow.core.*;
import org.flipdot.ninarow.player.RandomPlayer;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by anselm on 29.03.17.
 */
public class TicTacToe extends TwoPlayersBoard<Node2D> {

    public static void main(String[] args) {
        TicTacToe ttt = new TicTacToe(new RandomPlayer("X"), new RandomPlayer("O"));
        System.out.println("Beginner: " + (ttt.curPlayerIsA ? "X" : "O"));
        System.out.println("Winner: " + ttt.playFullGame());
        Node2D.Printer p = new Node2D.Printer(ttt.getNodes());
        p.print();
    }

    public TicTacToe(Player a, Player b) {
        super(a, b, new HashSet<>(Arrays.asList(new RegexPattern(b.name + b.name + b.name, b),
                new RegexPattern(a.name + a.name + a.name, a))));
        Node2D[][] n = new Node2D[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                n[i][j] = new Node2D(i, j);
                nodes.add(n[i][j]);
            }
        }
        for (int i = 0; i < 3; i++) {
            SymmetricEdge e1 = new SymmetricEdge(n[i][0], n[i][1]);
            SymmetricEdge e2 = new SymmetricEdge(n[i][1], n[i][2]);
            e1.addInSameDirection(e2);
            SymmetricEdge e3 = new SymmetricEdge(n[0][i], n[1][i]);
            SymmetricEdge e4 = new SymmetricEdge(n[1][i], n[2][i]);
            e3.addInSameDirection(e4);
        }
        SymmetricEdge e5 = new SymmetricEdge(n[0][0], n[1][1]);
        SymmetricEdge e6 = new SymmetricEdge(n[1][1], n[2][2]);
        e5.addInSameDirection(e6);
        SymmetricEdge e7 = new SymmetricEdge(n[2][0], n[1][1]);
        SymmetricEdge e8 = new SymmetricEdge(n[1][1], n[0][2]);
        e7.addInSameDirection(e8);
    }
}
