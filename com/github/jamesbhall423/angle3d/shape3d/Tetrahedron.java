package com.github.jamesbhall423.angle3d.shape3d;


import com.github.jamesbhall423.angle3d.inertia.SquareInertia;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;
/**
 * Regular Tetrahedron: One point is on the z axis (positive z value), another in the y-z plane (negative y value)
 */
public class Tetrahedron extends Solid {
    private static final double BASE_DEPTH = 1/(2*Math.sqrt(6));
    private static final double TRIANGLE_BASE_DEPTH = 1/(2*Math.sqrt(3));
    private static final double VOLUME_FACTOR = 1.0/(6*Math.sqrt(2));
    private double sideLength;
    /**
    * Constructs an {@code Tetrahedron} with the given radius and height.
    *
    * @param sideLength The length of an {@code Tetrahedron} edge
    */
    public Tetrahedron(double sideLength) {
        this.sideLength = sideLength;
    }
    

    /**
     * Calculates and retrieves the volume of the {@code Tetrahedron} object.
     * 
     * @return The volume of the {@code Tetrahedron}.
     */
    @Override
    public double volume() {
        return VOLUME_FACTOR*sideLength*sideLength*sideLength;
    }

    /**
     * Calculates and retrieves the square moment of inertia of the {@code Tetrahedron}.
     * 
     * @return The square moment of inertia of the {@code Tetrahedron}.
     */
    @Override
    public SquareInertia momentOfInertia() {
        // Reqular Tetrahedron rotationally symetric despite lacking reflective symmetry.
        // Each set of 4 points of the tetrahedron symmetry has the same inertia in all 3 dimensions.
        double intertia = mass()*sideLength*sideLength/40;
        return new SquareInertia(intertia, intertia, intertia, 0,0,0);
    }
    
    /**
     * Determines if a given position is inside the {@code Tetrahedron}.
     * The {@code Tetrahedron} is assumed to be centered at the origin.
     * 
     * @param position The 3D position to check.
     * @return {@code true} if the position is inside the {@code Tetrahedron}, {@code false} otherwise.
     */
    @Override
    public boolean inSolid(Vector3D position) {
        position = position.scale(1.0/sideLength);
        if (position.z()<-BASE_DEPTH) return false;
        if (position.z()/(4*BASE_DEPTH)-position.y()/(TRIANGLE_BASE_DEPTH)>0.75) return false;
        if (position.z()/(4*BASE_DEPTH)+position.y()/(2*TRIANGLE_BASE_DEPTH)+3*position.x()>0.75) return false;
        if (position.z()/(4*BASE_DEPTH)+position.y()/(2*TRIANGLE_BASE_DEPTH)-3*position.x()>0.75) return false;
        return true;
    }
    
}
