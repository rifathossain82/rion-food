package com.ecommerce.myapplicationtest.customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.myapplicationtest.Homepage;
import com.ecommerce.myapplicationtest.MainActivity;
import com.ecommerce.myapplicationtest.R;
import com.ecommerce.myapplicationtest.Refresh_Home;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cart_Fragment extends Fragment implements Interface_cart_item{
    String email,id,address,category;

    TextView hotel_name,e_time,subtotal,discount,d_fee,voucher,total;
    RecyclerView recyclerView1,recyclerView2;
    Button review;

    DatabaseReference databaseReference,databaseReference2;

    List<String> quantity;
    List<String> f_name;
    List<String> f_price;

    String hotel_id="",h_name="";

    Cart_Adapter_item cart_adapter_item;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_cart_, container, false);

        //get data from homepage
        email=getArguments().getString("email");
        id=getArguments().getString("id");
        address=getArguments().getString("address");
        category=getArguments().getString("cat");


        //initial widget
        hotel_name=view.findViewById(R.id.cart_hotel);
        e_time=view.findViewById(R.id.cart_time);
        subtotal=view.findViewById(R.id.cart_subtotal);
        discount=view.findViewById(R.id.cart_discount);
        d_fee=view.findViewById(R.id.cart_delivery_fee);
        voucher=view.findViewById(R.id.cart_voucher);
        total=view.findViewById(R.id.cart_total);

        recyclerView1=view.findViewById(R.id.cart_item_recycler);
        recyclerView2=view.findViewById(R.id.popular_item_recycler);

        review=view.findViewById(R.id.cart_button);

        //initial list
        quantity=new ArrayList<>();
        f_name=new ArrayList<>();
        f_price=new ArrayList<>();

        //initial database_reference
        databaseReference= FirebaseDatabase.getInstance().getReference("cart");
        databaseReference2= FirebaseDatabase.getInstance().getReference("users");

        //to find user cart item
        Query query2 = databaseReference.child(id);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "",photo="";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        quantity.add(snapshot1.child("f_quantity").getValue(String.class));
                        f_name.add(snapshot1.child("f_name").getValue(String.class));
                        f_price.add(snapshot1.child("f_price").getValue(String.class));
                        hotel_id=snapshot1.child("hotel_id").getValue(String.class);
                    }
                    cart_adapter_item=new Cart_Adapter_item(quantity,f_name,f_price,hotel_id,getContext(),Cart_Fragment.this);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
                    recyclerView1.setLayoutManager(gridLayoutManager);
                    recyclerView1.setHasFixedSize(true);
                    recyclerView1.setAdapter(cart_adapter_item);
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //to find user(customer) id---------end

        //to find hotel name start
        Query query3 = databaseReference2.child("Chef").child(hotel_id);
        query3.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "",photo="";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        name=snapshot1.child("name").getValue(String.class);
                        h_name=snapshot1.child("name").getValue(String.class);
                        if(hotel_id.contains(snapshot1.getKey())){
                            break;
                        }
                    }
                    hotel_name.setText(""+name);
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //to find hotel name end

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent it=new Intent(getContext(),Checkout.class);
            it.putExtra("address",address);
            it.putExtra("id",id);
            it.putExtra("email",email);
            it.putExtra("cat",category);
            it.putExtra("hotel_id",hotel_id);
            it.putExtra("hotel_name",h_name);
            startActivity(it);

            }
        });

        return view;

    }

    @Override
    public void setMyData(String tt) {
        subtotal.setText("Tk "+tt);
        total.setText("Tk "+(Integer.parseInt(tt)+13));
    }

    @Override
    public void update_data(String f_name,String quantity) {
//to find user cart item
        Query query2 = databaseReference.child(id).orderByChild("f_name").equalTo(f_name);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "",photo="",sub_id="";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        sub_id=snapshot1.getKey();
                    }
                    HashMap hashMap = new HashMap();
                    hashMap.put("f_quantity", quantity);

                    databaseReference.child(id).child(sub_id).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            load_cart_item();
                        }
                    });

                } else {
                    //Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //to find user(customer) id---------end
    }

    @Override
    public void delete_data(String f_name) {
       //to find user cart item
        Query query2 = databaseReference.child(id).orderByChild("f_name").equalTo(f_name);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "",photo="",sub_id="";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        sub_id=snapshot1.getKey();
                    }

                    databaseReference.child(id).child(sub_id).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError error,DatabaseReference ref) {
                            load_cart_item();
                        }
                    });

                } else {
                    //Toast.makeText(getContext(), "Not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //to find user(customer) id---------end
    }

    void load_cart_item(){
        //initial list
        quantity=new ArrayList<>();
        f_name=new ArrayList<>();
        f_price=new ArrayList<>();
        //to find user cart item
        Query query2 = databaseReference.child(id);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "",photo="";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        quantity.add(snapshot1.child("f_quantity").getValue(String.class));
                        f_name.add(snapshot1.child("f_name").getValue(String.class));
                        f_price.add(snapshot1.child("f_price").getValue(String.class));
                        hotel_id=snapshot1.child("hotel_id").getValue(String.class);
                    }
                    cart_adapter_item=new Cart_Adapter_item(quantity,f_name,f_price,hotel_id,getContext(),Cart_Fragment.this);
                    GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false);
                    recyclerView1.setLayoutManager(gridLayoutManager);
                    recyclerView1.setHasFixedSize(true);
                    recyclerView1.setAdapter(cart_adapter_item);
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //to find user(customer) id---------end
    }

}