package com.github.jamesbhall423.angle3d.rotation3d;
import com.github.jamesbhall423.angle3d.angle3d.Angle3D;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;
/**
 * Represents a rigid three-dimensional rotating body with angular momentum and inertia.
 * 
 * @param <T> The type of 3D angle representation used, which must extend {@link Angle3D}.
 */
public class RotatableBody<T extends Angle3D<T>> {
    
    /** 
     * A representation of the private axis in global coordinates
     * Applying this angles changes an angle or vector written in local
     * Coordinates into an angle or vector written in global coordinates.
     */
    private T angle;
    
    /** The angular momentum of the body in global coordinates. */
    private Vector3D rotationalMomentumGlobal;
    
    /** The rotational inertia of the body along its principal axes. */
    private Vector3D rotationalInertia;
    
    /** The threshold for breaking up large rotations into smaller steps for better accuracy. */
    private double thresholdRotation = 0.01;

    /** The current time. */
    private double timeElapsed = 0.0;

    /** The current time. */
    private TorqueFetcher<T> torqueFetcher = (angle, time) -> Vector3D.ZERO_VECTOR;

    
    /**
     * Constructs a new {@code RotatableBody} with the specified rotational inertia.
     *
     * @param rotationalInertia The moment of inertia along the principal axes. All components must be nonzero.
     * @throws IllegalArgumentException If any component of {@code rotationalInertia} is zero.
     */
    public RotatableBody(Vector3D rotationalInertia) {
        if (rotationalInertia.x()==0||rotationalInertia.y()==0||rotationalInertia.z()==0) throw new IllegalArgumentException("Rotatable body must have inertia in all three dimensions.");
        this.rotationalInertia = rotationalInertia;
        this.rotationalMomentumGlobal = new Vector3D(0, 0, 0);
    }
    /**
     * Returns the rotation threshold, which determines the maximum angle change before subdividing rotations.
     *
     * @return The rotation threshold.
     */
    public double rotationThreshold() {
        // The rotation threshold is the maximum angle change that the rotatable body is allowed to calculate before being broken up into smaller pieces.
        // The angle calculation is accurate to the third power of the rotation threshold.
        return thresholdRotation;
    }
    /**
     * Sets the rotation threshold for breaking up rotations into smaller steps.
     *
     * @param rotationThreshold The new rotation threshold.
     */
    public void setRotationThreshold(double rotationThreshold) {
        this.thresholdRotation = rotationThreshold;
    }
     /**
     * Sets the angular momentum of the body in global coordinates.
     *
     * @param rotationalMomentumGlobal The new rotational momentum.
     */
    public void setRotationalMomentum(Vector3D rotationalMomentumGlobal) {
        this.rotationalMomentumGlobal = rotationalMomentumGlobal;
    }
    /**
     * Sets the global-to-local orientation of the body.
     *
     * @param angle The new orientation.
     */
    public void setAngle(T angle) {
        this.angle = angle;
    }
    /**
     * Returns the global-to-local orientation of the body.
     *
     * @return The current orientation.
     */
    public T getAngle() {
        return angle;
    }
    /**
     * Returns the elapsed time.
     *
     * @return The elapsed time.
     */
    public double getElapsedTime() {
        return timeElapsed;
    }
    /**
     * Sets the time.
     *
     * @param time The time.
     */
    public void setTime(double time) {
       this.timeElapsed = time;
    }
    /**
     * Sets the torque fetcher.
     *
     * @param time The torque fetcher.
     */
    public void setTorqueFetcher(TorqueFetcher<T> torqueFetcher) {
       this.torqueFetcher = torqueFetcher;
    }
    /**
     * Applies an angular acceleration to the body, modifying its rotational momentum.
     *
     * @param accel The angular acceleration to apply.
     */
    public void accelerateAngular(Vector3D accel) {
        rotationalMomentumGlobal = rotationalMomentumGlobal.sum(accel);
    }
    /**
     * Returns the current angular momentum in global coordinates.
     *
     * @return The rotational momentum.
     */
    public Vector3D getRotationalMomentum() {
        return rotationalMomentumGlobal;
    }
    /**
     * Returns the combined external and internal torque in global coordinates.
     *
     * @param delayTime The time passing before the measurement of the torque
     * @return The torque, or rate of change in angular momentum.
     */
    public Vector3D getTorqueGlobal(double delayTime) {
        // Torque fetcher is external resource, so public-facing get-angle instead of private facing angle is used.
        return torqueFetcher.getExternalTorque(getAngle(), timeElapsed+delayTime);
    }
     /**
     * Returns the angular momentum in the body's local coordinate system.
     *
     * @return The local rotational momentum.
     */
    private Vector3D rotationalMomentumLocal() {
        return angle.inverse().rotate(rotationalMomentumGlobal);
    }
    /**
    * Returns the angular momentum in the body's local coordinate system after the specified period of time.
    *
    * @return The local rotational momentum.
    */
   private Vector3D rotationalMomentumLocal(double delayTime) {
       return angle.inverse().rotate(rotationalMomentumAfterTime(delayTime));
   }
    
