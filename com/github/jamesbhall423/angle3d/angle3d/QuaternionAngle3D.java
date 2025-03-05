package com.github.jamesbhall423.angle3d.angle3d;
import com.github.jamesbhall423.angle3d.position3d.AviationAxis;
import com.github.jamesbhall423.angle3d.position3d.AviationMapping;
import com.github.jamesbhall423.angle3d.position3d.AviationVectorBuilder;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;
/**
 * Represents a 3D rotational transformation using quaternions.
 * This class provides methods to rotate vectors, scale rotations, compute inverses, 
 * and extract pitch, yaw, and roll angles.
 * 
 * @author James Hall
 */
public class QuaternionAngle3D implements Angle3D<QuaternionAngle3D> {
    private Quaternion value;

    /**
     * Constructs a quaternion-based 3D angle. Ensures a unique representation by normalizing 
     * the quaternion to have a non-negative real component.
     *
     * @param in The quaternion representing the rotation.
     */
    public QuaternionAngle3D(Quaternion in) {
        if (in.real() >= 0) value = in.clone();
        else value = in.scale(-1);
    }

    /**
     * Rotates another quaternion-based angle by this quaternion-based rotation
     *
     * @param other The other angle to rotate by.
     * @return A new QuaternionAngle3D representing the combined rotation.
     */
    @Override
    public QuaternionAngle3D rotate(QuaternionAngle3D other) {
        return new QuaternionAngle3D(value.mult(other.value).normalize());
    }

    /**
     * Rotates a given 3D vector using this quaternion-based rotation.
     *
     * @param point The vector to be rotated.
     * @return The rotated vector.
     */
    @Override
    public Vector3D rotate(Vector3D point) {
        return value.rotateVector(point);
    }

    /**
     * Computes the inverse of this rotation.
     *
     * @return A new QuaternionAngle3D representing the inverse rotation.
     */
    @Override
    public QuaternionAngle3D inverse() {
        return new QuaternionAngle3D(value.inv());
    }

    /**
     * Scales this rotation by a given factor.
     *
     * @param scale The scaling factor.
     * @return A new QuaternionAngle3D with the scaled rotation.
     */
    @Override
    public QuaternionAngle3D scale(double scale) {
        double newScale = scale * magnitude();
        return scaleToSize(newScale);
    }

    /**
     * Rescales the rotation to a specific magnitude.
     * If this angle has 0 magnitude, the result of this operation is undefined
     *
     * @param scale The desired rotation magnitude in radians.
     * @return A new QuaternionAngle3D with the specified magnitude.
     */
    public QuaternionAngle3D scaleToSize(double scale) {
        double vectorStrength = Math.sin(scale / 2);
        Vector3D direction = value.normalizeVector().getVector();
        return new QuaternionAngle3D(new Quaternion(Math.cos(scale / 2), 
            vectorStrength * direction.x(), 
            vectorStrength * direction.y(), 
            vectorStrength * direction.z()));
    }

    /**
     * Computes the magnitude (angle in radians) of this rotation.
     *
     * @return The rotation angle in radians.
     */
    @Override
    public double magnitude() {
        return 2 * Math.acos(value.real());
    }

    /**
     * Retrieves the axis of rotation for this quaternion-based angle.
     * The vector's magnitude corresponds to the rotation angle.
     *
     * @return The axis vector scaled by the rotation magnitude.
     */
    @Override
    public Vector3D axis() {
        Vector3D vector = value.normalizeVector().getVector();
        return vector.scale(magnitude());
    }

    /**
     * Creates and returns a copy of this quaternion angle.
     *
     * @return A new QuaternionAngle3D instance with the same rotation.
     */
    public QuaternionAngle3D clone() {
        try {
            return (QuaternionAngle3D) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the associated angle system for quaternion-based rotations.
     *
     * @return The quaternion angle system.
     */
    @Override
    public QuaternionSystem getAngleSystem() {
        return QuaternionSystem.INSTANCE;
    }

    /**
     * Returns a string representation of this quaternion angle.
     *
     * @return A string describing the quaternion rotation.
     */
    @Override
    public String toString() {
        return "Angle3D: " + value;
    }

    /**
     * Computes the pitch (rotation about the lateral axis) from this angle.
     *
     * @param mapping The aviation mapping to use for interpretation.
     * @return The pitch angle in radians.
     */
    @Override
    public double getPitch(AviationMapping mapping) {
        Vector3D use = new AviationVectorBuilder().set(AviationAxis.LONGITUDINAL, 1.0).build(mapping);
        Vector3D rotated = rotate(use);
        return Math.asin(rotated.getVertical(mapping));
    }

    /**
     * Computes the yaw (rotation about the vertical axis) from this angle.
     *
     * @param mapping The aviation mapping to use for interpretation.
     * @return The yaw angle in radians.
     */
    @Override
    public double getYaw(AviationMapping mapping) {
        Vector3D use = new AviationVectorBuilder().set(AviationAxis.LONGITUDINAL, 1.0).build(mapping);
        Vector3D rotated = rotate(use);
        return Math.atan2(-rotated.getLateral(mapping), rotated.getLongitudinal(mapping));
    }

    /**
     * Computes the roll (rotation about the longitudinal axis) from this angle.
     *
     * @param mapping The aviation mapping to use for interpretation.
     * @return The roll angle in radians.
     */
    @Override
    public double getRoll(AviationMapping mapping) {
        Vector3D use = new AviationVectorBuilder().set(AviationAxis.LATERAL, 1.0).build(mapping);
        Vector3D rotated = rotate(use);
        double cosPitch = Math.max(1e-10, Math.cos(getPitch(mapping)));
        return Math.asin(-rotated.getVertical(mapping) / cosPitch);
    }
}