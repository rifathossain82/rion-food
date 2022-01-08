package com.ecommerce.myapplicationtest.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.myapplicationtest.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Cart_Adapter_item extends RecyclerView.Adapter<Cart_Adapter_item.MyViewHolder> {
    List<String> quantity;
    List<String> name;
    List<String> price;
    String hotel_id;
    Context context;
    Interface_cart_item interface_cart_item;
    int total=0;

    public Cart_Adapter_item(List<String> quantity, List<String> name, List<String> price, String hotel_id, Context context,Interface_cart_item interface_cart_item) {
        this.quantity = quantity;
        this.name = name;
        this.price = price;
        this.hotel_id = hotel_id;
        this.context = context;
        this.interface_cart_item=interface_cart_item;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Cart_Adapter_item.MyViewHolder holder, int position) {
        holder.qun.setText(quantity.get(position));
        holder.f_name.setText(name.get(position));

        int x=Integer.parseInt(quantity.get(position));
        int y=Integer.parseInt(price.get(position));
        x=x*y;
        holder.f_price.setText("TK "+x);
        total=total+x;
        interface_cart_item.setMyData(""+total);
    }

    @Override
    public int getItemCount() {
        return quantity.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView qun,f_name,f_price,remove,add,delete;
        int u=0,q=0;
        public MyViewHolder(View itemView) {
            super(itemView);

            qun=itemView.findViewById(R.id.item_quantity_cart);
            f_name=itemView.findViewById(R.id.item_name_cart);
            f_price=itemView.findViewById(R.id.item_price_cart);
            remove=itemView.findViewById(R.id.item_quantity_remove);
            add=itemView.findViewById(R.id.item_quantity_add);
            delete=itemView.findViewById(R.id.item_quantity_delete);

            qun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(u==0) {
                        remove.setVisibility(View.VISIBLE);
                        add.setVisibility(View.VISIBLE);
                        delete.setVisibility(View.VISIBLE);
                        f_name.setVisibility(View.GONE);
                        u=1;
                    }
                    else if(u==1){
                        remove.setVisibility(View.GONE);
                        add.setVisibility(View.GONE);
                        delete.setVisibility(View.GONE);
                        f_name.setVisibility(View.VISIBLE);
                        u=0;
                    }
                }
            });

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p=Integer.parseInt(qun.getText().toString());
                    if(p==1){
                        //remove.setImageResource(R.drawable.ic_remove_item_invisible);
                    }
                    else{
                        p=p-1;
                        qun.setText(""+p);
                        if(p<2){
                            //remove.setImageResource(R.drawable.ic_remove_item_invisible);
                        }
                        else{
                           // remove.setImageResource(R.drawable.ic_remove_item);
                        }
                    }
                    int value=Integer.parseInt(price.get(getAdapterPosition()));
                    f_price.setText("TK "+(p*value));
                    interface_cart_item.update_data(name.get(getAdapterPosition()),""+p);
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p=Integer.parseInt(qun.getText().toString());
                    p=p+1;
                    qun.setText(""+p);
                    if(p>1){
                        //remove.setImageResource(R.drawable.ic_remove_item);
                    }
                    int value=Integer.parseInt(price.get(getAdapterPosition()));
                    f_price.setText("TK "+(p*value));
                    interface_cart_item.update_data(name.get(getAdapterPosition()),""+p);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    interface_cart_item.delete_data(name.get(getAdapterPosition()));
                }
            });

        }
    }
}
