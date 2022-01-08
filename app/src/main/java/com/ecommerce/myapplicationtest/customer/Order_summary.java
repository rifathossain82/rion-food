package com.ecommerce.myapplicationtest.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.myapplicationtest.R;

import java.util.List;

public class Order_summary extends RecyclerView.Adapter<Order_summary.MyViewHolder> {

    List<String> name;
    List<String> quantity;
    List<String> price;
    Context context;

    public Order_summary(List<String> name, List<String> quantity, List<String> price, Context context) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.context = context;
    }

    @Override
    public Order_summary.MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.summary_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Order_summary.MyViewHolder holder, int position) {
        holder.name.setText(quantity.get(position)+"x "+name.get(position));
        int x= Integer.parseInt(quantity.get(position));
        int y= Integer.parseInt(price.get(position));
        x=x*y;
        holder.price.setText("Tk "+x);
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,price;
        public MyViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.item_name_summary);
            price=itemView.findViewById(R.id.item_price_summary);
        }
    }
}
