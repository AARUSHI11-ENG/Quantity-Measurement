import java.util.Objects;

public class UseCase11 {

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

        public double toBase(double value) {
            return value * factor;
        }

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

        public double toBase(double value) {
            return value * factor;
        }

        public double fromBase(double baseValue) {
            return baseValue / factor;
        }
    }

    enum VolumeUnit implements IMeasurable {
        LITRE(1.0),
        MILLILITRE(0.001),
        GALLON(3.78541);

        private final double factor;

        VolumeUnit(double factor) {
            this.factor = factor;
        }

        public double toBase(double value) {
            return value * factor;
        }

        public double fromBase(double baseValue) {
            return baseValue / factor;
        }
    }

    static class Quantity<U extends IMeasurable> {

        private final double value;
        private final U unit;

        private static final double EPSILON = 0.0001;

        public Quantity(double value, U unit) {
            if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Invalid value");

            this.value = value;
            this.unit = unit;
        }

        private double toBase() {
            return unit.toBase(value);
        }

        public Quantity<U> convertTo(U targetUnit) {
            double base = toBase();
            return new Quantity<>(targetUnit.fromBase(base), targetUnit);
        }

        public Quantity<U> add(Quantity<U> other) {
            return add(other, this.unit);
        }

        public Quantity<U> add(Quantity<U> other, U targetUnit) {
            validate(other);

            double sum = this.toBase() + other.toBase();
            return new Quantity<>(targetUnit.fromBase(sum), targetUnit);
        }

        private void validate(Quantity<U> other) {
            if (this.unit.getClass() != other.unit.getClass()) {
                throw new IllegalArgumentException("Different categories not allowed");
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Quantity<?> other)) return false;

            if (this.unit.getClass() != other.unit.getClass()) return false;

            double diff = Math.abs(this.toBase() - other.unit.toBase(other.value));
            return diff < EPSILON;
        }

        @Override
        public int hashCode() {
            return Objects.hash(Math.round(toBase() / EPSILON));
        }

        @Override
        public String toString() {
            return String.format("Quantity(%.4f, %s)", value, unit);
        }
    }

    static <U extends IMeasurable> void showEquality(Quantity<U> a, Quantity<U> b) {
        System.out.println(a + " == " + b + " → " + a.equals(b));
    }

    static <U extends IMeasurable> void showConversion(Quantity<U> q, U unit) {
        System.out.println(q + " → " + q.convertTo(unit));
    }

    static <U extends IMeasurable> void showAddition(Quantity<U> a, Quantity<U> b, U unit) {
        System.out.println(a + " + " + b + " → " + a.add(b, unit));
    }

    public static void main(String[] args) {

        System.out.println("=== LENGTH ===");
        Quantity<LengthUnit> l1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(12, LengthUnit.INCHES);

        showEquality(l1, l2);
        showConversion(l1, LengthUnit.INCHES);
        showAddition(l1, l2, LengthUnit.FEET);

        System.out.println("\n=== WEIGHT ===");
        Quantity<WeightUnit> w1 = new Quantity<>(1, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1000, WeightUnit.GRAM);

        showEquality(w1, w2);
        showConversion(w1, WeightUnit.GRAM);
        showAddition(w1, w2, WeightUnit.KILOGRAM);

        System.out.println("\n=== VOLUME (UC11) ===");
        Quantity<VolumeUnit> v1 = new Quantity<>(1, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1000, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> v3 = new Quantity<>(1, VolumeUnit.GALLON);

        showEquality(v1, v2);
        showConversion(v1, VolumeUnit.MILLILITRE);
        showConversion(v3, VolumeUnit.LITRE);
        showAddition(v1, v2, VolumeUnit.LITRE);
        showAddition(v1, v3, VolumeUnit.GALLON);

        System.out.println("\n=== CROSS CATEGORY ===");
        Quantity<?> mixed1 = v1;
        Quantity<?> mixed2 = l1;

        System.out.println(mixed1 + " == " + mixed2 + " → " + mixed1.equals(mixed2));
    }
}