package androidtd.dlnapp;
import android.app.Activity;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

/**
 * Created by GroupeProjetDLNApp on 26/12/2016.
 */

public class MyVideoMusicViewActivity extends Activity{

    VideoView videoView;

    String videoUrl;
    String type;

    boolean landscape = false;

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
                videoView.setBackgroundResource(R.drawable.ic_device);
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
            landscape = true;

        }else if(config.orientation == Configuration.ORIENTATION_PORTRAIT){
            videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            landscape = false;

        }
    }
}