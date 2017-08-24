package org.flipdot.ninarow.player;

import org.flipdot.ninarow.core.*;

import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by anselm on 26.04.17.
 */
public class ConsolePlayer<B extends Board<N>, N extends Node> extends Player<B, N> {
    public ConsolePlayer(String name) {
        super(name);
    }

    @Override
    public Step step(B board) {
        Scanner s = new Scanner(System.in);
        System.out.println(board);
        System.out.println(board.getNodes());
        System.out.println("Do something!");
        String in1 = s.nextLine();
        Set<Node> node = board.getNodes().stream().filter(n -> n.toString().contains(in1)).collect(Collectors.toSet());
        if (node.size() > 1)
            node = board.getNodes().stream().filter(n -> n.toString().contains("["+in1+"]")).collect(Collectors.toSet());
        while (node.size() != 1) {
            System.out.println("nononononooo");
            String in2 = s.nextLine();
            node = board.getNodes().stream().filter(n -> n.toString().contains(in2)).collect(Collectors.toSet());
            if (node.size() > 1)
                node = board.getNodes().stream().filter(n -> n.toString().contains("["+in2+"]")).collect(Collectors.toSet());
        }
        return new PlaceStoneStep<>(node.iterator().next());
    }
}
