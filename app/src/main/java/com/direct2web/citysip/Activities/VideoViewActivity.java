package com.direct2web.citysip.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;

import com.direct2web.citysip.R;
import com.direct2web.citysip.databinding.ActivityVideoViewBinding;

public class VideoViewActivity extends AppCompatActivity {

    ActivityVideoViewBinding binding;
    String url;
    MediaController mediaController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_view);

        if (mediaController == null) {
            // create an object of media controller class
            mediaController = new MediaController(this);
            mediaController.setAnchorView(binding.simpleVideoView);
        }

        binding.simpleVideoView.setMediaController(mediaController);

        url = getIntent().getStringExtra("video");

        if (url != null) {
            Log.e("ifGetVideoUrl : ", "URL : " + url);
            Uri uri = Uri.parse(url);
            binding.simpleVideoView.setVideoURI(uri);
            binding.simpleVideoView.start();
        } else {
            Log.e("elseGetVideoUrl : ", "URL : " + url);
            Toast.makeText(this, "Url Not Found!", Toast.LENGTH_SHORT).show();
        }


        // implement on completion listener on video view
        binding.simpleVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getApplicationContext(), "Thank You...!!!", Toast.LENGTH_LONG).show(); // display a toast when an video is completed
            }
        });
        binding.simpleVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(getApplicationContext(), "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show(); // display a toast when an error is occured while playing an video
                return false;
            }
        });
    }
}