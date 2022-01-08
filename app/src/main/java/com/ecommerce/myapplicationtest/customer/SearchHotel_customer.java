package com.ecommerce.myapplicationtest.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.myapplicationtest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchHotel_customer extends AppCompatActivity implements Hotel_interface{
    TextView back;
    EditText search;
    RecyclerView recyclerView;

    All_Hotel_Adapter all_hotel_adapter;
    DatabaseReference databaseReference;

    List<String> h_name;
    List<String> h_photo;
    List<String> h_email;
    List<String> h_id;
    String customer_id="",customer_email="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hotel_customer);

        //initial widget
        back=findViewById(R.id.back_search_hotel);
        search=findViewById(R.id.edit_search_hotel);
        recyclerView=findViewById(R.id.recycler_search_hotel);

        //initial reference
        databaseReference= FirebaseDatabase.getInstance().getReference("users");

        //receive customer email and id
        customer_email= getIntent().getStringExtra("email");
        customer_id=getIntent().getStringExtra("id");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    private void filter(String text) {
            //initial list
            h_name = new ArrayList<>();
            h_photo = new ArrayList<>();
            h_email = new ArrayList<>();
            h_id = new ArrayList<>();

            String sst = text.toUpperCase();
            //find all restaurant
            Query query = databaseReference.child("Chef").orderByChild("email");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = "", photo = "";
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            name = snapshot1.child("name").getValue(String.class);
                            if (name.contains(text) || name.contains(sst)) {
                                h_name.add(snapshot1.child("name").getValue(String.class));
                                h_photo.add(snapshot1.child("photo").getValue(String.class));
                                h_email.add(snapshot1.child("email").getValue(String.class));
                                h_id.add(snapshot1.getKey());
                            }

                        }

                        //set all restaurant
                        all_hotel_adapter = new All_Hotel_Adapter(SearchHotel_customer.this, h_name, h_photo, h_email, h_id, SearchHotel_customer.this::Go_activity);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(SearchHotel_customer.this, 1, GridLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(all_hotel_adapter);

                    } else {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    @Override
    public void Go_activity(int i, String email, String id) {
        Intent it=new Intent(SearchHotel_customer.this,Hotel_activity.class);
        it.putExtra("email",email);
        it.putExtra("id",id);
        it.putExtra("c_email",customer_email);
        it.putExtra("c_id",customer_id);
        startActivity(it);
    }
}