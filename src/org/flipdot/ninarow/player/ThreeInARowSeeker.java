package org.flipdot.ninarow.player;

import org.flipdot.ninarow.core.Node2D;
import org.flipdot.ninarow.core.Player;
import org.flipdot.ninarow.core.Step;
import org.flipdot.ninarow.games.FastFourInARow4x4x4x4;

/**
 * RandomPlayer vs ThreeInARowSeeker: 85:99915
 * RandomPlayer vs ThreeInARowSeeker: 84:99916
 *
 * Created by anselm on 26.04.17.
 */
public class ThreeInARowSeeker<B extends FastFourInARow4x4x4x4> extends RandomPlayer<B, Node2D> {

    public ThreeInARowSeeker(String name) {
        super(name);
    }

    // comparison with RandomPlayer:
    // start: 1493234499636
    // 100000 Games: ThreeInARowSeeker won 94699 times (94%), RandomPlayer won 5301 times (5%)
    // end: 1493234540466 (41 sec)

    @Override
    public Step step(B board) {
        Player[] stones = board.getStones();
        int stoneWhereOtherPlayerCouldWin = -1;
        for (int n = 0; n < 256; n++) {
            Player p = stones[n];
            if (p == null || (stoneWhereOtherPlayerCouldWin != -1 && p != this))//(p != this)
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
                            if (p == this)
                                return new FastFourInARow4x4x4x4.PlaceStoneStep(m);
                            else {
                                stoneWhereOtherPlayerCouldWin = m;
                                continue outer;
                            }
                        }
                    }
                }
            }
        }
        if (stoneWhereOtherPlayerCouldWin != -1)
            return new FastFourInARow4x4x4x4.PlaceStoneStep(stoneWhereOtherPlayerCouldWin);
        return super.step(board);
    }
}
