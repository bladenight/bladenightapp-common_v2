package app.bladenight.common.network.messages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import app.bladenight.common.time.Clock;
import app.bladenight.common.time.Sleep;

public class AdminMessageTest {
    @Before
    public void init() {
        //AdminMessage.setLog(new NoOpLog());
    }

    @Test
    public void checksumGeneration() {
        final long time = 123456789;
        AdminMessage message = new AdminMessage();
        message.setClock(new Clock() {
            @Override
            public long currentTimeMillis() {
                return time;
            }
        });
        message.setRandom(new Random() {
            private static final long serialVersionUID = 1L;
            @Override
            public long nextLong() {
                return 987654;
            }
        });
        message.authenticate("blabla");
        assertEquals("69d845b2bef234e6e023ef0d88c97aae72aba3d3", message.getChecksum());
    }

    @Test
    public void verification() {
        AdminMessage message = new AdminMessage();
        String password = UUID.randomUUID().toString();
        message.authenticate(password);
        assertTrue(message.verify(password, 60000));
        message.setNoise(0);
        assertTrue(! message.verify(password, 60000));
    }

    @Test
    public void constructorWithPassword() {
        String password = "blabla";
        AdminMessage message = new AdminMessage("blabla","testDevice");
        assertTrue(message.getTimestamp() >= System.currentTimeMillis() - 10000);
        assertTrue(message.getTimestamp() <= System.currentTimeMillis() );
        assertTrue(message.getNoise() >= 0);
        assertTrue(message.authenticate(password));
    }


    @Test
    public void timeout() {
        AdminMessage message = new AdminMessage();
        String password = UUID.randomUUID().toString();
        message.authenticate(password);
        assertTrue(message.verify(password, 60000));
        try {
            Sleep.sleep(2);
        } catch (InterruptedException e) {
        }
        assertTrue(! message.verify(password, 1));
    }
}
