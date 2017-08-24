package org.flipdot.ninarow.tests;

import org.flipdot.ninarow.core.Node2D;
import org.flipdot.ninarow.core.Player;
import org.flipdot.ninarow.core.TwoPlayersBoard;
import org.flipdot.ninarow.games.FastFourInARow4x4x4x4;
import org.flipdot.ninarow.player.RandomPlayer;
import org.flipdot.ninarow.player.ThreeInARowSeeker;

/**
 * Created by anselm on 14.04.17.
 */
public class TestGames {
    public static void main(String[] args) {
        TwoPlayersBoard ttt = new FastFourInARow4x4x4x4(new ThreeInARowSeeker("X"), new RandomPlayer("O"));
        System.out.println("Beginner: " + (ttt.isCurPlayerA() ? "X" : "O"));
        System.out.println("Winner: " + ttt.playFullGame());
        Node2D.Printer p = new Node2D.Printer(ttt.getNodes());
        p.print();
        System.exit(0);
        int O = 0, X = 0, games = 0;
        System.out.println("start: " + System.currentTimeMillis());
        for (int i = 0; i < 1000; i++) {
            ttt.letABegin(i % 2 == 0);
            games++;
            Player w = ttt.playFullGame();
            if (w == null)
                continue;
            if ("O".equals(w.name))
                O++;
            else if ("X".equals(w.name))
                X++;
        }
        System.out.println((games) + " Games: " + ttt.a.getClass().getSimpleName() + " won " + X + " times ("
                + ((int) (100.0 * X / (games))) + "%), " + ttt.b.getClass().getSimpleName() + " " +
                "won " + O + " times (" + ((int) (100.0 * O / (games))) + "%)");
        System.out.println("end: " + System.currentTimeMillis());
    }

}
