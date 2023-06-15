package app.bladenight.common.procession.tasks;

import java.io.File;
import java.io.IOException;


import app.bladenight.common.keyvaluestore.KeyValueStore;
import app.bladenight.common.procession.Procession;
import app.bladenight.common.time.Metronome;
import app.bladenight.common.valuelogger.ValueLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ProcessionLogger implements Runnable {

    public ProcessionLogger(File traceFile, Procession procession, long period) {
        this.procession = procession;
        this.period = period;
        this.valueLogger = new ValueLogger(traceFile);
    }

    @Override
    public void run() {
        boolean cont = true;
        Metronome metronome = new Metronome(period);
        while (cont) {
            try {
                write();
                metronome.waitNext();
            } catch (InterruptedException e) {
                cont = false;
            } catch (IOException e) {
                log.error("Failed to write: " + e);
            }
        }
    }

    public boolean shallContinue() {
        return true;
    }

    public void write() throws IOException {
        long headPosition = (long) procession.getHeadPosition();
        long tailPosition = (long) procession.getTailPosition();
        if (headPosition <= 0)
            return;
        valueLogger.flushAllValues();
        valueLogger.setValue(FIELD.HEAD_POS.toString(), Long.toString(headPosition));
        valueLogger.setValue(FIELD.TAIL_POS.toString(), Long.toString(tailPosition));
        valueLogger.write();
    }


    public enum FIELD {
        HEAD_POS("hp"),
        TAIL_POS("tp"),
        ;

        /**
         * @param text
         */
        private FIELD(final String text) {
            this.text = text;
        }

        private final String text;

        @Override
        public String toString() {
            return text;
        }
    }

    private Procession procession;
    private long period;
    private ValueLogger valueLogger;

    private static Logger log;

    public static void setLog(Logger log) {
        ProcessionLogger.log = log;
    }

    protected static Logger getLog() {
        if (log == null)
            log = LogManager.getLogger(ProcessionLogger.class.getName());
        return log;
    }
}
