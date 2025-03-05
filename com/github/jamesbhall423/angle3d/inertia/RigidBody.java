package com.github.jamesbhall423.angle3d.inertia;

import java.util.List;
import java.util.stream.Collectors;
/**
 * Represents a rigid body composed of multiple mass elements with combined rotational inertia.
 * 
 * <p>This class models a rigid structure that consists of one or more objects bound together, 
 * combining their mass and rotational properties. It allows for the calculation of the center of mass 
 * and total rotational inertia of the combined system.</p>
 */
public class RigidBody {
    private PointMass center;
    private SquareInertia rotationalInertia;

    /**
     * Constructs a rigid body with a specified center of mass and rotational inertia.
     * 
     * @param center The center of mass of the rigid body.
     * @param rotationalInertia The rotational inertia tensor of the rigid body.
     */
    public RigidBody(PointMass center, SquareInertia rotationalInertia) {
        this.center = center;
        this.rotationalInertia = rotationalInertia;
    }

    /**
     * Constructs a rigid body by combining multiple rigid body components.
     * 
     * <p>The resulting rigid body has a center of mass computed from the components, and its 
     * rotational inertia is determined by summing the centered inertia contributions of all components.</p>
     * 
     * @param components A list of rigid bodies to combine into a single rigid structure.
     */
    public RigidBody(List<RigidBody> components) {
        center = MomentCalculator.getCenterMass(
            components.stream().map((component) -> component.center).collect(Collectors.toList())
        );

        SquareInertia inertia = SquareInertia.getCentered(
            components.stream().map((component) -> component.center).collect(Collectors.toList())
        );

        for (RigidBody next : components) {
            inertia = MomentCalculator.sum(inertia, next.rotationalInertia);
        }
        rotationalInertia = inertia;
    }

    /**
     * Returns the center of mass of the rigid body.
     * 
     * @return The center of mass as a {@code PointMass}.
     */
    public PointMass center() {
        return center;
    }

    /**
     * Returns the rotational inertia tensor of the rigid body.
     * 
     * @return The rotational inertia represented as a {@code SquareInertia}.
     */
    public SquareInertia rotationalInertia() {
        return rotationalInertia;
    }
}