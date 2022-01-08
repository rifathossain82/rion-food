package com.ecommerce.myapplicationtest.chef;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.myapplicationtest.R;
import com.ecommerce.myapplicationtest.customer.Edit_Profile;
import com.ecommerce.myapplicationtest.customer.MyOrder;
import com.ecommerce.myapplicationtest.customer.Order_Adapter;
import com.ecommerce.myapplicationtest.customer.ShowOrder_interface;
import com.ecommerce.myapplicationtest.customer.Show_PastOrder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class OrderFragmentChef extends Fragment implements ShowOrder_Interface_chef {
    RecyclerView recyclerView;
    String user_id, user_email, user_address, user_cat;

    WaveSwipeRefreshLayout refreshLayout;
    List<String> f_name, f_price, f_quantity, f_details, address, cat, hotel_id, order_id, status, method, customer_id, date, key_order;
    List<String> foodName, foodPrice, foodQuantity, foodDetails, foodPhoto;
    DatabaseReference databaseReference, databaseReference1, databaseReference2, databaseReference3;

    Order_Adapter_chef order_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_chef, container, false);

        //initial widget
        recyclerView = view.findViewById(R.id.recent_recycler_chef);
        refreshLayout=(WaveSwipeRefreshLayout)view.findViewById(R.id.refresh_id);
        refreshLayout.setWaveColor(Color.rgb(0, 153, 255));
        refreshLayout.setShadowRadius(0);

        //receive data from homepage
        user_id = getArguments().getString("id");
        user_email = getArguments().getString("email");
        user_address = getArguments().getString("address");
        user_cat = getArguments().getString("cat");

        //initial list
        f_name = new ArrayList<>();
        f_price = new ArrayList<>();
        f_quantity = new ArrayList<>();
        f_details = new ArrayList<>();
        address = new ArrayList<>();
        cat = new ArrayList<>();
        hotel_id = new ArrayList<>();
        order_id = new ArrayList<>();
        status = new ArrayList<>();
        method = new ArrayList<>();
        customer_id = new ArrayList<>();
        date = new ArrayList<>();
        key_order = new ArrayList<>();

        foodName = new ArrayList<>();
        foodPrice = new ArrayList<>();
        foodDetails = new ArrayList<>();
        foodQuantity = new ArrayList<>();
        foodPhoto = new ArrayList<>();

        //initial database_reference
        databaseReference = FirebaseDatabase.getInstance().getReference("All_order");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("order");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("users");
        databaseReference3 = FirebaseDatabase.getInstance().getReference("order_item");


        //to find user recent order item start
        Query query2 = databaseReference.orderByChild("h_id").equalTo(user_id);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "", photo = "", id = "";
                    int s_price = 0;
                    String p = "";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        p = snapshot1.child("status").getValue(String.class);
                        if (p.equals("Order") || p.equals("Cooking") || p.equals("Packaging") || p.equals("Prepared")) {
                            f_name.add(snapshot1.child("f_name").getValue(String.class));
                            f_price.add(snapshot1.child("f_price").getValue(String.class));
                            address.add(snapshot1.child("address").getValue(String.class));
                            cat.add(snapshot1.child("cat").getValue(String.class));
                            hotel_id.add(snapshot1.child("h_id").getValue(String.class));
                            order_id.add(snapshot1.child("id").getValue(String.class));
                            status.add(snapshot1.child("status").getValue(String.class));
                            method.add(snapshot1.child("method").getValue(String.class));
                            customer_id.add(snapshot1.child("customer_id").getValue(String.class));
                            date.add(snapshot1.child("date").getValue(String.class));
                            key_order.add(snapshot1.getKey());
                        }

                    }
                    order_adapter = new Order_Adapter_chef(user_id, f_name, f_price, address, cat, hotel_id, order_id, status, method, customer_id, date, key_order, getContext(), OrderFragmentChef.this);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
                    //gridLayoutManager.setReverseLayout(true);
                    // gridLayoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(order_adapter);
                    //search_more();

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //to find user recent orders item end

        refreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            refreshLayout.setRefreshing(false);
            load_data_again();
            }
        });

        return view;
    }


    @Override
    public void show_data(String id, String h_id, String customer_id, String payment_method, String status) {
        if (id.contains("#")) {
            id = id.replace("#", "");
        }
        show_order(id, customer_id, payment_method);
    }


    @Override
    public void update_data(String keyOrder, String orderID, String customer_id, String payment_method, String status) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.status_option);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RadioGroup radioGroup = dialog.findViewById(R.id.status_option_radioGroup);
        TextView cancel = dialog.findViewById(R.id.status_option_cancel);
        TextView update = dialog.findViewById(R.id.status_option_update);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton=dialog.findViewById(x);

                HashMap hashMap = new HashMap();
                hashMap.put("status",""+radioButton.getText().toString());

                //update all_order table
                databaseReference.child(keyOrder).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        load_data_again();
                    }
                });

                //to update order table
                update_order_table(orderID,customer_id,radioButton.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();


        //order_adapter.notifyDataSetChanged();


    }

    private void update_order_table(String orderID, String customer_id,String status) {
        //to find user recent order item start
        Query query = databaseReference1.child(customer_id).orderByChild("id").equalTo((orderID));
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String hotelName = "", hotelID = "", address = "",key_="";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        key_ = snapshot1.getKey();
                    }

                    //update order status start
                    HashMap hashMap = new HashMap();
                    hashMap.put("status",""+status);

                    databaseReference1.child(customer_id).child(key_).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {}});
                    //update order status end

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //to find user recent orders item end
    }

    private void load_data_again() {
        //initial list
        f_name = new ArrayList<>();
        f_price = new ArrayList<>();
        f_quantity = new ArrayList<>();
        f_details = new ArrayList<>();
        address = new ArrayList<>();
        cat = new ArrayList<>();
        hotel_id = new ArrayList<>();
        order_id = new ArrayList<>();
        status = new ArrayList<>();
        method = new ArrayList<>();
        customer_id = new ArrayList<>();
        date = new ArrayList<>();
        key_order = new ArrayList<>();

        foodName = new ArrayList<>();
        foodPrice = new ArrayList<>();
        foodDetails = new ArrayList<>();
        foodQuantity = new ArrayList<>();
        foodPhoto = new ArrayList<>();

        //to find user recent order item start
        Query query2 = databaseReference.orderByChild("h_id").equalTo(user_id);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = "", photo = "", id = "";
                    int s_price = 0;
                    String p = "";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        p = snapshot1.child("status").getValue(String.class);
                        if (p.equals("Order") || p.equals("Cooking") || p.equals("Packaging") || p.equals("Prepared")) {
                            f_name.add(snapshot1.child("f_name").getValue(String.class));
                            f_price.add(snapshot1.child("f_price").getValue(String.class));
                            address.add(snapshot1.child("address").getValue(String.class));
                            cat.add(snapshot1.child("cat").getValue(String.class));
                            hotel_id.add(snapshot1.child("h_id").getValue(String.class));
                            order_id.add(snapshot1.child("id").getValue(String.class));
                            status.add(snapshot1.child("status").getValue(String.class));
                            method.add(snapshot1.child("method").getValue(String.class));
                            customer_id.add(snapshot1.child("customer_id").getValue(String.class));
                            date.add(snapshot1.child("date").getValue(String.class));
                            key_order.add(snapshot1.getKey());
                        }

                    }
                    order_adapter = new Order_Adapter_chef(user_id, f_name, f_price, address, cat, hotel_id, order_id, status, method, customer_id, date, key_order, getContext(), OrderFragmentChef.this);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
                    //gridLayoutManager.setReverseLayout(true);
                    // gridLayoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(order_adapter);
                    //search_more();
                    refreshLayout.setRefreshing(false);

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //to find user recent orders item end


    }

    private void show_order(String orderID, String customerID, String payment_method) {
        //to find user recent order item start
        Query query = databaseReference1.child(customerID).orderByChild("id").equalTo(("#" + orderID));
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String hotelName = "", hotelID = "", address = "";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        hotelName = snapshot1.child("h_name").getValue(String.class);
                        hotelID = snapshot1.child("h_id").getValue(String.class);
                        address = snapshot1.child("address").getValue(String.class);
                    }
                    Intent it = new Intent(getActivity(), Show_PastOrder.class);
                    it.putExtra("id", customerID);
                    it.putExtra("email", user_email);
                    it.putExtra("address", address);
                    it.putExtra("cat", user_cat);
                    it.putExtra("h_id", hotelID);
                    it.putExtra("h_name", hotelName);
                    it.putExtra("order_id", orderID);
                    it.putExtra("p_method", payment_method);
                    startActivity(it);

                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //to find user recent orders item end
    }
}