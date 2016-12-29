package androidtd.dlnapp;

import org.fourthline.cling.model.meta.Device;

import java.util.ArrayList;

import androidtd.dlnapp.MyObject.MyObject;

/**
 * Created by GroupeProjetDLNApp on 23/12/16.
 */

public interface Notification {

    void deviceAdded(final Device device);

    void deviceRemoved(final Device device);

    void clear();

    void showCurrentDirectory(ArrayList<MyObject> myObjects);

    void showDevices();

    ArrayList<String> getUrlMyObjectArray();

    int getPositionUrl(ArrayList<String> liste, String s);
}
