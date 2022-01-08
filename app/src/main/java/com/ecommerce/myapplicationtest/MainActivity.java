package com.ecommerce.myapplicationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN=0;
    int p=0;
    GoogleApiClient googleApiClient;
    TextView textView,refresh;
    private LoginButton loginButton;
    CallbackManager callbackManager;
    ImageView iv;
    EditText email,pass;
    Button login;
    Dialog dialog;
    TextView forget_pass,register_now,or,tv;
    String category="null",pass_test;
    DatabaseReference databaseReference;
    int value=0,id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInButton=findViewById(R.id.sign_in_button);

        FirebaseMessaging.getInstance().subscribeToTopic("notification");

        email=findViewById(R.id.edit_email_login_id);
        pass=findViewById(R.id.edit_pass_login_id);
        login=findViewById(R.id.login_ok);
        forget_pass=findViewById(R.id.forget_pass_id);
        register_now=findViewById(R.id.signup_go);
        or=findViewById(R.id.or_login_id);
        LinearLayout linearLayout=findViewById(R.id.linearLayout_login);
        refresh=findViewById(R.id.refresh_firstPage);

        email.setTranslationY(300);
        pass.setTranslationY(300);
        login.setTranslationY(300);
        or.setTranslationY(300);
        linearLayout.setTranslationY(300);
        register_now.setTranslationY(300);



        email.setAlpha(0);
        pass.setAlpha(0);
        login.setAlpha(0);
        or.setAlpha(0);
        linearLayout.setAlpha(0);
        register_now.setAlpha(0);

        email.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        pass.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        login.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(700).start();
        or.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
        linearLayout.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(900).start();
        register_now.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(1000).start();

        //dialog code

        dialog=new Dialog(this);
        openFirstDialog();
        //end dialog code

        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        //facebook login
        //textView=findViewById(R.id.text_id);
        loginButton=findViewById(R.id.login_button);
       // imageView=findViewById(R.id.imageview_id1);
        callbackManager = CallbackManager.Factory.create();



        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        check_login_status();
        // If you are using in a fragment, call loginButton.setFragment(this);

        if(value==0){
            openFirstDialog();
        }
        else {
            // Callback registration
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    //app code
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException error) {

                }
            });
        }
        //end facebook login

        //start g-mail sign in
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (value == 0) {
                    openFirstDialog();
                } else {
                    switch (view.getId()) {
                        case R.id.sign_in_button:
                            signIn();
                            break;
                    }
                }
            }
        });




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        register_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(value==0){
                    openFirstDialog();
                }
                else {

                    //start auto id process
                    int ii;
                    Query query = databaseReference.child(category);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String pass_db = "", cat = "";
                                int x=0;
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    pass_db = snapshot1.getKey();
                                    id = Integer.parseInt(snapshot1.getKey());
                                }
                                id=id+1;
                                Intent it = new Intent(MainActivity.this, Signup_page.class);
                                it.putExtra("val1", "" + value);
                                it.putExtra("id", "" + id);
                                startActivity(it);
                                finish();
                            }
                            else{
                                Intent it = new Intent(MainActivity.this, Signup_page.class);
                                it.putExtra("val1", "" + value);
                                it.putExtra("id", "" + id);
                                startActivity(it);
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    //end auto id process
                   // id=id+1;

                }
            }
        });

        //refresh this page
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //login start here
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (value == 0) {
                    openFirstDialog();
                } else {
                    String e = email.getText().toString().trim();
                    String pa = pass.getText().toString().trim();
                    if (e.isEmpty()) {
                        email.setError("Email Address is Empty!");
                        email.requestFocus();
                    } else if (!e.contains("@gmail.com")) {
                        email.setError("Invalid Email Address!");
                        email.requestFocus();
                    } else if (pa.isEmpty()) {
                        pass.setError("Password is Empty!");
                        pass.requestFocus();
                    } else if (pa.length() < 6 || pa.length() > 12) {
                        pass.setError("Password must be 6-12 characters long");
                        pass.requestFocus();
                    }
                   /* else if(e.equals("ra@gmail.com") && pa.equals("123123")){
                        Intent it = new Intent(MainActivity.this, Homepage.class);
                        it.putExtra("val1", "" + 1);
                        startActivity(it);
                        finish();
                    }*/
                    else {
                        Query query = databaseReference.child(category).orderByChild("email").equalTo(e);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String pass_db = "",cat="",id;
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                        pass_db = snapshot1.child("password").getValue(String.class);
                                    }
                                    for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                                        cat = snapshot2.child("category").getValue(String.class);
                                    }
                                    if (pass_db.equals(pa)) {
                                        if(cat.equals(category)){
                                            Intent it = new Intent(MainActivity.this, Homepage.class);
                                            it.putExtra("val1",""+value);
                                            it.putExtra("email",e);
                                            startActivity(it);
                                            finish(); 
                                        }
                                        else {
                                            Toast.makeText(MainActivity.this, "Category isn't matching. For change category, Rerun the apps.", Toast.LENGTH_SHORT).show();
                                        }
                                        

                                    } else {
                                        pass.setError("Wrong Password");
                                        pass.requestFocus();
                                    }
                                } else {
                                    email.setError("No such user exits.");
                                    email.requestFocus();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }


        });

    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

            Query query = databaseReference.child(category);
            query.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String pass_db = "", cat = "";
                        int x=0;
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            pass_db = snapshot1.getKey();
                            id = Integer.parseInt(snapshot1.getKey());
                        }
                        id=id+1;
                        GoogleSignInAccount acct = null;
                        try {
                            acct = completedTask.getResult(ApiException.class);
                            if(acct!=null) {
                                String personName = acct.getDisplayName();
                                String personEmail = acct.getEmail();
                                String personId = acct.getId();
                                String key=databaseReference.push().getKey();
                                User user=new User(personName,personEmail,"",personId,category,"","");
                                databaseReference.child(category).child(""+id).setValue(user);
                                Intent it=new Intent(MainActivity.this,Homepage.class);
                                it.putExtra("val1", "" + value);
                                it.putExtra("email",personEmail);
                                it.putExtra("id",id);
                                startActivity(it);
                                finish();
                            }
                        } catch (ApiException e) {
                            e.printStackTrace();
                        }

                    }
                    else{
                        GoogleSignInAccount acct = null;
                        try {
                            acct = completedTask.getResult(ApiException.class);
                            if(acct!=null) {
                                String personName = acct.getDisplayName();
                                String personEmail = acct.getEmail();
                                String personId = acct.getId();
                                String key=databaseReference.push().getKey();
                                User user=new User(personName,personEmail,"",personId,category,"","");
                                databaseReference.child(category).child(""+id).setValue(user);
                                Intent it=new Intent(MainActivity.this,Homepage.class);
                                it.putExtra("val1", "" + value);
                                it.putExtra("email",personEmail);
                                it.putExtra("id",id);
                                startActivity(it);
                                finish();
                            }
                        } catch (ApiException e) {
                            e.printStackTrace();
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    //facebook login

    AccessTokenTracker tt=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
               // textView.setText("");
                Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_SHORT).show();
            }
            else{
                load_userProfile(currentAccessToken);
            }
        }
    };
    private void load_userProfile(AccessToken accessToken){
        GraphRequest graphRequest=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if(object!=null){

                        //query for find auto id
                        Query query = databaseReference.child(category);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String pass_db = "", cat = "";
                                    int auto_id=0;
                                    int x=0;
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                        pass_db = snapshot1.getKey();
                                        auto_id = Integer.parseInt(snapshot1.getKey());
                                    }
                                    auto_id=auto_id+1;
                                    String name= null;
                                    try {
                                        name = object.getString("first_name")+" "+object.getString("last_name");
                                        String email=object.getString("email");
                                        String id=object.getString("id");
                                        String imageurl="https://graph.facebook.com/" + id + "/picture?type=large";
                                        //Picasso.get().load("https://graph.facebook.com/" + id + "/picture?type=large").into(imageView);
                                        // textView.setText("Name: "+name+"\n"+"Email: "+email+"\nId: "+id);
                                        if(p==0){
                                            String key=databaseReference.push().getKey();
                                            User user=new User(name,email,"",id,category,"","");
                                            databaseReference.child(category).child(""+auto_id).setValue(user);
                                            Toast.makeText(MainActivity.this, "Sign Up Successfully.", Toast.LENGTH_SHORT).show();
                                            Intent it=new Intent(MainActivity.this,Homepage.class);
                                            it.putExtra("val1", "" + value);
                                            it.putExtra("email",email);
                                            it.putExtra("id",auto_id);
                                            startActivity(it);
                                            finish();
                                        }
                                        else{
                                            Intent it=new Intent(MainActivity.this,Homepage.class);
                                            it.putExtra("val1", "" + value);
                                            it.putExtra("email",email);
                                            it.putExtra("id",auto_id);
                                            startActivity(it);
                                            finish();
                                        }
                                        p=0;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                else{
                                    String name= null;
                                    try {
                                        name = object.getString("first_name")+" "+object.getString("last_name");
                                        String email=object.getString("email");
                                        String id_=object.getString("id");
                                        String imageurl="https://graph.facebook.com/" + id_ + "/picture?type=large";
                                        //Picasso.get().load("https://graph.facebook.com/" + id + "/picture?type=large").into(imageView);
                                        // textView.setText("Name: "+name+"\n"+"Email: "+email+"\nId: "+id);
                                        if(p==0){
                                            String key=databaseReference.push().getKey();
                                            User user=new User(name,email,"",id_,category,"","");
                                            databaseReference.child(category).child(""+id).setValue(user);
                                            Toast.makeText(MainActivity.this, "Sign Up Successfully.", Toast.LENGTH_SHORT).show();
                                            Intent it=new Intent(MainActivity.this,Homepage.class);
                                            it.putExtra("val1", "" + value);
                                            it.putExtra("email",email);
                                            it.putExtra("id",id);
                                            startActivity(it);
                                            finish();
                                        }
                                        else{
                                            Intent it=new Intent(MainActivity.this,Homepage.class);
                                            it.putExtra("val1", "" + value);
                                            it.putExtra("email",email);
                                            it.putExtra("id",id);
                                            startActivity(it);
                                            finish();
                                        }
                                        p=0;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                }
                else {
                    Toast.makeText(MainActivity.this, "Null Object.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Bundle bundle=new Bundle();
        bundle.putString("fields","first_name,last_name,email,id");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }
    private void check_login_status(){
        if(AccessToken.getCurrentAccessToken()!=null){
            load_userProfile(AccessToken.getCurrentAccessToken());
            p=1;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            Intent it=new Intent(MainActivity.this,Homepage.class);
            it.putExtra("val1", "" + value);
            it.putExtra("email",acct.getEmail());
            startActivity(it);
            finish();
        }
    }
    private void openFirstDialog(){
        dialog.setContentView(R.layout.open_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        Button chef,customer,delivery_boy;
        chef=dialog.findViewById(R.id.button_dialog_1);
        customer=dialog.findViewById(R.id.button_2_dialog);
        delivery_boy=dialog.findViewById(R.id.button_3_dialog);

        chef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value=1;
                category="Chef";
                dialog.dismiss();
            }
        });
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value=2;
                category="Customer";
                dialog.dismiss();
            }
        });
        delivery_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value=3;
                category="Delivery Person";
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    void getId_child(){
        //to get auto id
        Query query = databaseReference.child(category);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String pass_db = "", cat = "";
                    int x=0;
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        pass_db = snapshot1.getKey();
                        id = Integer.parseInt(snapshot1.getKey());
                    }
                    id=id+1;
                    Toast.makeText(MainActivity.this, ""+id, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//end auto id process
    }
}