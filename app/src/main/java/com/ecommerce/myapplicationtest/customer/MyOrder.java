package com.ecommerce.myapplicationtest.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ecommerce.myapplicationtest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.v1.StructuredQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyOrder extends AppCompatActivity implements ShowOrder_interface{
    String user_id,user_email,user_address,user_cat;
    RecyclerView recent,past;
    TextView back;

     List<String> f_name,f_price,f_quantity,f_details,address,cat,hotel_id,order_id,status,method,hotelName,date;
     List<String> f_name2,f_price2,f_quantity2,f_details2,address2,cat2,hotel_id2,order_id2,status2,method2,hotelName2,date2;
     List<String> foodName,foodPrice,foodQuantity,foodDetails,foodPhoto;
    Order_Adapter order_adapter;
    DatabaseReference databaseReference,databaseReference2,databaseReference3;
    String ssid="";
    List<String> ar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        //receive data from homepage
        user_id=getIntent().getStringExtra("id");
        user_email=getIntent().getStringExtra("email");
        user_address=getIntent().getStringExtra("address");
        user_cat=getIntent().getStringExtra("cat");

        //initial recycler and others
        recent=findViewById(R.id.recent_recycler);
        past=findViewById(R.id.past_recycler);

        back=findViewById(R.id.back_MyOrder);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //initial list
        f_name=new ArrayList<>();
        f_price=new ArrayList<>();
        f_quantity=new ArrayList<>();
        f_details=new ArrayList<>();
        address=new ArrayList<>();
        cat=new ArrayList<>();
        hotel_id=new ArrayList<>();
        order_id=new ArrayList<>();
        status=new ArrayList<>();
        method=new ArrayList<>();
        hotelName=new ArrayList<>();
        date=new ArrayList<>();

        f_name2=new ArrayList<>();
        f_price2=new ArrayList<>();
        f_quantity2=new ArrayList<>();
        f_details2=new ArrayList<>();
        address2=new ArrayList<>();
        cat2=new ArrayList<>();
        hotel_id2=new ArrayList<>();
        order_id2=new ArrayList<>();
        status2=new ArrayList<>();
        method2=new ArrayList<>();
        hotelName2=new ArrayList<>();
        date2=new ArrayList<>();

        ar=new ArrayList<>();

        foodName=new ArrayList<>();
        foodPrice=new ArrayList<>();
        foodDetails=new ArrayList<>();
        foodQuantity=new ArrayList<>();
        foodPhoto=new ArrayList<>();

        //initial database_reference
        databaseReference= FirebaseDatabase.getInstance().getReference("order");
        databaseReference2= FirebaseDatabase.getInstance().getReference("users");
        databaseReference3= FirebaseDatabase.getInstance().getReference("order_item");

        //to find user recent order item start
        Query query2 = databaseReference.child(user_id);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ss = "";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        ss=snapshot1.child("status").getValue(String.class);
                        if(ss.equals("Order") || ss.equals("Cooking") || ss.equals("Packaging") || ss.equals("Prepared") || ss.equals("Pass")){
                            f_name.add(snapshot1.child("f_name").getValue(String.class));
                            f_price.add(snapshot1.child("f_price").getValue(String.class));
                            address.add(snapshot1.child("address").getValue(String.class));
                            cat.add(snapshot1.child("cat").getValue(String.class));
                            hotel_id.add(snapshot1.child("h_id").getValue(String.class));
                            order_id.add(snapshot1.child("id").getValue(String.class));
                            status.add(snapshot1.child("status").getValue(String.class));
                            method.add(snapshot1.child("method").getValue(String.class));
                            hotelName.add(snapshot1.child("h_name").getValue(String.class));
                            date.add(snapshot1.child("date").getValue(String.class));
                        }


                    }
                    order_adapter=new Order_Adapter(f_name,f_price,address,cat,hotel_id,order_id,status,method,hotelName,date,MyOrder.this,MyOrder.this);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(MyOrder.this,1,GridLayoutManager.VERTICAL,false);
                    recent.setLayoutManager(gridLayoutManager);
                    recent.setHasFixedSize(true);
                    recent.setAdapter(order_adapter);


                    //Toast.makeText(MyOrder.this, "Success " +ar.toString(), Toast.LENGTH_SHORT).show();
                    //search_more();

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //to find user recent orders item end


        //to find user past order item start

        Query query4 = databaseReference.child(user_id).orderByChild("status").equalTo("Delivered");
        query4.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "",photo="",id="";
                    int s_price=0;
                    int p=0;
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        f_name2.add(snapshot1.child("f_name").getValue(String.class));
                        f_price2.add(snapshot1.child("f_price").getValue(String.class));
                        address2.add(snapshot1.child("address").getValue(String.class));
                        cat2.add(snapshot1.child("cat").getValue(String.class));
                        hotel_id2.add(snapshot1.child("h_id").getValue(String.class));
                        order_id2.add(snapshot1.child("id").getValue(String.class));
                        status2.add(snapshot1.child("status").getValue(String.class));
                        method2.add(snapshot1.child("method").getValue(String.class));
                        hotelName2.add(snapshot1.child("h_name").getValue(String.class));
                        date2.add(snapshot1.child("date").getValue(String.class));

                    }
                    order_adapter=new Order_Adapter(f_name2,f_price2,address2,cat2,hotel_id2,order_id2,status2,method2,hotelName2,date2,MyOrder.this,MyOrder.this);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(MyOrder.this,1,GridLayoutManager.VERTICAL,false);
                    past.setLayoutManager(gridLayoutManager);
                    past.setHasFixedSize(true);
                    past.setAdapter(order_adapter);


                    //Toast.makeText(MyOrder.this, "Success " +ar.toString(), Toast.LENGTH_SHORT).show();
                    //search_more();

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //to find user past orders item end


    }

    @Override
    public void show_data(String id,String h_id,String h_name,String payment_method, String status) {
        if (id.contains("#")){
            id=id.replace("#","");
        }
        if(status.contains("Order") || status.contains("Cooking") || status.contains("Packaging") || status.contains("Prepared") || status.contains("Pass")  || status.contains("Pick Up")){
            Intent it = new Intent(MyOrder.this, Show_order.class);
            it.putExtra("id",user_id);
            it.putExtra("email",user_email);
            it.putExtra("address",user_address);
            it.putExtra("cat",user_cat);
            it.putExtra("h_id",h_id);
            it.putExtra("h_name",h_name);
            it.putExtra("order_id",id);
            it.putExtra("p_method",payment_method);
            it.putExtra("status",status);
            startActivity(it);
        }
        else if(status.contains("Delivered")){
            Intent it = new Intent(MyOrder.this, Show_PastOrder.class);
            it.putExtra("id",user_id);
            it.putExtra("email",user_email);
            it.putExtra("address",user_address);
            it.putExtra("cat",user_cat);
            it.putExtra("h_id",h_id);
            it.putExtra("h_name",h_name);
            it.putExtra("order_id",id);
            it.putExtra("p_method",payment_method);
            startActivity(it);
        }


    }

    @Override
    public void update_data(String id, String h_id, String h_name, String payment_method, String status) {

    }
}