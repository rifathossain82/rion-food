package com.ecommerce.myapplicationtest.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.myapplicationtest.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Hotel_Adapter extends RecyclerView.Adapter<Hotel_Adapter.ViewHolder> {
  List<String> name;
  List<String> price;
  List<String> details;
  List<String> photo;
  Context context;
  Hotel_Food_Interface hotel_food_interface;

    public Hotel_Adapter(List<String> name, List<String> price, List<String> details, List<String> photo, Context context,Hotel_Food_Interface hotel_food_interface) {
        this.name = name;
        this.price = price;
        this.details = details;
        this.photo = photo;
        this.context = context;
        this.hotel_food_interface=hotel_food_interface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.food_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Hotel_Adapter.ViewHolder holder, int position) {
        String url=photo.get(position);
        if(url.isEmpty()){
            //if photo null, this is default photo
            url="https://1080motion.com/wp-content/uploads/2018/06/NoImageFound.jpg.png";
        }
        holder.f_name.setText(name.get(position));
        holder.f_price.setText("Tk "+price.get(position));
        holder.f_details.setText(details.get(position));
        Picasso.get().load(""+url).into(holder.f_photo);

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView f_name,f_price,f_price_regular,f_details;
        ImageView f_photo;
        public ViewHolder(View itemView) {
            super(itemView);

            linearLayout=itemView.findViewById(R.id.liner_ID_Food);
            f_name=itemView.findViewById(R.id.food_item_name);
            f_price=itemView.findViewById(R.id.food_item_price);
            f_price_regular=itemView.findViewById(R.id.food_item_price_regular);
            f_details=itemView.findViewById(R.id.food_item_details);
            f_photo=itemView.findViewById(R.id.food_item_photo);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hotel_food_interface.open_bottomShitDialog(name.get(getAdapterPosition()),price.get(getAdapterPosition()),details.get(getAdapterPosition()),photo.get(getAdapterPosition()));
                }
            });

        }
    }
}
