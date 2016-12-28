package androidtd.dlnapp;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.support.model.BrowseFlag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * Created by GroupeProjetDLNApp on 23/12/16.
 */

public class Browser extends Fragment {

    private RegistryListener registryListener;
    private AndroidUpnpService androidUpnpService;
    private Context context;
    private Notification notification;
    private MyHandler myHandler;
    // Allow to backtrack in the previous folder when the back key is pressed
    private Stack<MyObject> stack;

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
        stack = new Stack<>();
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

    public void browseDirectory(MyObject myObject) {
        if(myObject instanceof MyObjectDevice && context != null){
            stack.push(myObject);
            if(((MyObjectDevice) myObject).getDevice().isFullyHydrated()) {
                myHandler = new MyHandler(((MyObjectDevice) myObject).getContentDirectory(), "0", BrowseFlag.DIRECT_CHILDREN, this.context);
                androidUpnpService.getControlPoint().execute(myHandler);
            }
        } else {
            if (myObject instanceof MyObjectContainer){
                stack.push(myObject);
                myHandler = new MyHandler(((MyObjectContainer) myObject).getService(),((MyObjectContainer) myObject).getId(), BrowseFlag.DIRECT_CHILDREN,this.context);
                androidUpnpService.getControlPoint().execute(myHandler);
            } else {
                if(myObject instanceof  MyObjectItem){
                    String extension=((MyObjectItem)myObject).getExtension();
                    String type=((MyObjectItem)myObject).getType();
                    Intent intent;
                    String uri = ((MyObjectItem) myObject).getItem().getFirstResource().getValue();

                    Uri location = Uri.parse(uri);
                    if(type.equals("image")) {
                        intent = new Intent(context, MyImageViewActivity.class);
                        intent.putExtra("uri", uri);
                        ArrayList<String> liste = notification.getUrlMyObjectArray();
                        intent.putStringArrayListExtra("array", liste);
                        intent.putExtra("index", notification.getPositionUrl(liste, uri));
                        startActivity(intent);
                    } else {
                        intent = new Intent(Intent.ACTION_VIEW,location);
                        intent.setDataAndType(location,extension);
                        Intent chooser = Intent.createChooser(intent,"bite");
                        if(intent.resolveActivity(context.getPackageManager())!=null){
                            startActivity(chooser);
                        } else {
                            if(type.equals( "video") || type.equals( "audio")) {
                                intent = new Intent(context, MyVideoMusicViewActivity.class);
                                intent.putExtra("uri", uri);
                                intent.putExtra("type", type);
                                startActivity(intent);
                            } else {
                                    intent = new Intent();
                                    intent.setAction(android.content.Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.parse(uri), extension);
                                    intent.putExtra("uri", uri);
                                    startActivity(intent);
                            }
                        }
                    }
                }
            }
        }
    }

    public void goBack(){
        if(stack.size() == 1){
            notification.showDevices();
            stack.pop();
        } else {
            MyObject myObject = stack.pop();
            myHandler = new MyHandler(((MyObjectContainer) myObject).getService(),((MyObjectContainer) myObject).getIdParent(), BrowseFlag.DIRECT_CHILDREN,this.context);
            androidUpnpService.getControlPoint().execute(myHandler);
        }
    }

    public void refresh(){
        MyObject myObject;
        switch(stack.size()){
            case 0:
                notification.showDevices();
                break;
            case 1:
                myObject = stack.peek();
                myHandler = new MyHandler(((MyObjectDevice)
                        myObject).getContentDirectory(), "0",
                        BrowseFlag.DIRECT_CHILDREN,this.context);
                androidUpnpService.getControlPoint().execute(myHandler);
                break;
            default:
                myObject = stack.peek();
                myHandler = new MyHandler(((MyObjectContainer)
                        myObject).getService(),((MyObjectContainer) myObject).getId(),
                        BrowseFlag.DIRECT_CHILDREN,this.context);
                androidUpnpService.getControlPoint().execute(myHandler);
                break;
        }
    }

    public boolean isRoot(){
        return stack.isEmpty();
    }
}
