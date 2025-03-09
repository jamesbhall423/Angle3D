# Angle3D
## Overview
A library for calculating angles and rotations in 3D space. The library consists of the following sections.
### Angle3D
A package for calculating angles in 3D space. Angle3D and Angle3DSystem are the primary interfaces, QuaternionAngle3D and QuaternionSystem are the provided implementations. The classes contain methods for creating, concatenating, and interpreting angles in 3 dimensional space.
### Inertia
A package for combining and transforming objects into a form that can calculate rotations.
### Position3D
A package for general purpose 3D classes.
### Rotation3D
A package for rotations and torque over time in 3D space. The RotatableBody class represents the base class for these operations.
### Shape3D
A package for the rotational and inertial properties of common 3D objects. The classes representing these objects are extensions of the Solid class.
## Contributions
A number of areas could use contributions. Add to the discussion wiki or open an issue request if you have ideas.
### Testing
Tests are needed for edge cases and as a combined system test against a real-world rotation measurement. Angles and rotations have been fully tested in their 2D components. However, the project does not yet have a real-world test for a general case. Such a test would be a welcome addition.
### Documentation
Help is needed with documentation. Even pointing out unclear sections can help.
### Features
Ideas for features are welcome.