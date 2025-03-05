package com.github.jamesbhall423.angle3d.position3d;
/**
 * A builder class for constructing aviation-related vectors based on aviation axes.
 * <p>
 * This class allows setting and retrieving values for the three aviation axes:
 * <ul>
 *   <li><b>Longitudinal:</b> Forward movement (typically longitude).</li>
 *   <li><b>Lateral:</b> Rightward movement (typically latitude).</li>
 *   <li><b>Vertical:</b> Upward movement (typically altitude).</li>
 * </ul>
 * The values set in this builder can then be used to construct a {@code Vector3D}
 * in a Cartesian coordinate system using an {@code AviationMapping}.
 * </p>
 *
 * <p>This class follows a builder pattern, allowing method chaining.</p>
 *
 * @author James Hall
 */
public class AviationVectorBuilder { 
    private double latitude = 0.0;
    private double longitude = 0.0;
    private double altitude = 0.0;

    /**
     * Sets the value for the specified aviation axis.
     * <p>
     * This method assigns a value to the appropriate axis based on the given {@code AviationAxis}.
     * </p>
     *
     * @param axis  The aviation axis to set (Longitudinal, Lateral, or Vertical).
     * @param value The value to assign to the specified axis.
     * @return This {@code AviationVectorBuilder} instance, enabling method chaining.
     * @throws IllegalArgumentException If an invalid axis is provided.
     */
    public AviationVectorBuilder set(AviationAxis axis, double value) {
        switch (axis) {
            case VERTICAL:
                altitude = value;
                break;
            case LATERAL:
                latitude = value;
                break;
            case LONGITUDINAL:
                longitude = value;
                break;
            default:
                throw new IllegalArgumentException("Invalid axis");
        }
        return this;
    }

    /**
     * Retrieves the value associated with the specified aviation axis.
     *
     * @param axis The aviation axis (Longitudinal, Lateral, or Vertical).
     * @return The value assigned to the given axis.
     * @throws IllegalArgumentException If an invalid axis is provided.
     */
    public double get(AviationAxis axis) {
        switch (axis) {
            case VERTICAL:
                return altitude;
            case LATERAL:
                return latitude;
            case LONGITUDINAL:
                return longitude;
        }
        throw new IllegalArgumentException("Invalid axis");
    }

    /**
     * This returns spacial coordinate specified axis
     * <p>
     * This returns the proper (x, y, or z) coordinate. This may be the same as an aviation coordinate, or it may be the negative of an aviation coordinate
     * </p>
     *
     * @param axis The {@code DimensionMapping} representing an aviation axis.
     * @return The signed value of the mapped coordinate.
     */
    public double get(DimensionMapping axis) {
        return axis.sign() * get(axis.aviation());
    }

    /**
     * Constructs a {@code Vector3D} using the specified {@code AviationMapping}.
     * <p>
     * This method converts aviation axis values into a 3D Cartesian vector 
     * using the given mapping, which defines how aviation axes correspond to X, Y, and Z.
     * </p>
     *
     * @param mapping The {@code AviationMapping} defining the coordinate transformation.
     * @return A {@code Vector3D} representing the aviation vector in Cartesian coordinates.
     */
    public Vector3D build(AviationMapping mapping) {
        return new Vector3D(get(mapping.x()), get(mapping.y()), get(mapping.z()));
    }
}