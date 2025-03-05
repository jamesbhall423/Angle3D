package com.github.jamesbhall423.angle3d.shape3d;


import com.github.jamesbhall423.angle3d.inertia.SquareInertia;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;
/**
 * Rectangular Prism: the edges are parallel to the axes
 */
public class RectangularPrism extends Solid {
    private double xLength;
    private double yLength;
    private double zLength;
    /**
    * Constructs an {@code RectagularPrism} with the given dimensions.
    *
    * @param xLength The length along the x axis
    * @param yLength The length along the y axis
    * @param yLength The length along the z axis
    */
    public RectangularPrism(double xLength, double yLength, double zLength) {
        this.xLength=xLength;
        this.yLength=yLength;
        this.zLength=zLength;
    }
    
    /**
     * Calculates and retrieves the volume of the {@code RectagularPrism}.
     * 
     * @return The volume of the {@code RectagularPrism}.
     */
    @Override
    public double volume() {
        return xLength*yLength*zLength;
    }

    /**
     * Calculates and retrieves the square moment of inertia of the {@code RectagularPrism}.
     * 
     * @return The square moment of inertia of the {@code RectagularPrism}.
     */
    @Override
    public SquareInertia momentOfInertia() {
        double mass = mass();
        return new SquareInertia(mass*xLength*xLength/12, mass*yLength*yLength/12, mass*zLength*zLength/12, 0,0,0);
    }
    
    /**
     * Determines if a given position is inside the {@code RectagularPrism}.
     * The {@code RectagularPrism} is assumed to be centered at the origin.
     * 
     * @param position The 3D position to check.
     * @return {@code true} if the position is inside the {@code RectagularPrism}, {@code false} otherwise.
     */
    @Override
    public boolean inSolid(Vector3D position) {
        return Math.abs(position.x())<=xLength/2&&Math.abs(position.y())<=yLength/2&&Math.abs(position.z())<=zLength/2;
    }

}
