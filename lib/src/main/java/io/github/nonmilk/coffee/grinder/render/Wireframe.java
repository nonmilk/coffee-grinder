package io.github.nonmilk.coffee.grinder.render;

import io.github.nonmilk.coffee.grinder.MeshFace;
import io.github.shimeoki.jfx.rasterization.Colorf;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public final class Wireframe {

    private final PixelWriter writer;
    private final ZBuffer zBuffer;
    private Color color = Color.BLACK;

    public Wireframe(GraphicsContext ctx, ZBuffer zBuffer) {
        this.writer = ctx.getPixelWriter();
        this.zBuffer = zBuffer;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private void line(int x0, int y0, int x1, int y1, float z0, float z1) {
        boolean steep = false;
        if (Math.abs(x0 - x1) < Math.abs(y0 - y1)) {
            int temp = x0;
            x0 = y0;
            y0 = temp;
            temp = x1;
            x1 = y1;
            y1 = temp;
            steep = true;
        }
        if (x0 > x1) {
            int temp = x0;
            x0 = x1;
            x1 = temp;
            temp = y0;
            y0 = y1;
            y1 = temp;
            float tmp = z0;
            z0 = z1;
            z1 = tmp;
        }

        int dx = x1 - x0;
        int dy = y1 - y0;
        float dz = (z1 - z0) / dx;
        int derror2 = Math.abs(dy) * 2;
        int error2 = 0;
        int y = y0;
        for (int x = x0; x <= x1; x++) {
            if (steep) {
                if (zBuffer.draw(y, x, z0)) {
                    writer.setColor(y, x, color);
                };
            } else {
                if (zBuffer.draw(x, y, z0)) {
                    writer.setColor(x, y, color);
                }
            }
            error2 += derror2;
            if (error2 > dx) {
                y += (y1 > y0 ? 1 : -1);
                error2 -= dx * 2;
            }
            z0 += dz;
        }
    }

    public void renderedFaceWireframe(RenderedFace face) {
        face.shape().v1().x();
        final int x1 = (int) face.shape().v1().x();
        final int y1 = (int) face.shape().v1().y();
        final float z1 = face.shape().v1().z() - 0.001f;
        final int x2 = (int) face.shape().v2().x();
        final int y2 = (int) face.shape().v2().y();
        final float z2 = face.shape().v2().z() - 0.001f;
        final int x3 = (int) face.shape().v3().x();
        final int y3 = (int) face.shape().v3().y();
        final float z3 = face.shape().v3().z() - 0.001f;

        line(x1, y1, x2, y2, z1, z2);
        line(x1, y1, x3, y3, z1, z3);
        line(x3, y3, x2, y2, z3, z2);
    }
}