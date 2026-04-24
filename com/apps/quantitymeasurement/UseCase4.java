package com.apps.quantitymeasurement;

import java.util.Objects;

public class UseCase4 {

    // ✅ Enum with all units using a single base unit (inches)
    enum LengthUnit {
        INCHES(1.0),
        FEET(12.0),
        YARDS(36.0),
        CENTIMETERS(0.393701);

        private final double toInchesFactor;

        LengthUnit(double toInchesFactor) {
            this.toInchesFactor = toInchesFactor;
        }

        public double toInches(double value) {
            return value * toInchesFactor;
        }
    }

    // ✅ QuantityLength class
    static class QuantityLength {
        private final double value;
        private final LengthUnit unit;

        public QuantityLength(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        public double toInches() {
            return unit.toInches(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof QuantityLength)) return false;

            QuantityLength other = (QuantityLength) obj;

            double thisInches = this.toInches();
            double otherInches = other.toInches();

            return Math.abs(thisInches - otherInches) < 0.0001;
        }

        @Override
        public int hashCode() {
            return Objects.hash(toInches());
        }

        @Override
        public String toString() {
            return "Quantity(" + value + ", " + unit + ")";
        }
    }

    public static void main(String[] args) {

        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength q2 = new QuantityLength(3.0, LengthUnit.FEET);
        System.out.println(q1 + " and " + q2 + " -> Equal: " + q1.equals(q2));

        QuantityLength q3 = new QuantityLength(36.0, LengthUnit.INCHES);
        System.out.println(q1 + " and " + q3 + " -> Equal: " + q1.equals(q3));

        QuantityLength q4 = new QuantityLength(1.0, LengthUnit.CENTIMETERS);
        QuantityLength q5 = new QuantityLength(0.393701, LengthUnit.INCHES);
        System.out.println(q4 + " and " + q5 + " -> Equal: " + q4.equals(q5));

        assertEqual(new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(1.0, LengthUnit.YARDS));

        assertEqual(new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(3.0, LengthUnit.FEET));

        assertEqual(new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(36.0, LengthUnit.INCHES));

        assertEqual(new QuantityLength(1.0, LengthUnit.CENTIMETERS),
                new QuantityLength(0.393701, LengthUnit.INCHES));

        assertNotEqual(new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(2.0, LengthUnit.FEET));

        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCHES);

        assertEqual(yard, feet);
        assertEqual(feet, inches);
        assertEqual(yard, inches);

        QuantityLength q = new QuantityLength(2.0, LengthUnit.YARDS);
        if (!q.equals(null)) {
            System.out.println("Null comparison test passed");
        }

        if (q.equals(q)) {
            System.out.println("Same reference test passed");
        }
        try {
            new QuantityLength(1.0, null);
        } catch (IllegalArgumentException e) {
            System.out.println("Null unit test passed");
        }
    }

    private static void assertEqual(QuantityLength a, QuantityLength b) {
        if (a.equals(b)) {
            System.out.println("PASS: " + a + " == " + b);
        } else {
            System.out.println("FAIL: " + a + " != " + b);
        }
    }

    private static void assertNotEqual(QuantityLength a, QuantityLength b) {
        if (!a.equals(b)) {
            System.out.println("PASS: " + a + " != " + b);
        } else {
            System.out.println("FAIL: " + a + " == " + b);
        }
    }
}