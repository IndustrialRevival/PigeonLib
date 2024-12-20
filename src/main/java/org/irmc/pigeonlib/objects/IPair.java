package org.irmc.pigeonlib.objects;

public interface IPair <A, B> {
    A getFirst();

    B getSecond();

    default A getFirstValue() {
        return getFirst();
    }

    default B getSecondValue() {
        return getSecond();
    }

    default void set(A first, B second) {
        setFirst(first);
        setSecond(second);
    }

    void setFirst(A first);

    void setSecond(B second);
}
