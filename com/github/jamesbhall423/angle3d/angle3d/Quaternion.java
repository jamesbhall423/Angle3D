package com.github.jamesbhall423.angle3d.angle3d;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;
/**
 * Represents a quaternion, a mathematical entity used for 3D rotations and complex number extensions.
 * Provides operations such as multiplication, addition, inversion, scaling, normalization, and vector rotation.
 * 
 * @author James Hall
 */
public class Quaternion implements Cloneable {
    private double real;
    private double i;
    private double j;
    private double k;

    /**
     * A builder class for creating Quaternion instances.
     */
    public static class Builder {
        public double real;
        public double i;
        public double j;
        public double k;

        /**
         * Sets the real part of the quaternion.
         * @param real the real component
         * @return this Builder instance
         */
        public Builder real(double real) {
            this.real = real;
            return this;
        }

        /**
         * Sets the i (x-axis imaginary) component.
         * @param i the i component
         * @return this Builder instance
         */
        public Builder i(double i) {
            this.i = i;
            return this;
        }

        /**
         * Sets the j (y-axis imaginary) component.
         * @param j the j component
         * @return this Builder instance
         */
        public Builder j(double j) {
            this.j = j;
            return this;
        }

        /**
         * Sets the k (z-axis imaginary) component.
         * @param k the k component
         * @return this Builder instance
         */
        public Builder k(double k) {
            this.k = k;
            return this;
        }

        /**
         * Builds and returns a new Quaternion instance.
         * @return a new Quaternion object
         */
        public Quaternion build() {
            return new Quaternion(real, i, j, k);
        }
    }

    /**
     * Returns a new builder for constructing quaternions.
     * @return a Builder instance
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Constructs a quaternion with the given components.
     * @param real the real component
     * @param i the i (x-axis imaginary) component
     * @param j the j (y-axis imaginary) component
     * @param k the k (z-axis imaginary) component
     */
    public Quaternion(double real, double i, double j, double k) {
        this.real = real;
        this.i = i;
        this.j = j;
        this.k = k;
    }

    /**
     * Constructs a pure imaginary quaternion from a 3D vector.
     * @param vector the vector representing the imaginary components
     */
    public Quaternion(Vector3D vector) {
        this(0, vector.x(), vector.y(), vector.z());
    }

    /**
     * Returns the real component of the quaternion.
     * @return the real component
     */
    public double real() {
        return real;
    }

    /**
     * Returns the i component of the quaternion.
     * @return the i component
     */
    public double i() {
        return i;
    }

    /**
     * Returns the j component of the quaternion.
     * @return the j component
     */
    public double j() {
        return j;
    }

    /**
     * Returns the k component of the quaternion.
     * @return the k component
     */
    public double k() {
        return k;
    }

    /**
     * Multiplies this quaternion by another quaternion.
     * @param right the quaternion to multiply with
     * @return the resulting quaternion
     */
    public Quaternion mult(Quaternion right) {
        return getBuilder()
            .real(real * right.real - i * right.i - j * right.j - k * right.k)
            .i(real * right.i + i * right.real + j * right.k - k * right.j)
            .j(real * right.j + j * right.real + k * right.i - i * right.k)
            .k(real * right.k + k * right.real + i * right.j - j * right.i)
            .build();
    }

    /**
     * Adds this quaternion to another quaternion.
     * @param right the quaternion to add
     * @return the resulting quaternion
     */
    public Quaternion sum(Quaternion right) {
        return getBuilder()
            .real(real + right.real)
            .i(i + right.i)
            .j(j + right.j)
            .k(k + right.k)
            .build();
    }

    /**
     * Returns the magnitude (norm) of the quaternion.
     * @return the magnitude of the quaternion
     */
    public double magnitude() {
        return Math.sqrt(sqMagnitude());
    }

    /**
     * Returns the squared magnitude of the quaternion.
     * @return the squared magnitude
     */
    public double sqMagnitude() {
        return real * real + i * i + j * j + k * k;
    }

    /**
     * Computes the inverse of the quaternion.
     * @return the inverted quaternion
     * @throws ArithmeticException if the quaternion has zero magnitude
     */
    public Quaternion inv() {
        double sqMagnitude = sqMagnitude();
        if (sqMagnitude == 0.0) throw new ArithmeticException("Cannot invert a Quaternion of magnitude 0");
        return new Quaternion(real, -i, -j, -k).scale(1 / sqMagnitude);
    }

    /**
     * Scales the quaternion by a given factor.
     * @param scale the scale factor
     * @return the scaled quaternion
     */
    public Quaternion scale(double scale) {
        return new Quaternion(real * scale, i * scale, j * scale, k * scale);
    }

    /**
     * Normalizes the quaternion to unit length.
     * @return a normalized quaternion
     */
    public Quaternion normalize() {
        double sign = real >= 0 ? 1 : -1;
        double mag = magnitude();
        if (mag == 0) return new Quaternion(1.0, 0.0, 0.0, 0.0);
        return scale(sign / mag);
    }

    /**
     * Normalizes the vector component of the quaternion to unit length.
     * @return a quaternion with a normalized vector part
     */
    public Quaternion normalizeVector() {
        double norm = Math.sqrt(i * i + j * j + k * k);
        if (norm == 0) return new Quaternion(0.0, 1.0, 0.0, 0.0);
        return new Quaternion(0, i / norm, j / norm, k / norm);
    }

    /**
     * Extracts the vector component of the quaternion.
     * @return a Vector3D representing the imaginary part
     */
    public Vector3D getVector() {
        return new Vector3D(i, j, k);
    }

    /**
     * Creates and returns a copy of this quaternion.
     * @return a cloned quaternion
     */
    public Quaternion clone() {
        return new Quaternion(real, i, j, k);
    }

    /**
     * Computes the dot product of this quaternion with another.
     * @param q the other quaternion
     * @return the dot product value
     */
    public double dot(Quaternion q) {
        return real * q.real + i * q.i + j * q.j + k * q.k;
    }

    /**
     * Computes the conjugate of the quaternion.
     * @return the conjugate of this quaternion
     */
    public Quaternion conjugate() {
        return new Quaternion(real, -i, -j, -k);
    }

    /**
     * Rotates a 3D vector using this quaternion.
     * @param v the vector to rotate
     * @return the rotated vector
     */
    public Vector3D rotateVector(Vector3D v) {
        Quaternion qVec = new Quaternion(0, v.x(), v.y(), v.z());
        Quaternion rotated = this.mult(qVec).mult(this.conjugate());
        return new Vector3D(rotated.i(), rotated.j(), rotated.k());
    }

    /**
     * Returns a string representation of the quaternion.
     * @return a string representing the quaternion
     */
    @Override
    public String toString() {
        return "Quaternion: (" + real + "," + i + "," + j + "," + k + ")";
    }
}
