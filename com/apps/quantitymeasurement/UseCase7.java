package com.apps.quantitymeasurement;

import java.util.Objects;

public class UseCase7 {

    // ✅ Base unit = INCHES
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

    // ✅ Immutable Value Object
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

        // =============================
        // ✅ UC5: Conversion
        // =============================
        public QuantityLength convertTo(LengthUnit target) {
            validate(value, target);
            double inches = toInches();
            return new QuantityLength(target.fromInches(inches), target);
        }

        public static double convert(double value, LengthUnit from, LengthUnit to) {
            validate(value, from);
            validate(value, to);
            return to.fromInches(from.toInches(value));
        }

        // =============================
        // ✅ UC6: Addition (default = this.unit)
        // =============================
        public QuantityLength add(QuantityLength other) {
            return add(other, this.unit);
        }

        // =============================
        // ✅ UC7: Addition with explicit target unit
        // =============================
        public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
            if (other == null)
                throw new IllegalArgumentException("Other cannot be null");

            validate(this.value, targetUnit);

            double sumInches = this.toInches() + other.toInches();
            double result = targetUnit.fromInches(sumInches);

            return new QuantityLength(result, targetUnit);
        }

        // ✅ Static version (core logic reused → DRY)
        public static QuantityLength add(QuantityLength a,
                                         QuantityLength b,
                                         LengthUnit targetUnit) {

            if (a == null || b == null)
                throw new IllegalArgumentException("Operands cannot be null");

            validate(a.value, targetUnit);

            double sumInches = a.toInches() + b.toInches();
            return new QuantityLength(targetUnit.fromInches(sumInches), targetUnit);
        }

        // ✅ Overloaded raw-value version
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

    // =============================
    // ✅ Demo Methods
    // =============================
    public static void demoAdd(QuantityLength a, QuantityLength b, LengthUnit target) {
        System.out.println("add(" + a + ", " + b + ", " + target + ") = " + a.add(b, target));
    }

    // =============================
    // ✅ MAIN (Demo + Tests)
    // =============================
    public static void main(String[] args) {

        // 🔹 UC7 Examples
        demoAdd(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCHES),
                LengthUnit.FEET);

        demoAdd(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCHES),
                LengthUnit.INCHES);

        demoAdd(new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityLength(12.0, LengthUnit.INCHES),
                LengthUnit.YARDS);

        demoAdd(new QuantityLength(1.0, LengthUnit.YARDS),
                new QuantityLength(3.0, LengthUnit.FEET),
                LengthUnit.YARDS);

        demoAdd(new QuantityLength(36.0, LengthUnit.INCHES),
                new QuantityLength(1.0, LengthUnit.YARDS),
                LengthUnit.FEET);

        demoAdd(new QuantityLength(2.54, LengthUnit.CENTIMETERS),
                new QuantityLength(1.0, LengthUnit.INCHES),
                LengthUnit.CENTIMETERS);

        // 🔹 Edge cases
        demoAdd(new QuantityLength(5.0, LengthUnit.FEET),
                new QuantityLength(0.0, LengthUnit.INCHES),
                LengthUnit.YARDS);

        demoAdd(new QuantityLength(5.0, LengthUnit.FEET),
                new QuantityLength(-2.0, LengthUnit.FEET),
                LengthUnit.INCHES);

        // 🔹 Commutativity test
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);

        if (a.add(b, LengthUnit.YARDS).equals(b.add(a, LengthUnit.YARDS))) {
            System.out.println("PASS: Commutativity holds (UC7)");
        }

        // 🔹 Exception tests
        try {
            a.add(null, LengthUnit.FEET);
        } catch (Exception e) {
            System.out.println("PASS: Null operand rejected");
        }

        try {
            a.add(b, null);
        } catch (Exception e) {
            System.out.println("PASS: Null target unit rejected");
        }
    }
}