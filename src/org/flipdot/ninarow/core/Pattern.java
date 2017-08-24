package org.flipdot.ninarow.core;

import java.util.List;

/**
 * Created by anselm on 05.04.17.
 */
public abstract class Pattern {
    public abstract Match matches(List<Node> nodes);

    public class Match {
        private final List<Node> nodes;
        public Match(List<Node> nodes) {
            this.nodes = nodes;
        }

        public List<Node> getNodes() {
            return nodes;
        }
    }
}
