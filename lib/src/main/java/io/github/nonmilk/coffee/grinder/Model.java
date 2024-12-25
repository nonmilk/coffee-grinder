package io.github.nonmilk.coffee.grinder;

import io.github.alphameo.linear_algebra.mat.*;
import io.github.shimeoki.jshaper.obj.data.ObjFile;
import io.github.shimeoki.jshaper.obj.data.ObjGroupName;
import io.github.shimeoki.jshaper.obj.data.ObjTriplet;
import io.github.shimeoki.jshaper.obj.geom.ObjFace;
import io.github.shimeoki.jshaper.obj.geom.ObjTextureVertex;
import io.github.shimeoki.jshaper.obj.geom.ObjVertex;
import io.github.shimeoki.jshaper.obj.geom.ObjVertexNormal;
import io.github.traunin.triangulation.Triangulation;
import ru.vsu.cs.konygina_d.render_engine.*;
import io.github.alphameo.linear_algebra.vec.Vec3;
import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.math.Floats;
import io.github.nonmilk.coffee.grinder.math.Vec2f;
import io.github.nonmilk.coffee.grinder.math.Vec3f;

import java.util.*;

public class Model {

    private final List<ObjVertex> vertices;
    private final List<ObjTextureVertex> textureVertices;
    private final List<ObjFace> faces;
    private final List<ObjVertexNormal> normals;
    private Matrix4 modelMatrix = Mat4Math.unitMat();

    private final Scaling scaling = new Scaling();
    RotationOrder rotationOrder = RotationOrder.XYZ;
    private final Rotator rotatorX = new Rotator(Rotator.Axis.X);
    private final Rotator rotatorY = new Rotator(Rotator.Axis.Y);
    private final Rotator rotatorZ = new Rotator(Rotator.Axis.Z);
    private final Translator translator = new Translator();
    private boolean calculated = false;

    public Model(final ObjFile obj) {
        vertices = obj.vertexData().vertices();
        textureVertices = obj.vertexData().textureVertices();
        normals = obj.vertexData().vertexNormals();
        faces = obj.elements().faces();
    }

    public enum RotationOrder {
        XYZ, XZY, YXZ, YZX, ZXY, ZYX;
    }

    public Matrix4 modelMatrix() {
        if (calculated) {
            return new Mat4(modelMatrix);
        }

        Transformation at = constructTransformation(rotationOrder);
        modelMatrix = at.getMatrix();
        calculated = true;

        return new Mat4(modelMatrix);
    }

    private Transformation constructTransformation(RotationOrder order) {
        switch (order) {
            case XYZ -> {
                return new Transformation(translator, rotatorZ, rotatorY, rotatorX, scaling);
            }
            case XZY -> {
                return new Transformation(translator, rotatorY, rotatorZ, rotatorX, scaling);
            }
            case YXZ -> {
                return new Transformation(translator, rotatorZ, rotatorX, rotatorY, scaling);
            }
            case YZX -> {
                return new Transformation(translator, rotatorX, rotatorZ, rotatorY, scaling);
            }
            case ZXY -> {
                return new Transformation(translator, rotatorY, rotatorX, rotatorZ, scaling);
            }
            case ZYX -> {
                return new Transformation(translator, rotatorX, rotatorY, rotatorZ, scaling);
            }
            default -> {
                return constructTransformation(RotationOrder.XYZ);
            }
        }
    }

    public void scale(float x, float y, float z) {
        scaling.set(x, y, z);
        calculated = false;
    }

    public void scaleRelative(float dx, float dy, float dz) {
        scaling.setRelative(dx, dy, dz);
        calculated = false;
    }

    public void translate(float x, float y, float z) {
        translator.set(x, y, z);
        calculated = false;
    }

    public void translateRelative(float dx, float dy, float dz) {
        translator.setRelative(dx, dy, dz);
        calculated = false;
    }

    public void rotateX(float angle) {
        rotatorX.setAngle(angle);
        calculated = false;
    }

    public void rotateXRelative(float dAngle) {
        rotatorX.setAngle(dAngle);
        calculated = false;
    }

    public void rotateY(float angle) {
        rotatorY.setAngle(angle);
        calculated = false;
    }

    public void rotateYRelative(float dAngle) {
        rotatorY.setAngle(dAngle);
        calculated = false;
    }

    public void rotateZ(float angle) {
        rotatorZ.setAngle(angle);
        calculated = false;
    }

    public void rotateZRelative(float dAngle) {
        rotatorZ.setAngle(dAngle);
        calculated = false;
    }

    public void setRotationOrder(RotationOrder order) {
        this.rotationOrder = order;
        calculated = false;
    }

    public List<ObjFace> faces() {
        return faces;
    }

    public void triangulateWithNormals() {
        // MUST BE USED FOR EVERY MODEL
        normals.clear();
        final int vertexCount = vertices.size();

        final Map<ObjVertex, Integer> normalFaceCounts = new HashMap<>(vertexCount);
        final Map<ObjVertex, ObjVertexNormal> vertexNormals = new HashMap<>(vertexCount);

        for (ObjFace face : faces) {
            clearFaceNormals(face, normalFaceCounts, vertexNormals);
        }

        for (int i = faces.size() - 1; i >= 0; i--) {
            final ObjFace face = faces.get(i);
            addFaceNormals(face, normalFaceCounts);

            if (face.triplets().size() > 3) {
                faces.remove(i);
                triangulateFace(face);
            }
        }
    }

