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

public class All_Hotel_Adapter extends RecyclerView.Adapter<All_Hotel_Adapter.ViewHolder> {

    List<String> name;
    List<String> photo;
    List<String> email;
    List<String> id;
    Context context;
    Hotel_interface hotel_interface;

    public All_Hotel_Adapter(Context context,List<String> name,List<String> photo,List<String> email,List<String> id,Hotel_interface hotel_interface) {
        this.context = context;
        this.name=name;
        this.photo=photo;
        this.email=email;
        this.id=id;
        this.hotel_interface=hotel_interface;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.show_hotel,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull All_Hotel_Adapter.ViewHolder holder, int position) {
        String url=photo.get(position);
        if(url.isEmpty()){
            url="https://1080motion.com/wp-content/uploads/2018/06/NoImageFound.jpg.png";
        }
        Picasso.get().load(url).into(holder.hotel_image);
        holder.hotel_name.setText(name.get(position));

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView hotel_image;
        TextView hotel_name;
        LinearLayout linearLayout;
        int p;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hotel_image=itemView.findViewById(R.id.imageView_hotel);
            hotel_name=itemView.findViewById(R.id.name_hotel);
            linearLayout=itemView.findViewById(R.id.linerId_hotel);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hotel_interface.Go_activity(getAdapterPosition(),email.get(getAdapterPosition()),id.get(getAdapterPosition()));
                }
            });

        }
    }
}
