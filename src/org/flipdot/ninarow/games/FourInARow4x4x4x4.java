package org.flipdot.ninarow.games;

import org.flipdot.ninarow.core.Node2D;
import org.flipdot.ninarow.core.PlaceStoneStep;
import org.flipdot.ninarow.core.Player;
import org.flipdot.ninarow.player.ConsolePlayer;
import org.flipdot.ninarow.player.RandomPlayer;

import java.util.Objects;

/**
 * Created by anselm on 14.04.17.
 */
public class FourInARow4x4x4x4 extends TorusSurface {

    public static void main(String[] args) {
        FourInARow4x4x4x4 ttt = new FourInARow4x4x4x4(new RandomPlayer("X"), new ConsolePlayer("O"));
        System.out.println("Beginner: " + (ttt.curPlayerIsA ? "X" : "O"));
        System.out.println("Winner: " + ttt.playFullGame());
        Node2D.Printer p = new Node2D.Printer(ttt.getNodes());
        ttt.nodes.stream().flatMap(n -> ttt.getMatches(new PlaceStoneStep<>(n)).stream())
                .filter(Objects::nonNull).findAny().ifPresent(p::printHighlighted);
    }

    public FourInARow4x4x4x4(Player a, Player b) {
        super(new int[]{4, 4, 4, 4}, true, a, b);
    }
    // TESTRESULTS:
    // start: 1492173305342
    // 100000 Games: Beginner won 51469 times (51%), second won 48531 times (48%)
    // end: 1492182206120 (~2h30)
}

