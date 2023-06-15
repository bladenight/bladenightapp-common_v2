package app.bladenight.common.network.messages;

import app.bladenight.common.imagesandlinks.ImageAndLink;
import app.bladenight.common.imagesandlinks.ImagesAndLinksList;

public class ImagesAndLinksListMessage {

    public ImageAndLinkMessage[] ial = new ImageAndLinkMessage[0];

    public static ImagesAndLinksListMessage newFromImagesAndLinks(ImagesAndLinksList list) {
        ImagesAndLinksListMessage message = new ImagesAndLinksListMessage();
        message.copyFromImagesAndLinks(list);
        return message;
    }

    public void copyFromImagesAndLinks(ImagesAndLinksList list) {
        ial = new ImageAndLinkMessage[list.size()];
        int i = 0;
        for (ImageAndLink e : list) {
            if (e != null) {//persistor 0s not a ImageAndLink
                ial[i++] = ImageAndLinkMessage.newFromImageAndLink(e);
            }
        }
    }

    static public ImagesAndLinksListMessage ImagesAndLinkList() {
        ImagesAndLinksListMessage message = new ImagesAndLinksListMessage();
        return message;
    }


}
