package androidtd.dlnapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by benoi on 26/12/2016.
 */

public class VideoViewActivity extends Activity{

    VideoView videoView;

    String videoUrl;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        videoUrl = getIntent().getStringExtra("uri");
        type = getIntent().getStringExtra("type");

        setContentView(R.layout.video_view_main);

        videoView = (VideoView) findViewById(R.id.videoView);

        try{
            MediaController mediaController = new MediaController(VideoViewActivity.this);
            mediaController.setAnchorView(videoView);
            Uri videoUri = Uri.parse(videoUrl);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(videoUri);
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
}
