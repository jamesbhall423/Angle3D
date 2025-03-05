package com.github.jamesbhall423.angle3d.shape3d;



import com.github.jamesbhall423.angle3d.inertia.SquareInertia;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;

/**
 * Right cylinder. The height runs along the z axis
 */
public class Cylinder extends Solid {
    private double radius;
    private double height;
    /**
    * Constructs a {@code Cylinder} with the given radius and height.
    *
    * @param radius The radius of the {@code Cylinder}
    * @param height The height of the {@code Cylinder}
    */
    public Cylinder(double radius, double height) {
        this.radius=radius;
        this.height=height;
    }

    
    /**
     * Calculates and retrieves the volume of the {@code Cylinder}.
     * 
     * @return The volume of the {@code Cylinder}.
     */
    @Override
    public double volume() {
        return Math.PI*radius*radius*height;
    }

    /**
     * Calculates and retrieves the square moment of inertia of the {@code Cylinder}.
     * 
     * @return The square moment of inertia of the {@code Cylinder}.
     */
    @Override
    public SquareInertia momentOfInertia() {
        double mass = mass();
        return new SquareInertia(0.25*mass*radius*radius, 0.25*mass*radius*radius, mass*height*height/12, 0,0,0);
    }
    
    /**
     * Determines if a given position is inside the {@code Cylinder}.
     * The {@code Cylinder} is assumed to be centered at the origin.
     * 
     * @param position The 3D position to check.
     * @return {@code true} if the position is inside the {@code Cylinder}, {@code false} otherwise.
     */
    @Override
    public boolean inSolid(Vector3D position) {
        if (position.z()>0.5*height||position.z()<-0.5*height) return false;
        return (radius*radius>=position.x()*position.x()+position.y()*position.y());
    }
}