     /**
     * Returns the combined external and internal torque in local coordinates.
     *
     * @param delayTime The time passing before the measurement of the torque
     * @return The torque, or rate of change in angular momentum.
     */
    private Vector3D getTorqueLocal(double delayTime) {
       return angle.inverse().rotate(getTorqueGlobal(delayTime));
    }
    /**
     * Rotates the body forward in time by the given duration, using an integration scheme.
     *
     * @param time The duration to rotate the body for.
     */
    public void rotateForTime(double time) {
        while (time>0) {
            // Uses a variation of Simpson's rule
            // Runge–Kutta RK4 cannot be directly applied as rotational velocity is non-commutative
            Vector3D startingVelocity = rotationalVelocityLocal();
            double timeUse = getUseTime(startingVelocity,time);
            Vector3D startHalfTorque = getTorqueGlobal(0).scale(timeUse/2);
            Vector3D quarterVelocity = fetchVelocityAfterRotation(startingVelocity, timeUse/4);
            Vector3D halfVelocity = fetchVelocityAfterRotation(quarterVelocity, timeUse/2);
            Vector3D fullVelocity = fetchVelocityAfterRotation(halfVelocity, timeUse);
            Vector3D velocityEstimate = estimateAverageVelocity(startingVelocity,halfVelocity,fullVelocity);
            rotateAverageVelocity(velocityEstimate, timeUse);
            timeElapsed+=timeUse;
            Vector3D endHalfTorque = getTorqueGlobal(0).scale(timeUse/2);
            // Torques use trapezoidal rule instead. This gives 2nd power accuracy instead of third power when torque is applied
            rotationalMomentumGlobal = rotationalMomentumGlobal.sum(startHalfTorque).sum(endHalfTorque);
            time -= timeUse;
        }
    }
    private Vector3D rotationalMomentumAfterTime(double time) {
        return rotationalMomentumGlobal.sum(getTorqueGlobal(time/2).scale(time));
    }
    private Vector3D estimateAverageVelocity(Vector3D startingVelocity, Vector3D halfVelocity, Vector3D fullVelocity) {
        double midFactor = 4;
        return (startingVelocity.sum(halfVelocity.scale(midFactor)).sum(fullVelocity)).scale(1.0/(2.0+midFactor));
    }
    private Vector3D fetchVelocityAfterRotation(Vector3D velocity, double time) {
        T temp = angle;
        T localShiftHalf = getLocalRotation(velocity, time/2);
        rotateAverageVelocity(velocity, time);
        //Local half shift is correction for noncomunative rotation.
        // Correction for local angles use angle.rotate(newVelocity), correction for global angles use angle.inverse().rotate(newVelocity)
        // RotatableBody uses local angles
        Vector3D out = localShiftHalf.rotate(rotationalVelocityLocal(time));
        angle=temp;
        return out;
    }
    private T getLocalRotation(Vector3D velocity, double time) {
        return angle.getAngleSystem().fromAxis(velocity.scale(time));
    }
    private void rotateAverageVelocity(Vector3D velocity, double time) {
        angle = angle.rotate(getLocalRotation(velocity, time));
    }
    /**
     * 
     * @return the rotational velocity in local coordinates
     */
    private Vector3D rotationalVelocityLocal() {
        return rotationalVelocityLocal(0.0);
    }
    /**
     * 
     * @return the rotational velocity in local coordinates after the specified period of time
     */
    private Vector3D rotationalVelocityLocal(double delayTime) {
        Vector3D rotationalMomentumLocal = rotationalMomentumLocal(delayTime);
        Vector3D rotationalVelocityLocal = rotationalMomentumLocal.elementWiseDivide(rotationalInertia);
        return rotationalVelocityLocal;
    }
    private Vector3D rotationalAccelerationLocal() {
        Vector3D torqueLocal = getTorqueLocal(0);
        Vector3D rotationalAccelerationLocal = torqueLocal.elementWiseDivide(rotationalInertia);
        return rotationalAccelerationLocal;
    }
    private double getUseTime(Vector3D rotationalVelocityLocal, double maxTime) {
        return Math.min(getMaxVelocityTime(rotationalVelocityLocal, maxTime),getMaxAccelTime(rotationalAccelerationLocal(), maxTime));
    }
    private double getMaxVelocityTime(Vector3D rotationalVelocityLocal, double maxTime) {
        double maxVelocity = rotationalVelocityLocal.magnitude();
        if (maxVelocity < 1e-6) { // Prevent division zero or by near-zero
            return maxTime;
        }
        return Math.min(thresholdRotation / maxVelocity, maxTime);
    }
    private double getMaxAccelTime(Vector3D rotationalAccelLocal, double maxTime) {
        double accel = rotationalAccelLocal.magnitude();
        if (accel < 1e-6) { // Prevent division zero or by near-zero
            return maxTime;
        }
        return Math.min(thresholdRotation / Math.sqrt(accel), maxTime);
    }
    /**
     * Computes the rotational kinetic energy of the body.
     *
     * @return The rotational kinetic energy.
     */
    public double rotationalEnergy() {
        Vector3D momentumLocal = rotationalMomentumLocal();
        Vector3D parts = momentumLocal.elementWiseProduct(momentumLocal).elementWiseDivide(rotationalInertia);
        double sum = parts.x()+parts.y()+parts.z();
        return 0.5*sum;
    }
    /**
     * Rotates the body by the specified external angle.
     *
     * @param externalAngle The angle by which to rotate the body.
     */
    public void rotate(T externalAngle) {
        angle = externalAngle.rotate(angle);
    }
    /**
     * Returns a string representation of the {@code RotatableBody}.
     *
     * @return A string describing the rotatable body.
     */
    @Override
    public String toString() {
        return "RotatableBody: "+angle+" "+rotationalMomentumGlobal+" "+rotationalInertia+" "+thresholdRotation;
    }
}
