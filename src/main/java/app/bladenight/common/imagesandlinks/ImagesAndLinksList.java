package app.bladenight.common.imagesandlinks;

import app.bladenight.common.keyvaluestore.KeyValueStore;
import app.bladenight.common.persistence.InconsistencyException;
import app.bladenight.common.persistence.ListPersistor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.util.*;

public class ImagesAndLinksList implements Iterable<ImageAndLink> {

    public ImagesAndLinksList() {
        imagesAndLinksList = new ArrayList<>();
    }

    public void read() throws IOException, InconsistencyException {
        persistor.read();
    }

    public void write() throws IOException {
        persistor.write();
    }

    public void addImageAndLink(ImageAndLink imageAndLink) {
        imagesAndLinksList.add(imageAndLink);
    }

    public ImageAndLink get(int pos) {
        return imagesAndLinksList.get(pos);
    }


    public int size() {
        return imagesAndLinksList.size();
    }

    public int indexOf(ImageAndLink imageAndLink) {
        return imagesAndLinksList.indexOf(imageAndLink);
    }

    @Override
    public Iterator<ImageAndLink> iterator() {
        return imagesAndLinksList.iterator();
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    protected List<ImageAndLink> imagesAndLinksList;
    // don't serialize the persistor (e.g. transient)
    private transient ListPersistor<ImageAndLink> persistor;

    private static Logger log;

    public static void setLog(Logger log) {
        ImagesAndLinksList.log = log;
    }

    protected static Logger getLog() {
        if (log == null)
            log = LogManager.getLogger(ImagesAndLinksList.class.getName());
        return log;
    }
}
