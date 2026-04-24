package com.apps.quantitymeasurement;

import java.util.Objects;

public class UseCase5 {

    enum LengthUnit {
        INCHES(1.0),
        FEET(12.0),
        YARDS(36.0),
        CENTIMETERS(0.393701);

        private final double toInchesFactor;

        LengthUnit(double factor) {
            this.toInchesFactor = factor;
        }

        public double toInches(double value) {
            return value * toInchesFactor;
        }

        public double fromInches(double inches) {
            return inches / toInchesFactor;
        }

        public double getFactor() {
            return toInchesFactor;
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

        public QuantityLength convertTo(LengthUnit targetUnit) {
            validate(value, targetUnit);

            double inches = toInches();
            double converted = targetUnit.fromInches(inches);

            return new QuantityLength(converted, targetUnit);
        }

        public static double convert(double value, LengthUnit source, LengthUnit target) {
            validate(value, source);
            validate(value, target);

            double inches = source.toInches(value);
            return target.fromInches(inches);
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

    public static void demonstrateLengthConversion(double value,
                                                   LengthUnit from,
                                                   LengthUnit to) {

        double result = QuantityLength.convert(value, from, to);
        System.out.println("convert(" + value + ", " + from + ", " + to + ") = " + result);
    }

    public static void demonstrateLengthConversion(QuantityLength q,
                                                   LengthUnit to) {

        QuantityLength result = q.convertTo(to);
        System.out.println(q + " -> " + result);
    }

    public static void demonstrateLengthEquality(QuantityLength a,
                                                 QuantityLength b) {

        System.out.println(a + " and " + b + " Equal: " + a.equals(b));
    }

    public static void demonstrateLengthComparison(double v1, LengthUnit u1,
                                                   double v2, LengthUnit u2) {

        QuantityLength q1 = new QuantityLength(v1, u1);
        QuantityLength q2 = new QuantityLength(v2, u2);

        demonstrateLengthEquality(q1, q2);
    }

    public static void main(String[] args) {

        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);
        demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);
        demonstrateLengthConversion(36.0, LengthUnit.INCHES, LengthUnit.YARDS);
        demonstrateLengthConversion(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCHES);

        QuantityLength q = new QuantityLength(2.0, LengthUnit.YARDS);
        demonstrateLengthConversion(q, LengthUnit.FEET);

        demonstrateLengthComparison(1.0, LengthUnit.YARDS, 3.0, LengthUnit.FEET);
        demonstrateLengthComparison(1.0, LengthUnit.CENTIMETERS, 0.393701, LengthUnit.INCHES);

        assertEqual(12.0, QuantityLength.convert(1.0, LengthUnit.FEET, LengthUnit.INCHES));
        assertEqual(2.0, QuantityLength.convert(24.0, LengthUnit.INCHES, LengthUnit.FEET));
        assertEqual(36.0, QuantityLength.convert(1.0, LengthUnit.YARDS, LengthUnit.INCHES));

        double original = 5.0;
        double converted = QuantityLength.convert(original, LengthUnit.FEET, LengthUnit.INCHES);
        double back = QuantityLength.convert(converted, LengthUnit.INCHES, LengthUnit.FEET);
        assertApprox(original, back);

        assertEqual(0.0, QuantityLength.convert(0.0, LengthUnit.FEET, LengthUnit.INCHES));

        assertEqual(-12.0, QuantityLength.convert(-1.0, LengthUnit.FEET, LengthUnit.INCHES));

        try {
            QuantityLength.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCHES);
        } catch (Exception e) {
            System.out.println("PASS: NaN rejected");
        }

        try {
            QuantityLength.convert(1.0, null, LengthUnit.INCHES);
        } catch (Exception e) {
            System.out.println("PASS: Null unit rejected");
        }
    }

    private static void assertEqual(double expected, double actual) {
        if (Math.abs(expected - actual) < 1e-6) {
            System.out.println("PASS: " + actual);
        } else {
            System.out.println("FAIL: Expected " + expected + " but got " + actual);
        }
    }

    private static void assertApprox(double expected, double actual) {
        if (Math.abs(expected - actual) < 1e-6) {
            System.out.println("PASS: Round-trip OK");
        } else {
            System.out.println("FAIL: Round-trip error");
        }
    }
}