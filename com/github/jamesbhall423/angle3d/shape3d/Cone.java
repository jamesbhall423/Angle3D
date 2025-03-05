package com.github.jamesbhall423.angle3d.shape3d;

import com.github.jamesbhall423.angle3d.inertia.SquareInertia;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;
/**
 * Right circular cone. The height runs along the z axis
 */
public class Cone extends Solid {
    private static final double Z_INERTIA_FACTOR = 3.0/80;
    private static final double XY_INERTIA_FACTOR = 3.0/20;
    private double radius;
    private double height;
    /**
    * Constructs a {@code Cone} with the given radius and height.
    *
    * @param radius The radius of the {@code Cone} base
    * @param height The height of the {@code Cone}
    */
    public Cone(double radius, double height) {
        this.radius=radius;
        this.height=height;
    }

    /**
     * Calculates and retrieves the volume of the {@code Cone}.
     * 
     * @return The volume of the {@code Cone}.
     */
    @Override
    public double volume() {
        return Math.PI*radius*radius*height/3;
    }

    /**
     * Calculates and retrieves the square moment of inertia of the {@code Cone}.
     * 
     * @return The square moment of inertia of the {@code Cone}.
     */
    @Override
    public SquareInertia momentOfInertia() {
        double mass = mass();
        return new SquareInertia(XY_INERTIA_FACTOR*mass*radius*radius, XY_INERTIA_FACTOR*mass*radius*radius, Z_INERTIA_FACTOR*mass*height*height, 0,0,0);
    }

    /**
     * Determines if a given position is inside the {@code Cone}.
     * The {@code Cone} is assumed to be centered at the origin.
     * 
     * @param position The 3D position to check.
     * @return {@code true} if the position is inside the {@code Cone}, {@code false} otherwise.
     */
    @Override
    public boolean inSolid(Vector3D position) {
        if (position.z()>0.75*height||position.z()<-0.25*height) return false;
        double proportion = (0.75-position.z()/height);
        return (proportion*proportion*radius*radius>=position.x()*position.x()+position.y()*position.y());
    }
    
}
