package org.flipdot.ninarow.player;

import org.flipdot.ninarow.core.Node2D;
import org.flipdot.ninarow.core.Player;
import org.flipdot.ninarow.core.Step;
import org.flipdot.ninarow.games.FastFourInARow4x4x4x4;

/**
 * Created by anselm on 26.04.17.
 */
public class TwoAndThreeInARowSeeker<B extends FastFourInARow4x4x4x4> extends RandomPlayer<B, Node2D> {

    public TwoAndThreeInARowSeeker(String name) {
        super(name);
    }

    // comparison with RandomPlayer:
    // start: 1493246083715
    // 100000 Games: RandomPlayer won 1142 times (1%), TwoAndThreeInARowSeeker won 98858 times (98%)
    // end: 1493246120240

    // comparison with ThreeInARowSeeker:
    //

    @Override
    public Step step(B board) {
        Player[] stones = board.getStones();
        int stoneWhereOtherPlayerCouldWin = -1, twoOwn = -1, twoOther = -1;
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
                            if (p == this)
                                return new FastFourInARow4x4x4x4.PlaceStoneStep(m);
                            else {
                                stoneWhereOtherPlayerCouldWin = m;
                                continue outer;
                            }
                        }
                    }
                } else if (c == 2 && stoneWhereOtherPlayerCouldWin == -1) {
                    int m = ((n + 2 * (base & 0x03)) & 0x03)
                            | ((n + 2 * (base & 0x0C)) & 0x0C)
                            | ((n + 2 * (base & 0x30)) & 0x30)
                            | ((n + 2 * (base & 0xC0)) & 0xC0);
                    if (p == this)
                        twoOwn = m;
                    else
                        twoOther = m;
                }
            }
        }
        if (stoneWhereOtherPlayerCouldWin != -1)
            return new FastFourInARow4x4x4x4.PlaceStoneStep(stoneWhereOtherPlayerCouldWin);
        if (twoOwn != -1)
            return new FastFourInARow4x4x4x4.PlaceStoneStep(twoOwn);
        if (twoOther != -1)
            return new FastFourInARow4x4x4x4.PlaceStoneStep(twoOther);
        return super.step(board);
    }
}
