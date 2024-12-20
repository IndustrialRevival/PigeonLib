package org.irmc.pigeonlib.objects;

import it.unimi.dsi.fastutil.Pair;

public interface IPair <A, B> extends Pair<A, B> {
    A getFirst();

    B getSecond();

    default A getFirstValue() {
        return getFirst();
    }

    default B getSecondValue() {
        return getSecond();
    }

    @Override
    default A left() {
        return getFirst();
    }

    @Override
    default B right() {
        return getSecond();
    }

    default void set(A first, B second) {
        setFirst(first);
        setSecond(second);
    }

    void setFirst(A first);

    void setSecond(B second);
}
