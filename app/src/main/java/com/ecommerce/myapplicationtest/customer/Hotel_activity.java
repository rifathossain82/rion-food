package com.ecommerce.myapplicationtest.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.myapplicationtest.Homepage;
import com.ecommerce.myapplicationtest.R;
import com.ecommerce.myapplicationtest.chef.add_data;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jgabrielfreitas.core.BlurImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Hotel_activity extends AppCompatActivity implements Hotel_Food_Interface{
    String email,id;

    List<String> name;
    List<String> price;
    List<String> details;
    List<String> photo;

    RecyclerView recyclerView;
    ImageView imageView;
    TextView hotel_name;

    String customer_email="",customer_id="";

    DatabaseReference databaseReference,databaseReference2,databaseReference3;
    Hotel_Adapter hotel_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);

        //restaurant email address
        email=getIntent().getStringExtra("email");
        //restaurant id
        id=getIntent().getStringExtra("id");

        //receive customer info
        customer_email=getIntent().getStringExtra("c_email");


        //initial reference
        databaseReference= FirebaseDatabase.getInstance().getReference("food_item_add_chef");
        databaseReference2= FirebaseDatabase.getInstance().getReference("users");
        databaseReference3= FirebaseDatabase.getInstance().getReference("cart");

        //to find user(customer) id-------start
        Query query2 = databaseReference2.child("Customer").orderByChild("email").equalTo(customer_email);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "",photo="";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        customer_id=snapshot1.getKey();
                    }

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //to find user(customer) id---------end


        //initial list
        name=new ArrayList<>();
        price=new ArrayList<>();
        details=new ArrayList<>();
        photo=new ArrayList<>();

        //initial widget
        recyclerView=findViewById(R.id.show_food_item);
        imageView=findViewById(R.id.hotel_photo);
        hotel_name=findViewById(R.id.hotel_name);

        setImage();


        //find restaurant food item
        Query query = databaseReference.child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        name.add(snapshot1.child("name").getValue(String.class));
                        price.add(snapshot1.child("price").getValue(String.class));
                        details.add(snapshot1.child("details").getValue(String.class));
                        photo.add(snapshot1.child("filepath").getValue(String.class));
                    }

                    //set food item
                    hotel_adapter=new Hotel_Adapter(name,price,details,photo,Hotel_activity.this, Hotel_activity.this::open_bottomShitDialog);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(Hotel_activity.this,1,GridLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(hotel_adapter);

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //end here to finding cart food





    }

    private void setImage() {
        Query query = databaseReference2.child("Chef").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String photo ="",name="";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        photo=snapshot1.child("photo").getValue(String.class);
                        name=snapshot1.child("name").getValue(String.class);
                    }

                    //set restaurant photo
                    if(photo.isEmpty()){
                        Picasso.get().load("https://1080motion.com/wp-content/uploads/2018/06/NoImageFound.jpg.png").into(imageView);
                    }
                    else{
                        Picasso.get().load(""+photo).into(imageView);

                    }
                    hotel_name.setText(name);
                    //imageView.setBlur(2);

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void open_bottomShitDialog(String name, String price, String details, String photo) {
        try {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Hotel_activity.this,R.style.BottomSheetDialogTheme);
            bottomSheetDialog.setContentView(R.layout.bottom_shit_item);
            bottomSheetDialog.setCanceledOnTouchOutside(true);

            //to show bottom sheet dialog
            bottomSheetDialog.show();

            //set bottom food photo
            Picasso.get().load(""+photo).into((ImageView) bottomSheetDialog.findViewById(R.id.photo_bottom_item));
            //initial bottom content
            TextView f_name=bottomSheetDialog.findViewById(R.id.name_bottom_item);
            TextView f_price=bottomSheetDialog.findViewById(R.id.price_bottom_item);
            TextView f_details=bottomSheetDialog.findViewById(R.id.details_bottom_item);
            ImageView remove=bottomSheetDialog.findViewById(R.id.remove_bottom_item);
            ImageView add=bottomSheetDialog.findViewById(R.id.add_bottom_item);
            TextView count=bottomSheetDialog.findViewById(R.id.count_bottom_item);
            Button add_to_cart=bottomSheetDialog.findViewById(R.id.cart_bottom_item);


            //se;t bottoms info
            //se;t bottoms info
            f_name.setText(name);
            f_price.setText(price);
            f_details.setText(details);


            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //count process start
                    int p=Integer.parseInt(count.getText().toString());
                    p=p+1;
                    count.setText(""+p);
                    if(p>1){
                        remove.setImageResource(R.drawable.ic_remove_item);
                    }
                    //count process end
                }
            });

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //count process start
                    int p=Integer.parseInt(count.getText().toString());
                    if(p==1){
                        remove.setImageResource(R.drawable.ic_remove_item_invisible);
                    }
                    else{
                        p=p-1;
                        count.setText(""+p);
                        if(p<2){
                            remove.setImageResource(R.drawable.ic_remove_item_invisible);
                        }
                        else{
                            remove.setImageResource(R.drawable.ic_remove_item);
                        }
                    }

                    //count process end
                }
            });

            add_to_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //find auto id
                    Query query2 = databaseReference3.child(customer_id);
                    query2.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                int id_=0;
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    id_ = Integer.parseInt(snapshot1.getKey());

                                }
                                id_=id_+1;
                                add_food(id_,id,email,name,price,details,photo,count.getText().toString());
                                bottomSheetDialog.dismiss();
                            }
                            else {
                                Add_item_cart ad=new Add_item_cart(id,email,name,price,details,photo,count.getText().toString());
                                databaseReference3.child(customer_id).child(""+0).setValue(ad);
                                Toast.makeText(Hotel_activity.this, "Item add in cart.", Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });


        }catch (Exception e){
            Toast.makeText(Hotel_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //a customer can buy one or more food from one restaurant , he/she can't buy more than one restaurant
    private void add_food(int id_,String id,String email,String name,String price,String details,String photo,String count) {
        Query query2 = databaseReference3.child(customer_id).orderByChild("hotel_id").equalTo(id);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Add_item_cart ad=new Add_item_cart(id,email,name,price,details,photo,count);
                    databaseReference3.child(customer_id).child(""+id_).setValue(ad);
                    Toast.makeText(Hotel_activity.this, "Item add in cart.", Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog.Builder builder=new AlertDialog.Builder(Hotel_activity.this)
                            .setTitle("Alert")
                            .setIcon(R.drawable.ic_alert)
                            .setMessage("If you add this item, the previous item will be canceled.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    databaseReference3.child(customer_id).removeValue();
                                    Add_item_cart ad=new Add_item_cart(id,email,name,price,details,photo,count);
                                    databaseReference3.child(customer_id).child("0").setValue(ad);
                                    Toast.makeText(Hotel_activity.this, "Item add in cart.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    builder.create();
                    builder.show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}