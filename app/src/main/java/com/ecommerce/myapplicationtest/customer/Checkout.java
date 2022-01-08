package com.ecommerce.myapplicationtest.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.myapplicationtest.Homepage;
import com.ecommerce.myapplicationtest.MainActivity;
import com.ecommerce.myapplicationtest.R;
import com.ecommerce.myapplicationtest.chef.add_data;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Checkout extends AppCompatActivity {
    TextView edit,address1,address2,payment_method,edit_payment_method,subtotal_price,discount,delivery_fee,total,back,hotel_name;
    View fragment;
    Switch aSwitch;
    Button payment;
    RecyclerView recyclerView;

    String user_address,user_id,user_email,user_cat,hotel_id,h_name,value="";

    FusedLocationProviderClient fusedLocationProviderClient;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    DatabaseReference databaseReference,databaseReference2,databaseReference3,databaseReference4,databaseReference5;

    List<String> f_name;
    List<String> f_quantity;
    List<String> f_price;
    int s_price=0;
    Dialog dialog;

    String orderID="";

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String date;

    ///adapter
    Order_summary order_summary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        //initial calender and simpleDateFormat
        calendar=Calendar.getInstance();
        date=new SimpleDateFormat("dd LLL, yyy, hh:mm a", Locale.getDefault()).format(new Date());

        //initial all uses widget
        edit=findViewById(R.id.edit_location_checkout);
        address1=findViewById(R.id.address1_checkout);
        address2=findViewById(R.id.address2_checkout);
        payment_method=findViewById(R.id.payment_method_checkout);
        edit_payment_method=findViewById(R.id.edit_payment_method_checkout);
        recyclerView=findViewById(R.id.recycler_summary);
        subtotal_price=findViewById(R.id.subtotal_checkout);
        discount=findViewById(R.id.discount_checkout);
        delivery_fee=findViewById(R.id.delivery_fee_checkout);
        total=findViewById(R.id.total_checkout);
        back=findViewById(R.id.back_checkout);
        hotel_name=findViewById(R.id.hotel_name_checkout);

        fragment=findViewById(R.id.fragment_checkout);
        aSwitch=findViewById(R.id.switch_checkout);
        payment=findViewById(R.id.payment_checkout);

        if (payment_method.getText().toString().contains("Add a payment method")){

        }
        else{
            payment.setText("Place Order");
        }

        //receive data from cart_fragment
        user_address=getIntent().getStringExtra("address");
        user_email=getIntent().getStringExtra("email");
        user_id=getIntent().getStringExtra("id");
        user_cat=getIntent().getStringExtra("cat");
        hotel_id=getIntent().getStringExtra("hotel_id");
        h_name=getIntent().getStringExtra("hotel_name");

        //set hotel name
        hotel_name.setText(h_name);

        //initial list
        f_name=new ArrayList<>();
        f_quantity=new ArrayList<>();
        f_price=new ArrayList<>();

        //initial dialog
        dialog=new Dialog(this);

        //set category's value
        if (user_cat.equals("Chef")) {
            value="1";
        } else if (user_cat.equals("Customer")) {
            value="2";
        } else if (user_cat.equals("Delivery Person")) {
            value="3";
        }

        //push notification start
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Done";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                    }
                });
        //push notification end



        //set location in fragment
        try {
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_checkout);

            //initial fused location
            client = LocationServices.getFusedLocationProviderClient(Checkout.this);

            //check permission
            if (ActivityCompat.checkSelfPermission(Checkout.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //when permission granted
                getlocation2();
            } else {
                //when permission denied
                //request permission
                ActivityCompat.requestPermissions(Checkout.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }catch (Exception e){
            Toast.makeText(Checkout.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        // to find location end here


        //find cart item start
        //initial database_reference
        databaseReference= FirebaseDatabase.getInstance().getReference("cart");
        databaseReference2= FirebaseDatabase.getInstance().getReference("users");
        databaseReference3= FirebaseDatabase.getInstance().getReference("order");
        databaseReference4= FirebaseDatabase.getInstance().getReference("order_item");
        databaseReference5= FirebaseDatabase.getInstance().getReference("All_order");

        //to find user cart item
        Query query2 = databaseReference.child(user_id);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "",photo="";
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
                    subtotal_price.setText("Tk "+s_price);
                    total.setText("Tk "+(s_price+13));
                    //set recycler view with this data
                    order_summary=new Order_summary(f_name,f_quantity,f_price,Checkout.this);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(Checkout.this,1,GridLayoutManager.VERTICAL,false);
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


        //
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payment.getText().toString().contains("Palace Order")){
                    order_confirm();
                }
                else {
                    paymentMethodSelectDialog();
                }
            }
        });

        payment_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethodSelectDialog();
            }
        });

        //set on back
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private void order_confirm() {
        //here I add item data in order and order_item table both
        String x=String.valueOf(System.currentTimeMillis());
        orderID="#"+user_id+x+h_name;
        Query query2 = databaseReference.child(user_id);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String hID = "",fName="";
                    String Food_name="",fPrice="",fDetails="",fQuantity="",f_photo="";
                    String p="";
                    int ppp=0;
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        hID=snapshot1.child("hotel_id").getValue(String.class);
                        if(fName.isEmpty()){

                        }
                        else {
                            fName=fName+", ";
                        }
                        fName=fName+snapshot1.child("f_name").getValue(String.class);
                        p=snapshot1.getKey();

                        //for order item start
                        Food_name=snapshot1.child("f_name").getValue(String.class);
                        fPrice=snapshot1.child("f_price").getValue(String.class);
                        fDetails=snapshot1.child("f_details").getValue(String.class);
                        fQuantity=snapshot1.child("f_quantity").getValue(String.class);
                        f_photo=snapshot1.child("f_photo").getValue(String.class);
                        Order_item oi=new Order_item(Food_name,fPrice,fDetails,fQuantity,f_photo);
                        databaseReference4.child(user_id).child(""+(user_id+x+h_name)).child(p).setValue(oi);
                        //for order item end
                    }

                    Place_order po=new Place_order(orderID,user_address,user_cat,hID,fName,""+total.getText().toString(),""+hotel_name.getText().toString(),"Order",payment_method.getText().toString(),date);
                    databaseReference3.child(user_id).child(""+(p+"_"+user_id+x+h_name)).setValue(po);

                    Place_order_All tt=new Place_order_All(orderID,user_address,user_cat,hID,fName,""+total.getText().toString(),""+user_id,"Order",payment_method.getText().toString(),date);
                    databaseReference5.child(""+(p+"_"+user_id+x+h_name)).setValue(tt);


                    //delete all item from cart
                    databaseReference.child(user_id).removeValue();
                   Toast.makeText(Checkout.this, "Order Success.", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(Checkout.this, Show_order.class);
                    it.putExtra("id",user_id);
                    it.putExtra("email",user_email);
                    it.putExtra("address",user_address);
                    it.putExtra("cat",user_cat);
                    it.putExtra("h_id",hotel_id);
                    it.putExtra("h_name",h_name);
                    it.putExtra("order_id",""+(user_id+x+h_name));
                    it.putExtra("p_method",""+payment_method.getText().toString());
                    it.putExtra("status","Order");
                    startActivity(it);
                    finish();

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getlocation2() {
        //initial task location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                //when success
                if(location!=null){
                    //sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng=new LatLng(location.getLatitude()
                                    ,location.getLongitude());
                            //create marker option
                            MarkerOptions options=new MarkerOptions().position(latLng)
                                    .title("I am here!!");
                            //ZOOM app
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
                            googleMap.addMarker(options);
                        }
                    });
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            if (requestCode == 44) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //when permission granted
                    //call method
                    getlocation2();
                }
            }
        }catch (Exception exception){
            Toast.makeText(this, "Error : "+exception, Toast.LENGTH_SHORT).show();
        }
    }

    private void paymentMethodSelectDialog(){
        dialog.setContentView(R.layout.payment_method_select);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView cancel,card,cash,bkash;

        cancel=dialog.findViewById(R.id.cancel_method);
        card=dialog.findViewById(R.id.card_method);
        cash=dialog.findViewById(R.id.cash_method);
        bkash=dialog.findViewById(R.id.bkash_method);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_method.setText("Card");
                payment.setText("Palace Order");
                dialog.dismiss();
            }
        });
        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_method.setText("Cash");
                payment.setText("Palace Order");
                dialog.dismiss();
            }
        });
        bkash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_method.setText("bkash");
                payment.setText("Palace Order");
                dialog.dismiss();
            }
        });

        dialog.show();

    }

}