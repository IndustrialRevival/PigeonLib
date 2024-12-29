package org.irmc.pigeonlib.objects.percentage;

public final class PositiveHundredPercentage extends HundredPercentage{
    PositiveHundredPercentage(double value, boolean flowValidated) {
        super(value, flowValidated);
    }

    @Override
    public void add(Percentage percentage) {
        if (percentage.getValue() < 0) {
            if (percentage.getValue() + this.getValue() < 0) {
                throw new IllegalArgumentException("Cannot make a negative percentage");
            }
        }

        super.add(percentage);
    }

    @Override
    public void subtract(Percentage percentage) {
        if (this.getValue() - percentage.getValue() < 0) {
            throw new IllegalArgumentException("Cannot make a negative percentage");
        }

        super.subtract(percentage);
    }
}
