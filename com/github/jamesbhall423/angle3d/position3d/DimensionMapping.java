package com.github.jamesbhall423.angle3d.position3d;
/**
 * Represents a mapping between a Cartesian coordinate system and an aviation coordinate system.
 * <p>
 * This mapping defines how a spatial axis (X, Y, or Z) corresponds to an aviation axis
 * (Longitudinal, Lateral, or Vertical). The mapping can be either:
 * <ul>
 *   <li><b>Direct:</b> An increase in the Cartesian coordinate results in an increase in the aviation coordinate.</li>
 *   <li><b>Inverse:</b> An increase in the Cartesian coordinate results in a decrease in the aviation coordinate.</li>
 * </ul>
 * This is determined by the {@code inverse} flag or a sign multiplier.
 * </p>
 *
 * @author James Hall
 */
public class DimensionMapping {
    private AviationAxis aviation;
    private boolean inverse;
    private CartesianAxis xyz;

    /**
     * Creates a mapping between an aviation axis and a Cartesian spatial axis.
     *
     * @param dimension The aviation axis (Longitudinal, Lateral, or Vertical).
     * @param inverse   {@code true} if the mapping is inverse (i.e., an increase in the Cartesian axis
     *                  results in a decrease in the aviation axis); {@code false} otherwise.
     * @param xyz       The corresponding spatial axis in the Cartesian system (X, Y, or Z).
     */
    public DimensionMapping(AviationAxis dimension, boolean inverse, CartesianAxis xyz) {
        this.aviation = dimension;
        this.inverse = inverse;
        this.xyz = xyz;
    }

    /**
     * Creates a mapping using a sign multiplier instead of a boolean inverse flag.
     * <p>
     * A negative sign value indicates an inverse mapping, while a positive sign value
     * indicates a direct mapping.
     * </p>
     *
     * @param dimension The aviation axis (Longitudinal, Lateral, or Vertical).
     * @param sign      The sign multiplier, where negative values indicate an inverse mapping.
     * @param xyz       The corresponding spatial axis in the Cartesian system (X, Y, or Z).
     */
    public DimensionMapping(AviationAxis dimension, double sign, CartesianAxis xyz) {
        this.aviation = dimension;
        this.inverse = (sign < 0);
        this.xyz = xyz;
    }

    /**
     * Returns the aviation axis associated with this mapping.
     *
     * @return The mapped aviation axis.
     */
    public AviationAxis aviation() {
        return aviation;
    }

    /**
     * Returns whether this mapping is inverse.
     * <p>
     * If {@code true}, an increase in the Cartesian axis results in a decrease in the aviation axis.
     * </p>
     *
     * @return {@code true} if the mapping is inverse, {@code false} otherwise.
     */
    public boolean inverse() {
        return inverse;
    }

    /**
     * Returns the corresponding spatial axis in the Cartesian coordinate system.
     *
     * @return The mapped Cartesian spatial axis (X, Y, or Z).
     */
    public CartesianAxis xyz() {
        return xyz;
    }

    /**
     * Returns the sign multiplier representing the mapping direction.
     * <p>
     * A direct mapping returns {@code 1}, while an inverse mapping returns {@code -1}.
     * </p>
     *
     * @return {@code 1} for direct mapping, {@code -1} for inverse mapping.
     */
    public double sign() {
        return inverse ? -1 : 1;
    }
}