package androidtd.dlnapp;

import android.net.Uri;
import android.webkit.MimeTypeMap;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.item.Item;
import java.util.StringTokenizer;

/**
 * Created by GroupeProjetDLNApp on 23/12/16.
 */

public class MyObjectItem extends MyObject {

    private final DIDLObject item;
    String extension;

    public MyObjectItem(Item childItem) {
        super(R.drawable.file,childItem.getTitle(),"");
        this.item = childItem;
        getMimeType(super.getTitleMyObject());
    }


    public DIDLObject getItem(){
        return this.item;
    }

    public void getMimeType(String s){
        String s2 = replaceChar(s);
        this.extension = MimeTypeMap.getFileExtensionFromUrl(s2);
        System.out.println(extension);
        if(this.extension != null){
            this.extension=MimeTypeMap.getSingleton().getMimeTypeFromExtension(this.extension);
        }
    }

    public String getType() {
        if(this.extension == null){
            return "format inconnu";
        }
        StringTokenizer st = new StringTokenizer(this.extension,"/");
        return st.nextToken();
    }

    public String getExtension(){
        return this.extension;
    }

    public String replaceChar(String s){
        String s2 =  Uri.encode(s);
        s = s2.replaceAll("'","%27");
        return s;
    }
}
