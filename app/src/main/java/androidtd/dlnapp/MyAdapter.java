package androidtd.dlnapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidtd.dlnapp.MyObject.MyObject;
import androidtd.dlnapp.MyObject.MyObjectDevice;
import androidtd.dlnapp.UpnpConnection.Browser;

/**
 * Manage the array of MyObject and match MyObject's properties with xml properties
 *
 * Created by GroupeProjetDLNApp on 23/12/16.
 */
public class MyAdapter extends ArrayAdapter<MyObject> {

    /**
     * Context of MainActivity
     */
    Context context;

    /**
     * The Browse fragment
     */
    Browser browser;

    /**
     * Constructor
     *
     * @param context   The context of MainActivity
     * @param myObjects The ArrayList of MyObject
     */
    public MyAdapter(Context context,ArrayList<MyObject> myObjects) {
        super(context,0,myObjects);
        this.context = context;
    }

    /**
     * Inflate the view with the properties of a MyObject
     *
     * @param position      The position of the current MyObject in the ArrayList
     * @param convertView   The view in which properties will be displayed
     * @param parent
     *
     * @return  The view inflated with the MyObject properties
     */
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        final MyObject myObject = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.my_object, parent, false);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (MyViewHolder)convertView.getTag();
        }
        if(myObject instanceof MyObjectDevice && ((MyObjectDevice) myObject).getUrlIcon()!=null){
            Picasso.with(context).load(((MyObjectDevice) myObject).getUrlIcon()).into(holder.iconMyObject);
        } else {
            holder.iconMyObject.setImageResource(myObject.getIcon());
        }
        holder.descriptionMyObject.setText(myObject.getDescriptionMyObject());
        holder.titleMyObject.setText(myObject.getTitleMyObject());
        holder.holder_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browser.browseDirectory(myObject);
            }
        });
        return convertView;
    }

    /**
     * Setter for the Browser fragment
     *
     * @param browser   The Browser
     */
    public void setBrowser(Browser browser){
        this.browser = browser;
    }

    /**
     * Match his properties with ids of the layout
     */
    private class MyViewHolder {
        TextView titleMyObject;
        TextView descriptionMyObject;
        ImageView iconMyObject;
        LinearLayout holder_layout;

        public MyViewHolder(View view) {
            titleMyObject = (TextView) view.findViewById(R.id.titleMyObject);
            descriptionMyObject = (TextView) view.findViewById(R.id.descriptionMyObject);
            iconMyObject = (ImageView) view.findViewById(R.id.iconMyObject);
            holder_layout = (LinearLayout) view.findViewById(R.id.holder_layout);
        }
    }
}
