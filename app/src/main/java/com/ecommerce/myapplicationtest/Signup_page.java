package com.ecommerce.myapplicationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Signup_page extends AppCompatActivity {
    Button back,sign_up;
    EditText name,email,phone,pass,con_pass;
    DatabaseReference databaseReference;
    Dialog dialog;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        back=findViewById(R.id.back_signup);
        sign_up=findViewById(R.id.signup_id);
        name=findViewById(R.id.edit_name_signup);
        email=findViewById(R.id.edit_email_signup);
        phone=findViewById(R.id.edit_phone_signup);
        pass=findViewById(R.id.edit_pass_signup);
        con_pass=findViewById(R.id.edit_con_pass_signup);

        back.setTranslationY(300);
        name.setTranslationY(300);
        email.setTranslationY(300);
        phone.setTranslationY(300);
        pass.setTranslationY(300);
        con_pass.setTranslationY(300);
        sign_up.setTranslationY(300);

        back.setAlpha(0);
        name.setAlpha(0);
        email.setAlpha(0);
        phone.setAlpha(0);
        pass.setAlpha(0);
        con_pass.setAlpha(0);
        sign_up.setAlpha(0);

        back.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        name.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        email.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(700).start();
        phone.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        pass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();
        con_pass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1100).start();
        sign_up.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1200).start();

        databaseReference= FirebaseDatabase.getInstance().getReference("users");

       String value=getIntent().getStringExtra("val1");
       int auto_id=Integer.parseInt(getIntent().getStringExtra("id"));
       Toast.makeText(this, ""+auto_id, Toast.LENGTH_SHORT).show();
       if(value.equals("1")){
           name.setHint("Enter Restaurant Name");
           category="Chef";
       }
       else if(value.equals("2")){
            category="Customer";
        }
        else if(value.equals("3")){
            category="Delivery Person";
        }
       else{
           name.setHint("Enter Name");
       }

       //dialog code

        dialog=new Dialog(this);


        //end dialog code
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(Signup_page.this,MainActivity.class);
                startActivity(it);
                finish();
            }
        });

        //sign up start
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n=name.getText().toString().trim();
                String e=email.getText().toString().trim();
                String p=phone.getText().toString().trim();
                String pa=pass.getText().toString().trim();
                String cpa=con_pass.getText().toString().trim();

                if(n.isEmpty()){
                    name.setError("Name is Empty!");
                    name.requestFocus();
                }
                else if(e.isEmpty()){
                    email.setError("Email Address is Empty!");
                    email.requestFocus();
                }
                else if(!e.contains("@gmail.com")){
                    email.setError("Invalid Email Address!");
                    email.requestFocus();
                }
                else if(p.isEmpty()){
                    phone.setError("Phone Number is Empty!");
                    phone.requestFocus();
                }
                else if(p.length()!=11 ){
                    phone.setError("Invalid Phone Number!");
                    phone.requestFocus();
                }
                else if(pa.isEmpty()){
                    pass.setError("Password is Empty!");
                    pass.requestFocus();
                }
                else if(cpa.isEmpty()){
                    con_pass.setError("Confirm Password is Empty!");
                    con_pass.requestFocus();
                }
                else if(pa.length()<6 || pa.length()>12){
                    pass.setError("Password must be 6-12 characters long");
                    pass.requestFocus();
                }
                else if(cpa.length()<6 || cpa.length()>12){
                    con_pass.setError("Password must be 6-12 characters long");
                    con_pass.requestFocus();
                }
                else if(!pa.equals(cpa)){
                    pass.setError("The password doesn't match.!");
                    con_pass.setError("The password doesn't match!");
                }
                else{
                    Query query=databaseReference.child(category).orderByChild("email").equalTo(e);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Toast.makeText(Signup_page.this, "You used a duplicate email address.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String key=databaseReference.push().getKey();
                                User user=new User(n,e,p,pa,category,"","");
                                databaseReference.child(category).child(""+auto_id).setValue(user);
                                Toast.makeText(Signup_page.this, "Sign Up Successfully.", Toast.LENGTH_SHORT).show();
                                name.setText("");
                                email.setText("");
                                phone.setText("");
                                pass.setText("");
                                con_pass.setText("");
                                name.requestFocus();
                                Intent it=new Intent(Signup_page.this,Homepage.class);
                                it.putExtra("val1",""+value);
                                it.putExtra("email",e);
                                startActivity(it);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    void getId_child(){
        Query query = databaseReference.child(category);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String pass_db = "", cat = "";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        pass_db = snapshot1.getKey();
                    }
                    for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                        cat = snapshot2.child("category").getValue(String.class);
                    }

                    Toast.makeText(Signup_page.this, ""+pass_db, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}