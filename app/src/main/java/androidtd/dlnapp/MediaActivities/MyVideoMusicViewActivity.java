package androidtd.dlnapp.MediaActivities;

import android.app.Activity;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidtd.dlnapp.R;

/**
 * Display a custom view for music or video media
 *
 * Created by GroupeProjetDLNApp on 26/12/2016.
 */
public class MyVideoMusicViewActivity extends Activity{

    /**
     * VideoView in the xml file which contain the current media displayed
     */
    VideoView videoView;

    /**
     * The url of the media displayed
     */
    String videoUrl;

    /**
     * Type of the media : audio or video
     */
    String type;

    /**
     * Orientation of the mobile phone
     */
    boolean landscape = false;

    /**
     * Set the videoview with the media controller
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        videoUrl = getIntent().getStringExtra("uri");
        type = getIntent().getStringExtra("type");

        setContentView(R.layout.video_view_main);

        videoView = (VideoView) findViewById(R.id.videoView);

        try{
            MediaController mediaController = new MediaController(MyVideoMusicViewActivity.this);
            mediaController.setAnchorView(videoView);
            Uri videoUri = Uri.parse(videoUrl);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(videoUri);
            if(type.equals("audio")) {
                videoView.setBackgroundResource(R.drawable.music_background);
            }
        }catch(Exception e){

        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
    }

    /**
     * Display some features when the user press on the screen
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && landscape) {
            videoView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }else if(hasFocus && !landscape){
            videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    /**
     * Manage the change in the mobile phone's orientation
     *
     * @param config
     */
    @Override
    public void onConfigurationChanged(Configuration config){
        super.onConfigurationChanged(config);

        if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
            if(type.equals("audio")){
                videoView.setBackgroundResource(0);
                videoView.setBackgroundResource(R.drawable.music_background);
            }
            landscape = true;

        }else if(config.orientation == Configuration.ORIENTATION_PORTRAIT){
            videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            if(type.equals("audio")){
                videoView.setBackgroundResource(0);
                videoView.setBackgroundResource(R.drawable.music_background);
            }
            landscape = false;

        }
    }
}