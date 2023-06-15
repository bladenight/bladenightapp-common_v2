package app.bladenight.common.valuelogger;

import app.bladenight.common.keyvaluestore.KeyValueStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValueReader {

    public class Entry {

        Entry(String line) {
            parseLine(line);
        }

        private void parseLine(String line) {
            map = new HashMap<String, String>();
            Pattern pattern = Pattern.compile("([a-z]+)=([^\\t]*)");
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                map.put(matcher.group(1), matcher.group(2));
            }
        }

        public double getDouble(String key) {
            return Double.parseDouble(map.get(key));
        }

        public String getString(String key) {
            return map.get(key);
        }

        private Map<String, String> map;
    }

    public interface Consumer {
        public void consume(Entry entry);
    }

    public ValueReader(File file, Consumer consumer) {
        this.file = file;
        this.consumer = consumer;
    }

    public void read() throws IOException {
        BufferedReader bufferedReader = null;
        try {
            String line;
            bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                consumer.consume(new Entry(line));
            }
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException ex) {
                // We don't care
            }
        }
    }

    private File file;
    private Consumer consumer;

    private static Logger log;

    public static void setLog(Logger log) {
        ValueReader.log = log;
    }

    protected static Logger getLog() {
        if (log == null)
            log = LogManager.getLogger(ValueReader.class.getName());
        return log;
    }
}
