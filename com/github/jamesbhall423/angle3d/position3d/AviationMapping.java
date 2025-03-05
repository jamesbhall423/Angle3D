package com.github.jamesbhall423.angle3d.position3d;
/**
 * Represents a mapping between Cartesian coordinate axes (X, Y, Z) 
 * and aviation axes (Longitudinal, Lateral, Vertical).
 * <p>
 * This class ensures that all three dimensions are properly assigned 
 * and provides access to both coordinate and aviation-based mappings.
 * </p>
 * 
 * @author James Hall
 */
public class AviationMapping {
    private final DimensionMapping x;
    private final DimensionMapping y;
    private final DimensionMapping z;
    private final DimensionMapping longitudinal;
    private final DimensionMapping lateral;
    private final DimensionMapping vertical;

    /**
     * Constructs an {@code AviationMapping} from an array of three {@code DimensionMapping} objects.
     * 
     * @param mappings An array containing exactly three {@code DimensionMapping} objects.
     * @throws IllegalArgumentException If the array length is not 3 or if any required mapping is missing.
     */
    public AviationMapping(DimensionMapping[] mappings) {
        if (mappings.length != 3) {
            throw new IllegalArgumentException("Mappings must have a length of 3. Value = " + mappings.length);
        }

        DimensionMapping tempX = null, tempY = null, tempZ = null;
        DimensionMapping tempLongitudinal = null, tempLateral = null, tempVertical = null;

        for (DimensionMapping next : mappings) {
            switch (next.aviation()) {
                case LATERAL:
                    tempLateral = next;
                    break;
                case LONGITUDINAL:
                    tempLongitudinal = next;
                    break;
                case VERTICAL:
                    tempVertical = next;
                    break;
            }
            switch (next.xyz()) {
                case X:
                    tempX = next;
                    break;
                case Y:
                    tempY = next;
                    break;
                case Z:
                    tempZ = next;
                    break;
            }
        }

        if (tempX == null) throw new IllegalArgumentException("X not specified");
        if (tempY == null) throw new IllegalArgumentException("Y not specified");
        if (tempZ == null) throw new IllegalArgumentException("Z not specified");
        if (tempVertical == null) throw new IllegalArgumentException("Vertical not specified");
        if (tempLongitudinal == null) throw new IllegalArgumentException("Longitudinal not specified");
        if (tempLateral == null) throw new IllegalArgumentException("Lateral not specified");

        this.x = tempX;
        this.y = tempY;
        this.z = tempZ;
        this.longitudinal = tempLongitudinal;
        this.lateral = tempLateral;
        this.vertical = tempVertical;
    }

    /**
     * @return The dimension mapping for the X-axis.
     */
    public DimensionMapping x() {
        return x;
    }

    /**
     * @return The dimension mapping for the Y-axis.
     */
    public DimensionMapping y() {
        return y;
    }

    /**
     * @return The dimension mapping for the Z-axis.
     */
    public DimensionMapping z() {
        return z;
    }

    /**
     * @return The dimension mapping for the lateral axis (rightward direction).
     */
    public DimensionMapping lateral() {
        return lateral;
    }

    /**
     * @return The dimension mapping for the vertical axis (upward direction).
     */
    public DimensionMapping vertical() {
        return vertical;
    }

    /**
     * @return The dimension mapping for the longitudinal axis (forward direction).
     */
    public DimensionMapping longitudinal() {
        return longitudinal;
    }
}