package com.example.dimondeyejava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

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