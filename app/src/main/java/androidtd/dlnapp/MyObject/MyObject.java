package androidtd.dlnapp.MyObject;

/**
 * General class combining the elements shared by all type of MyObject (Device, Container or Item)
 *
 * Created by GroupeProjetDLNApp on 23/12/16.
 */
public class MyObject {

    /**
     * Icon displayed in the list
     */
    private int icon;

    /**
     * Title displayed in the list
     */
    private String titleMyObject;

    /**
     * Description displayed in the list
     */
    private String descriptionMyObject;

    /**
     * Constructor  with only the icon
     *
     * @param icon The icon to put
     */
    public MyObject(int icon){
        this.icon = icon;
    }

    /**
     * Complete constructor
     *
     * @param icon                  The icon of the MyObject
     * @param titleMyObject         The title of the MyObject
     * @param descriptionMyObject   The description of the MyObject
     */
    public MyObject(int icon, String titleMyObject, String descriptionMyObject) {
        this.icon = icon;
        this.titleMyObject = titleMyObject;
        this.descriptionMyObject = descriptionMyObject;
    }

    /**
     * Getter of the icon
     *
     * @return The icon
     */
    public int getIcon() {
        return icon;
    }

    /**
     * Getter of the title
     *
     * @return  The title
     */
    public String getTitleMyObject() {
        return titleMyObject;
    }

    /**
     * Getter of the description
     *
     * @return The description
     */
    public String getDescriptionMyObject() {
        return descriptionMyObject;
    }
}
