package org.flipdot.ninarow.player;

import org.flipdot.ninarow.core.Board;
import org.flipdot.ninarow.core.Node;
import org.flipdot.ninarow.core.Player;
import org.flipdot.ninarow.core.Step;
import org.flipdot.ninarow.games.FastFourInARow4x4x4x4;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by anselm on 26.04.17.
 */
public class ConsolePlayer2<B extends Board<N>, N extends Node> extends Player<B, N> {
    public ConsolePlayer2(String name) {
        super(name);
    }

    @Override
    public Step step(B board) {
        Scanner s = new Scanner(System.in);
        System.out.println(board);
        System.out.println("Do something!");
        String in1 = s.nextLine();
        String[] node = in1.replace(',', ' ').replaceAll("\\W+"," ").trim().split(" ");
        System.out.println(in1.replace(',', ' ').replaceAll("\\W+"," "));
        System.out.println(Arrays.toString(node));
        while (node.length != 4) {
            System.out.println("nononononooo");
            String in2 = s.nextLine();
            node = in2.replace(',', ' ').replaceAll("\\w+"," ").trim().split(" ");
        }
        int n = 0;
        for (int i = 0; i < 4; i++) {
            n += (Integer.parseInt(node[i])%4) << i*2;
        }
        n = (((n&0x0f)<<4) + ((n&0xf0)>>4))%256;
        return new FastFourInARow4x4x4x4.PlaceStoneStep(n);
    }
}
