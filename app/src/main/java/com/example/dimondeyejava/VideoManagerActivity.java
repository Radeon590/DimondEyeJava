package com.example.dimondeyejava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;

public class VideoManagerActivity extends AppCompatActivity {

    private static final int SELECT_VIDEO = 1;
    private static ArrayList<Video> videoArrayList = new ArrayList<Video>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_manager);
        downloadVideosList();
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
            addVideo(getPath(imageReturnedIntent.getData()));
        }
    }

    private void addVideo(String videoPath){
        StringBuilder stringBuilder = new StringBuilder("");
        for(int i = videoPath.length() - 1; i >= 0; i--){
            if(videoPath.charAt(i) == '/'){
                break;
            }
            stringBuilder.append(videoPath.charAt(i));
        }
        String fileName = stringBuilder.toString();
        addVideoToLayout(fileName, getResources().getString(R.string.loading));
    }

    private void addVideoToLayout(String fileName, String status){
        TextView nameText = new TextView(this);
        nameText.setText(fileName);
        nameText.setGravity(Gravity.CENTER);
        nameText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        nameText.setTextColor(getResources().getColor(R.color.black, null));
        nameText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);
        nameText.setTypeface(ResourcesCompat.getFont(this, R.font.mts_sans_bold));
        ConstraintLayout.LayoutParams nameTextParams
                = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                75);
        nameTextParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        nameText.setLayoutParams(nameTextParams);
        //
        TextView statusText = new TextView(this);
        statusText.setText(status);
        statusText.setGravity(Gravity.CENTER);
        statusText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        statusText.setTextColor(getResources().getColor(R.color.black, null));
        statusText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);
        statusText.setTypeface(ResourcesCompat.getFont(this, R.font.mts_sans_bold));
        ConstraintLayout.LayoutParams statusTextParams
                = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                75);
        statusTextParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        statusText.setLayoutParams(statusTextParams);
        //
        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        ConstraintLayout.LayoutParams constraintLayoutParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT, 150);
        constraintLayout.setLayoutParams(constraintLayoutParams);
        constraintLayout.addView(nameText);
        constraintLayout.addView(statusText);
        //
        View lineView = new View(this);
        lineView.setBackgroundColor(getResources().getColor(R.color.dark_red, null));
        ConstraintLayout.LayoutParams lineViewParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                5
        );
        //
        LinearLayout linearLayout = findViewById(R.id.videos_list);
        linearLayout.addView(constraintLayout);
        linearLayout.addView(lineView);
    }

    private void uploadVideoToServer(){

    }

    private void downloadVideosList(){

    }

    public String getPath(Uri uri) {

        String path = null;
        String[] projection = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if(cursor == null){
            path = uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }

        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }
}