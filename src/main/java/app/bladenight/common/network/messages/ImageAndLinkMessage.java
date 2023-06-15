package app.bladenight.common.network.messages;

import app.bladenight.common.imagesandlinks.ImageAndLink;
import app.bladenight.common.keyvaluestore.KeyValueStore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.Serializable;

public class ImageAndLinkMessage implements Serializable {

    private static final long serialVersionUID = 7033146365216620983L;

    public String img;    // imagename
    public String lnk;    // linktoimage
    public String txt;
    public String key;    // key for image and link

    public ImageAndLinkMessage() {
    }

    public ImageAndLinkMessage(ImageAndLink e) {
        copyFromImageAndLink(e);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.lnk = lnk;
    }

    public String getLink() {
        return lnk;
    }

    public void setLink(String lnk) {
        this.lnk = lnk;
    }

    public String getImage() {
        return img;
    }

    public void setImage(String img) {
        this.img = img;
    }

    public String getText() {
        return txt;
    }

    public void setText(String img) {
        this.txt = txt;
    }

    public void copyFromImageAndLink(ImageAndLink c) {
        img = c.getImage();
        lnk = c.getLink();
        key = c.getKey();
        txt=c.getText();
    }

    public static ImageAndLinkMessage newFromImageAndLink(ImageAndLink e) {
        ImageAndLinkMessage message = new ImageAndLinkMessage();
        if (e==null){
            return message;
        }
        message.copyFromImageAndLink(e);
        return message;
    }

    public ImageAndLink toImagesAndLink() {
        return new ImageAndLink.Builder()
                .setKey(key)
                .setLink(lnk)
                .setImage(img)
                .setText(txt)
                .build();
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private static Logger log;

    public static void setLog(Logger log) {
        ImageAndLinkMessage.log = log;
    }

    protected static Logger getLog() {
        if (log == null)
            log = LogManager.getLogger(ImageAndLinkMessage.class.getName());
        return log;
    }
}
