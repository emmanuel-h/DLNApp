package androidtd.dlnapp;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;

/**
 * Created by GroupeProjetDLNAppv on 23/12/16.
 */

public class MyObjectDevice extends MyObject {

    private Device device;

    public MyObjectDevice(int icon, Device device) {
        super(icon);
        this.device = device;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Service getContentDirectory() {
        for (Service current : this.device.getServices())
            if (current.getServiceType().getType().equals("ContentDirectory"))
                return current;

        return null;
    }

    @Override
    public String getTitleMyObject() {
        return device.getDetails().getFriendlyName();
    }

    @Override
    public String getDescriptionMyObject() {
        return device.getDisplayString();
    }

    @Override
    public boolean equals (Object object){
        MyObjectDevice myObjectDevice = (MyObjectDevice) object;
        if (getTitleMyObject().equals(myObjectDevice.getTitleMyObject())){
            return true;
        } else {
            return false;
        }
    }
}
