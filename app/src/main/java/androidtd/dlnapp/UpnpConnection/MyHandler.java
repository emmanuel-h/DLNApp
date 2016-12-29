package androidtd.dlnapp.UpnpConnection;

import android.content.Context;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.contentdirectory.callback.Browse;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;

import java.util.ArrayList;

import androidtd.dlnapp.MyObject.MyObject;
import androidtd.dlnapp.MyObject.MyObjectContainer;
import androidtd.dlnapp.MyObject.MyObjectItem;
import androidtd.dlnapp.Notification;

/**
 * Create MyObjects children of a DIDL content
 *
 * Created by GroupeProjetDLNApp on 26/12/2016.
 */
public class MyHandler extends Browse {

    /**
     * Communication with the main activity
     */
    private Notification notification;

    /**
    * Cling-Core service managing the devices stack
     */
    private Service androidUpnpService;

    /**
     * Constructor
     *
     * @param service       The androidUpnpService
     * @param containerId   MyObject to explore
     * @param flag          What children to get
     * @param context       The main activity
     */
    public MyHandler(Service service, String containerId, BrowseFlag flag, Context context) {
        super(service, containerId, flag);
        this.notification = (Notification) context;
        this.androidUpnpService = service;
    }

    /**
     * Create the MyObject depending of their types
     *
     * @param actionInvocation
     * @param didl  The device or container who have been selected by the user
     */
    @Override
    public void received(ActionInvocation actionInvocation, DIDLContent didl) {
        ArrayList<MyObject> myObjects = new ArrayList<>();
        for (Container childContainer : didl.getContainers())
            myObjects.add( new MyObjectContainer(childContainer,androidUpnpService));
        for (Item childItem : didl.getItems())
            myObjects.add( new MyObjectItem(childItem));
        notification.showCurrentDirectory(myObjects);
    }

    /**
     * Update the status
     *
     * @param status
     */
    @Override
    public void updateStatus(Status status) {
    }

    /**
     * When an error with a MyObject is encountered
     *
     * @param invocation
     * @param operation
     * @param defaultMsg
     */
    @Override
    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
    }
}
