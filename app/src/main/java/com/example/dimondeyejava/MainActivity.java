package com.example.dimondeyejava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}