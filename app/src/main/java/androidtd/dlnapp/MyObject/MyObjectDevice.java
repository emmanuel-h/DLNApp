package androidtd.dlnapp.MyObject;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Icon;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;

/**
 * Represent a Cling-Core device
 *
 * Created by GroupeProjetDLNAppv on 23/12/16.
 */
public class MyObjectDevice extends MyObject {

    /**
     * The Cling-Core device
     */
    private Device device;

    /**
     * Url of the device's own icon, if it exist
     */
    private String urlIcon;

    /**
     * Constructor
     *
     * @param icon      The device's icon
     * @param device    The Cling-Core device
     */
    public MyObjectDevice(int icon, Device device) {
        super(icon);
        this.device = device;
        this.urlIcon = findUrlIcon();
    }

    /**
     * Getter of the device
     *
     * @return  The device
     */
    public Device getDevice() {
        return device;
    }

    /**
     * Return the ContentDirectory service of the device, which is its file tree
     *
     * @return
     */
    public Service getContentDirectory() {
        for (Service current : this.device.getServices())
            if (current.getServiceType().getType().equals("ContentDirectory"))
                return current;

        return null;
    }

    /**
     * Getter of the title
     *
     * @return  The title
     */
    @Override
    public String getTitleMyObject() {
        return device.getDetails().getFriendlyName();
    }

    /**
     * Getter of the description
     *
     * @return  The description
     */
    @Override
    public String getDescriptionMyObject() {
        return device.getDisplayString();
    }

    /**
     * Allow to compare two devices. The comparison is made relative to the title of the device.
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals (Object object){
        MyObjectDevice myObjectDevice = (MyObjectDevice) object;
        if (getTitleMyObject().equals(myObjectDevice.getTitleMyObject())){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Find the url of the device's icon if it exists
     *
     * @return  The icon's url
     */
    public String findUrlIcon(){
        for(Icon icon : this.device.getIcons()){
            // Test image size
            if(icon != null && icon.getHeight() >= 64 && icon.getWidth() >= 64){
                return ((RemoteDevice) device).normalizeURI(icon.getUri()).toString();
            }
        }
        return null;
    }

    /**
     * Getter de l'icon
     *
     * @return  The icon
     */
    public String getUrlIcon() {
        return urlIcon;
    }

}
