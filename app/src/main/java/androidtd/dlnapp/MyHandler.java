package androidtd.dlnapp;

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

/**
 * Created by GroupeProjetDLNApp on 26/12/2016.
 */

public class MyHandler extends Browse {

    private Notification notification;
    private Service androidUpnpService;

    public MyHandler(Service service, String containerId, BrowseFlag flag, Context context) {
        super(service, containerId, flag);
        this.notification = (Notification) context;
        this.androidUpnpService = service;
    }

    @Override
    public void received(ActionInvocation actionInvocation, DIDLContent didl) {
        ArrayList<MyObject> myObjects = new ArrayList<>();
        for (Container childContainer : didl.getContainers())
            myObjects.add( new MyObjectContainer(childContainer,androidUpnpService));
        for (Item childItem : didl.getItems())
            myObjects.add( new MyObjectItem(childItem));
        notification.showCurrentDirectory(myObjects);
    }

    @Override
    public void updateStatus(Status status) {

    }

    @Override
    public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {

    }
}
