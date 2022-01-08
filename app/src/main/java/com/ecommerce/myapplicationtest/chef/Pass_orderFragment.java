package com.ecommerce.myapplicationtest.chef;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.myapplicationtest.R;
import com.ecommerce.myapplicationtest.customer.Show_PastOrder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class Pass_orderFragment extends Fragment implements ShowOrder_Interface_chef{
    RecyclerView recyclerView;
    EditText search;
    TextView filter,close_filter,showFilter;
    String user_id, user_email, user_address, user_cat;
    LinearLayout linearLayout;

    WaveSwipeRefreshLayout refreshLayout;
    List<String> f_name, f_price, f_quantity, f_details, address, cat, hotel_id, order_id, status, method, customer_id, date, key_order;
    List<String> foodName, foodPrice, foodQuantity, foodDetails, foodPhoto;
    DatabaseReference databaseReference, databaseReference1, databaseReference2, databaseReference3;

    Order_Adapter_chef order_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pass_order, container, false);

        //initial widget
        linearLayout=view.findViewById(R.id.linerID_filter_show);
        showFilter=view.findViewById(R.id.show_filter);
        close_filter=view.findViewById(R.id.close_filter);
        search=view.findViewById(R.id.pass_order_search);
        filter=view.findViewById(R.id.filter_pass_order);
        recyclerView = view.findViewById(R.id.recycler_pass_order);
        refreshLayout=(WaveSwipeRefreshLayout)view.findViewById(R.id.refresh_id_passOrder);
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
                        if (p.equals("Pass") || p.equals("Delivery")) {
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
                    order_adapter = new Order_Adapter_chef(user_id, f_name, f_price, address, cat, hotel_id, order_id, status, method, customer_id, date, key_order, getContext(), Pass_orderFragment.this);
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
                search.setText("");
                refreshLayout.setRefreshing(false);
                load_data_again();
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilterDialog();
            }
        });

        close_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.GONE);
                load_data_again();
            }
        });


        return view;
    }

    private void openFilterDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.filter_option);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RadioGroup radioGroup = dialog.findViewById(R.id.filter_radioGroup);
        RadioButton radioButton,radioButton1,radioButton2;
        TextView select = dialog.findViewById(R.id.select_filter);


        radioButton=dialog.findViewById(R.id.date_filter);
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.setText("Select Date");
            }
        });

        radioButton1=dialog.findViewById(R.id.month_filter);
        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.setText("Select Month");
            }
        });

        radioButton2=dialog.findViewById(R.id.year_filter);
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.setText("Select Year");
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x=radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton3=dialog.findViewById(x);

                switch (x){
                    case R.id.date_filter:
                        Calendar calendar=Calendar.getInstance();
                        int yearNow=calendar.get(Calendar.YEAR);
                        int yearMonth=calendar.get(Calendar.MONTH);
                        int yearDay=calendar.get(Calendar.DAY_OF_MONTH);
                        Locale id=new Locale("bn","BN");
                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd LLL, yyy");

                        DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(year,month,dayOfMonth);
                                linearLayout.setVisibility(View.VISIBLE);
                                showFilter.setText("Filter by : "+simpleDateFormat.format(calendar.getTime()));
                                filter_separately(""+simpleDateFormat.format(calendar.getTime()));
                            }
                        },yearNow,yearMonth,yearDay);
                        datePickerDialog.setTitle("Select date");
                        datePickerDialog.show();
                        break;


                    case R.id.month_filter:
                        Calendar today=Calendar.getInstance();
                        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(),
                            new MonthPickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(int selectedMonth, int selectedYear) { // on date set
                                    linearLayout.setVisibility(View.VISIBLE);
                                    String monthN=monthName(selectedMonth+1);
                                    showFilter.setText("Filter by : "+monthN+", "+selectedYear);
                                    filter_separately(""+monthN+", "+selectedYear);
                                 }
                                }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));
                        builder.setActivatedMonth(Calendar.JULY)
                                .setMinYear(1990)
                                .setActivatedYear(today.get(Calendar.YEAR))
                                .setMaxYear(2100)
                                .setTitle("Select month year")
                                .build().show();
                        break;




                    case R.id.year_filter:
                        Calendar today2=Calendar.getInstance();
                        MonthPickerDialog.Builder builder2 = new MonthPickerDialog.Builder(getContext(),
                                new MonthPickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(int selectedMonth, int selectedYear) { // on date set
                                        linearLayout.setVisibility(View.VISIBLE);
                                        showFilter.setText("Filter by : "+selectedYear);
                                        filter_separately(""+selectedYear);
                                    }
                                }, today2.get(Calendar.YEAR), today2.get(Calendar.MONTH));
                        builder2.setActivatedMonth(Calendar.JULY)
                                .setMinYear(1990)
                                .setActivatedYear(today2.get(Calendar.YEAR))
                                .setMaxYear(2100)
                                .setTitle("Select year")
                                .showYearOnly()
                                .build().show();
                        break;
                }
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void filter_separately(String value) {
        String v=value.replace("Filter by : ","");
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
                    String  dd = "",p = "";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        p = snapshot1.child("status").getValue(String.class);
                        dd = snapshot1.child("date").getValue(String.class);
                        if (p.equals("Pass") || p.equals("Delivery")) {
                            if(dd.contains(v)){
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

                    }
                    order_adapter = new Order_Adapter_chef(user_id, f_name, f_price, address, cat, hotel_id, order_id, status, method, customer_id, date, key_order, getContext(), Pass_orderFragment.this);
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

    String monthName(int i){String month = "";
        switch (i){
            case 1:
               month="Jan";
               break;
            case 2:
                month="Feb";
                break;
            case 3:
                month="Mar";
                break;
            case 4:
                month="Apr";
                break;
            case 5:
                month="May";
                break;
            case 6:
                month="Jun";
                break;
            case 7:
                month="Jul";
                break;
            case 8:
                month="Aug";
                break;
            case 9:
                month="Sep";
                break;
            case 10:
                month="Oct";
                break;
            case 11:
                month="Nov";
                break;
            case 12:
                month="Dec";
                break;
        }

        return month;
    }

    private void filter(String OrderID) {
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
                    String  id = "",p = "";
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        p = snapshot1.child("status").getValue(String.class);
                        id = snapshot1.child("id").getValue(String.class);
                        if (p.equals("Pass") || p.equals("Delivery")) {
                            if(id.contains(OrderID)){
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

                    }
                    order_adapter = new Order_Adapter_chef(user_id, f_name, f_price, address, cat, hotel_id, order_id, status, method, customer_id, date, key_order, getContext(), Pass_orderFragment.this);
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

    @Override
    public void show_data(String id, String h_id, String customer_id, String payment_method, String status) {
        if (id.contains("#")) {
            id = id.replace("#", "");
        }
        show_order(id, customer_id, payment_method);
    }


    @Override
    public void update_data(String keyOrder, String orderID, String customer_id, String payment_method, String status) {


        //order_adapter.notifyDataSetChanged();


    }

    private void load_data_again() {
        linearLayout.setVisibility(View.GONE);
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
                        if (p.equals("Pass") || p.equals("Delivery")) {
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
                    if(f_name.size()<0){
                        Toast.makeText(getContext(), "There is no data.", Toast.LENGTH_SHORT).show();
                    }
                    order_adapter = new Order_Adapter_chef(user_id, f_name, f_price, address, cat, hotel_id, order_id, status, method, customer_id, date, key_order, getContext(), Pass_orderFragment.this);
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