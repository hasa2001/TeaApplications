package com.zaviron.teaapplication.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zaviron.teaapplication.R;
import com.zaviron.teaapplication.ViewDailyBillsActivity;
import com.zaviron.teaapplication.model.TeaDetails;

import java.util.ArrayList;

public class ViewDailyBillsAdapter extends RecyclerView.Adapter<ViewDailyBillsAdapter.ViewHolder> {
    private ArrayList<TeaDetails> teaDetails;
    private Context context;
    private FirebaseFirestore firestore;

    public ViewDailyBillsAdapter(ArrayList<TeaDetails> teaDetails, Context context) {
        this.teaDetails = teaDetails;
        this.context = context;
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void showAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete item")
                .setMessage("Are you want to delete this ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("yes");
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        System.out.println("no");
                    }
                }).create().show();
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

                firestore.collection("dailyBills").whereEqualTo("user_id", details.getUser_id()).whereEqualTo("date", details.getDate()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot snapshot :task.getResult()){
                                snapshot.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        teaDetails.remove(position);
                                        notifyDataSetChanged();
                                    }
                                });
                            }
                            ViewDailyBillsAdapter.this.notifyDataSetChanged();
                        }
                    }
                });


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
