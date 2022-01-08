package com.ecommerce.myapplicationtest.chef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ecommerce.myapplicationtest.Homepage;
import com.ecommerce.myapplicationtest.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Edit_Profile_name extends AppCompatActivity {
    TextInputEditText f_name,phone_,address_;
    String auto_id="";
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_name);

        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        f_name=findViewById(R.id.fname_id);
        phone_=findViewById(R.id.phone_id);
        phone_=findViewById(R.id.phone_id);
        address_=findViewById(R.id.address_chef_id);

        String phone=getIntent().getStringExtra("phone");
        String s=getIntent().getStringExtra("user");
        auto_id=getIntent().getStringExtra("id");
        f_name.setText(""+s);
        phone_.setText(""+phone);
        address_.setText(getIntent().getStringExtra("address"));

    }

    public void save_name(View view) {
        String first_name=f_name.getText().toString();
        String pn=phone_.getText().toString();
        String add=address_.getText().toString();

        if(first_name.isEmpty()){
            f_name.setError("How should we address you?");
            f_name.requestFocus();
        }
        else if(pn.isEmpty()){
            phone_.setError("How should we contact you?");
            phone_.requestFocus();
        }
        else if(add.isEmpty()){
            address_.setError("How should we contact you?");
            address_.requestFocus();
        }
        else {
            String name = getIntent().getStringExtra("user");
            String cat = getIntent().getStringExtra("cat");

            HashMap hashMap = new HashMap();
            hashMap.put("name", first_name);
            hashMap.put("phone", pn);
            hashMap.put("address", add);

            databaseReference.child(cat).child(auto_id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Toast.makeText(Edit_Profile_name.this, "Save Success", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            });
        }

    }
}