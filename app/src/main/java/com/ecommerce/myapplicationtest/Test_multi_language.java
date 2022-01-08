package com.ecommerce.myapplicationtest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class Test_multi_language extends AppCompatActivity {
Button change;
    private String locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        load_lang();
        setContentView(R.layout.activity_test_multi_language);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));

        change=findViewById(R.id.change_lang);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLanguage_dialog();
            }
        });

    }

    private void showLanguage_dialog() {
        final String[] list_item={"বাংলা","हिन्दी","اردو","English"};
        AlertDialog.Builder builder=new AlertDialog.Builder(Test_multi_language.this);
        builder.setTitle("Choose Language");
        builder.setSingleChoiceItems(list_item, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0){
                    setlocale("bn");
                    recreate();
                }
                else if(i==1){
                    setlocale("hi");
                    recreate();
                }
                else if(i==2){
                    setlocale("ur");
                    recreate();
                }
                else if(i==3){
                    setlocale("en");
                    recreate();
                }
                dialogInterface.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void setlocale(String en) {
        Locale locale=new Locale(en);
        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor=getSharedPreferences("settings",MODE_PRIVATE).edit();
        editor.putString("My_lang",en);
        editor.apply();
    }
    void load_lang(){
        SharedPreferences sharedPreferences=getSharedPreferences("settings", Activity.MODE_PRIVATE);
        String lang=sharedPreferences.getString("My_lang","");
        setlocale(lang);
    }
}