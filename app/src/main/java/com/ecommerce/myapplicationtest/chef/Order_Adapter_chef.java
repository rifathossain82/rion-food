package com.ecommerce.myapplicationtest.chef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.myapplicationtest.R;
import com.ecommerce.myapplicationtest.customer.Order_Adapter;
import com.ecommerce.myapplicationtest.customer.ShowOrder_interface;

import java.util.List;

public class Order_Adapter_chef extends RecyclerView.Adapter<Order_Adapter_chef.MyViewHolder> {
    String userID;
    List<String> f_name,f_price,address,cat,hotel_id,order_id,status,method,customer_id,date,key_order;
    Context context;
    ShowOrder_Interface_chef showOrder_interface_chef;

    public Order_Adapter_chef(String userID,List<String> f_name, List<String> f_price, List<String> address, List<String> cat, List<String> hotel_id, List<String> order_id, List<String> status, List<String> method, List<String> customer_id, List<String> date, List<String> key_order, Context context, ShowOrder_Interface_chef showOrder_interface_chef) {
        this.userID=userID;
        this.f_name = f_name;
        this.f_price = f_price;
        this.address = address;
        this.cat = cat;
        this.hotel_id = hotel_id;
        this.order_id = order_id;
        this.status = status;
        this.method = method;
        this.customer_id = customer_id;
        this.date = date;
        this.key_order = key_order;
        this.context = context;
        this.showOrder_interface_chef = showOrder_interface_chef;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.order_item_chef,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Order_Adapter_chef.MyViewHolder holder, int position) {
            holder.orderid.setText(order_id.get(position));
            holder.price.setText(f_price.get(position));
            holder.foodName.setText(f_name.get(position));
            holder.date_time.setText(date.get(position));
            holder.order_status.setText(status.get(position));
            if(status.get(position).contains("Pass") || status.get(position).contains("Delivery")){
                holder.order_status.setVisibility(View.GONE);
            }

    }

    @Override
    public int getItemCount() {
        return f_name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView orderid,price,foodName,date_time;
        TextView order_status;
        public MyViewHolder(View itemView) {
            super(itemView);

            linearLayout=itemView.findViewById(R.id.linerID_oItem_chef);
            orderid=itemView.findViewById(R.id.orderId_oItem_chef);
            price=itemView.findViewById(R.id.price_oItem_chef);
            foodName=itemView.findViewById(R.id.fName_oItem_chef);
            date_time=itemView.findViewById(R.id.date_oItem_chef);
            order_status=itemView.findViewById(R.id.spinner_oItem_chef);

            order_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String item=order_status.getText().toString();
                    showOrder_interface_chef.update_data(key_order.get(getAdapterPosition()),order_id.get(getAdapterPosition()),customer_id.get(getAdapterPosition()),method.get(getAdapterPosition()),item);

                }
            });


            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOrder_interface_chef.show_data(order_id.get(getAdapterPosition()),hotel_id.get(getAdapterPosition()),customer_id.get(getAdapterPosition()),method.get(getAdapterPosition()),status.get(getAdapterPosition()));
                }
            });
        }
    }
}
