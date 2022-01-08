package com.ecommerce.myapplicationtest.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.myapplicationtest.R;

import java.util.List;

public class Order_Adapter extends RecyclerView.Adapter<Order_Adapter.MyViewHolder> {
    List<String> f_name,f_price,address,cat,hotel_id,order_id,status,method,hotelName,date;
    Context context;
    ShowOrder_interface showOrder_interface;

    public Order_Adapter(List<String> f_name, List<String> f_price, List<String> address, List<String> cat, List<String> hotel_id, List<String> order_id, List<String> status, List<String> method, List<String> hotelName, List<String> date, Context context,ShowOrder_interface showOrder_interface) {
        this.f_name = f_name;
        this.f_price = f_price;
        this.address = address;
        this.cat = cat;
        this.hotel_id = hotel_id;
        this.order_id = order_id;
        this.status = status;
        this.method = method;
        this.hotelName = hotelName;
        this.date = date;
        this.context = context;
        this.showOrder_interface=showOrder_interface;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.order_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Order_Adapter.MyViewHolder holder, int position) {
        holder.hotel_name.setText(hotelName.get(position));
        holder.price.setText(f_price.get(position));
        holder.foodName.setText(f_name.get(position));
        holder.date_time.setText(date.get(position));


    }

    @Override
    public int getItemCount() {
        return f_name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView hotel_name,price,foodName,date_time;
        public MyViewHolder(View itemView) {
            super(itemView);

            linearLayout=itemView.findViewById(R.id.linerID_oItem);
            hotel_name=itemView.findViewById(R.id.hotel_name_oItem);
            price=itemView.findViewById(R.id.price_oItem);
            foodName=itemView.findViewById(R.id.fName_oItem);
            date_time=itemView.findViewById(R.id.date_oItem);


            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showOrder_interface.show_data(order_id.get(getAdapterPosition()),hotel_id.get(getAdapterPosition()),hotelName.get(getAdapterPosition()),method.get(getAdapterPosition()),status.get(getAdapterPosition()));
                }
            });

        }
    }
}
