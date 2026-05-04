import java.util.Objects;

// Simple unit interface
interface IMeasurable {
    double toBaseUnit(double value);
    double fromBaseUnit(double value);
}

// Enum for operations
enum Operation {
    ADD, SUBTRACT, DIVIDE
}

class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null || Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("Invalid input");
        }
        this.value = value;
        this.unit = unit;
    }

    // ---------- PUBLIC API ----------

    public Quantity<U> add(Quantity<U> other) {
        return operate(other, Operation.ADD, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        return operate(other, Operation.SUBTRACT, this.unit);
    }

    public double divide(Quantity<U> other) {
        validate(other);
        double a = unit.toBaseUnit(this.value);
        double b = other.unit.toBaseUnit(other.value);

        if (b == 0) throw new ArithmeticException("Divide by zero");

        return a / b;
    }

    // ---------- CENTRALIZED LOGIC (DRY) ----------

    private Quantity<U> operate(Quantity<U> other, Operation op, U targetUnit) {
        validate(other);

        double a = this.unit.toBaseUnit(this.value);
        double b = other.unit.toBaseUnit(other.value);
        double result;

        switch (op) {
            case ADD:
                result = a + b;
                break;
            case SUBTRACT:
                result = a - b;
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation");
        }

        double finalValue = targetUnit.fromBaseUnit(result);
        return new Quantity<>(finalValue, targetUnit);
    }

    private void validate(Quantity<U> other) {
        if (other == null) throw new IllegalArgumentException("Null not allowed");
        if (!this.unit.getClass().equals(other.unit.getClass()))
            throw new IllegalArgumentException("Different unit types not allowed");
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}