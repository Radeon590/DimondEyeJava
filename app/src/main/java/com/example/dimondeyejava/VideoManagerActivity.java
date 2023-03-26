package com.example.dimondeyejava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.net.URI;

public class VideoManagerActivity extends AppCompatActivity {
    private static final int SELECT_VIDEO = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_manager);
    }

    public void showFiles(View view){
        Intent fileSearcherIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileSearcherIntent.setType("video/*");
        startActivityForResult(fileSearcherIntent, SELECT_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        //
        if(requestCode == SELECT_VIDEO && resultCode == RESULT_OK){
            Uri fileURI = imageReturnedIntent.getData();
            Button button = findViewById(R.id.back_button);
            button.setText(fileURI.getPath());
        }
    }
}