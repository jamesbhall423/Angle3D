package com.github.jamesbhall423.angle3d.angle3d;

import com.github.jamesbhall423.angle3d.position3d.AviationMapping;

public class AviationAngleBuilder {
    private double pitch=0.0;
    private double yaw=0.0;
    private double roll=0.0;
    public AviationAngleBuilder pitch(double pitch) {
        this.pitch=pitch;
        return this;
    }
    public AviationAngleBuilder yaw(double yaw) {
        this.yaw=yaw;
        return this;
    }
    public AviationAngleBuilder roll(double roll) {
        this.roll=roll;
        return this;
    }
    public <T extends Angle3D<T>> T build(AviationMapping mapping, Angle3DSystem<T> system) {
        T pitchT = system.getAngle(mapping.longitudinal(), mapping.vertical(), pitch);
        T yawT = system.getAngle(mapping.lateral(), mapping.longitudinal(), yaw);
        T rollT = system.getAngle(mapping.vertical(), mapping.lateral(), roll);
        return yawT.rotate(pitchT.rotate(rollT));
    }
}
