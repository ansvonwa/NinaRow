package org.flipdot.ninarow.player;

import org.flipdot.ninarow.core.Node2D;
import org.flipdot.ninarow.core.Player;
import org.flipdot.ninarow.core.Step;
import org.flipdot.ninarow.games.FastFourInARow4x4x4x4;

import java.util.Random;

/**
 * Created by anselm on 27.04.17.
 */
public class AvoidWinningPlayer<B extends FastFourInARow4x4x4x4> extends RandomPlayer<B, Node2D> {
    private static Random r = new Random();

    public static void main(String[] args) {
        FastFourInARow4x4x4x4 ttt = new FastFourInARow4x4x4x4(new AvoidWinningPlayer("X"), new AvoidWinningPlayer("O"));
        Player w = ttt.playFullGame();
        new Node2D.Printer(ttt.getNodes()).printHighlighted(ttt.getMatches(null).iterator().next());
//        new Node2D.Printer(ttt.getNodes()).print();
        System.out.println("Winner: " + w);
    }

    public AvoidWinningPlayer(String name) {
        super(name);
    }

    @Override
    public Step step(B board) {
        Player[] stones = board.getStones();
        boolean[] forbidden = new boolean[256];
        boolean[] badForOther = new boolean[256];
        for (int n = 0; n < 256; n++) {
            Player p = stones[n];
            if (p == null)
                continue;
            outer:
            for (int base : board.getBases()) {
                int c = 1;
                for (int i = 1; i < 4; i++) {
                    Player s = stones[((n + i * (base & 0x03)) & 0x03)
                            | ((n + i * (base & 0x0C)) & 0x0C)
                            | ((n + i * (base & 0x30)) & 0x30)
                            | ((n + i * (base & 0xC0)) & 0xC0)];
                    if (s == p)
                        c++;
                    else if (s != null || c == 1)
                        continue outer;
                }
                if (c == 3) {//3 in a row
                    for (int i = 2; i < 4; i++) {
                        int m = ((n + i * (base & 0x03)) & 0x03)
                                | ((n + i * (base & 0x0C)) & 0x0C)
                                | ((n + i * (base & 0x30)) & 0x30)
                                | ((n + i * (base & 0xC0)) & 0xC0);
                        if (stones[m] == null) {
                            if (p == this) {
                                forbidden[m] = true;
                            } else {
                                badForOther[m] = true;
//                                return new FastFourInARow4x4x4x4.PlaceStoneStep(m);
                            }
                        }
                    }
                }
            }
        }
        int m;
        for (int i = 0; i < 256; i++) {
            if (badForOther[i] && !forbidden[i] && stones[i] == null) {
                while (forbidden[(m = r.nextInt(256))] || !badForOther[m] || forbidden[m] && stones[m] != null)
                    ;
                return new FastFourInARow4x4x4x4.PlaceStoneStep(m);
            }
        }
        for (int i = 0; i < 256; i++) {
            if (stones[i] == null && !forbidden[i]) {
                while (forbidden[(m = r.nextInt(256))] || stones[m] != null)
                    ;
                return new FastFourInARow4x4x4x4.PlaceStoneStep(m);
            }
        }
        return super.step(board);
    }
}
