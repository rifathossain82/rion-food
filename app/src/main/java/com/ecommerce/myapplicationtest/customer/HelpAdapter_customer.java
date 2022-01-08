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

import java.util.List;

public class HelpAdapter_customer extends RecyclerView.Adapter<HelpAdapter_customer.MyViewHolder> {
    List<Integer> image;
    List<String> subject;
    Context context;

    public HelpAdapter_customer(List<Integer> image, List<String> subject, Context context) {
        this.image = image;
        this.subject = subject;
        this.context = context;
    }

    @Override
    public HelpAdapter_customer.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.help_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HelpAdapter_customer.MyViewHolder holder, int position) {
        holder.icon.setImageResource(image.get(position));
        holder.sub.setText(subject.get(position));
    }

    @Override
    public int getItemCount() {
        return subject.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ImageView icon;
        TextView sub;
        public MyViewHolder(View itemView) {
            super(itemView);
            linearLayout=itemView.findViewById(R.id.liner_id_help_item);
            icon=itemView.findViewById(R.id.icon_help_item);
            sub=itemView.findViewById(R.id.subject_help_item);


            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
