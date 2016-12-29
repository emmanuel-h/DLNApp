package androidtd.dlnapp.UpnpConnection;

import android.content.Context;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;

import androidtd.dlnapp.Notification;

/**
 * Listener on the devices connected on the network
 *
 * Created by GroupeProjetDLNApp on 23/12/16.
 */

public class RegistryListener extends DefaultRegistryListener {



    /**
     * Communication with the main activity
     */
    private Notification context;

    /**
     * Constructor
     *
     * @param context The main activity
     */
    public RegistryListener(Context context){
        super();
        this.context = (Notification) context;
    }

    /**
     * Refers to deviceAdded
     *
     * @param registry
     * @param device
     */
    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
        deviceAdded(device);
    }

    /**
     * Refers to deviceRemoved
     *
     * @param registry
     * @param device
     * @param ex
     */
    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry, final RemoteDevice device, final Exception ex) {
        deviceRemoved(device);
    }

    /**
     * Refers to deviceAdded
     *
     * @param registry
     * @param device
     */
    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
        deviceAdded(device);
    }

    /**
     * Refers to deviceRemoved
     *
     * @param registry
     * @param device
     */
    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
        deviceRemoved(device);
    }

    /**
     * Refers to deviceAdded
     *
     * @param registry
     * @param device
     */
    @Override
    public void localDeviceAdded(Registry registry, LocalDevice device) {
        deviceAdded(device);
    }

    /**
     * Refers to deviceRemoved
     *
     * @param registry
     * @param device
     */
    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice device) {
        deviceRemoved(device);
    }

    /**
     * Report to the main activity the new device
     *
     * @param device The new device
     */
    public void deviceAdded(final Device device) {
        context.deviceAdded(device);
    }

    /**
     * Report to the main activity the disconnection of a device
     *
     * @param device The device disconnected
     */
    public void deviceRemoved(final Device device) {
        context.deviceRemoved(device);
    }
}
