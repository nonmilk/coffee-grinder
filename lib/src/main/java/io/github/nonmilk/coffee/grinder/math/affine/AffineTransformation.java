package io.github.nonmilk.coffee.grinder.math.affine;

import io.github.alphameo.linear_algebra.mat.Mat4Math;
import io.github.alphameo.linear_algebra.mat.Matrix4;
import io.github.alphameo.linear_algebra.vec.Vec3;
import io.github.alphameo.linear_algebra.vec.Vec3Math;
import io.github.alphameo.linear_algebra.vec.Vector3;
import io.github.alphameo.linear_algebra.vec.Vector4;

import java.util.ArrayList;
import java.util.List;

/**
 * @author {@link https://github.com/dd-buntar}
 */
public interface AffineTransformation {
    Matrix4 getMatrix();

    default Vector3 transform(Vector3 v) {
        Vector4 resVertex = Mat4Math.prod(getMatrix(), Vec3Math.toVec4(v));
        return new Vec3(resVertex.x(), resVertex.y(), resVertex.z());
    }

    default List<Vector3> transform(List<Vector3> v) {
        Matrix4 m = getMatrix();
        List<Vector3> resV = new ArrayList<>();

        for (int i = 0; i < v.size(); i++) {
            Vector4 resVertex = Mat4Math.prod(m, Vec3Math.toVec4(v.get(i)));
            resV.add(new Vec3(resVertex.x(), resVertex.y(), resVertex.z()));
        }

        return resV;
    }
}
