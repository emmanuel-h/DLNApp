package androidtd.dlnapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by GroupeProjetDLNApp on 23/12/16.
 */

public class MyAdapter extends ArrayAdapter<MyObject> {

    Context context;

    public MyAdapter(Context context,ArrayList<MyObject> myObjects) {
        super(context,0,myObjects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        MyObject myObject = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.my_object, parent, false);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (MyViewHolder)convertView.getTag();
        }
        holder.iconMyObject.setImageResource(myObject.getIcon());
        holder.descriptionMyObject.setText(myObject.getDescriptionMyObject());
        holder.titleMyObject.setText(myObject.getTitleMyObject());
        holder.holder_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO GOTO POTO KODO
            }
        });
        return convertView;
    }


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
