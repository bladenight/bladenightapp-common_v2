package app.bladenight.common.network;

import static org.junit.Assert.*;

import org.junit.Test;

public class BladenightErrorTest {

    // This test is more to improve test coverage than to fix bugs
    @Test
    public void test() {
        String asText = BladenightError.INTERNAL_ERROR.getText();
        assertEquals(asText, "http://bladenight.app/internalError");

        String asString = BladenightError.INTERNAL_ERROR.toString();
        assertEquals(asString, "BladenightError:"+asText);
    }
}
