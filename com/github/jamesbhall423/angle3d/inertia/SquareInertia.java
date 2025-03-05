package com.github.jamesbhall423.angle3d.inertia;

import java.util.ArrayList;
import java.util.List;

import com.github.jamesbhall423.angle3d.angle3d.Angle3D;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;
/**
 * Represents the rotational inertia of a system
 * 
 * <p>(xx, yy, zz) represent inertia from vanriance
 * in the individual dimensions, while  (xy, yz, zx) 
 * represent correlations between dimensions.</p>
 * 
 * <p>
 * The moment of inertia in each axis is equal to the variance in the other two dimensions.
 * </p>
 */
public class SquareInertia {

    private double xx = 0;
    private double yy = 0;
    private double zz = 0;
    private double yz = 0;
    private double zx = 0;
    private double xy = 0;

    /**
     * Computes the absolute inertia tensor for a given list of point masses.
     * 
     * <p>The inertia tensor is calculated based on the mass and position of 
     * each point mass, assuming the origin is the reference point.</p>
     * 
     * @param masses The list of point masses to compute the inertia tensor from.
     * @return The absolute inertia tensor.
     */
    public static SquareInertia getAbsolute(List<PointMass> masses) {
        return new SquareInertia(masses);
    }

    /**
     * Computes the centered inertia tensor for a given list of point masses.
     * 
     * <p>This method centers the system around its center of mass before 
     * computing the inertia tensor.</p>
     * 
     * @param masses The list of point masses to compute the centered inertia tensor from.
     * @return The centered inertia tensor.
     */
    public static SquareInertia getCentered(List<PointMass> masses) {
        return new SquareInertia(MomentCalculator.center(masses));
    }

    /**
     * Constructs an inertia tensor from a list of point masses.
     * 
     * <p>Each point mass contributes to the tensor based on its mass and squared 
     * distance from the principal axes.</p>
     * 
     * @param masses The list of point masses used to compute the inertia tensor.
     */
    private SquareInertia(List<PointMass> masses) {
        for (PointMass mass : masses) {
            xx += mass.mass * mass.position.x() * mass.position.x();
            yy += mass.mass * mass.position.y() * mass.position.y();
            zz += mass.mass * mass.position.z() * mass.position.z();
            yz += mass.mass * mass.position.y() * mass.position.z();
            zx += mass.mass * mass.position.z() * mass.position.x();
            xy += mass.mass * mass.position.x() * mass.position.y();
        }
    }

    /**
     * Constructs an inertia tensor with explicit values for its components.
     * 
     * @param xx Moment of inertia from variance in the x dimension.
     * @param yy Moment of inertia from variance in the y dimension.
     * @param zz Moment of inertia from variance in the z dimension.
     * @param yz Product of inertia for the yz-plane.
     * @param zx Product of inertia for the zx-plane.
     * @param xy Product of inertia for the xy-plane.
     */
    public SquareInertia(double xx, double yy, double zz, double yz, double zx, double xy) {
        this.xx = xx;
        this.yy = yy;
        this.zz = zz;
        this.yz = yz;
        this.zx = zx;
        this.xy = xy;
    }

    /**
     * Returns the moment of inertia from variance in the x dimension.
     * 
     * @return The Ixx component of the inertia tensor.
     */
    public double xx() {
        return xx;
    }

    /**
     * Returns the moment of inertia from variance in the y dimension.
     * 
     * @return The Iyy component of the inertia tensor.
     */
    public double yy() {
        return yy;
    }

    /**
     * Returns the moment of inertia from variance in the z dimension.
     * 
     * @return The Izz component of the inertia tensor.
     */
    public double zz() {
        return zz;
    }

    /**
     * Returns the product of inertia for the yz-plane.
     * 
     * @return The Iyz component of the inertia tensor.
     */
    public double yz() {
        return yz;
    }

    /**
     * Returns the product of inertia for the zx-plane.
     * 
     * @return The Izx component of the inertia tensor.
     */
    public double zx() {
        return zx;
    }

    /**
     * Returns the product of inertia for the xy-plane.
     * 
     * @return The Ixy component of the inertia tensor.
     */
    public double xy() {
        return xy;
    }

    /**
     * Computes an equivalent set of point masses that would produce the same inertia tensor.
     * 
     * <p>This method creates a set of six point masses, each positioned along a principal axis
     * or along a diagonal, such that they collectively reproduce the same inertia tensor.</p>
     * 
     * @return A list of {@code PointMass} objects representing an equivalent mass distribution.
     */
    public List<PointMass> equivalentAbsoluteMass() {
        List<PointMass> out = new ArrayList<>();
        out.add(new PointMass(xx - xy - zx, new Vector3D(1, 0, 0)));
        out.add(new PointMass(yy - xy - yz, new Vector3D(0, 1, 0)));
        out.add(new PointMass(zz - zx - yz, new Vector3D(0, 0, 1)));
        out.add(new PointMass(yz, new Vector3D(0, 1, 1)));
        out.add(new PointMass(zx, new Vector3D(1, 0, 1)));
        out.add(new PointMass(xy, new Vector3D(1, 1, 0)));
        return out;
    }
    /**
     * Computes the rotational inertia formed by rotating a body with this inertia by the specified angle.
     * 
     * @return the rotated inertia
     * @param rotateBy The angle to rotate this inertia by.
     */
    public SquareInertia getRotated(Angle3D<?> rotateBy) {
        return getAbsolute(MomentCalculator.distributeRotation(equivalentAbsoluteMass(), rotateBy));
    }
    /**
     * Computes inertia formed by scaling the body by the specified amounts in the x, y, and z dimensions.
     * 
     * @return the scaled
     * @param scaleX The amount the X axis is scaled by
     * @param scaleY The amount the Y axis is scaled by
     * @param scaleY The amount the Z axis is scaled by
     */
    public SquareInertia getScaled(double scaleX, double scaleY, double scaleZ) {
        return new SquareInertia(xx*scaleX*scaleX, yy*scaleY*scaleY,zz*scaleZ*scaleZ, yz*scaleY*scaleZ, zx*scaleZ*scaleX, xy*scaleX*scaleY);
    }
}