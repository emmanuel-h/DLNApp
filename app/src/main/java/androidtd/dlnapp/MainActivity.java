package androidtd.dlnapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Notification {


    ListView listView;
    Fragment fragment;
    ArrayAdapter<MyObject> myObjectArrayAdapter;
    ArrayList<MyObject> myObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.liste);
        myObjects = new ArrayList<>();
        myObjectArrayAdapter = new MyAdapter(this,myObjects);
        listView.setAdapter(myObjectArrayAdapter);
        //MyObject bidon = new MyObject(R.drawable.ic_device,"titre 1","description 1");
        //MyObject bidon2 = new MyObject(R.drawable.ic_device,"titre 2","description 2");
        //myObjectArrayAdapter.add(bidon);
        //myObjectArrayAdapter.add(bidon2);
        System.out.println("bite main");
        FragmentManager fragmentManager = getFragmentManager();
        fragment = fragmentManager.findFragmentByTag("browser");

        if(fragment == null){
            fragment = new Browser();
            fragmentManager.beginTransaction().add(fragment, "browser").commit();
        }

        System.out.println("context main : " + this.toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh:
                /*MyObject bidon = new MyObject(R.drawable.ic_device,"titre 3","description 3");
                myObjectArrayAdapter.add(bidon);
                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }

    public void deviceAdded(final Device device) {
        runOnUiThread(new Runnable() {
            public void run() {
                MyObjectDevice myObjectDevice = new MyObjectDevice(R.drawable.ic_device, device);
                Service contentDirectory = myObjectDevice.getContentDirectory();
                // The device is display only if he has contentDirectory
                if(contentDirectory != null) {
                    int position = myObjectArrayAdapter.getPosition(myObjectDevice);
                    if (position >= 0) {
                        // Device already in the list, re-set new value at same position
                        myObjectArrayAdapter.remove(myObjectDevice);
                        myObjectArrayAdapter.insert(myObjectDevice, position);
                    } else {
                        myObjectArrayAdapter.add(myObjectDevice);
                    }
                }
            }
        });
    }

    public void deviceRemoved(final Device device) {
        runOnUiThread(new Runnable() {
            public void run() {
                myObjectArrayAdapter.remove(new MyObjectDevice(R.drawable.ic_device, device));
            }
        });
    }

    public void clear(){
        myObjectArrayAdapter.clear();
    }
}
