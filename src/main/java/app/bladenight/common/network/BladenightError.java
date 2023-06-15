package app.bladenight.common.network;

public enum BladenightError {
    INTERNAL_ERROR("http://bladenight.app/internalError"),
    INVALID_ARGUMENT("http://bladenight.app/invalidArgument"),
    INVALID_PASSWORD("http://bladenight.app/invalidPassword"),
    OUTDATED_CLIENT("http://bladenight.app/outdatedClient"),
    ;

    private BladenightError(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    private final String text;

    @Override
    public String toString() {
        return "BladenightError:"+text;
    }
}
