package org.irmc.pigeonlib.objects;

public class Pair<A, B> implements IPair<A, B> {
    private A first;
    private B second;

    public Pair(A first, B second) {
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
        this.first = first;
    }

    @Override
    public void setSecond(B second) {
        this.second = second;
    }

    public ImmutablePair<A, B> toImmutable() {
        return new ImmutablePair<>(first, second);
    }
}
