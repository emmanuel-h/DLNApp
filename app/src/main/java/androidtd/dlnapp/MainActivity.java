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

import androidtd.dlnapp.MyObject.MyObject;
import androidtd.dlnapp.MyObject.MyObjectDevice;
import androidtd.dlnapp.MyObject.MyObjectItem;
import androidtd.dlnapp.UpnpConnection.Browser;

/**
 * First activity displayed when the application is launched. Implements the Notification Interface, allowing him to react to other classes demands
 *
 * Created by GroupeProjetDLNApp on 23/12/16.
 */
public class MainActivity extends AppCompatActivity implements Notification,SwipeRefreshLayout.OnRefreshListener {

    /**
     * Display the current ArrayList of MyObject
     */
    ListView listView;

    /**
     * The fragment managing the cling-core services
     */
    Fragment fragment;

    /**
     * ArrayAdapter of the current list of MyObject displayed
     */
    ArrayAdapter<MyObject> myObjectArrayAdapter;

    /**
     * Used to refresh the current page by swiping down
     */
    SwipeRefreshLayout swipeLayout;

    /**
     * Create and initialize all the components useful for the application
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the app logo on the menu
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

    /**
     * Inflate the application's menu with the xml custom one
     *
     * @param menu  The menu to inflate
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app,menu);
        return true;
    }

    /**
     * Set up the refresh button in the menu
     *
     * @param item  The refresh button
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                ((Browser) fragment).refresh();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Add a device in the device's list myObjectArrayAdapter
     *
     * @param device
     */
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

    /**
     * Remove a device in the myObjectArrayAdapter's list
     *
     * @param device
     */
    public void deviceRemoved(final Device device) {
        runOnUiThread(new Runnable() {
            public void run() {
                myObjectArrayAdapter.remove(new MyObjectDevice(R.drawable.server, device));
            }
        });
    }

    /**
     * Clear the list of devices
     */
    public void clear(){
        myObjectArrayAdapter.clear();
    }

    /**
     * Show the current directory of a device or a DIDL object
     *
     * @param myObjects The list of items int the directory
     */
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

    /**
     * If the back key is pressed, back track in the file tree
     */
    @Override
    public void onBackPressed(){
        if(((Browser)fragment).isRoot()){
            super.onBackPressed();
        } else {
            ((Browser)fragment).goBack();
        }
    }

    /**
     * Reset the browser to refresh the display of the devices
     */
    @Override
    public void showDevices(){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().remove(fragment).commit();
        fragment = new Browser();
        fragmentManager.beginTransaction().add(fragment, "browser").commit();
        ((MyAdapter) myObjectArrayAdapter).setBrowser((Browser)fragment);
    }

    /**
     * Refresh the view if the user has swipe down
     */
    @Override
    public void onRefresh() {
        ((Browser) fragment).refresh();
        swipeLayout.setRefreshing(false);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(this);
    }

    /**
     * Find the urls of all the ressources found in the current folder
     *
     * @return  A array with all the urls
     */
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

    /**
     * Get the position on the list of a given image url
     *
     * @param list The list to search inside
     * @param s     The url to find
     * @return      The position
     */
    @Override
    public int getPositionUrl(ArrayList<String> list, String s){
        int index = 0;
        for(String s2 : list){
            if(s.equals(s2)){
                return index;
            }
            index++;
        }
        return index;
    }
}
