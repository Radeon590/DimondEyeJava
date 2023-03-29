package com.example.dimondeyejava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setUpVideoPlayer();
    }

    private void setUpVideoPlayer(){
        /*VideoView videoView = findViewById(R.id.example_video_view);
        videoView.setVideoURI(Uri.parse("https://www.youtube.com/watch?v=q1NjNA1GWBs&t=1s"));
        //videoView.setVideoPath("https://www.youtube.com/watch?v=q1NjNA1GWBs&t=1s");
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setMediaPlayer(videoView);*/
    }
    public void switchMainLayoutDrawer(View view){
        DrawerLayout drawerLayout = findViewById(R.id.main_layout_drawer);
        if(!drawerLayout.isOpen()){
            drawerLayout.open();
        }
        else{
            drawerLayout.close();
        }
    }

    public void goToVideoManager(View view){
        Intent videoManagerIntent = new Intent(this, VideoManagerActivity.class);
        startActivity(videoManagerIntent);
    }
}