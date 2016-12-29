package androidtd.dlnapp.MyObject;

import android.net.Uri;
import android.webkit.MimeTypeMap;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.item.Item;
import java.util.StringTokenizer;

import androidtd.dlnapp.R;

/**
 * Represent a DIDL item, ie a file in the file tree
 *
 * Created by GroupeProjetDLNApp on 23/12/16.
 */
public class MyObjectItem extends MyObject {

    /**
     * The DIDL Item
     */
    private final DIDLObject item;

    /**
     * The MIME file extension of the item
     */
    String extension;

    /**
     * Constructor
     *
     * @param childItem The item
     */
    public MyObjectItem(Item childItem) {
        super(R.drawable.file,childItem.getTitle(),"");
        this.item = childItem;
        getMimeType(super.getTitleMyObject());
    }

    /**
     * Gettor of the item
     *
     * @return  The item
     */
    public DIDLObject getItem(){
        return this.item;
    }

    /**
     * Recognize the MIME type of the current item
     *
     * @param s The filename
     */
    public void getMimeType(String s){
        String s2 = replaceChar(s);
        this.extension = MimeTypeMap.getFileExtensionFromUrl(s2);
        System.out.println(extension);
        if(this.extension != null){
            this.extension=MimeTypeMap.getSingleton().getMimeTypeFromExtension(this.extension);
        }
    }

    /**
     * Getter of the type. Work in relation with the MIME extension
     *
     * @return  The type
     */
    public String getType() {
        if(this.extension == null){
            return "format inconnu";
        }
        StringTokenizer st = new StringTokenizer(this.extension,"/");
        return st.nextToken();
    }

    /**
     * Getter of the extension
     *
     * @return  The extension
     */
    public String getExtension(){
        return this.extension;
    }

    /**
     * Replace char "'" with "%27", so the URI can be read by the view
     *
     * @param s The string to process
     *
     * @return  The String without "'"
     */
    private String replaceChar(String s){
        String s2 =  Uri.encode(s);
        s = s2.replaceAll("'","%27");
        return s;
    }
}
