package app.bladenight.common.routes;

import java.io.File;
import java.util.List;

public interface RouteLoader {
    public boolean load(File file);
    public List<Route.LatLong> getNodes();
}
