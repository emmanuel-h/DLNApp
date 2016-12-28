package androidtd.dlnapp;

import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.container.Container;
/**
 * Created by GroupeProjetDLNApp on 23/12/16.
 */

public class MyObjectContainer extends MyObject {

    private final DIDLObject container;
    private Service service;

    public MyObjectContainer(Container container, Service service){
        super(R.drawable.folder, container.getTitle(), "");
        this.container = container;
        this.service = service;
    }

    public Service getService(){
        return this.service;
    }

    public String getId(){
        return this.container.getId();
    }

    public String getIdParent(){
        return this.container.getParentID();
    }
}
