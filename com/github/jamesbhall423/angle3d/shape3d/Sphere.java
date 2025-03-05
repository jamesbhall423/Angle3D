package com.github.jamesbhall423.angle3d.shape3d;


import com.github.jamesbhall423.angle3d.inertia.SquareInertia;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;
/**
 * Sphere
 */
public class Sphere extends Solid {
    private double radius;
    /**
    * Constructs a {@code Sphere} with the given radius.
    *
    * @param radius The radius of the {@code Sphere}
    */
    public Sphere(double radius) {
        this.radius=radius;
    }
    
    /**
     * Calculates and retrieves the volume of the {@code Sphere}.
     * 
     * @return The volume of the {@code Sphere}.
     */
    @Override
    public double volume() {
        return 4.0/3.0*Math.PI*radius*radius*radius;
    }

    /**
     * Calculates and retrieves the square moment of inertia of the {@code Sphere}.
     * 
     * @return The square moment of inertia of the {@code Sphere}.
     */
    @Override
    public SquareInertia momentOfInertia() {
        double inertia = mass()*radius*radius/5;
        return new SquareInertia(inertia,inertia,inertia,0,0,0);
    }
    
    /**
     * Determines if a given position is inside the {@code Sphere}.
     * The {@code Sphere} is assumed to be centered at the origin.
     * 
     * @param position The 3D position to check.
     * @return {@code true} if the position is inside the {@code Sphere}, {@code false} otherwise.
     */
    @Override
    public boolean inSolid(Vector3D position) {
        return position.x()*position.x()+position.y()*position.y()+position.z()*position.z()<radius*radius;
    }
    
}
