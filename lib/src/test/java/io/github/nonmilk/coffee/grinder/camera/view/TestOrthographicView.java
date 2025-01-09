package io.github.nonmilk.coffee.grinder.camera.view;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestOrthographicView {

    @Test
    public void testInit1() {
        new OrthographicView(1, 12);
        Assertions.assertTrue(true);
    }

    @Test
    public void testInit2() {
        new OrthographicView(2000000, 12000000);
        Assertions.assertTrue(true);
    }

    @Test
    public void testInit3() {
        new OrthographicView(0.1f,0.005f);
        Assertions.assertTrue(true);
    }

    @Test
    public void testWidthException() {
        try {
            new OrthographicView(0, 12);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void testHeightException() {
        try {
            new OrthographicView(12, 0);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

}
