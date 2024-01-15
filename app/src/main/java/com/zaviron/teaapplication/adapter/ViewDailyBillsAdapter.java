package com.zaviron.teaapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.zaviron.teaapplication.R;
import com.zaviron.teaapplication.model.TeaDetails;

import java.util.ArrayList;

public class ViewDailyBillsAdapter extends RecyclerView.Adapter<ViewDailyBillsAdapter.ViewHolder> {
    private ArrayList<TeaDetails> teaDetails;
    private Context context;

    public ViewDailyBillsAdapter(ArrayList<TeaDetails> teaDetails, Context context) {
        this.teaDetails = teaDetails;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewDailyBillsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.daily_bills_item_view, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewDailyBillsAdapter.ViewHolder holder, int position) {
        TeaDetails details = teaDetails.get(position);
        holder.customer_id.setText(details.getUser_id());
        holder.date.setText(details.getDate());
        holder.total_kg.setText(details.getTotal_tea().toString());
        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ok");
            }
        });

    }

    @Override
    public int getItemCount() {
        return teaDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView customer_id, date, total_kg;
        ImageView deleteItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customer_id = itemView.findViewById(R.id.dailyBillsViewCustomerID);
            date = itemView.findViewById(R.id.dailyBillsViewDate);
            total_kg = itemView.findViewById(R.id.dailyBillsViewTotalTeaWeight);
            deleteItem = itemView.findViewById(R.id.dailyBillsViewDeleteItemBtn);


        }


    }
}
