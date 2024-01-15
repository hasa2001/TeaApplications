package com.zaviron.teaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.zaviron.teaapplication.adapter.ViewDailyBillsAdapter;
import com.zaviron.teaapplication.model.TeaDetails;

import java.util.ArrayList;

public class ViewDailyBillsActivity extends AppCompatActivity {
    EditText customerId;
    CalendarView calendarView;
    private ArrayList<TeaDetails> teaDetails;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daily_bills);
        teaDetails = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.dailyBillViewRecyclerView);
        ViewDailyBillsAdapter viewDailyBillsAdapter = new ViewDailyBillsAdapter(teaDetails, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(viewDailyBillsAdapter);
        firestore = FirebaseFirestore.getInstance();

        firestore.collection("dailyBills").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    viewDailyBillsAdapter.notifyDataSetChanged();
                    for (DocumentSnapshot snapshot:task.getResult()){
                        TeaDetails teaDetails1 =snapshot.toObject(TeaDetails.class);
                        teaDetails.add(teaDetails1);
                    }
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "මෙම කාර්ය සිදු කිරීමට නොහැක.", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.filterImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewDailyBillsActivity.this);
                View dialog = getLayoutInflater().inflate(R.layout.filter_daily_bills, null);
                builder.setView(dialog).create().show();


                dialog.findViewById(R.id.filterSearchBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        customerId = dialog.findViewById(R.id.filterCustomerId);
                        calendarView = dialog.findViewById(R.id.filterCalendarView);

                        if (customerId.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "කරුණාකර ගනුදරුගේ අංකය ඇතුලත් ක්‍රරන්න්.", Toast.LENGTH_LONG).show();
                        } else {

                            System.out.println("Customer id: " + customerId.getText().toString() + " " + "Date: " + calendarView.getDate());
                        }

                    }
                });


            }
        });
    }
}