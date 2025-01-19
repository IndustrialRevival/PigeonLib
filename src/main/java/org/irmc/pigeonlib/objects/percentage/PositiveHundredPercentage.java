package org.irmc.pigeonlib.objects.percentage;

public final class PositiveHundredPercentage extends HundredPercentage{
    PositiveHundredPercentage(double value, boolean flowValidated) {
        super(value, flowValidated);
    }

    public static PositiveHundredPercentage fromIntPercentage(int value) {
        return new PositiveHundredPercentage(value / 100.0, true);
    }

    public static PositiveHundredPercentage fromIntPercentage(int value, boolean flowValidated) {
        return new PositiveHundredPercentage(value / 100.0, flowValidated);
    }

    public static PositiveHundredPercentage fromIntDecimal(int value) {
        return new PositiveHundredPercentage(value, true);
    }

    public static PositiveHundredPercentage fromIntDecimal(int value, boolean flowValidated) {
        return new PositiveHundredPercentage(value, flowValidated);
    }

    public static PositiveHundredPercentage fromFloatPercentage(float value) {
        return new PositiveHundredPercentage(value / 100.0, true);
    }

    public static PositiveHundredPercentage fromFloatPercentage(float value, boolean flowValidated) {
        return new PositiveHundredPercentage(value / 100.0, flowValidated);
    }

    public static PositiveHundredPercentage fromFloatDecimal(float value) {
        return new PositiveHundredPercentage(value, true);
    }

    public static PositiveHundredPercentage fromFloatDecimal(float value, boolean flowValidated) {
        return new PositiveHundredPercentage(value, flowValidated);
    }

    public static PositiveHundredPercentage fromDoublePercentage(double value) {
        return new PositiveHundredPercentage(value / 100.0, true);
    }

    public static PositiveHundredPercentage fromDoublePercentage(double value, boolean flowValidated) {
        return new PositiveHundredPercentage(value / 100.0, flowValidated);
    }

    public static PositiveHundredPercentage createZero() {
        return new PositiveHundredPercentage(0, true);
    }

    @Override
    public PositiveHundredPercentage toPositive() {
        return this;
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
