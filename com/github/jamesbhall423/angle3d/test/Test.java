package com.github.jamesbhall423.angle3d.test;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.github.jamesbhall423.angle3d.angle3d.*;
import com.github.jamesbhall423.angle3d.inertia.*;
import com.github.jamesbhall423.angle3d.position3d.*;
import com.github.jamesbhall423.angle3d.rotation3d.*;
import com.github.jamesbhall423.angle3d.shape3d.*;

import static com.github.jamesbhall423.angle3d.position3d.AviationAxis.*;
import static com.github.jamesbhall423.angle3d.position3d.CartesianAxis.*;

public class Test<T extends Angle3D<T>, S extends Angle3DSystem<T>> {
    S system;
    public Test(S system) {
        this.system = system;
    }
    private void assertEquivelant(Vector3D vector1, Vector3D vector2) {
        System.out.println(vector1);
        System.out.println(vector2);
        System.out.println(vector1.distance(vector2));
        if (!(vector1.distance(vector2)<0.001)) throw new RuntimeException();
    }
    public void testXY() {
        Vector3D testVector = new Vector3D(1,0,0);
        double shift = Math.PI/6;
        Vector3D result = system.angleXY(shift).rotate(testVector);
        Vector3D expected = new Vector3D(Math.cos(shift),Math.sin(shift),0);
        assertEquivelant(result,expected);
    }
    public void testScale() {
        Vector3D testVector = new Vector3D(1,0,0);
        double shift = Math.PI/6;
        double scale = 3;
        Vector3D result = system.angleXY(shift).scale(scale).rotate(testVector);
        Vector3D expected = new Vector3D(Math.cos(shift*scale),Math.sin(shift*scale),0);
        assertEquivelant(result,expected);
    }
    public void testInverse() {
        Vector3D testVector = new Vector3D(1,1,1);
        double shift = Math.PI/6;
        Vector3D result = system.angleXY(shift).rotate(system.angleXY(shift).inverse().rotate(testVector));
        Vector3D expected = testVector;
        assertEquivelant(result,expected);
    }
    public void testXZ() {
        Vector3D testVector = new Vector3D(1,0,0);
        double shift = Math.PI/6;
        Vector3D result = system.angleXZ(shift).rotate(testVector);
        Vector3D expected = new Vector3D(Math.cos(shift),0,Math.sin(shift));
        assertEquivelant(result,expected);
    }
    public void testYX() {
        Vector3D testVector = new Vector3D(0,1,0);
        double shift = Math.PI/6;
        Vector3D result = system.angleYX(shift).rotate(testVector);
        Vector3D expected = new Vector3D(Math.sin(shift),Math.cos(shift),0);
        assertEquivelant(result,expected);
    }
    public void testYZ() {
        Vector3D testVector = new Vector3D(0,1,0);
        double shift = Math.PI/6;
        Vector3D result = system.angleYZ(shift).rotate(testVector);
        Vector3D expected = new Vector3D(0,Math.cos(shift),Math.sin(shift));
        assertEquivelant(result,expected);
    }
    public void testZX() {
        Vector3D testVector = new Vector3D(0,0,1);
        double shift = Math.PI/6;
        Vector3D result = system.angleZX(shift).rotate(testVector);
        Vector3D expected = new Vector3D(Math.sin(shift),0,Math.cos(shift));
        assertEquivelant(result,expected);
    }
    public void testZY() {
        Vector3D testVector = new Vector3D(0,0,1);
        double shift = Math.PI/6;
        Vector3D result = system.angleZY(shift).rotate(testVector);
        Vector3D expected = new Vector3D(0,Math.sin(shift),Math.cos(shift));
        assertEquivelant(result,expected);
    }
    public void testAssociation() {
        QuaternionSystem sys = (QuaternionSystem) system;
        Vector3D testVector = new Vector3D(1,0,0);
        double shift = Math.PI/2;
        Vector3D result = sys.angleYZ(shift).rotate(sys.angleXY(shift)).rotate(testVector);
        System.out.println("Result ="+result);
        Vector3D expected = sys.angleXZ(shift).rotate(testVector);
        assertEquivelant(result, expected);
    }
    public void testAxis() {
        double shift = Math.PI/4;
        T ang = system.angleZY(shift);
        Vector3D axis = ang.axis();
        Vector3D result = ang.rotate(axis);
        assertEquivelant(axis, result);
        System.out.println(shift);
        if (Math.abs(result.magnitude()-shift)>0.001) throw new RuntimeException();
    }
    public void testFromAxis() {
        Vector3D testVector = new Vector3D(0.1, 0.2, 0.3);
        T angle = system.fromAxis(testVector);
        Vector3D result = angle.axis();
        assertEquivelant(result, testVector);
    }
    public void testRotationKE() {
        Vector3D inertia = new Vector3D(1,2,3);
        RotatableBody<T> body = new RotatableBody<>(inertia);
        Vector3D momentum = new Vector3D(2.1,2.3,1.9);
        T startAngle = system.fromAxis(new Vector3D(0.4, 0.2, 0.3));
        body.setAngle(startAngle);
        body.setRotationalMomentum(momentum);
        double KE = body.rotationalEnergy();
        System.out.println("KE: "+KE);
        for (int i = 0; i < 10; i++) {
            body.rotateForTime(1);
            System.out.println("KE: "+body.rotationalEnergy());
            if (Math.abs(body.rotationalEnergy()-KE)>0.001) {
                throw new RuntimeException();
            }
        }
    }
    public void testRotationMagnitude() {
        double inertiaVal = 2;
        double momentumVal = 1.5;
        double time = 0.3;
        Vector3D inertia = new Vector3D(inertiaVal,1,1);
        RotatableBody<T> body = new RotatableBody<>(inertia);
        Vector3D momentum = new Vector3D(momentumVal,0,0);
        T startAngle = system.fromAxis(new Vector3D(0,0,0));
        System.out.println(startAngle);
        body.setAngle(startAngle);
        body.setRotationalMomentum(momentum);
        body.rotateForTime(time);
        Vector3D result = body.getAngle().axis();
        Vector3D expected = new Vector3D(time*momentumVal/inertiaVal,0,0);
        System.out.println(body.getAngle());
        assertEquivelant(result, expected);
    }
    public void testRotBodyComute() {
        Vector3D inertia = new Vector3D(1, 1, 1);
        Vector3D momentum1 = new Vector3D(1,0,0);
        Vector3D momentum2 = new Vector3D(0,1,0);
        double time = Math.PI/2;
        Vector3D testVector = new Vector3D(1,2,3);
        //Vector3D intermediate = new Vector3D(1,-3,2);
        Vector3D expected = new Vector3D(2,-3,-1);
        RotatableBody<T> body = new RotatableBody<>(inertia);
        body.setAngle(system.angleXY(0));
        body.setRotationalMomentum(momentum1);
        body.rotateForTime(time);
        body.setRotationalMomentum(momentum2);
        body.rotateForTime(time);
        assertEquivelant(body.getAngle().rotate(testVector), expected);
    }
    public void testRotationThresholdChanges() {
        Vector3D inertia = new Vector3D(1,2,3);
        RotatableBody<T> fineBody = new RotatableBody<>(inertia);
        RotatableBody<T> coarseBody = new RotatableBody<>(inertia);
        RotatableBody<T> midBody = new RotatableBody<>(inertia);
        Vector3D momentum = new Vector3D(2.1,2.3,1.9);
        T startAngle = system.fromAxis(new Vector3D(0.4, 0.2, 0.3));
        fineBody.setAngle(startAngle);
        fineBody.setRotationalMomentum(momentum);
        coarseBody.setAngle(startAngle);
        coarseBody.setRotationalMomentum(momentum);
        midBody.setAngle(startAngle);
        midBody.setRotationalMomentum(momentum);
        double time = 70;
        fineBody.setRotationThreshold(0.001);
        midBody.setRotationThreshold(0.01);
        coarseBody.setRotationThreshold(0.1);
        fineBody.rotateForTime(time);
        coarseBody.rotateForTime(time);
        midBody.rotateForTime(time);
        System.out.println("Fine: "+fineBody.getAngle());
        System.out.println("Coarse: "+coarseBody.getAngle());
        System.out.println("Mid: "+midBody.getAngle());
    }
    public void nonCommutivenessTest() {
        Vector3D a1 = new Vector3D(Math.PI/4,0,0);
        Vector3D a2 = new Vector3D(0,Math.PI/4,0);
        T t1 = system.fromAxis(a1);
        T t2 = system.fromAxis(a2);
        Vector3D startVector = new Vector3D(0,1,0);
        // Rotatable body produces this -> hence correction is average.rotate(a2) not average.inverse().rotate(a2)
        System.out.println("Actual local body: "+t1.rotate(t2.rotate(startVector)));
        System.out.println("Actual global body: "+t2.rotate(t1).rotate(startVector));
        // inverse here but not in Rotatable body because this uses global angles, while rotatable body uses local
        Vector3D adjustedA2 = system.fromAxis(a1.scale(0.5)).inverse().rotate(a2);
        Vector3D estimatedSum = adjustedA2.sum(a1);
        T t3 = system.fromAxis(estimatedSum);
        System.out.println("Conversion: "+t3.rotate(startVector));
        // rotatable body
        Vector3D average = a1.sum(a2).scale(0.5);
        Vector3D adjA2mid = system.fromAxis(average).rotate(a2);
        Vector3D est2 = adjA2mid.sum(a1);
        T t4 = system.fromAxis(est2);
        System.out.println("Conversion rot body: "+t4.rotate(startVector));
    }
    public void intermediateAxisTest() {
        Vector3D inertia = new Vector3D(1,2,3);
        RotatableBody<T> body = new RotatableBody<>(inertia);
        Vector3D momentum = new Vector3D(0.01,2,0);
        T startAngle = system.fromAxis(new Vector3D(0, 0, 0));
        body.setAngle(startAngle);
        body.setRotationalMomentum(momentum);
        for (int i = 0; i < 1000; i++) {
            body.rotateForTime(0.1);
            System.out.println(body.getAngle());
        }
    }
    public void testInertia() {
        double xx = 1.0;
        double yy = 2.0;
        double zz = 3.0;
        Vector3D baseInertia = new Vector3D(yy+zz,xx+zz,xx+yy);
        Vector3D center = new Vector3D(0.4,0.2,0.6);
        List<PointMass> equivalentMass = new ArrayList<>();
        equivalentMass.add(new PointMass(0.5*xx,new Vector3D(1,0,0).sum(center)));
        equivalentMass.add(new PointMass(0.5*xx,new Vector3D(-1,0,0).sum(center)));
        equivalentMass.add(new PointMass(0.5*yy,new Vector3D(0,1,0).sum(center)));
        equivalentMass.add(new PointMass(0.5*yy,new Vector3D(0,-1,0).sum(center)));
        equivalentMass.add(new PointMass(0.5*zz,new Vector3D(0,0,1).sum(center)));
        equivalentMass.add(new PointMass(0.5*zz,new Vector3D(0,0,-1).sum(center)));
        T angle = system.fromAxis(new Vector3D(-0.4,0.6,0.7));
        List<PointMass> rotatedMass = equivalentMass.stream().map((m)->new PointMass(m.mass,angle.rotate(m.position))).collect(Collectors.toList());
        SquareInertia rotatedInertia = SquareInertia.getCentered(rotatedMass);
        RotatableBody<T> originalBody = new RotatableBody<>(baseInertia);
        RotatableBody<T> rotatedBody = MomentCalculator.getRotatableBody(system, rotatedInertia);
        Vector3D momentum = new Vector3D(1.2,2.1,2.9);
        T angleToSet = system.angleXY(1.0);
        TorqueFetcher<T> torqueFetcher = new TorqueFetcher<>() {
            @Override
            public Vector3D getExternalTorque(T angleIn, double time) {
                return angleIn.rotate(new Vector3D(1.2, 0.8, -0.3+time));
            }
        };
        TorqueFetcher<T> correctorTorque = new TorqueFetcher<>() {
            @Override
            public Vector3D getExternalTorque(T angleIn, double time) {
                return torqueFetcher.getExternalTorque(angleIn.rotate(angle.inverse()), time);
            }
        };
        originalBody.setAngle(angle);
        originalBody.rotate(angleToSet);
        rotatedBody.setAngle(angleToSet);
        originalBody.setRotationalMomentum(momentum);
        rotatedBody.setRotationalMomentum(momentum);
        originalBody.setTorqueFetcher(correctorTorque);
        rotatedBody.setTorqueFetcher(torqueFetcher);
        // System.out.println(angle);
        // System.out.println(originalBody);
        System.out.println(rotatedBody);
        // System.out.println(rotatedBody.getAngle());
        // System.out.println(angleToSet);
        // System.out.println(angle.rotate(((CrossRotatableBody<T>) rotatedBody).getCorrectionAngle()));
        for (int i = 0; i < 1; i++) {
            // System.out.println("From original: "+originalBody.getAngle().rotate(angle.inverse()));
            // System.out.println("Rotated: "+rotatedBody.getAngle());
            // System.out.println(originalBody);
            // System.out.println(rotatedBody);
            originalBody.rotateForTime(1);
            rotatedBody.rotateForTime(1);
        }
        Vector3D testVector = new Vector3D(3,2,1);
        assertEquivelant(originalBody.getAngle().rotate(angle.inverse()).rotate(testVector), rotatedBody.getAngle().rotate(testVector));
    }
    public void rotationFlipTest() {
        Vector3D inertia = new Vector3D(1, 2, 3);
        Vector3D momentum = new Vector3D(1.2,1.4,-0.5);
        T start = system.angleXY(0.0);
        T inverse = system.angleXY(Math.PI);
        RotatableBody<T> mainRot = new RotatableBody<>(inertia);
        RotatableBody<T> invRot = new RotatableBody<>(inertia);
        mainRot.setRotationalMomentum(momentum);
        invRot.setRotationalMomentum(momentum);
        mainRot.setAngle(start);
        invRot.setAngle(inverse);
        for (int i = 0; i < 10; i++) {
            System.out.println("From original: "+mainRot.getAngle().rotate(start.inverse()));
            System.out.println("Rotated: "+invRot.getAngle().rotate(inverse.inverse()));
            // System.out.println(originalBody);
            // System.out.println(rotatedBody);
            mainRot.rotateForTime(0.01);
            invRot.rotateForTime(0.01);
        }
    }
    public void testAviationAngles() {
        AviationMapping classic = new AviationMapping(new DimensionMapping[] {new DimensionMapping(VERTICAL, false, Z),new DimensionMapping(LATERAL, false, X),new DimensionMapping(LONGITUDINAL, false, Y)});
        AviationMapping pointy = new AviationMapping(new DimensionMapping[] {new DimensionMapping(VERTICAL, false, Y),new DimensionMapping(LATERAL, true, X),new DimensionMapping(LONGITUDINAL, false, Z)});
        AviationMapping rotated = new AviationMapping(new DimensionMapping[] {new DimensionMapping(VERTICAL, false, Z),new DimensionMapping(LATERAL, true, Y),new DimensionMapping(LONGITUDINAL, false, X)});
        double threshold = 0.000001;
        testAviationAngles(classic, threshold);
        testAviationAngles(pointy, threshold);
        testAviationAngles(rotated, threshold);
    }
    private void testAviationAngles(AviationMapping mapping, double threshold) {
        testAviationAngle(Math.PI/4, 0, 0, mapping, threshold);
        testAviationAngle(0,Math.PI/4,  0, mapping, threshold);
        testAviationAngle(0, 0,Math.PI/4,  mapping, threshold);
        testAviationAngle(0.2,0.35,-0.7,mapping,threshold);
        testAviationAngle(-0.5,0.1,0.4,mapping,threshold);
    }
    private void testAviationAngle(double pitch, double yaw, double roll, AviationMapping mapping, double threshold) {
        T angle = new AviationAngleBuilder().pitch(pitch).yaw(yaw).roll(roll).build(mapping,system);
        if (Math.abs(angle.getYaw(mapping)-yaw)>threshold) throw new RuntimeException("Yaw Result: "+angle.getYaw(mapping)+" does not match expected: "+yaw);
        if (Math.abs(angle.getPitch(mapping)-pitch)>threshold) throw new RuntimeException("Pitch Result: "+angle.getPitch(mapping)+" does not match expected: "+pitch);
        if (Math.abs(angle.getRoll(mapping)-roll)>threshold) throw new RuntimeException("Roll Result: "+angle.getRoll(mapping)+" does not match expected: "+roll);
    }
    public void testRigidBodyAsemby() {
        System.out.println("Rigid test");
        PointMass[] p = new PointMass[12];
        p[0] = new PointMass(1.0,new Vector3D(1, 0, 0));
        p[1] = new PointMass(2.0,new Vector3D(0, 1, 0));
        p[2] = new PointMass(1.5,new Vector3D(0, 0, 1));
        p[3] = new PointMass(3.2,new Vector3D(1, 0, -1));
        p[4] = new PointMass(4.0,new Vector3D(1, 2, 0));
        p[5] = new PointMass(5.0,new Vector3D(4, 0.9, 2));
        p[6] = new PointMass(6.0,new Vector3D(-2, -1, -6));
        p[7] = new PointMass(7.0,new Vector3D(-2, -3, 7));
        p[8] = new PointMass(8.0,new Vector3D(-1, 4, 5));
        p[9]= new PointMass(9.0,new Vector3D(1, -2, -3.5));
        p[10] = new PointMass(10.0,new Vector3D(1.3, -4.5, 1));
        p[11] = new PointMass(12.0,new Vector3D(-1, 2.2, 0.1));
        List<PointMass> l1 = new ArrayList<>();
        List<PointMass> l2 = new ArrayList<>();
        List<PointMass> l3 = new ArrayList<>();
        List<PointMass> lc1 = new ArrayList<>();
        for (int i = 0; i < 4; i++) l1.add(p[i]);
        for (int i = 4; i < 8; i++) l2.add(p[i]);
        for (int i = 8; i < 12; i++) l3.add(p[i]);
        for (int i = 0; i < 12; i++) lc1.add(p[i]);
        RigidBody r1 = new RigidBody(MomentCalculator.getCenterMass(l1),SquareInertia.getCentered(l1));
        RigidBody r2 = new RigidBody(MomentCalculator.getCenterMass(l2),SquareInertia.getCentered(l2));
        RigidBody r3 = new RigidBody(MomentCalculator.getCenterMass(l3),SquareInertia.getCentered(l3));
        RigidBody rc1 = new RigidBody(MomentCalculator.getCenterMass(lc1),SquareInertia.getCentered(lc1));
        List<RigidBody> lc2 = new ArrayList<>();
        lc2.add(r1);
        lc2.add(r2);
        lc2.add(r3);
        RigidBody rc2 = new RigidBody(lc2);
        if (Math.abs(rc1.center().mass-rc2.center().mass)>0.001) throw new RuntimeException();
        if (Math.abs(rc1.center().position.x()-rc2.center().position.x())>0.001) throw new RuntimeException();
        if (Math.abs(rc1.center().position.y()-rc2.center().position.y())>0.001) throw new RuntimeException();
        if (Math.abs(rc1.center().position.z()-rc2.center().position.z())>0.001) throw new RuntimeException();
        if (Math.abs(rc1.rotationalInertia().xx()-rc2.rotationalInertia().xx())>0.001) throw new RuntimeException();
        if (Math.abs(rc1.rotationalInertia().yy()-rc2.rotationalInertia().yy())>0.001) throw new RuntimeException();
        if (Math.abs(rc1.rotationalInertia().zz()-rc2.rotationalInertia().zz())>0.001) throw new RuntimeException();
        if (Math.abs(rc1.rotationalInertia().xy()-rc2.rotationalInertia().xy())>0.001) throw new RuntimeException();
        if (Math.abs(rc1.rotationalInertia().yz()-rc2.rotationalInertia().yz())>0.001) throw new RuntimeException();
        if (Math.abs(rc1.rotationalInertia().zx()-rc2.rotationalInertia().zx())>0.001) throw new RuntimeException();
    }
    public void testGyroscope() {
        Vector3D inertia = new Vector3D(0.0002, 0.0001, 0.0001);
        Vector3D initialMomentum = new Vector3D(1, 0, 0);
        double torqueMag = Math.PI/2.0;
        RotatableBody<T> body = new RotatableBody<>(inertia);
        body.setRotationalMomentum(initialMomentum);
        body.setAngle(system.angleXY(0.0));
        body.setRotationThreshold(0.01);
        System.out.println(torqueMag);
        double torqueMomentumChange = torqueMag;
        System.out.println(torqueMomentumChange);
        body.setTorqueFetcher(new TorqueFetcher<T>() {
            public Vector3D getExternalTorque(T angle,double time) {
                Vector3D translated = angle.rotate(initialMomentum);
                return new Vector3D(-torqueMag*translated.y(),torqueMag*translated.x(),0);
        }});
        body.rotateForTime(1);
        Vector3D translated = body.getAngle().rotate(initialMomentum);
        Vector3D momentum = body.getRotationalMomentum();
        Vector3D correct = new Vector3D(0,1,0);
        assertEquivelant(translated, correct);
        assertEquivelant(momentum, correct);
    }
    public void testTorquePrecision() {
        Vector3D inertia = new Vector3D(2, 2.1, 0.8);
        Vector3D initialMomentum = new Vector3D(1, -1.2, 0.93);
        Vector3D translationVector = new Vector3D(-0.5, 0.2, 0.8);
        T initialAngle = system.angleXY(0.3);
        double timeStep = 1;
        TorqueFetcher<T> torqueFetcher = new TorqueFetcher<T>() {
            public Vector3D getExternalTorque(T angle,double time) {
                Vector3D translated = angle.rotate(translationVector);
                return new Vector3D(0.2*translated.y(),-0.4,0.2*time);
        }};
        double[] rotationThreasholds = new double[] {0.1,0.01,0.001};
        for (int i = 0; i < rotationThreasholds.length; i++) {
            RotatableBody<T> body = new RotatableBody<>(inertia);
            body.setRotationalMomentum(initialMomentum);
            body.setAngle(initialAngle);
            body.setRotationThreshold(rotationThreasholds[i]);
            body.setTorqueFetcher(torqueFetcher);
            body.rotateForTime(timeStep);
            System.out.println("Threshold: "+rotationThreasholds[i]);
            System.out.println(body.getAngle());
            System.out.println(body.getRotationalMomentum());
            System.out.println(body.getElapsedTime());
        }
    }
    public void testTorqueMagnitude1D(Vector3D inertia, Vector3D initialMomentum, Vector3D torque, double timeStep) {
        T initialAngle = system.angleXY(0.0);
        TorqueFetcher<T> torqueFetcher = new TorqueFetcher<T>() {
            public Vector3D getExternalTorque(T angle,double time) {
                return torque;
        }};
        RotatableBody<T> body = new RotatableBody<>(inertia);
        body.setRotationalMomentum(initialMomentum);
        body.setAngle(initialAngle);
        body.setTorqueFetcher(torqueFetcher);
        body.rotateForTime(timeStep);
        Vector3D finalMomentum = body.getRotationalMomentum();
        Vector3D angleAxis = body.getAngle().axis();
        assertEquivelant(finalMomentum, initialMomentum.sum(torque.scale(timeStep)));
        Vector3D expectedRotationAxis = system.fromAxis(initialMomentum.sum(torque.scale(0.5*timeStep)).scale(timeStep).elementWiseDivide(inertia)).axis();
        assertEquivelant(angleAxis, expectedRotationAxis);
    }
    public void testTorqueMagnitude() {
        testTorqueMagnitude1D(new Vector3D(1, 2, 3), new Vector3D(1, 0, 0), new Vector3D(-1, 0, 0), 1);
        testTorqueMagnitude1D(new Vector3D(1, 2, 1), new Vector3D(0,2, 0), new Vector3D(0,1, 0), 0.5);
        testTorqueMagnitude1D(new Vector3D(1, 2, 1), new Vector3D(0,0,0.5), new Vector3D(0,0,-1), 0.75);
        testTorqueMagnitude1D(new Vector3D(1, 1, 1), new Vector3D(1, 1, 0), new Vector3D(-2, -2, 0), 0.25);
    }
    public void testSolids() {
        Random random = new Random(28454);
        testSolid(new Tetrahedron(1.3),0.02,1.1,random);
        testSolid(new Cone(0.9,1.1),0.02,1.1,random);
        testSolid(new Cylinder(0.8,1.2),0.02,1.2,random);
        testSolid(new Octahedron(1.1),0.02,0.9,random);
        testSolid(new Sphere(0.95),0.02,2.4,random);
        testSolid(new RectangularPrism(1.1,0.9,1.3),0.02,0.8,random);
    }
    public void testSolid(Solid solid, double threshold, double density, Random random) {
        double indiMass = density*threshold*threshold*threshold;
        solid.setDensity(density);
        List<PointMass> masses = new ArrayList<>();
        for (double x = -1.0; x < 1.0; x+=threshold)  for (double y = -1.0; y < 1.0; y+=threshold)  for (double z = -1.0; z < 1.0; z+=threshold)  {
            Vector3D position = new Vector3D(x+threshold*random.nextDouble(), y+threshold*random.nextDouble(), z+threshold*random.nextDouble());
            if (solid.inSolid(position)) {
                masses.add(new PointMass(indiMass, position));
            } 
        }
        SquareInertia massInertia = SquareInertia.getCentered(masses);
        SquareInertia solidInertia = solid.momentOfInertia();
        if (Math.abs(massInertia.xx()-solidInertia.xx())>0.002) throw new RuntimeException(massInertia.xx()+" "+solidInertia.xx());
        if (Math.abs(massInertia.yy()-solidInertia.yy())>0.002) throw new RuntimeException(massInertia.yy()+" "+solidInertia.yy());
        if (Math.abs(massInertia.zz()-solidInertia.zz())>0.002) throw new RuntimeException(massInertia.zz()+" "+solidInertia.zz());
        if (Math.abs(massInertia.xy()-solidInertia.xy())>0.002) throw new RuntimeException(massInertia.xy()+" "+solidInertia.xy());
        if (Math.abs(massInertia.zx()-solidInertia.zx())>0.002) throw new RuntimeException(massInertia.zx()+" "+solidInertia.zx());
        if (Math.abs(massInertia.yz()-solidInertia.yz())>0.002) throw new RuntimeException(massInertia.yz()+" "+solidInertia.yz());
    }
    public void testTrueKE() {
        List<PointMass> massOriginal = new ArrayList<>();
        massOriginal.add(new PointMass(1.1, new Vector3D(0.8,0.2,0)));
        massOriginal.add(new PointMass(0.8, new Vector3D(-0.6,-0.5,0)));
        massOriginal.add(new PointMass(3.6, new Vector3D(0.8,0.4,1)));
        massOriginal.add(new PointMass(2.4, new Vector3D(2.2,0,-1)));
        massOriginal.add(new PointMass(1.3, new Vector3D(0,0.8,-0.5)));
        SquareInertia inertia = SquareInertia.getAbsolute(massOriginal);
        RotatableBody<T> body = MomentCalculator.getRotatableBody(system, inertia);
        body.setRotationalMomentum(new Vector3D(1.43724, -0.8226, 0.3115));
        double timeGap = 0.0001;
        double rejectionThreshold = 0.001;
        double totalTime = 1;
        List<PointMass> massOld = massOriginal;
        List<PointMass> massNew;
        T oldAngle = null;
        for (double time = 0; time < totalTime; time+=timeGap) {
            double reportedKE = body.rotationalEnergy();
            body.rotateForTime(timeGap);
            T angle = body.getAngle();
            massNew = massOriginal.stream().map((PointMass m) -> new PointMass(m.mass,angle.rotate(m.position))).collect(Collectors.toList());
            double trueKE = 0.0;
            for (int i = 0; i < massOriginal.size(); i++) {
                trueKE += 0.5 * massOriginal.get(i).mass*massOld.get(i).position.difference(massNew.get(i).position).sqMagnitude()/(timeGap*timeGap);
            }
            massOld=massNew;
            if (Math.abs(trueKE-reportedKE)>rejectionThreshold) {
                System.out.println(trueKE);
                System.out.println(reportedKE);
                System.out.println(oldAngle);
                System.out.println(angle);
                throw new RuntimeException();
            }
            oldAngle=angle;
        }
    }
    public static void main(String[] args) {
        Test<?,?> test = new Test<>(QuaternionSystem.INSTANCE);
        test.testXY();
        test.testYX();
        test.testYZ();
        test.testZY();
        test.testZX();
        test.testXZ();
        test.testAssociation();
        test.testScale();
        test.testInverse();
        test.testAxis();
        test.testFromAxis();
        test.testRotationKE();
        test.testRotationMagnitude();
        test.nonCommutivenessTest();
        test.testRotationThresholdChanges();
        //test.intermediateAxisTest();
        test.testInertia();
        // test.rotationFlipTest();
        test.testRotBodyComute();
        test.testAviationAngles();
        test.testRigidBodyAsemby();
        test.testGyroscope();
        test.testTorquePrecision();
        test.testSolids();
        test.testTorqueMagnitude();
        test.testTrueKE();

        System.out.println("Test completed");
    }
}
