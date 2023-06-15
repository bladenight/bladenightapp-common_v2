package app.bladenight.common.network;

public enum BladenightUrl {
    GET_ACTIVE_EVENT("http://bladenight.app/rpc/getActiveEvent"),
    GET_ACTIVE_ROUTE("http://bladenight.app/rpc/getActiveRoute"),
    GET_ROUTE("http://bladenight.app/rpc/getRoute"),
    GET_ALL_PARTICIPANTS("http://bladenight.app/rpc/getAllParticipants"),
    GET_REALTIME_UPDATE("http://bladenight.app/rpc/getRealtimeUpdate"),
    CREATE_RELATIONSHIP("http://bladenight.app/rpc/createRelationship"),
    GET_ALL_EVENTS("http://bladenight.app/rpc/getAllEvents"),
    SET_ACTIVE_ROUTE("http://bladenight.app/rpc/setActiveRoute"),
    SET_ACTIVE_STATUS("http://bladenight.app/rpc/setActiveStatus"),
    GET_ALL_ROUTE_NAMES("http://bladenight.app/rpc/getAllRouteNames"),
    VERIFY_ADMIN_PASSWORD("http://bladenight.app/rpc/verifyAdminPassword"),
    GET_FRIENDS("http://bladenight.app/rpc/getFriends"),
    DELETE_RELATIONSHIP("http://bladenight.app/rpc/deleteRelationship"),
    SET_MIN_POSITION("http://bladenight.app/rpc/setMinimumLinearPosition"),
    KILL_SERVER("http://bladenight.app/rpc/killServer"),
    SHAKE_HANDS("http://bladenight.app/rpc/shakeHand"),
    SHAKE_HANDS2("http://bladenight.app/rpc/WampEndpoint.shakeHand"),
    GET_IMAGES_AND_LINKS("http://bladenight.app/rpc/getImagesAndLinks"),
    SUBSCRIBE_MESSAGE("http://bladenight.app/rpc/subscribeToMessage"),
    UNSUBSCRIBE_MESSAGE("http://bladenight.app/rpc/unsubcribeFromMessage"),

    ;

    final public static String BASE = "http://bladenight.app/";

    BladenightUrl(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    private final String text;

    @Override
    public String toString() {
        return "BladenightUrl:"+text;
    }
}
