package app.bladenight.common.keyvaluestore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.util.Date;


/**
 * Manages a KeyvalueStore Singleton, typically for the application configuration.
 * Its interface is very similar to KeyValueStoreInterface, with 2 majors differences:
 * it implements readFromFile() vs. readExternalResource
 * it has static methods
 */
public class KeyValueStoreSingleton {

    private static KeyValueStoreInterface currentStore = null;
    private static String configurationFilePath = null;

    public static KeyValueStoreInterface getCurrentStore() {
        return currentStore;
    }

    public static void setCurrentStore(KeyValueStoreInterface currentStore) {
        KeyValueStoreSingleton.currentStore = currentStore;
    }

    public static boolean readFromFile(String path) {
        KeyValueStorePropertyBased store = new KeyValueStorePropertyBased();
        if (! store.readExternalResource(path) )
            // TODO logging
            return false;
        configurationFilePath = path;
        setCurrentStore(store);
        return true;
    }

    public static boolean writeToFile() {
        return writeToFile(configurationFilePath);
    }

    public static boolean writeToFile(String targetFilePath) {
        return getCurrentStore().writeExternalResource(targetFilePath);
    }

    public static String getString(String key) {
        return getCurrentStore().getString(key);
    }

    public static void setString(String key, String value) {
        getCurrentStore().setString(key, value);
    }

    public static String getString(String key, String defaultValue) {
        return getCurrentStore().getString(key, defaultValue);
    }

    public static String getNonNullString(String key) throws IllegalArgumentException {
        return getCurrentStore().getNonNullString(key);
    }

    public static long getLong(String key, long defaultValue) {
        return getCurrentStore().getLong(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return getCurrentStore().getInt(key, defaultValue);
    }

    public static double getDouble(String key, double defaultValue) {
        return getCurrentStore().getDouble(key, defaultValue);
    }

    public static Date getDate(String key, String defaultValue) throws ParseException {
        return getCurrentStore().getDate(key, defaultValue);
    }

    public static Date getDate(String key) throws IllegalArgumentException,
    ParseException {
        return getCurrentStore().getDate(key);
    }

    public static String getPath(String key, String defaultPath) {
        return getCurrentStore().getPath(key, defaultPath);
    }

    public static String getPath(String key) {
        return getCurrentStore().getPath(key);
    }

    private static Logger log;

    public static void setLog(Logger log) {
        KeyValueStoreSingleton.log = log;
    }

    protected static Logger getLog() {
        if (log == null)
            log = LogManager.getLogger(KeyValueStoreSingleton.class.getName());
        return log;
    }
}
