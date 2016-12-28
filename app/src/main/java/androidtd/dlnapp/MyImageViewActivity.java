package androidtd.dlnapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by GroupeProjetDLNApp on 27/12/16.
 */

public class MyImageViewActivity extends Activity {

    ImageView imageView;
    String uri;
    ArrayList<String> imageArray;

    int index;

    // for left / right swipes
    private float x1,x2;
    static final int MIN_DISTANCE = 150;

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

    public void imageLeft(){
        if(index == 0) {
            index = imageArray.size() - 1;
        }else {
            index--;
        }
        String url = imageArray.get(index);
        Picasso.with(this).load(url).into(imageView);
    }

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
