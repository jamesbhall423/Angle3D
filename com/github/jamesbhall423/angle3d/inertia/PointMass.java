package com.github.jamesbhall423.angle3d.inertia;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;
/**
* Represents a point mass with a fixed position in three-dimensional space.
* 
* <p>This class models a mass located at a specific position, which can be 
* used for calculations involving center of mass, rotational inertia, and 
* other physics-based computations.</p>
*/
public class PointMass {
   
   /** The mass of the point mass. */
   public final double mass;

   /** The position of the point mass in 3D space. */
   public final Vector3D position;

   /**
    * Constructs a {@code PointMass} with the given mass and position.
    *
    * @param mass     The mass of the point mass.
    * @param position The position of the point mass in three-dimensional space.
    */
   public PointMass(double mass, Vector3D position) {
       this.mass = mass;
       this.position = position;
   }
}