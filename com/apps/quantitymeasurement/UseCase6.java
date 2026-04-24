package com.apps.quantitymeasurement;

import java.util.Objects;

public class UseCase6 {

    enum LengthUnit {
        INCHES(1.0),
        FEET(12.0),
        YARDS(36.0),
        CENTIMETERS(0.393701);

        private final double factor;

        LengthUnit(double factor) {
            this.factor = factor;
        }

        public double toInches(double value) {
            return value * factor;
        }

        public double fromInches(double inches) {
            return inches / factor;
        }
    }

    static class QuantityLength {

        private final double value;
        private final LengthUnit unit;
        private static final double EPSILON = 1e-6;

        public QuantityLength(double value, LengthUnit unit) {
            validate(value, unit);
            this.value = value;
            this.unit = unit;
        }

        private static void validate(double value, LengthUnit unit) {
            if (unit == null)
                throw new IllegalArgumentException("Unit cannot be null");

            if (!Double.isFinite(value))
                throw new IllegalArgumentException("Value must be finite");
        }

        private double toInches() {
            return unit.toInches(value);
        }

        public QuantityLength convertTo(LengthUnit target) {
            validate(value, target);
            double inches = toInches();
            double result = target.fromInches(inches);
            return new QuantityLength(result, target);
        }

        public static double convert(double value, LengthUnit from, LengthUnit to) {
            validate(value, from);
            validate(value, to);

            double inches = from.toInches(value);
            return to.fromInches(inches);
        }

        public QuantityLength add(QuantityLength other) {
            if (other == null)
                throw new IllegalArgumentException("Other cannot be null");

            double sumInches = this.toInches() + other.toInches();
            double result = unit.fromInches(sumInches);

            return new QuantityLength(result, this.unit);
        }

        public static QuantityLength add(QuantityLength a, QuantityLength b, LengthUnit targetUnit) {
            if (a == null || b == null)
                throw new IllegalArgumentException("Operands cannot be null");

            validate(a.value, targetUnit);

            double sumInches = a.toInches() + b.toInches();
            double result = targetUnit.fromInches(sumInches);

            return new QuantityLength(result, targetUnit);
        }

        public static QuantityLength add(double v1, LengthUnit u1,
                                         double v2, LengthUnit u2,
                                         LengthUnit targetUnit) {

            return add(new QuantityLength(v1, u1),
                    new QuantityLength(v2, u2),
                    targetUnit);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof QuantityLength)) return false;

            QuantityLength other = (QuantityLength) obj;
            return Math.abs(this.toInches() - other.toInches()) < EPSILON;
        }

        @Override
        public int hashCode() {
            return Objects.hash(Math.round(toInches() / EPSILON));
        }

        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    public static void demonstrateAddition(QuantityLength a, QuantityLength b) {
        System.out.println(a + " + " + b + " = " + a.add(b));
    }

    public static void demonstrateAddition(double v1, LengthUnit u1,
                                           double v2, LengthUnit u2,
                                           LengthUnit target) {

        QuantityLength result = QuantityLength.add(v1, u1, v2, u2, target);
        System.out.println("add(" + v1 + "," + u1 + " + " + v2 + "," + u2 + ") = " + result);
    }

    public static void main(String[] args) {

        demonstrateAddition(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(2.0, LengthUnit.FEET));

        demonstrateAddition(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCHES));

        demonstrateAddition(new QuantityLength(12.0, LengthUnit.INCHES),
                new QuantityLength(1.0, LengthUnit.FEET));

        demonstrateAddition(new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(3.0, LengthUnit.FEET));

        demonstrateAddition(new QuantityLength(2.54, LengthUnit.CENTIMETERS),
                new QuantityLength(1.0, LengthUnit.INCHES));

        demonstrateAddition(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES, LengthUnit.FEET);

        demonstrateAddition(new QuantityLength(5.0, LengthUnit.FEET),
                new QuantityLength(0.0, LengthUnit.INCHES));

        demonstrateAddition(new QuantityLength(5.0, LengthUnit.FEET),
                new QuantityLength(-2.0, LengthUnit.FEET));

        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);

        if (a.add(b).equals(b.add(a))) {
            System.out.println("PASS: Commutativity holds");
        }

        try {
            a.add(null);
        } catch (Exception e) {
            System.out.println("PASS: Null operand rejected");
        }
    }
}