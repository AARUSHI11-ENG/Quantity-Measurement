import java.util.Objects;

// ===============================
// ✅ Standalone Enum (UC8)
// ===============================
enum LengthUnit {

    // Base unit = FEET
    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double toFeetFactor;

    LengthUnit(double factor) {
        this.toFeetFactor = factor;
    }

    // Convert THIS unit → base unit (feet)
    public double toBase(double value) {
        return value * toFeetFactor;
    }

    // Convert base unit (feet) → THIS unit
    public double fromBase(double baseValue) {
        return baseValue / toFeetFactor;
    }
}

// ===============================
// ✅ QuantityLength (Value Object)
// ===============================
class QuantityLength {

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

    private double toBase() {
        return unit.toBase(value);
    }

    // ===============================
    // ✅ UC5: Conversion
    // ===============================
    public QuantityLength convertTo(LengthUnit target) {
        validate(value, target);
        double base = toBase();
        return new QuantityLength(target.fromBase(base), target);
    }

    public static double convert(double value, LengthUnit from, LengthUnit to) {
        validate(value, from);
        validate(value, to);

        double base = from.toBase(value);
        return to.fromBase(base);
    }

    // ===============================
    // ✅ UC6: Addition (default unit)
    // ===============================
    public QuantityLength add(QuantityLength other) {
        return add(other, this.unit);
    }

    // ===============================
    // ✅ UC7: Addition with target unit
    // ===============================
    public QuantityLength add(QuantityLength other, LengthUnit target) {
        if (other == null)
            throw new IllegalArgumentException("Other cannot be null");

        validate(this.value, target);

        double sumBase = this.toBase() + other.toBase();
        return new QuantityLength(target.fromBase(sumBase), target);
    }

    // ===============================
    // ✅ Static add (core logic)
    // ===============================
    public static QuantityLength add(QuantityLength a,
                                     QuantityLength b,
                                     LengthUnit target) {

        if (a == null || b == null)
            throw new IllegalArgumentException("Operands cannot be null");

        validate(a.value, target);

        double sumBase = a.toBase() + b.toBase();
        return new QuantityLength(target.fromBase(sumBase), target);
    }

    // ===============================
    // ✅ Overloaded add
    // ===============================
    public static QuantityLength add(double v1, LengthUnit u1,
                                     double v2, LengthUnit u2,
                                     LengthUnit target) {

        return add(new QuantityLength(v1, u1),
                new QuantityLength(v2, u2),
                target);
    }

    // ===============================
    // ✅ Equality
    // ===============================
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof QuantityLength)) return false;

        QuantityLength other = (QuantityLength) obj;
        return Math.abs(this.toBase() - other.toBase()) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.round(toBase() / EPSILON));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}

// ===============================
// ✅ App (Demo + Tests)
// ===============================
public class UseCase8 {

    public static void main(String[] args) {

        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);

        // 🔹 Conversion
        System.out.println(a + " -> " + a.convertTo(LengthUnit.INCHES));

        // 🔹 Equality
        System.out.println(a + " equals " + b + " ? " + a.equals(b));

        // 🔹 Addition (default)
        System.out.println(a + " + " + b + " = " + a.add(b));

        // 🔹 Addition with target
        System.out.println(a + " + " + b + " in YARDS = "
                + a.add(b, LengthUnit.YARDS));

        // 🔹 Static conversion
        double val = QuantityLength.convert(36.0, LengthUnit.INCHES, LengthUnit.FEET);
        System.out.println("36 inches = " + val + " feet");

        // 🔹 Static addition
        QuantityLength result = QuantityLength.add(
                2.54, LengthUnit.CENTIMETERS,
                1.0, LengthUnit.INCHES,
                LengthUnit.CENTIMETERS
        );
        System.out.println("2.54 cm + 1 inch = " + result);

        // 🔹 Edge cases
        System.out.println("Zero test: " +
                new QuantityLength(5.0, LengthUnit.FEET)
                        .add(new QuantityLength(0.0, LengthUnit.INCHES)));

        System.out.println("Negative test: " +
                new QuantityLength(5.0, LengthUnit.FEET)
                        .add(new QuantityLength(-2.0, LengthUnit.FEET)));

        // 🔹 Commutativity test
        if (a.add(b, LengthUnit.FEET).equals(b.add(a, LengthUnit.FEET))) {
            System.out.println("PASS: Commutativity holds");
        }

        // 🔹 Exception tests
        try {
            new QuantityLength(1.0, null);
        } catch (Exception e) {
            System.out.println("PASS: Null unit rejected");
        }

        try {
            QuantityLength.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCHES);
        } catch (Exception e) {
            System.out.println("PASS: Invalid value rejected");
        }
    }
}