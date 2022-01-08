package com.ecommerce.myapplicationtest.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.myapplicationtest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Show_PastOrder extends AppCompatActivity {
    TextView back,toolbar_name,orderNumber,hotelName,address,subtotal,delivery,tax,total;
    ImageView imageView;
    RecyclerView recyclerView;

    String user_id,user_email,user_cat,user_address,hotel_id,hotel_name,order_id,payment_method;

    DatabaseReference databaseReference,databaseReference2,databaseReference3;

    List<String> f_name;
    List<String> f_price;
    List<String> f_quantity;
    List<String> f_photo;

    Order_summary order_summary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_past_order);

        //initial all widget
        back=findViewById(R.id.backPastOrder);
        toolbar_name=findViewById(R.id.toolbar_pastOrder);
        orderNumber=findViewById(R.id.order_number_pastOrder);
        hotelName=findViewById(R.id.hotel_name_pastOrder);
        address=findViewById(R.id.address_pastOrder);
        subtotal=findViewById(R.id.subtotal_pastOrder);
        delivery=findViewById(R.id.delivery_fee_pastOrder);
        tax=findViewById(R.id.tax_pastOrder);
        total=findViewById(R.id.total_pastOrder);
        imageView=findViewById(R.id.photo_pastOrder);
        recyclerView=findViewById(R.id.recycler_pastOrder);

        //receive data from MyOrder.java
        user_id=getIntent().getStringExtra("id");
        user_email=getIntent().getStringExtra("email");
        user_cat=getIntent().getStringExtra("cat");
        user_address=getIntent().getStringExtra("address");
        hotel_id=getIntent().getStringExtra("h_id");
        hotel_name=getIntent().getStringExtra("h_name");
        order_id=getIntent().getStringExtra("order_id");
        payment_method=getIntent().getStringExtra("p_method");

        //set data
        toolbar_name.setText(hotel_name);
        orderNumber.setText("#"+order_id);
        hotelName.setText(hotel_name);
        address.setText(user_address);

        //initial list
        f_name=new ArrayList<>();
        f_price=new ArrayList<>();
        f_quantity=new ArrayList<>();
        f_photo=new ArrayList<>();

        //initial database_reference
        databaseReference= FirebaseDatabase.getInstance().getReference("order");
        databaseReference2= FirebaseDatabase.getInstance().getReference("users");
        databaseReference3= FirebaseDatabase.getInstance().getReference("order_item");

        //to find user previous order item
        Query query2 = databaseReference3.child(user_id).child(order_id);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "",photo="";
                    int s_price=0;
                    int p=0;
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        f_quantity.add(snapshot1.child("f_quantity").getValue(String.class));
                        f_name.add(snapshot1.child("f_name").getValue(String.class));
                        f_price.add(snapshot1.child("f_price").getValue(String.class));
                        f_photo.add(snapshot1.child("f_photo").getValue(String.class));
                        int x=Integer.parseInt(snapshot1.child("f_quantity").getValue(String.class));
                        int y=Integer.parseInt(snapshot1.child("f_price").getValue(String.class));
                        x=x*y;
                        s_price=s_price+x;
                    }
                    //set total and subtotal price
                    subtotal.setText("Tk "+s_price);
                    total.setText("Tk "+(s_price+13));
                    //set recycler view with this data
                    order_summary=new Order_summary(f_name,f_quantity,f_price,Show_PastOrder.this);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(Show_PastOrder.this,1,GridLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(order_summary);

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //to find user previous order item ---------end


        //to find hotel photo
        Query query = databaseReference2.child("Chef").orderByChild("name").equalTo(hotel_name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String url="";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        url=snapshot1.child("photo").getValue(String.class);
                    }
                    Picasso.get().load(""+url).into(imageView);
                 } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}