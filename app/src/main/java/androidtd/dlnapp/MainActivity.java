package androidtd.dlnapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;

import java.util.ArrayList;

/**
 * Created by GroupeProjetDLNApp on 23/12/16.
 */

public class MainActivity extends AppCompatActivity implements Notification,SwipeRefreshLayout.OnRefreshListener {


    ListView listView;
    Fragment fragment;
    ArrayAdapter<MyObject> myObjectArrayAdapter;
    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_logo_inapp);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        listView = (ListView) findViewById(R.id.liste);

        // Allow to refresh a view
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(this);

        // List of MyObjects displayed
        ArrayList<MyObject> myObjects = new ArrayList<>();
        myObjectArrayAdapter = new MyAdapter(this,myObjects);
        listView.setAdapter(myObjectArrayAdapter);
        FragmentManager fragmentManager = getFragmentManager();
        fragment = fragmentManager.findFragmentByTag("browser");

        // Part of the application who make the upnp work with the cling core library
        if(fragment == null){
            fragment = new Browser();
            fragmentManager.beginTransaction().add(fragment, "browser").commit();
        }
        ((MyAdapter) myObjectArrayAdapter).setBrowser((Browser)fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                ((Browser) fragment).refresh();
        }
        return super.onOptionsItemSelected(item);
    }

    public void deviceAdded(final Device device) {
        runOnUiThread(new Runnable() {
            public void run() {
            if(device.isFullyHydrated()) {
                // Creating the device
                MyObject myObjectDevice = new MyObjectDevice(R.drawable.server, device);
                Service contentDirectory = ((MyObjectDevice)myObjectDevice).getContentDirectory();
                // The device is display only if he has contentDirectory
                if (contentDirectory != null) {
                    try {
                        int position = myObjectArrayAdapter.getPosition(myObjectDevice);

                        if (position >= 0) {
                            // Device already in the list, re-set new value at same position
                            myObjectArrayAdapter.remove(myObjectDevice);
                            myObjectArrayAdapter.insert(myObjectDevice, position);
                        } else {
                            myObjectArrayAdapter.add(myObjectDevice);
                        }
                    } catch (ClassCastException e){}
                }
            }
            }
        });
    }

    public void deviceRemoved(final Device device) {
        runOnUiThread(new Runnable() {
            public void run() {
                myObjectArrayAdapter.remove(new MyObjectDevice(R.drawable.server, device));
            }
        });
    }

    public void clear(){
        myObjectArrayAdapter.clear();
    }

    @Override
    public void showCurrentDirectory(final ArrayList<MyObject> myObjects) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myObjectArrayAdapter.clear();
                myObjectArrayAdapter.addAll(myObjects);
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(((Browser)fragment).isRoot()){
            super.onBackPressed();
        } else {
            ((Browser)fragment).goBack();
        }
    }

    @Override
    public void showDevices(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().remove(fragment).commit();
        fragment = new Browser();
        fragmentManager.beginTransaction().add(fragment, "browser").commit();
        ((MyAdapter) myObjectArrayAdapter).setBrowser((Browser)fragment);
    }

    @Override
    public void onRefresh() {
        ((Browser) fragment).refresh();
        swipeLayout.setRefreshing(false);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(this);
    }

    @Override
    public ArrayList<String> getUrlMyObjectArray(){
        int size = myObjectArrayAdapter.getCount();
        ArrayList<String> listItem = new ArrayList<>();
        for(int i = 0; i < size; i++){
            MyObject o = myObjectArrayAdapter.getItem(i);
            if(o instanceof MyObjectItem){
                listItem.add(((MyObjectItem)
                        o).getItem().getFirstResource().getValue());
            }
        }
        return listItem;
    }

    @Override
    public int getPositionUrl(ArrayList<String> liste, String s){
        int index = 0;
        for(String s2 : liste){
            if(s.equals(s2)){
                return index;
            }
            index++;
        }
        return index;
    }
}
