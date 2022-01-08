package com.ecommerce.myapplicationtest.chef;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.myapplicationtest.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{
    private List<String> name;
    private List<String> price;
    private List<String> details;
    private List<String> photo;
    private List<String> email;
    private List<String> key;
    private Context context;
    chef_interface chefInterface;


    public MainAdapter(List<String> name, List<String> price, List<String> details, List<String> photo, List<String> email, List<String> key, Context context,chef_interface chefInterface) {
        this.name = name;
        this.price = price;
        this.details = details;
        this.photo = photo;
        this.email = email;
        this.key = key;
        this.context = context;
        this.chefInterface=chefInterface;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        Picasso.get().load(photo.get(position)).into(holder.imageView);
        holder.textView.setText(name.get(position));
        holder.price_show.setText("Tk  "+price.get(position));
        holder.details_show.setText(details.get(position));

        holder.id=key.get(position);
        holder.name=name.get(position);
        holder.price=price.get(position);
        holder.details0=details.get(position);

    }

    @Override
    public int getItemCount() {
        return name.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,edit,delete;
        TextView textView,price_show,details_show;
        String id,name,price,details0;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.imageView_row);
            textView=itemView.findViewById(R.id.textView_row);
            price_show=itemView.findViewById(R.id.price_show_item);
            details_show=itemView.findViewById(R.id.details_show_item);
            edit=itemView.findViewById(R.id.edit_item_id);
            delete=itemView.findViewById(R.id.delete_item_id);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chefInterface.edit(id,name,price,details0);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chefInterface.delete(id);
                }
            });


        }
    }
}
