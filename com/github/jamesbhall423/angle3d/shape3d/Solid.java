package com.github.jamesbhall423.angle3d.shape3d;


import com.github.jamesbhall423.angle3d.inertia.SquareInertia;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;
/**
 * Represents a three-dimensional solid object with properties such as mass, density,
 * volume, and moments of inertia. This abstract class provides methods to set and retrieve
 * mass and density, and to calculate key physical properties of the solid.
 */
public abstract class Solid {
    /**
     * The mass of the solid object. Default value is 1.
     */
    private double mass = 1;

    /**
     * Default constructor that initializes the solid with default mass.
     */
    public Solid() {
    }

    /**
     * Sets the mass of the solid object.
     * 
     * @param mass The mass of the solid.
     * @return The current Solid instance for method chaining.
     */
    public Solid setMass(double mass) {
        this.mass = mass;
        return this;
    }

    /**
     * Sets the density of the solid object by calculating the mass based on the given density
     * and the volume of the solid.
     * 
     * @param density The density of the solid.
     * @return The current Solid instance for method chaining.
     */
    public Solid setDensity(double density) {
        this.mass = volume() * density;
        return this;
    }

    /**
     * Retrieves the mass of the solid object.
     * 
     * @return The mass of the solid.
     */
    public double mass() {
        return mass;
    }

    /**
     * Calculates and retrieves the density of the solid object.
     * 
     * @return The density of the solid.
     */
    public double density() {
        return mass / volume();
    }

    /**
     * Determines if a given position is inside the solid object.
     * The solid is assumed to be centered at the origin.
     * 
     * @param position The 3D position to check.
     * @return {@code true} if the position is inside the solid, {@code false} otherwise.
     */
    public abstract boolean inSolid(Vector3D position);

    /**
     * Calculates and retrieves the volume of the solid object.
     * 
     * @return The volume of the solid.
     */
    public abstract double volume();

    /**
     * Calculates and retrieves the square moment of inertia of the solid object.
     * 
     * @return The square moment of inertia of the solid.
     */
    public abstract SquareInertia momentOfInertia();
}
