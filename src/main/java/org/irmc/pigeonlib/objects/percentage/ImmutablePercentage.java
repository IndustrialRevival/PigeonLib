package org.irmc.pigeonlib.objects.percentage;

public class ImmutablePercentage extends Percentage {
    ImmutablePercentage(double value) {
        super(value);
    }

    public static ImmutablePercentage of(double value) {
        return new ImmutablePercentage(value);
    }

    @Override
    public void subtract(Percentage percentage) {
        throw new UnsupportedOperationException("Cannot modify an immutable percentage");
    }

    @Override
    public void add(Percentage percentage) {
        throw new UnsupportedOperationException("Cannot modify an immutable percentage");
    }

    @Override
    public void setValue(double value) {
        throw new UnsupportedOperationException("Cannot modify an immutable percentage");
    }
}
