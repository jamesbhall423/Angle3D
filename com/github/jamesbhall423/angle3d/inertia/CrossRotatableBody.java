package com.github.jamesbhall423.angle3d.inertia;

import com.github.jamesbhall423.angle3d.angle3d.Angle3D;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;
import com.github.jamesbhall423.angle3d.rotation3d.RotatableBody;
/**
 * Represents a rotatable body that applies a correction angle to account for cross-rotational inertia.
 * 
 * <p>This class extends {@link RotatableBody} and allows for transformations between a coordinate system
 * with cross-rotational inertia and one without. The correction angle is used to adjust the rotation
 * so that calculations can be performed in a standard vector rotational inertia system.</p>
 *
 * @param <T> The type of angle used for rotation, which must extend {@link Angle3D}.
 */
public class CrossRotatableBody<T extends Angle3D<T>> extends RotatableBody<T> {
    
    private T correctionAngle;

    /**
     * Constructs a {@code CrossRotatableBody} with the given rotational inertia and correction angle.
     *
     * @param rotationalInertia The rotational inertia represented as a {@link Vector3D}.
     * @param correctionAngle   The correction angle that transforms from a no-cross axis 
     *                          to a cross-coordinate system.
     */
    public CrossRotatableBody(Vector3D rotationalInertia, T correctionAngle) {
        super(rotationalInertia);
        
        /*
         * The correction angle represents the no-cross axis in cross-coordinates,
         * effectively transforming from the no-cross system to the cross system.
         */
        this.correctionAngle = correctionAngle;
        setAngle(correctionAngle.getAngleSystem().angleXY(0));
    }

    /**
     * Returns the corrected angle such that setting this angle using {@code setAngle()} 
     * will result in the same value being retrieved using {@code getAngle()}.
     * 
     * <p>The transformation follows:
     * {@code setAngle = angle * correctionInv}
     * {@code setAngle * correctionAngle = angle * correctionInv * correctionAngle = angle}</p>
     *
     * @return The adjusted angle in the corrected rotational coordinate system.
     */
    @Override
    public T getAngle() {
        return super.getAngle().rotate(correctionAngle);
    }

    /**
     * Sets the angle after transforming it from the cross-coordinate system to the no-cross system.
     * 
     * <p>This ensures that internal angle calculations are performed in a system without 
     * cross-rotational inertia.</p>
     *
     * @param angle The angle to set in the cross-coordinate system.
     */
    @Override
    public void setAngle(T angle) {
        super.setAngle(angle.rotate(correctionAngle.inverse()));
    }

    /**
     * Returns a string representation of the {@code CrossRotatableBody}, 
     * including its base properties and the correction angle.
     *
     * @return A string containing the rotation properties and the correction angle.
     */
    @Override
    public String toString() {
        return super.toString() + " correction angle: " + correctionAngle;
    }

    /**
     * Gets the correction angle used for transforming between cross and no-cross coordinates.
     *
     * @return The correction angle.
     */
    public T getCorrectionAngle() {
        return correctionAngle;
    }

    /**
     * Returns the raw angle stored in the parent {@link RotatableBody} without applying the correction transformation.
     *
     * @return The uncorrected angle.
     */
    public T getSuperAngle() {
        return super.getAngle();
    }
}