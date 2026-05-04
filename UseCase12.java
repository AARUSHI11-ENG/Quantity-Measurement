import java.util.Objects;

interface IMeasurable {
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
}

enum LengthUnit implements IMeasurable {
    INCH(1.0 / 12.0),
    FOOT(1.0),
    YARD(3.0),
    CM(1.0 / 30.48);

    private final double factorToFoot;

    LengthUnit(double factorToFoot) {
        this.factorToFoot = factorToFoot;
    }

    public double convertToBaseUnit(double value) {
        return value * factorToFoot;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / factorToFoot;
    }
}

enum WeightUnit implements IMeasurable {
    GRAM(0.001),
    KILOGRAM(1.0);

    private final double factorToKg;

    WeightUnit(double factorToKg) {
        this.factorToKg = factorToKg;
    }

    public double convertToBaseUnit(double value) {
        return value * factorToKg;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / factorToKg;
    }
}

enum VolumeUnit implements IMeasurable {
    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double factorToLitre;

    VolumeUnit(double factorToLitre) {
        this.factorToLitre = factorToLitre;
    }

    public double convertToBaseUnit(double value) {
        return value * factorToLitre;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / factorToLitre;
    }
}

class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;
    private static final double EPSILON = 0.0001;

    public Quantity(double value, U unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        this.value = value;
        this.unit = unit;
    }

    public double getBaseValue() {
        return unit.convertToBaseUnit(value);
    }

    public U getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quantity)) return false;

        Quantity<?> other = (Quantity<?>) obj;

        if (!this.unit.getClass().equals(other.unit.getClass())) return false;

        return Math.abs(this.getBaseValue() - other.getBaseValue()) < EPSILON;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBaseValue(), unit.getClass());
    }

    public Quantity<U> convertTo(U targetUnit) {
        double base = this.getBaseValue();
        double converted = targetUnit.convertFromBaseUnit(base);
        return new Quantity<>(converted, targetUnit);
    }

    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        double sum = this.getBaseValue() + other.getBaseValue();
        double result = targetUnit.convertFromBaseUnit(sum);
        return new Quantity<>(result, targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        double diff = this.getBaseValue() - other.getBaseValue();
        double result = targetUnit.convertFromBaseUnit(diff);
        return new Quantity<>(result, targetUnit);
    }

    public double divide(Quantity<U> other) {
        if (other.getBaseValue() == 0)
            throw new ArithmeticException("Division by zero");

        return this.getBaseValue() / other.getBaseValue();
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}

public class UseCase12 {

    public static void main(String[] args) {

        Quantity<LengthUnit> len1 = new Quantity<>(10, LengthUnit.FOOT);
        Quantity<LengthUnit> len2 = new Quantity<>(120, LengthUnit.INCH);

        System.out.println("Length Equality: " + len1.equals(len2));
        System.out.println("Length Add: " + len1.add(len2));
        System.out.println("Length Subtract: " + len1.subtract(len2));
        System.out.println("Length Divide: " + len1.divide(len2));

        Quantity<WeightUnit> w1 = new Quantity<>(5, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(5000, WeightUnit.GRAM);

        System.out.println("\nWeight Equality: " + w1.equals(w2));
        System.out.println("Weight Add: " + w1.add(w2));
        System.out.println("Weight Subtract: " + w1.subtract(w2));
        System.out.println("Weight Divide: " + w1.divide(w2));

        Quantity<VolumeUnit> v1 = new Quantity<>(1, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1000, VolumeUnit.MILLILITRE);

        System.out.println("\nVolume Equality: " + v1.equals(v2));
        System.out.println("Volume Add: " + v1.add(v2));
        System.out.println("Volume Subtract: " + v1.subtract(v2));
        System.out.println("Volume Divide: " + v1.divide(v2));

        System.out.println("\nConvert Litre → mL: " + v1.convertTo(VolumeUnit.MILLILITRE));
        System.out.println("Convert Gallon → Litre: " +
                new Quantity<>(1, VolumeUnit.GALLON).convertTo(VolumeUnit.LITRE));
    }
}