package com.ecommerce.myapplicationtest.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.ecommerce.myapplicationtest.R;

import java.util.ArrayList;
import java.util.List;

public class HelpCenter_customer extends AppCompatActivity {
    TextView back;
    RecyclerView recyclerView;
    List<Integer> icon;
    List<String> subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center_customer);

        //initial widget
        back=findViewById(R.id.back_help_customer);
        recyclerView=findViewById(R.id.recycler_help_customer);


        //initial list
        icon=new ArrayList<>();
        subject=new ArrayList<>();

        icon.add(R.drawable.ic_account);
        icon.add(R.drawable.ic_orders3);
        icon.add(R.drawable.ic_payments);
        icon.add(R.drawable.ic_vouchar2);
        icon.add(R.drawable.ic_support);
        icon.add(R.drawable.ic_menu);

        subject.add("My Account");
        subject.add("My Orders");
        subject.add("Payment");
        subject.add("Vouchers");
        subject.add("My Support requests");
        subject.add("FAQ");

        HelpAdapter_customer helpAdapter_customer=new HelpAdapter_customer(icon,subject,HelpCenter_customer.this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(HelpCenter_customer.this,1, GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(helpAdapter_customer);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}