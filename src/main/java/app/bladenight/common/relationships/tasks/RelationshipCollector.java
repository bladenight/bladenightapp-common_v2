package app.bladenight.common.relationships.tasks;

import java.io.IOException;

import app.bladenight.common.keyvaluestore.KeyValueStore;
import app.bladenight.common.relationships.RelationshipStore;
import app.bladenight.common.time.Sleep;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RelationshipCollector implements Runnable {
    public RelationshipCollector(RelationshipStore store, long period, long maxAge) {
        this.store = store;
        this.period = period;
        this.maxAge = maxAge;
    }

    @Override
    public void run() {
        boolean cont = true;
        while (cont) {
            int hits = store.removePendingRelationshipsOlderThan(maxAge);
            if ( hits > 0 )
                try {
                    store.write();
                } catch (IOException e) {
                    getLog().error("Error while writting:",e);
                }
            try {
                Sleep.sleep(period);
            } catch (InterruptedException e) {
                cont = false;
            }
        }
    }

    public boolean shallContinue() {
        return true;
    }

    private RelationshipStore store;
    private long period;
    private long maxAge;

    private static Logger log;

    public static void setLog(Logger log) {
        RelationshipCollector.log = log;
    }

    protected static Logger getLog() {
        if (log == null)
            log = LogManager.getLogger(RelationshipCollector.class.getName());
        return log;
    }
}
