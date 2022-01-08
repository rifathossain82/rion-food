package com.ecommerce.myapplicationtest.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
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
import java.util.List;

public class Show_order extends AppCompatActivity {

    String user_id,user_email,user_cat,user_address,hotel_id,hotel_name,order_id,payment_method,orderStatus;

    TextView status, back,toolbar_hName,o_id,h_name,address,subtotal,delivery_fee,tax,total,method,method_total;
    CardView cardView;
    RecyclerView recyclerView;

    DatabaseReference databaseReference,databaseReference2,databaseReference3;

    List<String> f_name;
    List<String> f_price;
    List<String> f_quantity;

    Order_summary order_summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);

        //receive data from checkout.java and MyOrder.java
        user_id=getIntent().getStringExtra("id");
        user_email=getIntent().getStringExtra("email");
        user_cat=getIntent().getStringExtra("cat");
        user_address=getIntent().getStringExtra("address");
        hotel_id=getIntent().getStringExtra("h_id");
        hotel_name=getIntent().getStringExtra("h_name");
        order_id=getIntent().getStringExtra("order_id");
        payment_method=getIntent().getStringExtra("p_method");
        orderStatus=getIntent().getStringExtra("status");

        Toast.makeText(this, "Hotel name: "+hotel_name, Toast.LENGTH_SHORT).show();

        //initial widget
        back=findViewById(R.id.back_order);
        toolbar_hName=findViewById(R.id.hotel_name_toolber_order);
        o_id=findViewById(R.id.order_number_order);
        h_name=findViewById(R.id.hotel_name_order);
        address=findViewById(R.id.address_order);
        subtotal=findViewById(R.id.subtotal_order);
        delivery_fee=findViewById(R.id.delivery_fee_order);
        tax=findViewById(R.id.tax_order);
        total=findViewById(R.id.total_order);
        method=findViewById(R.id.method_order);
        method_total=findViewById(R.id.method_total_order);
        cardView=findViewById(R.id.rider_contact_order);
        recyclerView=findViewById(R.id.recycler_order);
        status=findViewById(R.id.order_status_myOrder);

        //set data
        toolbar_hName.setText(hotel_name);
        o_id.setText("#"+order_id);
        h_name.setText(hotel_name);
        address.setText(user_address);
        method.setText(payment_method);
        status.setText("Status : "+orderStatus);

        //initial list
        f_name=new ArrayList<>();
        f_price=new ArrayList<>();
        f_quantity=new ArrayList<>();

        //initial database_reference
        databaseReference= FirebaseDatabase.getInstance().getReference("order");
        databaseReference2= FirebaseDatabase.getInstance().getReference("users");
        databaseReference3= FirebaseDatabase.getInstance().getReference("order_item");

        //to find user order item
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
                        int x=Integer.parseInt(snapshot1.child("f_quantity").getValue(String.class));
                        int y=Integer.parseInt(snapshot1.child("f_price").getValue(String.class));
                        x=x*y;
                        s_price=s_price+x;
                    }
                    //set total and subtotal price
                    subtotal.setText("Tk "+s_price);
                    total.setText("Tk "+(s_price+13));
                    method_total.setText("Tk "+(s_price+13));
                    //set recycler view with this data
                    order_summary=new Order_summary(f_name,f_quantity,f_price,Show_order.this);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(Show_order.this,1,GridLayoutManager.VERTICAL,false);
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
        //to find user ---------end


        //set on click listener for back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}