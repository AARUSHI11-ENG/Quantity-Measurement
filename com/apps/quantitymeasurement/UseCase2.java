package com.apps.quantitymeasurement;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UseCase2 {

    // ---------------- FEET CLASS ----------------
    public static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;

            Feet other = (Feet) obj;
            return Double.compare(this.value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }
    }

    // ---------------- INCHES CLASS ----------------
    public static class Inches {
        private final double value;

        public Inches(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;

            Inches other = (Inches) obj;
            return Double.compare(this.value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }
    }

    // ---------------- STATIC METHODS ----------------
    public static boolean compareFeet(double v1, double v2) {
        Feet f1 = new Feet(v1);
        Feet f2 = new Feet(v2);
        return f1.equals(f2);
    }

    public static boolean compareInches(double v1, double v2) {
        Inches i1 = new Inches(v1);
        Inches i2 = new Inches(v2);
        return i1.equals(i2);
    }

    // ---------------- MAIN METHOD ----------------
    public static void main(String[] args) {
        System.out.println("Feet equality (1.0, 1.0): " + compareFeet(1.0, 1.0));
        System.out.println("Inches equality (1.0, 1.0): " + compareInches(1.0, 1.0));
    }

    // ---------------- TEST CASES ----------------

    // FEET TESTS
    @Test
    public void testFeetEquality_SameValue() {
        assertTrue(compareFeet(1.0, 1.0));
    }

    @Test
    public void testFeetEquality_DifferentValue() {
        assertFalse(compareFeet(1.0, 2.0));
    }

    @Test
    public void testFeetEquality_NullComparison() {
        Feet f = new Feet(1.0);
        assertFalse(f.equals(null));
    }

    @Test
    public void testFeetEquality_DifferentClass() {
        Feet f = new Feet(1.0);
        Object obj = new Object();
        assertFalse(f.equals(obj));
    }

    @Test
    public void testFeetEquality_SameReference() {
        Feet f = new Feet(1.0);
        assertTrue(f.equals(f));
    }

    // INCHES TESTS
    @Test
    public void testInchesEquality_SameValue() {
        assertTrue(compareInches(1.0, 1.0));
    }

    @Test
    public void testInchesEquality_DifferentValue() {
        assertFalse(compareInches(1.0, 2.0));
    }

    @Test
    public void testInchesEquality_NullComparison() {
        Inches i = new Inches(1.0);
        assertNotNull(i);
    }

    @Test
    public void testInchesEquality_DifferentClass() {
        Inches i = new Inches(1.0);
        Object obj = new Object();
        assertNotEquals(i, obj);
    }

    @Test
    public void testInchesEquality_SameReference() {
        Inches i = new Inches(1.0);
        assertEquals(i, i);
    }
}
