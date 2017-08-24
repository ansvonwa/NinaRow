package org.flipdot.ninarow.core;

import java.util.List;

/**
 * Created by anselm on 08.04.17.
 */
public class RegexPattern extends WinningPattern {
    private final String pattern;
    public RegexPattern(String pattern, Player winner) {
        super(winner);
        this.pattern = pattern;
    }

    @Override
    public Match matches(List<Node> nodes) {
        StringBuilder sb = new StringBuilder();
        nodes.forEach(n -> sb.append(n.stones.stream().findAny().map(p -> p.name.charAt(0)).orElse('_')));
        if (sb.toString().matches(pattern))
            return new Match(nodes);
        return null;
    }
}
