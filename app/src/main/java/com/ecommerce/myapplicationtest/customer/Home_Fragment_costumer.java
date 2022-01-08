package com.ecommerce.myapplicationtest.customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.myapplicationtest.R;
import com.ecommerce.myapplicationtest.chef.Chef_profile;
import com.ecommerce.myapplicationtest.chef.MainAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Home_Fragment_costumer extends Fragment implements Hotel_interface{
    TextView name_show,eat_time;
    EditText search_food;
    RecyclerView recyclerView1,recyclerView2;

    Offer_Adapter offer_adapter;
    All_Hotel_Adapter all_hotel_adapter;
    DatabaseReference databaseReference;


    List<String> h_name;
    List<String> h_photo;
    List<String> h_email;
    List<String> h_id;
    String customer_id="",customer_email="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home__costumer, container, false);

        //initial wedget
        name_show=view.findViewById(R.id.show_name_homeID);
        eat_time=view.findViewById(R.id.show_eatTime_id);

        search_food=view.findViewById(R.id.search_homeID);

        recyclerView1=view.findViewById(R.id.recyclerview_offer_id);
        recyclerView2=view.findViewById(R.id.recyclerview_item_id);

        //initial reference
        databaseReference= FirebaseDatabase.getInstance().getReference("users");

        //initial list
        h_name=new ArrayList<>();
        h_photo=new ArrayList<>();
        h_email=new ArrayList<>();
        h_id=new ArrayList<>();

        //receive customer email and id
        customer_email=getArguments().getString("email");
        customer_id=getArguments().getString("id");

        //search
        search_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getContext(),SearchHotel_customer.class);
                it.putExtra("c_email",customer_email);
                it.putExtra("c_id",customer_id);
                startActivity(it);
            }
        });

        //find all restaurant
        Query query = databaseReference.child("Chef").orderByChild("email");
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "",photo="";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        h_name.add(snapshot1.child("name").getValue(String.class));
                        h_photo.add(snapshot1.child("photo").getValue(String.class));
                        h_email.add(snapshot1.child("email").getValue(String.class));
                        h_id.add(snapshot1.getKey());
                    }

                    //set all restaurant
                    all_hotel_adapter=new All_Hotel_Adapter(getContext(),h_name,h_photo,h_email,h_id,Home_Fragment_costumer.this::Go_activity);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
                    recyclerView2.setLayoutManager(gridLayoutManager);
                    recyclerView2.setHasFixedSize(true);
                    recyclerView2.setAdapter(all_hotel_adapter);

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //end here to finding all restaurant





        //set offer adapter
        offer_adapter=new Offer_Adapter(getContext());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1,GridLayoutManager.HORIZONTAL,false);
        recyclerView1.setLayoutManager(gridLayoutManager);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setAdapter(offer_adapter);


        return view;
    }

    @Override
    public void Go_activity(int i,String email, String id) {
        Intent it=new Intent(getContext(),Hotel_activity.class);
        it.putExtra("email",email);
        it.putExtra("id",id);
        it.putExtra("c_email",customer_email);
        it.putExtra("c_id",customer_id);
        startActivity(it);
    }
}