package com.github.jamesbhall423.angle3d.position3d;
/**
 * Represents a three-dimensional vector with various mathematical operations.
 * This class provides methods for vector arithmetic, normalization, magnitude calculations,
 * distance computation, and coordinate transformations.
 * 
 * All operations return new instances of {@code Vector3D}, ensuring immutability.
 * 
 * @author James Hall
 */
public class Vector3D {
    public static final Vector3D ZERO_VECTOR = new Vector3D(0, 0, 0);
    private double x;
    private double y;
    private double z;

    /**
     * Constructs a 3D vector with the specified coordinates.
     *
     * @param x the x-component of the vector
     * @param y the y-component of the vector
     * @param z the z-component of the vector
     */
    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the x-component of the vector.
     *
     * @return the x-coordinate
     */
    public double x() {
        return x;
    }

    /**
     * Returns the y-component of the vector.
     *
     * @return the y-coordinate
     */
    public double y() {
        return y;
    }

    /**
     * Returns the z-component of the vector.
     *
     * @return the z-coordinate
     */
    public double z() {
        return z;
    }

    /**
     * Adds the given vector to this vector element-wise.
     *
     * @param other the vector to add
     * @return a new vector representing the sum
     */
    public Vector3D sum(Vector3D other) {
        return new Vector3D(x + other.x, y + other.y, z + other.z);
    }

    /**
     * Scales this vector by a given factor.
     *
     * @param factor the scalar multiplier
     * @return a new vector representing the scaled vector
     */
    public Vector3D scale(double factor) {
        return new Vector3D(factor * x, factor * y, factor * z);
    }

    /**
     * Normalizes this vector, returning a unit vector in the same direction.
     * If the vector has zero magnitude, it returns a unit vector with an unspecified direction
     *
     * @return a new unit vector pointing in the direction of this vector
     */
    public Vector3D normalize() {
        double magnitude = magnitude();
        if (magnitude == 0.0) return new Vector3D(1.0, 0.0, 0.0);
        return scale(1 / magnitude);
    }

    /**
     * Computes the difference between this vector and another vector.
     *
     * @param other the vector to subtract
     * @return a new vector representing the difference
     */
    public Vector3D difference(Vector3D other) {
        return new Vector3D(x - other.x, y - other.y, z - other.z);
    }

    /**
     * Computes the squared magnitude (length squared) of this vector.
     *
     * @return the squared magnitude
     */
    public double sqMagnitude() {
        return x * x + y * y + z * z;
    }

    /**
     * Computes the magnitude (length) of this vector.
     *
     * @return the magnitude of the vector
     */
    public double magnitude() {
        return Math.sqrt(sqMagnitude());
    }

    /**
     * Computes the Euclidean distance between this vector and another vector.
     *
     * @param other the vector to measure distance to
     * @return the distance between the vectors
     */
    public double distance(Vector3D other) {
        return difference(other).magnitude();
    }

    /**
     * Performs element-wise division of this vector by another vector.
     * 
     * @param other the divisor vector
     * @return a new vector representing the element-wise division
     * @throws ArithmeticException if any component of {@code other} is zero
     */
    public Vector3D elementWiseDivide(Vector3D other) {
        if (other.x == 0 || other.y == 0 || other.z == 0) {
            throw new ArithmeticException("Division by zero in element-wise division");
        }
        return new Vector3D(x / other.x, y / other.y, z / other.z);
    }

    /**
     * Performs element-wise multiplication of this vector by another vector.
     * 
     * @param other the vector to multiply
     * @return a new vector representing the element-wise product
     */
    public Vector3D elementWiseProduct(Vector3D other) {
        return new Vector3D(x * other.x, y * other.y, z * other.z);
    }

    /**
     * Returns a string representation of this vector in the form (x, y, z).
     *
     * @return a string representation of the vector
     */
    @Override
    public String toString() {
        return "Vector3D: (" + x + "," + y + "," + z + ")";
    }

    /**
     * Retrieves the coordinate of this vector along a specified mapped axis.
     *
     * @param axis the axis mapping to use
     * @return the coordinate value based on the mapping
     * @throws IllegalArgumentException if an invalid spatial axis is provided
     */
    public double getCoordinate(DimensionMapping axis) {
        double mult = axis.sign();
        switch (axis.xyz()) {
            case X:
                return mult * x;
            case Y:
                return mult * y;
            case Z:
                return mult * z;
            default:
                throw new IllegalArgumentException("Invalid spatial axis");
        }
    }

    /**
     * Retrieves the vertical component of this vector based on an aviation mapping.
     *
     * @param mapping the aviation mapping
     * @return the vertical coordinate
     */
    public double getVertical(AviationMapping mapping) {
        return getCoordinate(mapping.vertical());
    }

    /**
     * Retrieves the lateral component of this vector based on an aviation mapping.
     *
     * @param mapping the aviation mapping
     * @return the lateral coordinate
     */
    public double getLateral(AviationMapping mapping) {
        return getCoordinate(mapping.lateral());
    }

    /**
     * Retrieves the longitudinal component of this vector based on an aviation mapping.
     *
     * @param mapping the aviation mapping
     * @return the longitudinal coordinate
     */
    public double getLongitudinal(AviationMapping mapping) {
        return getCoordinate(mapping.longitudinal());
    }

    /**
     * Computes the dot product of this vector with another vector.
     *
     * @param other the vector to compute the dot product with
     * @return the dot product (scalar value)
     */
    public double dotProduct(Vector3D other) {
        return x * other.x + y * other.y + z * other.z;
    }
}
