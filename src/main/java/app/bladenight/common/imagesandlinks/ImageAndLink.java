package app.bladenight.common.imagesandlinks;

import app.bladenight.common.keyvaluestore.KeyValueStore;
import app.bladenight.common.persistence.ListItem;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.Serializable;

public class ImageAndLink implements ListItem, Serializable {
    private static final long serialVersionUID = -2865857999368371094L;

    private String image;
    private String link;
    private String text;
    private String key;

    public boolean equalsName(String otherName) {
        return (otherName == null) ? false : image.equals(otherName);
    }

    @Override
    public String toString() {
        return "ImageAndLink [" + key + "]:" + image + " Link " + link + " text " + text;
    }

    @Override
    public String getPersistenceId() {
        return String.valueOf(this.hashCode());
    }

    public static class Builder {
        private ImageAndLink imageAndLink;

        public Builder() {
            imageAndLink = new ImageAndLink();
        }

        public Builder setkey(String key) {
            imageAndLink.setKey(key);
            return this;
        }

        public Builder setImage(String image) {
            imageAndLink.setImage(image);
            return this;
        }

        public Builder setLink(String link) {
            imageAndLink.setLink(link);
            return this;
        }

        public Builder setText(String text) {
            imageAndLink.setText(text);
            return this;
        }

        public Builder setKey(String key) {
            imageAndLink.setKey(key);
            return this;
        }

        public ImageAndLink build() {
            return imageAndLink;
        }

    }

    public ImageAndLink(String key, String image, String link) {
        this.key = key;
        this.link = link;
        this.image = image;
    }

    public ImageAndLink() {
    }


    public String setImage(String image) {
        return this.image;
    }

    public String getImage() {
        return image;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    private static Logger log;

    public static void setLog(Logger log) {
        ImageAndLink.log = log;
    }

    protected static Logger getLog() {
        if (log == null)
            log = LogManager.getLogger(ImageAndLink.class.getName());
        return log;
    }

}
