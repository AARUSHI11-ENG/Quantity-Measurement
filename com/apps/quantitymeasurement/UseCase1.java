
package com.apps.quantitymeasurement;


import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UseCase1 {

    // Main application class
    static class QuantityMeasurementApp {

        // Inner class representing Feet
        public static class Feet {
            private final double value;

            public Feet(double value) {
                this.value = value;
            }

            public double getValue() {
                return value;
            }

            @Override
            public boolean equals(Object obj) {
                // Reflexive check
                if (this == obj) return true;

                // Null check
                if (obj == null) return false;

                // Type check
                if (getClass() != obj.getClass()) return false;

                // Cast and compare
                Feet other = (Feet) obj;
                return Double.compare(this.value, other.value) == 0;
            }

            @Override
            public int hashCode() {
                return Double.hashCode(value);
            }
        }
    }

    // ----------- TEST CASES -----------

    @Test
    public void testFeetEquality_SameValue() {
        QuantityMeasurementApp.Feet f1 = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet f2 = new QuantityMeasurementApp.Feet(1.0);

        assertTrue(f1.equals(f2));
    }

    @Test
    public void testFeetEquality_DifferentValue() {
        QuantityMeasurementApp.Feet f1 = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet f2 = new QuantityMeasurementApp.Feet(2.0);

        assertFalse(f1.equals(f2));
    }

    @Test
    public void testFeetEquality_NullComparison() {
        QuantityMeasurementApp.Feet f1 = new QuantityMeasurementApp.Feet(1.0);

        assertFalse(f1.equals(null));
    }

    @Test
    public void testFeetEquality_DifferentClass() {
        QuantityMeasurementApp.Feet f1 = new QuantityMeasurementApp.Feet(1.0);
        Object obj = new Object();

        assertFalse(f1.equals(obj));
    }

    @Test
    public void testFeetEquality_SameReference() {
        QuantityMeasurementApp.Feet f1 = new QuantityMeasurementApp.Feet(1.0);

        assertTrue(f1.equals(f1));
    }
}

