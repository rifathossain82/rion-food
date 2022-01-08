package com.ecommerce.myapplicationtest.chef;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ecommerce.myapplicationtest.R;
import com.squareup.picasso.Picasso;

public class Show_profile_photo extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile_photo);

        imageView=findViewById(R.id.show_profile_photo_id);
        Picasso.get().load(getIntent().getStringExtra("url")).into(imageView);
    }


}