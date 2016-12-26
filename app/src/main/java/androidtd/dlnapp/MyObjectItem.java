package androidtd.dlnapp;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.item.Item;

/**
 * Created by GroupeProjetDLNApp on 23/12/16.
 */

public class MyObjectItem extends MyObject {

    private final DIDLObject item;

    public MyObjectItem(Item childItem) {
        super(R.drawable.ic_device,childItem.getTitle(),"");
        this.item = childItem;
    }

    public DIDLObject getItem(){
        return this.item;
    }

}
