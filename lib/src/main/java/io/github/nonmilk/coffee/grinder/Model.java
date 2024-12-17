package io.github.nonmilk.coffee.grinder;

import io.github.shimeoki.jshaper.obj.data.ObjElements;
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
import io.github.alphameo.linear_algebra.mat.Matrix3;
import io.github.alphameo.linear_algebra.vec.Vec3;
import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
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

    public Model(final ObjFile obj) {
        vertices = obj.vertexData().vertices();
        final int vertexCount = vertices.size();
        textureVertices = obj.vertexData().textureVertices();
        // FIXME recalculate normals
        normals = obj.vertexData().vertexNormals();
        faces = obj.elements().faces();

        final List<Integer> vertexPolygonCount = new ArrayList<>(vertexCount);

        for (int i = 0; i < vertexCount; i++) {
            // addFaceNormals(face, vertexPolygonCount);
            if (faces.get(i).triplets().size() > 3) {
                final ObjFace face = faces.remove(i);
                i--;
                triangulateFace(face);
            }
        }
    }

    private void triangulateFace(final ObjFace face) {
        final List<ObjTriplet> faceTriplets = face.triplets();
        final Vector3 faceNormal = faceNormal(face);
        final Vector3 axis = Vec3Math.cross(faceNormal, Vec3f.VECTOR_K);
        final float angle = (float) Math.acos(
                Vec3Math.dot(faceNormal, Vec3f.VECTOR_K) / Vec3Math.len(faceNormal));
        final List<Vec2f> flatPolygon = rotatePolygon(faceTriplets, axis, angle);

        final List<int[]> triangles = Triangulation.earClippingTriangulate(flatPolygon);
        final Set<ObjGroupName> groupNames = face.groupNames();
        for (int[] triangle : triangles) {
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
        Matrix3 rotationMatrix = new Mat3(
                ux * ux * (1 - cos) + cos, ux * ux * (1 - cos), uy * sin,
                ux * uy * (1 - cos), uy * uy * (1 - cos) + cos, ux * sin,
                -uy * sin, ux * sin, cos);

        final List<Vec2f> rotatedVertices = new ArrayList<>(triplets.size());
        for (ObjTriplet triplet : triplets) {
            final Vector3 rotatedVertex = Mat3Math.prod(
                    rotationMatrix, new Vec3f(triplet.vertex()));
            rotatedVertices.add(new Vec2f(rotatedVertex.x(), rotatedVertex.y()));
        }

        return rotatedVertices;
    }

    private void clearNormals() {
        for (ObjVertexNormal normal : normals) {
            normal.setI(0);
            normal.setJ(0);
            normal.setK(0);
        }
    }

    private void addFaceNormals(final ObjFace face, final List<Integer> vertexPolygonCount) {

        // FIXME sum all face normals with vertex and divide by face count
    }

    private Vector3 faceNormal(final ObjFace face) {
        final ObjVertex vertex1 = face.triplets().get(0).vertex();
        final ObjVertex vertex2 = face.triplets().get(1).vertex();
        final ObjVertex vertex3 = face.triplets().get(2).vertex();

        final Vector3 vector1 = new Vec3(vertex1.x(), vertex1.y(), vertex1.z());
        final Vector3 vector2 = new Vec3(vertex2.x(), vertex2.y(), vertex2.z());
        final Vector3 vector3 = new Vec3(vertex3.x(), vertex3.y(), vertex3.z());

        final Vector3 edge1 = Vec3Math.subtracted(vector2, vector1);
        final Vector3 edge2 = Vec3Math.subtracted(vector3, vector2);

        return Vec3Math.cross(edge1, edge2);
    }
}
