# Angle3D
## Overview
A library for calculating angles and rotations in 3D space. The library consists of the following sections.
### Angle3D
A package for calculating angles in 3D space. Angle3D and Angle3DSystem are the primary interfaces, QuaternionAngle3D and QuaternionSystem are the provided implementations. The classes contain methods for creating, concatenating, and interpreting angles in 3 dimensional space.
### Inertia
Consists of classes related to rotating and combining objects into a form that can calculate rotations.
### Position3D
Consists of general purpose classes for working in 3D space.
### Rotation3D
The RotatableBody class represents a body experiencing torque and rotation over time.
### Shape3D
Contains a number of Solid objects with thier rotational and inertial properties.
## Contributions
A number of areas could use contibutions. Add to the discussion wiki or open an issue request if you have ideas.
### Testing
Tests are needed for edge cases and as a combined system test against a real-world rotation measurement. Angles and rotations have been fully tested in their 2D components. Tests also identify that the precision of the calculation in general cases. However, the project does not yet have a real-world test for a general case. Such a test would be a welcome addition.
### Documentation
Help is needed with documentation. Even noticing when things seam unclear can help.
### Feature
Ideas for features are welcome.