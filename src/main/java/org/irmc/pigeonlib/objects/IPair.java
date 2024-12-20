package org.irmc.pigeonlib.objects;
public interface IPair <A, B> {
    A getFirst();
    B getSecond();
    void setFirst(A first);
    void setSecond(B second);
}
