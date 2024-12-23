package io.github.nonmilk.coffee.grinder;

import io.github.shimeoki.jshaper.obj.data.ObjFile;
import io.github.shimeoki.jshaper.obj.data.ObjGroupName;
import io.github.shimeoki.jshaper.obj.data.ObjTriplet;
import io.github.shimeoki.jshaper.obj.geom.ObjFace;
import io.github.shimeoki.jshaper.obj.geom.ObjTextureVertex;
import io.github.shimeoki.jshaper.obj.geom.ObjVertex;
import io.github.shimeoki.jshaper.obj.geom.ObjVertexNormal;
import io.github.traunin.triangulation.Triangulation;
import io.github.alphameo.linear_algebra.mat.Mat3;
import io.github.alphameo.linear_algebra.mat.Mat3Math;
import io.github.alphameo.linear_algebra.mat.Mat4;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.alphameo.linear_algebra.mat.Matrix3;
import io.github.alphameo.linear_algebra.vec.Vec3;
import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.nonmilk.coffee.grinder.math.Floats;
import io.github.nonmilk.coffee.grinder.math.Vec2f;
import io.github.nonmilk.coffee.grinder.math.Vec3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Model {

    private final List<ObjVertex> vertices;
    private final List<ObjTextureVertex> textureVertices;
    private final List<ObjFace> faces;
    private final List<ObjVertexNormal> normals;
    private final Matrix4 modelMatrix = new Mat4(
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1);

    public Model(final ObjFile obj) {
        vertices = obj.vertexData().vertices();
        textureVertices = obj.vertexData().textureVertices();
        normals = obj.vertexData().vertexNormals();
        faces = obj.elements().faces();
    }

    public Matrix4 modelMatrix() {
        return modelMatrix;
    }

    public List<ObjFace> faces() {
        return faces;
    }

    public void triangulateWithNormals() {
        // MUST BE USED FOR EVERY MODEL
        for (ObjFace face : faces) {
            clearFaceNormals(face);
        }

        final int vertexCount = vertices.size();
        final int facesCount = faces.size();

        final List<Integer> vertexPolygonCount = new ArrayList<>(vertexCount);
        // cringe
        for (int i = 0; i < vertexCount; i++) {
            vertexPolygonCount.add(0);
        }

        for (int i = 0; i < facesCount; i++) {
            ObjFace face = faces.get(i);
            addFaceNormals(face, vertexPolygonCount);
            if (face.triplets().size() > 3) {
                faces.remove(i);
                i--;
                triangulateFace(face);
            }
        }

        for (int i = 0; i < vertexCount; i++) {
            int scalingCoefficient = vertexPolygonCount.get(i);
            ObjVertexNormal normal = normals.get(i);
            normal.setI(normal.i() / scalingCoefficient);
            normal.setJ(normal.j() / scalingCoefficient);
            normal.setK(normal.k() / scalingCoefficient);
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

    private void clearFaceNormals(ObjFace face) {
        for (ObjTriplet triplet : face.triplets()) {
            switch (triplet.format()) {
                case VERTEX_NORMAL, ALL:
                    triplet.vertexNormal().clear();
                    break;
                default:
                    ObjVertexNormal emptyNormal = new ObjVertexNormal(0, 0, 0);
                    normals.add(emptyNormal);
                    triplet.setVertexNormal(emptyNormal);
            }
        }
    }

    private void addFaceNormals(final ObjFace face, final List<Integer> vertexPolygonCount) {
        Vector3 normal = faceNormal(face);
        for (ObjTriplet triplet : face.triplets()) {
            ObjVertexNormal vertexNormal = triplet.vertexNormal();
            vertexNormal.setI(vertexNormal.i() + normal.x());
            vertexNormal.setJ(vertexNormal.j() + normal.y());
            vertexNormal.setK(vertexNormal.k() + normal.z());

            int vertexIndex = normals.indexOf(vertexNormal);
            int vertexInPolygon = vertexPolygonCount.get(vertexIndex) + 1;
            vertexPolygonCount.set(vertexIndex, vertexInPolygon);
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
