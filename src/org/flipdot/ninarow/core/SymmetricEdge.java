package org.flipdot.ninarow.core;

/**
 * Created by anselm on 29.03.17.
 */
public class SymmetricEdge extends Edge {
    final SymmetricEdge mirrorEdge;
    public SymmetricEdge(Node from, Node to) {
        super(from, to);
        mirrorEdge = new SymmetricEdge(this);
        this.addInSameDirection(mirrorEdge);
    }

    private SymmetricEdge(SymmetricEdge mirrorEdge) {
        super(mirrorEdge.to, mirrorEdge.from);
        this.mirrorEdge = mirrorEdge;
        this.addInSameDirection(mirrorEdge);
    }

    @Override
    public void addInSameDirection(Edge other) {
        this.inSameDirection.add(other);
        other.inSameDirection.add(this);
    }

}
