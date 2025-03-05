package com.github.jamesbhall423.angle3d.angle3d;
import com.github.jamesbhall423.angle3d.position3d.DimensionMapping;
import com.github.jamesbhall423.angle3d.position3d.SpacialParity3D;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;
/**
 * Represents a system for creating and representing {@code Angle3D} objects.
 * 
 * @param <T> The type of {@code Angle3D} objects managed by this system.
 * @author James Hall
 */
public abstract class Angle3DSystem<T extends Angle3D<T>> {
    
    /**
     * The spatial parity of this angle system.
     */
    private final SpacialParity3D parity;
    
    /**
     * Constructs an {@code Angle3DSystem} with the specified spatial parity.
     *
     * @param parity The spatial parity of the system.
     */
    public Angle3DSystem(SpacialParity3D parity) {
        this.parity = parity;
    }
    
    /**
     * Returns the spatial parity of this angle system.
     *
     * @return The spatial parity.
     */
    public SpacialParity3D parity() {
        return parity;
    }
    
    /**
     * Creates an {@code Angle3D} object from the specified axis vector.
     *
     * @param axis The axis vector.
     * @return The corresponding {@code Angle3D} object.
     */
    public abstract T fromAxis(Vector3D axis);
    
    /**
     * Creates an {@code Angle3D} object representing a rotation in the XY plane.
     *
     * @param magnitude The angle magnitude.
     * @return The corresponding {@code Angle3D} object.
     */
    public abstract T angleXY(double magnitude);
    
    /**
     * Creates an {@code Angle3D} object representing a rotation in the XZ plane.
     *
     * @param magnitude The angle magnitude.
     * @return The corresponding {@code Angle3D} object.
     */
    public abstract T angleXZ(double magnitude);
    
    /**
     * Creates an {@code Angle3D} object representing a rotation in the YZ plane.
     *
     * @param magnitude The angle magnitude.
     * @return The corresponding {@code Angle3D} object.
     */
    public abstract T angleYZ(double magnitude);
    
    /**
     * Creates an {@code Angle3D} object representing a rotation in the YX plane.
     *
     * @param magnitude The angle magnitude.
     * @return The corresponding {@code Angle3D} object.
     */
    public abstract T angleYX(double magnitude);
    
    /**
     * Creates an {@code Angle3D} object representing a rotation in the ZX plane.
     *
     * @param magnitude The angle magnitude.
     * @return The corresponding {@code Angle3D} object.
     */
    public abstract T angleZX(double magnitude);
    
    /**
     * Creates an {@code Angle3D} object representing a rotation in the ZY plane.
     *
     * @param magnitude The angle magnitude.
     * @return The corresponding {@code Angle3D} object.
     */
    public abstract T angleZY(double magnitude);
    
    /**
     * Creates an {@code Angle3D} object for a rotation from one dimension mapping to another.
     *
     * @param axisFrom The initial axis mapping.
     * @param axisTo The target axis mapping.
     * @param magnitude The angle magnitude.
     * @return The corresponding {@code Angle3D} object.
     */
    public abstract T getAngle(DimensionMapping axisFrom, DimensionMapping axisTo, double magnitude);

}
