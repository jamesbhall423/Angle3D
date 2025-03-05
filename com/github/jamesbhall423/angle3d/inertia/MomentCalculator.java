package com.github.jamesbhall423.angle3d.inertia;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.jamesbhall423.angle3d.angle3d.*;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;
import com.github.jamesbhall423.angle3d.rotation3d.RotatableBody;
/**
 * Provides utility methods for calculating moments of inertia, centering mass distributions, 
 * and converting between different rotational representations.
 * 
 * <p>This class includes methods for computing the center of mass, determining the rotation 
 * needed to align with principal axes, and converting a mass distribution into a rotatable 
 * body representation.</p>
 */
public class MomentCalculator {

    /**
     * Centers a given list of point masses by shifting their positions relative to the center of mass.
     * 
     * @param masses The list of point masses.
     * @return A new list of point masses with positions relative to the center of mass.
     */
    public static List<PointMass> center(List<PointMass> masses) {
        Vector3D center = getCenter(masses);
        List<PointMass> out = new ArrayList<>();
        for (PointMass next : masses) {
            out.add(new PointMass(next.mass, next.position.difference(center)));
        }
        return out;
    }

    /**
     * Computes the center of mass position for a given list of point masses.
     * 
     * @param masses The list of point masses.
     * @return The center of mass as a {@code Vector3D}.
     */
    public static Vector3D getCenter(List<PointMass> masses) {
        return getCenterMass(masses).position;
    }

    /**
     * Computes the center of mass for a given list of point masses, returning it as a {@code PointMass}.
     * 
     * @param masses The list of point masses.
     * @return A {@code PointMass} representing the total mass and center of mass position.
     */
    public static PointMass getCenterMass(List<PointMass> masses) {
        Vector3D center = new Vector3D(0, 0, 0);
        double mass = 0.0;
        for (PointMass next : masses) {
            center = center.sum(next.position.scale(next.mass));
            mass += next.mass;
        }
        center = center.scale(1 / mass);
        return new PointMass(mass, center);
    }

    /**
     * Determines the rotation needed to align the inertia tensor with its principal axes.
     * 
     * <p>This method iteratively refines the rotation to minimize cross terms in the inertia tensor.</p>
     * 
     * @param <T>     The type of {@code Angle3D}.
     * @param system  The angle system used for calculations.
     * @param body    The inertia tensor represented as a {@code SquareInertia}.
     * @return The rotation needed to align with the principal axes.
     */
    public static <T extends Angle3D<T>> T getRotationToAxis(Angle3DSystem<T> system, SquareInertia body) {
        double frac = 0.5;
        double threshold = Math.pow(10, -12);
        T rotation = system.angleXY(0);
        List<PointMass> distribution = body.equivalentAbsoluteMass();
        RotationTestClass<T> tester = new RotationTestClass<>(rotation, distribution);
        while (frac > threshold) {
            if (!tester.test(system.angleXY(frac))
                && !tester.test(system.angleZX(frac))
                && !tester.test(system.angleYZ(frac))
                && !tester.test(system.angleYX(frac))
                && !tester.test(system.angleXZ(frac))
                && !tester.test(system.angleZY(frac))) {
                frac /= 2;
            }
        }
        System.out.println("Final tester rotation: " + tester.rotation);
        return tester.rotation;
    }

    /**
     * A helper class used to test and refine rotations that minimize cross-inertia.
     */
    private static class RotationTestClass<T extends Angle3D<T>> {
        T rotation;
        double crossInertia;
        List<PointMass> distribution;

        /**
         * Constructs a rotation tester with an initial rotation and mass distribution.
         * 
         * @param rotation     The initial rotation.
         * @param distribution The mass distribution.
         */
        public RotationTestClass(T rotation, List<PointMass> distribution) {
            this.rotation = rotation;
            this.distribution = distribution;
            crossInertia = getCrossInertia(distribution);
        }

        /**
         * Tests whether a new rotation reduces the cross-inertia.
         * 
         * @param newRotation The new rotation to test.
         * @return {@code true} if the new rotation reduces cross-inertia, otherwise {@code false}.
         */
        public boolean test(T newRotation) {
            T combined = newRotation.rotate(rotation);
            List<PointMass> newDistribution = distributeRotation(distribution, combined);
            double newInertia = getCrossInertia(newDistribution);
            if (newInertia >= crossInertia) return false;
            rotation = combined;
            crossInertia = newInertia;
            return true;
        }
    }

    /**
     * Rotates a given list of point masses by a specified angle.
     * 
     * @param in       The list of point masses to rotate.
     * @param rotation The rotation to apply.
     * @return A new list of rotated point masses.
     */
    public static List<PointMass> distributeRotation(List<PointMass> in, Angle3D<?> rotation) {
        return in.stream()
                 .map(next -> new PointMass(next.mass, rotation.rotate(next.position)))
                 .collect(Collectors.toList());
    }

    /**
     * Computes the cross-inertia of a given mass distribution.
     * 
     * <p>Cross-inertia is a measure of how mass is distributed relative to the principal axes.</p>
     * 
     * @param masses The list of point masses.
     * @return The computed cross-inertia.
     */
    private static double getCrossInertia(List<PointMass> masses) {
        SquareInertia body = SquareInertia.getAbsolute(masses);
        return body.xy() * body.xy() + body.yz() * body.yz() + body.zx() * body.zx();
    }

    /**
     * Constructs a {@code RotatableBody} representation from an inertia tensor.
     * 
     * <p>The method determines the appropriate rotation and converts the inertia tensor 
     * into a form that can be used in rotational dynamics calculations.</p>
     * 
     * @param <T>    The type of {@code Angle3D}.
     * @param system The angle system used for calculations.
     * @param body   The inertia tensor.
     * @return A {@code RotatableBody} representing the rotational inertia.
     */
    public static <T extends Angle3D<T>> RotatableBody<T> getRotatableBody(Angle3DSystem<T> system, SquareInertia body) {
        T angle = getRotationToAxis(system, body);
        SquareInertia newInertia = SquareInertia.getAbsolute(distributeRotation(body.equivalentAbsoluteMass(), angle));
        return new CrossRotatableBody<>(
            new Vector3D(newInertia.zz() + newInertia.yy(), newInertia.xx() + newInertia.zz(), newInertia.xx() + newInertia.yy()), 
            angle
        );
    }

    /**
     * Computes the sum of two inertia tensors.
     * 
     * <p>This method is useful for combining the inertia tensors of separate bodies.</p>
     * 
     * @param s1 The first inertia tensor.
     * @param s2 The second inertia tensor.
     * @return The summed inertia tensor.
     */
    public static SquareInertia sum(SquareInertia s1, SquareInertia s2) {
        return new SquareInertia(
            s1.xx() + s2.xx(), 
            s1.yy() + s2.yy(), 
            s1.zz() + s2.zz(), 
            s1.yz() + s2.yz(), 
            s1.zx() + s2.zx(), 
            s1.xy() + s2.xy()
        );
    }
}