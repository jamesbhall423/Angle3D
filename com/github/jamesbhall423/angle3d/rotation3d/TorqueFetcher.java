package com.github.jamesbhall423.angle3d.rotation3d;

import com.github.jamesbhall423.angle3d.angle3d.Angle3D;
import com.github.jamesbhall423.angle3d.position3d.Vector3D;

public interface TorqueFetcher<T extends Angle3D<T>> {
    Vector3D getExternalTorque(T angle, double time);
}