    private void triangulateFace(final ObjFace face) {
        final List<ObjTriplet> faceTriplets = face.triplets();
        final Vector3 faceNormal = faceNormal(face);
        final Vector3 axis = Vec3Math.cross(faceNormal, Vec3f.VECTOR_K);
        // if polygon is parallel to XY, we set the rotation axis
        // as Z axis so that the rotation doesn't mess with triangulation
        if (Floats.equals(Vec3Math.len2(axis), 0)) {
            axis.setZ(1);
        }
        final float angle = (float) Math.acos(
                Vec3Math.dot(faceNormal, Vec3f.VECTOR_K) / Vec3Math.len(faceNormal));
        final List<Vec2f> flatPolygon = rotatePolygon(faceTriplets, axis, angle);

        final List<int[]> triangles = Triangulation.earClippingTriangulate(flatPolygon);
        final Set<ObjGroupName> groupNames = face.groupNames();
        for (final int[] triangle : triangles) {
            final List<ObjTriplet> triplets = new ArrayList<>(3);
            // better way?
            triplets.add(faceTriplets.get(triangle[0]));
            triplets.add(faceTriplets.get(triangle[1]));
            triplets.add(faceTriplets.get(triangle[2]));
            faces.add(new ObjFace(triplets, groupNames));
        }
    }

    private List<Vec2f> rotatePolygon(
            final List<ObjTriplet> triplets,
            final Vector3 axis,
            final float angle) {

        final float cos = (float) Math.cos(angle);
        final float sin = (float) Math.sin(angle);
        final Vector3 u = Vec3Math.normalized(axis);
        final float ux = u.x();
        final float uy = u.y();

        // rotate by angle around axis
        final Matrix3 rotationMatrix = new Mat3(
                ux * ux * (1 - cos) + cos, ux * ux * (1 - cos), uy * sin,
                ux * uy * (1 - cos), uy * uy * (1 - cos) + cos, ux * sin,
                -uy * sin, ux * sin, cos);

        final List<Vec2f> rotatedVertices = new ArrayList<>(triplets.size());
        for (final ObjTriplet triplet : triplets) {
            final Vector3 rotatedVertex = Mat3Math.prod(
                    rotationMatrix, new Vec3f(triplet.vertex()));
            rotatedVertices.add(new Vec2f(rotatedVertex.x(), rotatedVertex.y()));
        }

        return rotatedVertices;
    }

    private void clearFaceNormals(
            final ObjFace face,
            final Map<ObjVertex, Integer> normalFaceCounts,
            final Map<ObjVertex, ObjVertexNormal> vertexNormals) {
        for (ObjTriplet triplet : face.triplets()) {
            ObjVertex vertex = triplet.vertex();
            if (normalFaceCounts.containsKey(vertex)) {
                triplet.setVertexNormal(vertexNormals.get(vertex));
            } else {
                final ObjVertexNormal emptyNormal = new ObjVertexNormal(0, 0, 0);
                normals.add(emptyNormal);
                triplet.setVertexNormal(emptyNormal);
                normalFaceCounts.put(triplet.vertex(), 1);
                vertexNormals.put(triplet.vertex(), emptyNormal);
            }
        }
    }

    private void addFaceNormals(final ObjFace face, final Map<ObjVertex, Integer> normalFaceCounts) {
        final Vector3 normal = faceNormal(face);
        for (ObjTriplet triplet : face.triplets()) {
            System.out.println(triplet.format());
            final ObjVertexNormal vertexNormal = triplet.vertexNormal();
            final ObjVertex vertex = triplet.vertex();
            final int scalingCoefficient = normalFaceCounts.get(vertex);
            final int scalingIncremented = scalingCoefficient + 1;
            // calculates avg in one pass
            // some consider it to be unnatural
            vertexNormal.setI(
                    (vertexNormal.i() * scalingCoefficient + normal.x()) / scalingIncremented);
            vertexNormal.setJ(
                    (vertexNormal.j() * scalingCoefficient + normal.y()) / scalingIncremented);
            vertexNormal.setK(
                    (vertexNormal.k() * scalingCoefficient + normal.z()) / scalingIncremented);

            normalFaceCounts.put(vertex, scalingIncremented);
        }

    }

    private Vector3 faceNormal(final ObjFace face) {
        // FIXME we might be calculating negative normals, switch order if black
        // FIXME needs a loop for when vertices are in a line
        final ObjVertex vertex1 = face.triplets().get(0).vertex();
        final ObjVertex vertex2 = face.triplets().get(1).vertex();
        final ObjVertex vertex3 = face.triplets().get(2).vertex();

        final Vector3 vector1 = new Vec3(vertex1.x(), vertex1.y(), vertex1.z());
        final Vector3 vector2 = new Vec3(vertex2.x(), vertex2.y(), vertex2.z());
        final Vector3 vector3 = new Vec3(vertex3.x(), vertex3.y(), vertex3.z());

        final Vector3 edge1 = Vec3Math.subtracted(vector2, vector1);
        final Vector3 edge2 = Vec3Math.subtracted(vector3, vector2);

        return Vec3Math.normalized(Vec3Math.cross(edge1, edge2));
    }
}
