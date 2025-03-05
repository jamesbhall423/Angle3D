package com.github.jamesbhall423.angle3d.angle3d;
import com.github.jamesbhall423.angle3d.position3d.AviationMapping;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;
/**
 * An immutable structure that represents a transformation of 3D space.
 * This rotation can be represented by a series of 2D rotations in the XY, YZ, and XZ planes.
 * Alternatively, the rotation can be accomplished by a single rotation around a single combined axis vector.
 * 
 * @author James Hall
 */
public interface Angle3D<T extends Angle3D<T>> extends Cloneable{
/**
     * Rotates the other angle by this angle
     *
     * @param other - The angle this will rotate
     * @return The resulting angle after rotation
     */
    public T rotate(T other);

    /**
     * Rotates a 3D vector by this angle.
     *
     * @param point - The vector to be rotated
     * @return The rotated vector
     */
    public Vector3D rotate(Vector3D point);

    /**
     * Computes the inverse of this angle.
     *
     * @return The inverse angle
     */
    public T inverse();

    /**
     * Scales this angle by a given factor.
     *
     * @param scale - The scaling factor
     * @return The scaled angle
     */
    public T scale(double scale);

    /**
     * Calculates the magnitude of this angle.
     *
     * @return The magnitude of the angle, in radians
     */
    public double magnitude();

    /**
     * Returns the axis of rotation for this angle.
     * The magnitude of the vector, in radians, will be the same as the magnitude of the angle
     *
     * @return The axis vector
     */
    public Vector3D axis();

    /**
     * Creates and returns a copy of this angle.
     *
     * @return A clone of this angle
     */
    public T clone();

    /**
     * Returns the angle system associated with this angle.
     *
     * @return The angle system
     */
    public Angle3DSystem<T> getAngleSystem();

    /**
     * Calculates the pitch component of this angle using the specified aviation mapping.
     *
     * @param mapping - The aviation mapping to use for the calculation
     * @return The pitch angle, in radians
     */
    public double getPitch(AviationMapping mapping);

    /**
     * Calculates the yaw component of this angle using the specified aviation mapping.
     *
     * @param mapping - The aviation mapping to use for the calculation
     * @return The yaw angle, in radians
     */
    public double getYaw(AviationMapping mapping);

    /**
     * Calculates the roll component of this angle using the specified aviation mapping.
     *
     * @param mapping - The aviation mapping to use for the calculation
     * @return The roll angle, in radians
     */
    public double getRoll(AviationMapping mapping);
}
