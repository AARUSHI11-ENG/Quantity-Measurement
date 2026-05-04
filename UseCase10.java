import java.util.Objects;

public class UseCase10 {

    interface IMeasurable {
        double toBase(double value);
        double fromBase(double baseValue);
    }

    enum LengthUnit implements IMeasurable {
        FEET(0.3048),
        INCHES(0.0254),
        YARDS(0.9144),
        CENTIMETERS(0.01);

        private final double factor;

        LengthUnit(double factor) {
            this.factor = factor;
        }

        @Override
        public double toBase(double value) {
            return value * factor;
        }

        @Override
        public double fromBase(double baseValue) {
            return baseValue / factor;
        }
    }

    enum WeightUnit implements IMeasurable {
        KILOGRAM(1.0),
        GRAM(0.001),
        TONNE(1000.0);

        private final double factor;

        WeightUnit(double factor) {
            this.factor = factor;
        }

        @Override
        public double toBase(double value) {
            return value * factor;
        }

        @Override
        public double fromBase(double baseValue) {
            return baseValue / factor;
        }
    }

    static class Quantity<U extends IMeasurable> {

        private final double value;
        private final U unit;

        private static final double EPSILON = 0.0001;

        public Quantity(double value, U unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Value must be finite");
            }
            this.value = value;
            this.unit = unit;
        }

        private double toBase() {
            return unit.toBase(value);
        }

        public Quantity<U> convertTo(U targetUnit) {
            double baseValue = this.toBase();
            double converted = targetUnit.fromBase(baseValue);
            return new Quantity<>(converted, targetUnit);
        }

        public Quantity<U> add(Quantity<U> other) {
            return add(other, this.unit);
        }

        public Quantity<U> add(Quantity<U> other, U targetUnit) {
            validateSameCategory(other);

            double sumBase = this.toBase() + other.toBase();
            double result = targetUnit.fromBase(sumBase);

            return new Quantity<>(result, targetUnit);
        }

        private void validateSameCategory(Quantity<U> other) {
            if (this.unit.getClass() != other.unit.getClass()) {
                throw new IllegalArgumentException("Different measurement categories");
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Quantity<?> other)) return false;

            if (this.unit.getClass() != other.unit.getClass()) {
                return false;
            }

            double diff = Math.abs(this.toBase() - other.unit.toBase(other.value));
            return diff < EPSILON;
        }

        @Override
        public int hashCode() {
            double base = toBase();
            return Objects.hash(Math.round(base / EPSILON));
        }

        @Override
        public String toString() {
            return String.format("Quantity(%.2f, %s)", value, unit);
        }
    }

    public static <U extends IMeasurable> void demonstrateEquality(
            Quantity<U> q1, Quantity<U> q2) {
        System.out.println(q1 + " equals " + q2 + " → " + q1.equals(q2));
    }

    public static <U extends IMeasurable> void demonstrateConversion(
            Quantity<U> q, U targetUnit) {
        System.out.println(q + " converted → " + q.convertTo(targetUnit));
    }

    public static <U extends IMeasurable> void demonstrateAddition(
            Quantity<U> q1, Quantity<U> q2, U targetUnit) {
        System.out.println(q1 + " + " + q2 + " → " + q1.add(q2, targetUnit));
    }

    public static void main(String[] args) {

        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

        demonstrateEquality(l1, l2);
        demonstrateConversion(l1, LengthUnit.INCHES);
        demonstrateAddition(l1, l2, LengthUnit.FEET);

        System.out.println();

        Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);

        demonstrateEquality(w1, w2);
        demonstrateConversion(w1, WeightUnit.GRAM);
        demonstrateAddition(w1, w2, WeightUnit.KILOGRAM);

        System.out.println();

        Quantity<?> mixed1 = l1;
        Quantity<?> mixed2 = w1;

        System.out.println(mixed1 + " equals " + mixed2 + " → " + mixed1.equals(mixed2));
    }
}