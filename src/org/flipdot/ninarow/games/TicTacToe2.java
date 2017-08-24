package org.flipdot.ninarow.games;

import org.flipdot.ninarow.core.Node2D;
import org.flipdot.ninarow.core.Player;
import org.flipdot.ninarow.player.RandomPlayer;

/**
 * Created by anselm on 14.04.17.
 */
public class TicTacToe2 extends TorusSurface {

    public static void main(String[] args) {
        TicTacToe2 ttt = new TicTacToe2(new RandomPlayer("X"), new RandomPlayer("O"));
        System.out.println("Beginner: " + (ttt.curPlayerIsA ? "X" : "O"));
        System.out.println("Winner: " + ttt.playFullGame());
        Node2D.Printer p = new Node2D.Printer(ttt.getNodes());
        p.print();
        int O = 0, X = 0, games = 0;
        for (int i = 0; i < 10000; i++) {
            ttt.clear();
            ttt.curPlayerIsA = true;
            games++;
            Player w = ttt.playFullGame();
            if (w == null)
                continue;
            if ("O".equals(w.name))
                O++;
            else if ("X".equals(w.name))
                X++;
        }
        System.out.println((games)+" Games: Beginner won "+X+" times ("+((int) (100.0 * X / (games)))+"%), second " +
                "won "+O+" times ("+((int) (100.0 * O / (games)))+"%)");
    }

    public TicTacToe2(Player a, Player b) {
        super(new int[]{3, 3}, false, a, b);
    }
}
