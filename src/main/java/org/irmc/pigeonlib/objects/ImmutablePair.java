package org.irmc.pigeonlib.objects;

import lombok.Getter;

@Getter
public class ImmutablePair<A, B> implements IPair<A, B> {
    private final A first;
    private final B second;

    public ImmutablePair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public A getFirst() {
        return first;
    }

    @Override
    public B getSecond() {
        return second;
    }

    @Override
    public void setFirst(A first) {
        throw new UnsupportedOperationException("ImmutablePair is immutable");
    }

    @Override
    public void setSecond(B second) {
        throw new UnsupportedOperationException("ImmutablePair is immutable");
    }
}
