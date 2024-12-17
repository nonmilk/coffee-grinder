package io.github.nonmilk.coffee.grinder;

import io.github.shimeoki.jshaper.obj.data.ObjElements;
import io.github.shimeoki.jshaper.obj.data.ObjFile;
import io.github.shimeoki.jshaper.obj.geom.ObjFace;
import io.github.shimeoki.jshaper.obj.geom.ObjTextureVertex;
import io.github.shimeoki.jshaper.obj.geom.ObjVertex;
import io.github.shimeoki.jshaper.obj.geom.ObjVertexNormal;
import io.github.traunin.triangulation.Triangulation;
import io.github.alphameo.linear_algebra.vec.Vec3;
import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private final List<ObjVertex> vertices;
    private final List<ObjTextureVertex> textureVertices;
    private final List<ObjFace> faces;
    private final List<ObjVertexNormal> normals;

    public Model(ObjFile obj) {
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
                ObjFace face = faces.remove(i);
                i--;
                triangulateFace(face);
            }
        }
    }

    private void triangulateFace(ObjFace face) {

    }

    private void clearNormals() {
        for (ObjVertexNormal normal : normals) {
            normal.setI(0);
            normal.setJ(0);
            normal.setK(0);
        }
    }

    private void addFaceNormals(ObjFace face, List<Integer> vertexPolygonCount) {


        // FIXME sum all face normals with vertex and divide by face count
    }

    private Vector3 faceNormal(ObjFace face) {
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
