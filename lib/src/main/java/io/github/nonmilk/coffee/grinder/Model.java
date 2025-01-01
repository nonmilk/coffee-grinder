package io.github.nonmilk.coffee.grinder;

import io.github.shimeoki.jshaper.ObjFile;
import io.github.shimeoki.jshaper.obj.Group;
import io.github.shimeoki.jshaper.obj.Triplet;
import io.github.shimeoki.jshaper.obj.Face;
import io.github.shimeoki.jshaper.obj.TextureVertex;
import io.github.shimeoki.jshaper.obj.Vertex;
import io.github.shimeoki.jshaper.obj.VertexNormal;
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
import io.github.nonmilk.coffee.grinder.render.Texture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Model {

    private final List<Vertex> vertices;
    private final List<TextureVertex> textureVertices;
    private final List<Face> faces;
    private final List<VertexNormal> normals;
    private final Matrix4 modelMatrix = new Mat4(
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1);
    private Texture texture;

    public Model(final ObjFile obj, final Texture texture) {
        vertices = obj.vertexData().vertices();
        textureVertices = obj.vertexData().textureVertices();
        normals = obj.vertexData().vertexNormals();
        faces = obj.elements().faces();
        this.texture = texture;
    }

    public Matrix4 modelMatrix() {
        return modelMatrix;
    }

    public List<Face> faces() {
        return faces;
    }

    public Texture texture() {
        return texture;
    }

    public void setTexture(final Texture texture) {
        this.texture = Objects.requireNonNull(texture);
    }

    public void triangulateWithNormals() {
        // MUST BE USED FOR EVERY MODEL
        normals.clear();
        final int vertexCount = vertices.size();

        final Map<Vertex, Integer> normalFaceCounts = new HashMap<>(vertexCount);
        final Map<Vertex, VertexNormal> vertexNormals = new HashMap<>(vertexCount);

        for (Face face : faces) {
            clearFaceNormals(face, normalFaceCounts, vertexNormals);
        }

        for (int i = faces.size() - 1; i >= 0; i--) {
            final Face face = faces.get(i);
            addFaceNormals(face, normalFaceCounts);

            if (face.triplets().size() > 3) {
                faces.remove(i);
                triangulateFace(face);
            }
        }

        for (VertexNormal normal : normals) {
            float length = Vec3Math.len(new Vec3f(normal.i(), normal.j(), normal.k()));
            normal.setI(normal.i() / length);
            normal.setJ(normal.j() / length);
            normal.setK(normal.k() / length);
        }
    }

    private void triangulateFace(final Face face) {
        final List<Triplet> faceTriplets = face.triplets();
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
        final Set<Group> groups = face.groups();
        for (final int[] triangle : triangles) {
            final List<Triplet> triplets = new ArrayList<>(3);
            // better way?
            triplets.add(faceTriplets.get(triangle[0]));
            triplets.add(faceTriplets.get(triangle[1]));
            triplets.add(faceTriplets.get(triangle[2]));
            faces.add(new Face(triplets, groups));
        }
    }

    private List<Vec2f> rotatePolygon(
            final List<Triplet> triplets,
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
        for (final Triplet triplet : triplets) {
            final Vector3 rotatedVertex = Mat3Math.prod(
                    rotationMatrix, new Vec3f(triplet.vertex()));
            rotatedVertices.add(new Vec2f(rotatedVertex.x(), rotatedVertex.y()));
        }

        return rotatedVertices;
    }

    private void clearFaceNormals(
            final Face face,
            final Map<Vertex, Integer> normalFaceCounts,
            final Map<Vertex, VertexNormal> vertexNormals) {
        for (Triplet triplet : face.triplets()) {
            Vertex vertex = triplet.vertex();
            if (normalFaceCounts.containsKey(vertex)) {
                triplet.setVertexNormal(vertexNormals.get(vertex));
            } else {
                final VertexNormal emptyNormal = new VertexNormal(0, 0, 0);
                normals.add(emptyNormal);
                triplet.setVertexNormal(emptyNormal);
                normalFaceCounts.put(triplet.vertex(), 0);
                vertexNormals.put(triplet.vertex(), emptyNormal);
            }
        }
    }

    private void addFaceNormals(final Face face, final Map<Vertex, Integer> normalFaceCounts) {
        final Vector3 normal = faceNormal(face);
        for (Triplet triplet : face.triplets()) {
            final VertexNormal vertexNormal = triplet.vertexNormal();
            final Vertex vertex = triplet.vertex();
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

    private Vector3 faceNormal(final Face face) {
        // FIXME we might be calculating negative normals, switch order if black
        // FIXME needs a loop for when vertices are in a line
        final Vertex vertex1 = face.triplets().get(0).vertex();
        final Vertex vertex2 = face.triplets().get(1).vertex();
        final Vertex vertex3 = face.triplets().get(2).vertex();

        final Vector3 vector1 = new Vec3(vertex1.x(), vertex1.y(), vertex1.z());
        final Vector3 vector2 = new Vec3(vertex2.x(), vertex2.y(), vertex2.z());
        final Vector3 vector3 = new Vec3(vertex3.x(), vertex3.y(), vertex3.z());

        final Vector3 edge1 = Vec3Math.subtracted(vector2, vector1);
        final Vector3 edge2 = Vec3Math.subtracted(vector3, vector2);

        return Vec3Math.normalized(Vec3Math.cross(edge1, edge2));
    }
}
