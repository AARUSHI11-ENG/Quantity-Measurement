package com.apps.quantitymeasurement;

import java.util.Objects;

/**
 * Main Application
 */
public class UseCase9 {

    public static void main(String[] args) {

        // ===== LENGTH TESTS =====
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(12.0, LengthUnit.INCHES);

        System.out.println("Length Equality: " + l1.equals(l2)); // true

        System.out.println("Convert 1 ft to inches: " + l1.convertTo(LengthUnit.INCHES));

        System.out.println("Add (default unit): " + l1.add(l2)); // 2 feet

        System.out.println("Add (target inches): " + l1.add(l2, LengthUnit.INCHES));

        // ===== WEIGHT TESTS =====
        QuantityWeight w1 = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight w2 = new QuantityWeight(1000.0, WeightUnit.GRAM);

        System.out.println("\nWeight Equality: " + w1.equals(w2)); // true

        System.out.println("Convert 1 kg to pounds: " + w1.convertTo(WeightUnit.POUND));

        System.out.println("Add (default unit): " + w1.add(w2));

        System.out.println("Add (target grams): " + w1.add(w2, WeightUnit.GRAM));
    }
}

/**
 * ================= LENGTH =================
 */
enum LengthUnit {

    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double factor; // relative to feet

    LengthUnit(double factor) {
        this.factor = factor;
    }

    public double convertToBase(double value) {
        return value * factor;
    }

    public double convertFromBase(double baseValue) {
        return baseValue / factor;
    }
}

/**
 * Length Value Object
 */
class QuantityLength {

    private final double value;
    private final LengthUnit unit;
    private static final double EPSILON = 1e-6;

    public QuantityLength(double value, LengthUnit unit) {
        if (unit == null || !Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid length input");
        }
        this.value = value;
        this.unit = unit;
    }

    public QuantityLength convertTo(LengthUnit target) {
        if (target == null) throw new IllegalArgumentException();

        double base = unit.convertToBase(value);
        double converted = target.convertFromBase(base);

        return new QuantityLength(converted, target);
    }

    public QuantityLength add(QuantityLength other) {
        return add(other, this.unit);
    }

    public QuantityLength add(QuantityLength other, LengthUnit target) {
        if (other == null || target == null) throw new IllegalArgumentException();

        double sumBase =
                unit.convertToBase(value) +
                        other.unit.convertToBase(other.value);

        return new QuantityLength(target.convertFromBase(sumBase), target);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuantityLength)) return false;

        QuantityLength other = (QuantityLength) obj;

        double base1 = unit.convertToBase(value);
        double base2 = other.unit.convertToBase(other.value);

        return Math.abs(base1 - base2) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit.convertToBase(value));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}

/**
 * ================= WEIGHT =================
 */
enum WeightUnit {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592);

    private final double factor; // relative to kg

    WeightUnit(double factor) {
        this.factor = factor;
    }

    public double convertToBase(double value) {
        return value * factor;
    }

    public double convertFromBase(double baseValue) {
        return baseValue / factor;
    }
}

/**
 * Weight Value Object
 */
class QuantityWeight {

    private final double value;
    private final WeightUnit unit;
    private static final double EPSILON = 1e-6;

    public QuantityWeight(double value, WeightUnit unit) {
        if (unit == null || !Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid weight input");
        }
        this.value = value;
        this.unit = unit;
    }

    public QuantityWeight convertTo(WeightUnit target) {
        if (target == null) throw new IllegalArgumentException();

        double base = unit.convertToBase(value);
        double converted = target.convertFromBase(base);

        return new QuantityWeight(converted, target);
    }

    public QuantityWeight add(QuantityWeight other) {
        return add(other, this.unit);
    }

    public QuantityWeight add(QuantityWeight other, WeightUnit target) {
        if (other == null || target == null) throw new IllegalArgumentException();

        double sumBase =
                unit.convertToBase(value) +
                        other.unit.convertToBase(other.value);

        return new QuantityWeight(target.convertFromBase(sumBase), target);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuantityWeight)) return false;

        QuantityWeight other = (QuantityWeight) obj;

        double base1 = unit.convertToBase(value);
        double base2 = other.unit.convertToBase(other.value);

        return Math.abs(base1 - base2) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit.convertToBase(value));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}