package org.irmc.pigeonlib.objects;

public class Percentage {
    private double value;

    Percentage(double value) {
        this.value = value;
    }

    public static Percentage fromPercent(double percentValue) {
        return new Percentage(percentValue);
    }

    public static Percentage fromDecimal(double decimalValue) {
        return new Percentage(decimalValue / 100);
    }

    public static Percentage fromString(String stringValue) {
        if (stringValue.endsWith("%")) {
            return fromPercent(Double.parseDouble(stringValue.substring(0, stringValue.length() - 1)));
        } else {
            return fromDecimal(Double.parseDouble(stringValue));
        }
    }

    public void subtract(Percentage percentage) {
        value -= percentage.getValue();
    }

    public void add(Percentage percentage) {
        value += percentage.getValue();
    }

    public void multiply(double factor) {
        value *= factor;
    }

    public void divide(double factor) {
        value /= factor;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public double getDecimalValue() {
        return value * 100;
    }

    public String toString() {
        return getDecimalValue() + "%";
    }
}
