package com.github.jamesbhall423.angle3d.shape3d;


import com.github.jamesbhall423.angle3d.inertia.SquareInertia;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;
/**
 * Regular Octahedron Every vertex lies on an axis.
 */
public class Octahedron extends Solid {
    private static final double FACTOR = Math.sqrt(2)/3;
    private static final double ROOT2 = Math.sqrt(2);
    private double sideLength;
    /**
    * Constructs an {@code Octahedron} with the given radius and height.
    *
    * @param sideLength The length of an {@code Octahedron} edge
    */
    public Octahedron(double sideLength) {
        this.sideLength = sideLength;
    }
    
    /**
     * Calculates and retrieves the volume of the {@code Octahedron}.
     * 
     * @return The volume of the {@code Octahedron}.
     */
    @Override
    public double volume() {
        return FACTOR*sideLength*sideLength*sideLength;
    }

    /**
     * Calculates and retrieves the square moment of inertia of the {@code Octahedron}.
     * 
     * @return The square moment of inertia of the {@code Octahedron}.
     */
    @Override
    public SquareInertia momentOfInertia() {
        double oneDim = mass()*sideLength*sideLength/20;
        return new SquareInertia(oneDim,oneDim,oneDim,0,0,0);
    }
    
    /**
     * Determines if a given position is inside the {@code Octahedron}.
     * The {@code Octahedron} is assumed to be centered at the origin.
     * 
     * @param position The 3D position to check.
     * @return {@code true} if the position is inside the {@code Octahedron}, {@code false} otherwise.
     */
    @Override
    public boolean inSolid(Vector3D position) {
        return Math.abs(position.x())+Math.abs(position.y())+Math.abs(position.z())<=sideLength/ROOT2;
    }
}
