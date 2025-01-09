package io.github.nonmilk.coffee.grinder.camera;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestClippingBox {

    @Test
    public void testInit1() {
        new ClippingBox(0, 13);
        Assertions.assertTrue(true);
    }

    @Test
    public void testInit2() {
        new ClippingBox(13, 60000f);
        Assertions.assertTrue(true);
    }

    @Test
    public void testInit3() {
        new ClippingBox(13.3f, 13.31f);
        Assertions.assertTrue(true);
    }

    @Test
    public void testException1() {
        try {
            new ClippingBox(0, 0);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void testException2() {
        try {
            new ClippingBox(5, 5);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void testException3() {
        try {
            new ClippingBox(12, 3);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void testException4() {
        try {
            new ClippingBox(-2, 13);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }
}
