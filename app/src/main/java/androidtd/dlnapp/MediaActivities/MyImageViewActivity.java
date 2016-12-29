package androidtd.dlnapp.MediaActivities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidtd.dlnapp.R;

/**
 * Display a custom view of an image file, which allow to swipe left and right to navigate between images in the folder
 *
 * Created by GroupeProjetDLNApp on 27/12/16.
 */
public class MyImageViewActivity extends Activity {

    /**
     * ImageView in the xml file which contain the current image displayed
     */
    ImageView imageView;

    /**
     * Uri of the image displayed
     */
    String uri;

    /**
     * Array of list allowing to swipe left and right
     */
    ArrayList<String> imageArray;

    /**
     * Image's position in the imageArray
     */
    int index;

    /**
     * Used to know if user has swipe left or right
     */
    private float x1,x2;

    /**
     * Minimal distance for an user interaction to be considered as a swipe
     */
    static final int MIN_DISTANCE = 150;

    /**
     * Set the content view and display the current image
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        uri = getIntent().getStringExtra("uri");
        imageArray = getIntent().getStringArrayListExtra("array");
        getIntent().getParcelableArrayListExtra("array");

        setContentView(R.layout.image_view_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this).load(uri).into(imageView);

        index = getIntent().getIntExtra("index", 0);

    }

    /**
     * Catch an user interaction
     *
     * @param event The interaction
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE){
                    if(x2 > x1){ // left swipe
                        imageLeft();
                    }else{ // right swipe
                        imageRight();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Find the image on the left of the current one
     */
    public void imageLeft(){
        if(index == 0) {
            index = imageArray.size() - 1;
        }else {
            index--;
        }
        String url = imageArray.get(index);
        Picasso.with(this).load(url).into(imageView);
    }

    /**
     * Find the image on the right of the current one
     */
    public void imageRight(){
        if(index == (imageArray.size() - 1)){
            index = 0;
        }else{
            index++;
        }
        String url = imageArray.get(index);
        Picasso.with(this).load(url).into(imageView);
    }
}
