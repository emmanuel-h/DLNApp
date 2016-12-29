package androidtd.dlnapp.MyObject;

import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.container.Container;
import androidtd.dlnapp.R;

/**
 * Represent a DIDL container, ie a folder in the file tree
 *
 * Created by GroupeProjetDLNApp on 23/12/16.
 */
public class MyObjectContainer extends MyObject {

    /**
     * The DIDL Container
     */
    private final DIDLObject container;

    /**
     * The androidUpnpService
     */
    private Service service;

    /**
     * Constructor
     *
     * @param container The DIDL container
     * @param service   The androidUpnpService
     */
    public MyObjectContainer(Container container, Service service){
        super(R.drawable.folder, container.getTitle(), "");
        this.container = container;
        this.service = service;
    }

    /**
     * Getter of the service
     *
     * @return  The service
     */
    public Service getService(){
        return this.service;
    }

    /**
     * Getter of the DIDL container's id
     *
     * @return  The id
     */
    public String getId(){
        return this.container.getId();
    }

    /**
     * Getter of the DIDL's container's parent id
     *
     * @return  The id
     */
    public String getIdParent(){
        return this.container.getParentID();
    }
}
