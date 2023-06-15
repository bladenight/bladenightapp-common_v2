package app.bladenight.common.utils;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import app.bladenight.common.time.Sleep;

public class SleepTest {

    @Test
    public void sleepTest() throws InterruptedException {
        long start = System.currentTimeMillis();
        long requestedMs = 1;
        Sleep.sleep(requestedMs);
        assertTrue(System.currentTimeMillis() - start >= requestedMs);
    }
}
