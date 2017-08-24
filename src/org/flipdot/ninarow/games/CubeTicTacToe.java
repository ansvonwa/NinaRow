package org.flipdot.ninarow.games;

import org.flipdot.ninarow.core.*;
import org.flipdot.ninarow.player.RandomPlayer;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by anselm on 12.04.17.
 */
public class CubeTicTacToe extends TwoPlayersBoard<Node2D> {

    public static void main(String[] args) {
        CubeTicTacToe t = new CubeTicTacToe(new RandomPlayer("X"), new RandomPlayer("O"));
        new Node2D.Printer(t.nodes).print();
    }

    public CubeTicTacToe(Player a, Player b) {
        super(a, b, new HashSet<>(Arrays.asList(new RegexPattern(b.name + b.name + b.name + b.name, b),
                new RegexPattern(a.name + a.name + a.name + a.name, a))));
        Node2D[][][] nodeArr = new Node2D[6][2][2];
        for (int surf = 0; surf < 6; surf++) {
            int sx = surf * 2, sy = 2;
            if (surf >= 4) {
                sx = 2;
                sy = (surf - 4) * 4;
            }
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    nodeArr[surf][i][j] = new Node2D(sx + i, sy + j);
                    nodes.add(nodeArr[surf][i][j]);
                    Node2D cur = nodeArr[surf][i][j];
                    nodes.stream().filter(n -> n.x == cur.x && n.y == cur.y - 1)
                            .forEach(n -> new SymmetricEdge(n, cur));
                    nodes.stream().filter(n -> n.x == cur.x - 1 && n.y == cur.y)
                            .forEach(n -> new SymmetricEdge(n, cur));

                }
            }
        }
        // TODO
        nodes.forEach(from -> nodes.stream()
                .filter(to -> to.x == from.x && to.y == 1 + from.y)
                .map(to -> new SymmetricEdge(from, to))
                .forEach(e -> {
                    from.edges.stream().filter(e2 -> e2 != e).forEach(e::addInSameDirection);
                    e.to.edges.stream().filter(e2 -> e2 != e).forEach(e::addInSameDirection);
                }));
        throw new Error("implementation not finished");
    }

    private Edge edge(int x1, int y1, int x2, int y2) {
        Node n1 = nodes.stream().filter(n -> n.x == x1 && n.y == y1).findAny().orElse(null);
        Node n2 = nodes.stream().filter(n -> n.x == x2 && n.y == y2).findAny().orElse(null);
        if (n1 == null || n2 == null)
            return null;
        return n1.edges.stream().filter(e -> e.from == n2 || e.to == n2).findAny().orElseGet(() -> new SymmetricEdge(n1, n2));
    }
}
