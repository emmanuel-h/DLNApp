package androidtd.dlnapp;

/**
 * Created by GroupeProjetDLNApp on 23/12/16.
 */

public class MyObject {

    private int icon;
    private String titleMyObject;
    private String descriptionMyObject;

    public MyObject(int icon){
        this.icon = icon;
    }

    public MyObject(int icon, String titleMyObject, String descriptionMyObject) {
        this.icon = icon;
        this.titleMyObject = titleMyObject;
        this.descriptionMyObject = descriptionMyObject;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitleMyObject() {
        return titleMyObject;
    }

    public String getDescriptionMyObject() {
        return descriptionMyObject;
    }
}
