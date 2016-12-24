package androidtd.dlnapp;

import org.fourthline.cling.model.meta.Device;

/**
 * Created by GroupeProjetDLNApp on 23/12/16.
 */

public interface Notification {

    public void deviceAdded(final Device device);

    public void deviceRemoved(final Device device);

    public void clear();
}
