package io.github.nonmilk.coffee.grinder;

import io.github.shimeoki.jshaper.obj.data.ObjElements;
import io.github.shimeoki.jshaper.obj.data.ObjFile;
import io.github.shimeoki.jshaper.obj.geom.ObjFace;
import io.github.shimeoki.jshaper.obj.geom.ObjTextureVertex;
import io.github.shimeoki.jshaper.obj.geom.ObjVertex;
import io.github.shimeoki.jshaper.obj.geom.ObjVertexNormal;
import io.github.traunin.triangulation.Triangulation;
import java.util.List;

public class Model {

    private List<ObjVertex> vertices;
    private List<ObjTextureVertex> textureVertices;
    private List<ObjFace> faces;
    private List<ObjVertexNormal> normals;

    public Model(ObjFile obj) {
        vertices = obj.vertexData().vertices();
        textureVertices = obj.vertexData().textureVertices();
        faces = obj.elements().faces();
        for (ObjFace face : faces) {
            if (face.triplets().size() > 3) {
                triangulateFace(face);
            }
        }
    }

    private void triangulateFace(ObjFace face) {
        
    }
}
