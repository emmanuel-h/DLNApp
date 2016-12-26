package androidtd.dlnapp;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.audiofx.NoiseSuppressor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.model.meta.Device;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by GroupeProjetDLNApp on 23/12/16.
 */

public class Browser extends Fragment {

    private RegistryListener registryListener;

    private AndroidUpnpService androidUpnpService;

    private Context context;

    private Notification notification;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            androidUpnpService = (AndroidUpnpService) service;

            notification.clear();

            // Get ready for future device advertisements
            androidUpnpService.getRegistry().addListener(registryListener);

            // Now add all devices to the list we already know about
            Collection<Device> collection = androidUpnpService.getRegistry().getDevices();
            for (Device device : collection) {
                registryListener.deviceAdded(device);
            }

            // Search asynchronously for all devices
            androidUpnpService.getControlPoint().search();
        }

        public void onServiceDisconnected(ComponentName className) {
            androidUpnpService = null;
        }
    };

    @Override
    public void onAttach(Context context) {
        this.context = context;
        this.notification = (Notification) context;
        super.onAttach(this.context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context.bindService(new Intent(this.context, AndroidUpnpServiceImpl.class), serviceConnection, Context.BIND_AUTO_CREATE);

        registryListener = new RegistryListener(this.context);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        this.context = null;
        this.notification = null;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(androidUpnpService != null){
            androidUpnpService.getRegistry().removeListener(registryListener);
        }
        context.unbindService(serviceConnection);
    }
}
