package org.irmc.pigeonlib.objects.percentage;

public class HundredPercentage extends Percentage {
    private final boolean flowValidated;

    HundredPercentage(double value, boolean flowValidated) {
        super(value);

        this.flowValidated = flowValidated;
    }

    public static HundredPercentage fromIntPercentage(int value) {
        return fromIntPercentage(value, true);
    }

    public static HundredPercentage fromIntPercentage(int value, boolean flowValidated) {
        return new HundredPercentage(value, flowValidated);
    }

    public static HundredPercentage fromIntDecimal(int value) {
        return fromIntDecimal(value, false);
    }

    public static HundredPercentage fromIntDecimal(int value, boolean flowValidated) {
        return new HundredPercentage(value / 100.0, flowValidated);
    }

    public static HundredPercentage fromFloatPercentage(float value) {
        return fromFloatPercentage(value, true);
    }

    public static HundredPercentage fromFloatPercentage(float value, boolean flowValidated) {
        return new HundredPercentage(Math.floor(value), flowValidated);
    }

    public static HundredPercentage fromFloatDecimal(float value) {
        return fromFloatDecimal(value, false);
    }

    public static HundredPercentage fromFloatDecimal(float value, boolean flowValidated) {
        return new HundredPercentage(value / 100.0, flowValidated);
    }

    public static HundredPercentage fromDoublePercentage(double value) {
        return fromDoublePercentage(value, true);
    }

    public static HundredPercentage fromDoublePercentage(double value, boolean flowValidated) {
        return new HundredPercentage(Math.floor(value), flowValidated);
    }

    @Override
    public void subtract(Percentage percentage) {
        validateFlow();

        super.subtract(percentage);
    }

    @Override
    public void add(Percentage percentage) {
        validateFlow();

        super.add(percentage);
    }

    private void validateFlow() {
        if (!flowValidated) return;

        if (this.getValue() < -1.00 || this.getValue() > 1.00) {
            throw new IllegalArgumentException("Invalid percentage value: " + this.getValue());
        }
    }

    public PositiveHundredPercentage toPositive() {
        if (this.getValue() < 0) {
            throw new IllegalArgumentException("Cannot convert negative percentage to positive");
        }

        return new PositiveHundredPercentage(this.getValue(), this.flowValidated);
    }
}
