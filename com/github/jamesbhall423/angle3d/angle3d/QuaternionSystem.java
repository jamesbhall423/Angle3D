package com.github.jamesbhall423.angle3d.angle3d;
import com.github.jamesbhall423.angle3d.position3d.DimensionMapping;
import com.github.jamesbhall423.angle3d.position3d.SpacialParity3D;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;
/**
 * Represents a quaternion-based implementation of the {@code Angle3DSystem}.
 * This system provides methods for creating quaternion rotations around different planes
 * and constructing rotations from axis-angle representations.
 * <p>
 * This class follows a singleton pattern, with {@code INSTANCE} as the only instance.
 * </p>
 *
 * @author James Hall
 */
public class QuaternionSystem extends Angle3DSystem<QuaternionAngle3D> {

    /**
     * The singleton instance of {@code QuaternionSystem}.
     */
    public static final QuaternionSystem INSTANCE = new QuaternionSystem();

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes the system with a right-handed coordinate system.
     */
    private QuaternionSystem() {
        super(SpacialParity3D.RightHandXYZ);
    }

    /**
     * Creates the rotation about the XY plane (X -> Y -> -X).
     *
     * @param magnitude The rotation angle in radians.
     * @return A {@code QuaternionAngle3D} representing the rotation.
     */
    @Override
    public QuaternionAngle3D angleXY(double magnitude) {
        return new QuaternionAngle3D(new Quaternion(Math.cos(magnitude/2), 0, 0, Math.sin(magnitude/2)));
    }

    /**
     * Creates the rotation about the XZ plane (X -> Z -> -X).
     *
     * @param magnitude The rotation angle in radians.
     * @return A {@code QuaternionAngle3D} representing the rotation.
     */
    @Override
    public QuaternionAngle3D angleXZ(double magnitude) {
        return new QuaternionAngle3D(new Quaternion(Math.cos(magnitude/2), 0, -Math.sin(magnitude/2), 0));
    }

    /**
     * Creates the rotation about the YZ plane (Y -> Z -> -X).
     *
     * @param magnitude The rotation angle in radians.
     * @return A {@code QuaternionAngle3D} representing the rotation.
     */
    @Override
    public QuaternionAngle3D angleYZ(double magnitude) {
        return new QuaternionAngle3D(new Quaternion(Math.cos(magnitude/2), Math.sin(magnitude/2), 0, 0));
    }

    /**
     * Creates the rotation about the YX plane (Y -> X -> -Y).
     *
     * @param magnitude The rotation angle in radians.
     * @return A {@code QuaternionAngle3D} representing the rotation.
     */
    @Override
    public QuaternionAngle3D angleYX(double magnitude) {
        return new QuaternionAngle3D(new Quaternion(Math.cos(magnitude/2), 0, 0, -Math.sin(magnitude/2)));
    }

    /**
     * Creates the rotation about the ZX plane (Z -> X -> -Z).
     *
     * @param magnitude The rotation angle in radians.
     * @return A {@code QuaternionAngle3D} representing the rotation.
     */
    @Override
    public QuaternionAngle3D angleZX(double magnitude) {
        return new QuaternionAngle3D(new Quaternion(Math.cos(magnitude/2), 0, Math.sin(magnitude/2), 0));
    }

    /**
     * Creates the rotation about the ZY plane (Z -> Y -> -Z).
     *
     * @param magnitude The rotation angle in radians.
     * @return A {@code QuaternionAngle3D} representing the rotation.
     */
    @Override
    public QuaternionAngle3D angleZY(double magnitude) {
        return new QuaternionAngle3D(new Quaternion(Math.cos(magnitude/2), -Math.sin(magnitude/2), 0, 0));
    }

    /**
     * Creates a quaternion representing a rotation from an axis-angle representation.
     * The magnitude of the vector is the magnitude of the resulting rotation.
     *
     * @param axis The axis of rotation as a {@code Vector3D}.
     * @return A {@code QuaternionAngle3D} representing the rotation.
     */
    @Override
    public QuaternionAngle3D fromAxis(Vector3D axis) {
        double magnitude = axis.magnitude();
        if (magnitude == 0.0) return angleXY(0);
        Vector3D scaledAxis = axis.scale(Math.sin(magnitude / 2) / magnitude);
        return new QuaternionAngle3D(new Quaternion(Math.cos(magnitude / 2),
                                                    scaledAxis.x(),
                                                    scaledAxis.y(),
                                                    scaledAxis.z()));
    }

    /**
     * Creates a quaternion rotation between two coordinate axes based on the specified magnitude.
     * The magnitude is adjusted according to the signs of the axis mappings to calculate the true direction of the rotation angle.
     *
     * @param axisFrom The initial axis mapping.
     * @param axisTo The target axis mapping.
     * @param magnitude The rotation angle in radians.
     * @return A {@code QuaternionAngle3D} representing the rotation.
     * @throws IllegalArgumentException if {@code axisFrom} and {@code axisTo} are the same.
     */
    @Override
    public QuaternionAngle3D getAngle(DimensionMapping axisFrom, DimensionMapping axisTo, double magnitude) {
        double trueMagnitude = axisFrom.sign() * axisTo.sign() * magnitude;
        switch (axisFrom.xyz()) {
            case X:
                switch (axisTo.xyz()) {
                    case Y: return angleXY(trueMagnitude);
                    case Z: return angleXZ(trueMagnitude);
                }
                break;
            case Y:
                switch (axisTo.xyz()) {
                    case X: return angleYX(trueMagnitude);
                    case Z: return angleYZ(trueMagnitude);
                }
                break;
            case Z:
                switch (axisTo.xyz()) {
                    case X: return angleZX(trueMagnitude);
                    case Y: return angleZY(trueMagnitude);
                }
                break;
        }
        throw new IllegalArgumentException("Axes are the same");
    }
}