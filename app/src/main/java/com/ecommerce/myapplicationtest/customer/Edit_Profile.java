package com.ecommerce.myapplicationtest.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ecommerce.myapplicationtest.R;
import com.ecommerce.myapplicationtest.chef.Edit_Profile_name;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Edit_Profile extends AppCompatActivity {
    TextInputEditText name,phone;
    Button save;

    String auto_id="";
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //initial used widget
        name=findViewById(R.id.edit_cus_name_id);
        phone=findViewById(R.id.edit_cus_phone_id);
        save=findViewById(R.id.save_edit_cus_id);

        //initial database reference
        databaseReference= FirebaseDatabase.getInstance().getReference("users");


        //set data in widget primary
        String phone_=getIntent().getStringExtra("phone");
        String name_=getIntent().getStringExtra("user");
        auto_id=getIntent().getStringExtra("id");
        String category=getIntent().getStringExtra("cat");
        String email_=getIntent().getStringExtra("email");;

        name.setText(""+name_);
        phone.setText(phone_);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_name=name.getText().toString();
                String pn=phone.getText().toString();

                if(first_name.isEmpty()){
                    name.setError("How should we address you?");
                    name.requestFocus();
                }
                else if(pn.isEmpty()){
                    phone.setError("How should we contact you?");
                    phone.requestFocus();
                }
                else {
                    String user_name = getIntent().getStringExtra("user");
                    String cat = getIntent().getStringExtra("cat");

                    HashMap hashMap = new HashMap();
                    hashMap.put("name", first_name);
                    hashMap.put("phone", pn);

                    databaseReference.child(cat).child(auto_id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(Edit_Profile.this, "Save Success", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    });
                }
            }
        });



    }
}