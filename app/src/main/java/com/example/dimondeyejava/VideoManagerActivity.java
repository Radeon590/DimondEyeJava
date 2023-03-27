package com.example.dimondeyejava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class VideoManagerActivity extends AppCompatActivity {
    private static final int SELECT_VIDEO = 1;
    private static ArrayList<Video> videoArrayList = new ArrayList<Video>();

    private static String serverUrlPath = "";
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
            addVideoFromFiles(imageReturnedIntent.getData());
        }
    }
    private void addVideoFromFiles(Uri videoUri){
        String videoPath = getPath(videoUri);
        StringBuilder stringBuilder = new StringBuilder("");
        for(int i = videoPath.length() - 1; i >= 0; i--){
            if(videoPath.charAt(i) == '/'){
                break;
            }
            stringBuilder.append(videoPath.charAt(i));
        }
        String fileName = stringBuilder.toString();
        //
        videoArrayList.add(new Video(fileName, "Загружается", addVideoToLayout(fileName, getResources().getString(R.string.loading))));
        uploadVideoToServer(videoUri, videoArrayList.size() - 1);
    }

    private int addVideoToLayout(String fileName, String status){
        TextView nameText = new TextView(this);
        nameText.setText(fileName);
        nameText.setGravity(Gravity.CENTER);
        nameText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        nameText.setTextColor(getResources().getColor(R.color.black, null));
        nameText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);
        nameText.setTypeface(ResourcesCompat.getFont(this, R.font.mts_sans_bold));
        ConstraintLayout.LayoutParams nameTextParams
                = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, getResources().getDisplayMetrics()));
        nameTextParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        int nameTextId = View.generateViewId();
        nameText.setId(nameTextId);
        nameText.setLayoutParams(nameTextParams);
        //
        TextView statusText = new TextView(this);
        statusText.setText(status);
        statusText.setGravity(Gravity.CENTER);
        statusText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        statusText.setTextColor(getResources().getColor(R.color.red, null));
        statusText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 32);
        statusText.setTypeface(ResourcesCompat.getFont(this, R.font.mts_sans_bold));
        ConstraintLayout.LayoutParams statusTextParams
                = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, getResources().getDisplayMetrics()));
        statusTextParams.topToBottom = nameTextId;
        int statusTextId = View.generateViewId();
        statusText.setId(statusTextId);
        statusText.setLayoutParams(statusTextParams);
        //
        ConstraintLayout constraintLayout = new ConstraintLayout(this);
        LinearLayout.LayoutParams constraintLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics()));
        constraintLayout.setLayoutParams(constraintLayoutParams);
        constraintLayout.addView(nameText);
        constraintLayout.addView(statusText);
        int constraintLayoutId = View.generateViewId();
        constraintLayout.setId(constraintLayoutId);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(view);
            }
        });
        //
        View lineView = new View(this);
        lineView.setBackgroundColor(getResources().getColor(R.color.dark_red, null));
        LinearLayout.LayoutParams lineViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics())
        );
        lineView.setLayoutParams(lineViewParams);
        //
        LinearLayout linearLayout = findViewById(R.id.videos_list);
        linearLayout.addView(constraintLayout);
        linearLayout.addView(lineView);
        return constraintLayoutId;
    }

    public void playVideo(View view){
        Video video = null;
        boolean found = false;
        for(int i = 0; i < videoArrayList.size(); i++){
            video = videoArrayList.get(i);
            if(video.layoutId == view.getId()){
                found = true;
                break;
            }
        }
        if(found){
            DrawerLayout drawerLayout = findViewById(R.id.videos_drawer);
            drawerLayout.open();
            Button backButton = findViewById(R.id.back_button);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    backFromVideoDrawer(view);
                }
            });
            VideoView videoView = findViewById(R.id.video_view);
            videoView.setVideoPath((new StringBuilder(serverUrlPath)).append("/video/").append(video.name).toString());
            videoView.start();
        }
    }

    public void backFromVideoDrawer(View view){
        VideoView videoView = findViewById(R.id.video_view);
        videoView.stopPlayback();
        videoView.resume();
        DrawerLayout drawerLayout = findViewById(R.id.videos_drawer);
        drawerLayout.close();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backFromVideoPage(view);
            }
        });
    }

    public void backFromVideoPage(View view){

    }
    private void uploadVideoToServer(Uri videoUri, int videoIndex){
        try{
            URL url = new URL(serverUrlPath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //

            //
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    /*public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }*/
}