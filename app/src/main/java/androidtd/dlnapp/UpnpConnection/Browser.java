package androidtd.dlnapp.UpnpConnection;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.support.model.BrowseFlag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

import androidtd.dlnapp.MediaActivities.MyImageViewActivity;
import androidtd.dlnapp.MediaActivities.MyVideoMusicViewActivity;
import androidtd.dlnapp.MyObject.MyObject;
import androidtd.dlnapp.MyObject.MyObjectContainer;
import androidtd.dlnapp.MyObject.MyObjectDevice;
import androidtd.dlnapp.MyObject.MyObjectItem;
import androidtd.dlnapp.Notification;

/**
 * Allow all the transactions with the server, and manage the upnp connexion.
 *
 * Created by GroupeProjetDLNApp on 23/12/16.
 */
public class Browser extends Fragment {

    /**
     * Listener on the status of devices connected on the network
     */
    private RegistryListener registryListener;

    /**
     * Cling-Core service managing the devices stack
     */
    private AndroidUpnpService androidUpnpService;

    /**
     * Context of MainActivity
     */
    private Context context;

    /**
     * Communication with the main activity
     */
    private Notification notification;

    /**
     * Create the current list of MyObjects
     */
    private MyHandler myHandler;


    /**
     * Allow to backtrack in the previous folder when the back key is pressed
     */
    private Stack<MyObject> stack;

    /**
     * Constantly watch the androidUpnpService
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        /**
         * Attach the androidUpnpService and set the listeners
         *
         * @param className The class name
         * @param service   The service to watch
         */
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

        /**
         * Detach the androidUpnpService
         * @param className The class name
         */
        public void onServiceDisconnected(ComponentName className) {
            androidUpnpService = null;
        }
    };


    /**
     * Initialize the parameters when the fragment is attached to the activity
     *
     * @param context The activity attached
     */
    @Override
    public void onAttach(Context context) {
        this.context = context;
        this.notification = (Notification) context;
        super.onAttach(this.context);
    }

    /**
     * Initialize parameters
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context.bindService(new Intent(this.context, AndroidUpnpServiceImpl.class), serviceConnection, Context.BIND_AUTO_CREATE);
        registryListener = new RegistryListener(this.context);
        stack = new Stack<>();
    }

    /**
     * Delete the link to the activity
     */
    @Override
    public void onDetach(){
        super.onDetach();
        this.context = null;
        this.notification = null;
    }

    /**
     * Cut and unbind the connection with the androidUpnpService
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(androidUpnpService != null){
            androidUpnpService.getRegistry().removeListener(registryListener);
        }
        context.unbindService(serviceConnection);
    }

    /**
     * Browse the sons of the MyObject in order to display them
     * If it is a device, displays its root, and a container its children. If it is an item, open it with a media player
     *
     * @param myObject The MyObject selected by the user
     */
    public void browseDirectory(MyObject myObject) {
        if(myObject instanceof MyObjectDevice && context != null){
            stack.push(myObject);
            // If myObject is a device, display his root content
            if(((MyObjectDevice) myObject).getDevice().isFullyHydrated()) {
                myHandler = new MyHandler(((MyObjectDevice) myObject).getContentDirectory(), "0", BrowseFlag.DIRECT_CHILDREN, this.context);
                androidUpnpService.getControlPoint().execute(myHandler);
            }
        } else {
            // If myObject is a container, display the content inside this container
            if (myObject instanceof MyObjectContainer){
                stack.push(myObject);
                myHandler = new MyHandler(((MyObjectContainer) myObject).getService(),((MyObjectContainer) myObject).getId(), BrowseFlag.DIRECT_CHILDREN,this.context);
                androidUpnpService.getControlPoint().execute(myHandler);
            } else {
                if(myObject instanceof MyObjectItem){
                    String extension=((MyObjectItem)myObject).getExtension();
                    String type=((MyObjectItem)myObject).getType();
                    Intent intent;
                    String uri = ((MyObjectItem) myObject).getItem().getFirstResource().getValue();

                    Uri location = Uri.parse(uri);
                    // If it is an image, open it into our custom ImageView to benefit the swipe right and left
                    if(type.equals("image")) {
                        intent = new Intent(context, MyImageViewActivity.class);
                        intent.putExtra("uri", uri);
                        ArrayList<String> liste = notification.getUrlMyObjectArray();
                        intent.putStringArrayListExtra("array", liste);
                        intent.putExtra("index", notification.getPositionUrl(liste, uri));
                        startActivity(intent);
                    } else {
                        // If an application can open the media, user can choose it
                        intent = new Intent(Intent.ACTION_VIEW,location);
                        intent.setDataAndType(location,extension);
                        Intent chooser = Intent.createChooser(intent,"Choose an application to open the media");
                        if(intent.resolveActivity(context.getPackageManager())!=null){
                            startActivity(chooser);
                        } else {
                            // If no application can open the video, open it into our custom video view
                            if(type.equals( "video") || type.equals( "audio")) {
                                intent = new Intent(context, MyVideoMusicViewActivity.class);
                                intent.putExtra("uri", uri);
                                intent.putExtra("type", type);
                                startActivity(intent);
                            } else {
                                // If the media is not recognized, open it with android default browser
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

    /**
     * Go back in the DIDL architecture
     */
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

    /**
     * Refresh the display depending on the type of myObject
     */
    public void refresh(){
        MyObject myObject;
        switch(stack.size()){
            // Refresh the list of devices
            case 0:
                notification.showDevices();
                break;
            // Refresh display if we are on the root of a device
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

    /**
     * Test if the user is currently seeing the root of a device
     *
     * @return true if it is the root
     */
    public boolean isRoot(){
        return stack.isEmpty();
    }
}
