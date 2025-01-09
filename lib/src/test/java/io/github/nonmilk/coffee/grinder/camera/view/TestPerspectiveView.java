package io.github.nonmilk.coffee.grinder.camera.view;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestPerspectiveView {

    @Test
    public void testInit1() {
        new PerspectiveView((float) Math.toRadians(14), 1);
        Assertions.assertTrue(true);
    }

    @Test
    public void testInit2() {
        new PerspectiveView((float) Math.toRadians(100), 600);
        Assertions.assertTrue(true);
    }

    @Test
    public void testInit3() {
        new PerspectiveView((float) Math.toRadians(0.1f), 0.000001f);
        Assertions.assertTrue(true);
    }

    @Test
    public void testFovException1() {
        try {
            new PerspectiveView((float) Math.toRadians(-12), 1);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void testFovException2() {
        try {
            new PerspectiveView((float) Math.toRadians(0), 1);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void testFovException3() {
        try {
            new PerspectiveView((float) Math.toRadians(900), 1);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void testAspectRatioException1() {
        try {
            new PerspectiveView((float) Math.toRadians(90), 0);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void testAspectRatioException2() {
        try {
            new PerspectiveView((float) Math.toRadians(90), -1);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }
}
