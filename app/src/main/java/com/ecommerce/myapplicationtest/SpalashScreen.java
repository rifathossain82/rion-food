package com.ecommerce.myapplicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class SpalashScreen extends AppCompatActivity {
    public static int splash_screen=5000;
    Animation top_animation,bottom_animation;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_spalash_screen);
        bottom_animation= AnimationUtils.loadAnimation(this,R.anim.name_spalash);

        name=findViewById(R.id.name_spalash);
        name.setAnimation(bottom_animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SpalashScreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },splash_screen);
    }

}