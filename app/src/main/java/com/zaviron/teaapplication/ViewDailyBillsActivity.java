package com.zaviron.teaapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.zaviron.teaapplication.adapter.ViewDailyBillsAdapter;
import com.zaviron.teaapplication.model.TeaDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ViewDailyBillsActivity extends AppCompatActivity {
    EditText customerId;
    CalendarView calendarView;
    private ArrayList<TeaDetails> teaDetails;
    private FirebaseFirestore firestore;

    private ImageView filterImage;
    private ViewDailyBillsAdapter viewDailyBillsAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_daily_bills);
        searchView = findViewById(R.id.dailySearchBar);
        searchView.clearFocus();
        firestore = FirebaseFirestore.getInstance();
        teaDetails = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.dailyBillViewRecyclerView);
        viewDailyBillsAdapter = new ViewDailyBillsAdapter(teaDetails, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(viewDailyBillsAdapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                search(newText);
                return false;
            }
        });

        firestore.collection("dailyBills").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                for (DocumentChange change : value.getDocumentChanges()) {
                    TeaDetails details = change.getDocument().toObject(TeaDetails.class);
                    switch (change.getType()) {
                        case ADDED:
                            teaDetails.add(details);
                        case MODIFIED:

                            teaDetails.add(details);

                        case REMOVED:
                            teaDetails.remove(details);
                    }

                }
                viewDailyBillsAdapter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.filterImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewDailyBillsActivity.this);
                View dialog = getLayoutInflater().inflate(R.layout.filter_daily_bills, null);

                AlertDialog alertDialog = builder.setView(dialog).create();
                alertDialog.show();
                customerId = dialog.findViewById(R.id.filterCustomerId);
                calendarView = dialog.findViewById(R.id.filterCalendarView);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        calendarView.setDate(calendar.getTimeInMillis());


                    }
                });


                dialog.findViewById(R.id.filterSearchBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String format = simpleDateFormat.format(calendarView.getDate());

                        if (customerId.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "කරුණාකර ගනුදරුගේ අංකය ඇතුලත් ක්‍රරන්න්.", Toast.LENGTH_LONG).show();
                        } else {

                            System.out.println("Customer id: " + customerId.getText().toString() + " " + "Date: " + calendarView.getDate());
                            firestore.collection("dailyBills").whereEqualTo("user_id", customerId.getText().toString()).whereEqualTo("date", format).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    teaDetails.clear();
                                    if (value!=null){
                                        for (DocumentChange change : value.getDocumentChanges()) {
                                            TeaDetails details = change.getDocument().toObject(TeaDetails.class);
                                            switch (change.getType()) {
                                                case ADDED:
                                                    teaDetails.add(details);
                                                case MODIFIED:
                                                    teaDetails.add(details);

                                                case REMOVED:
                                                    teaDetails.remove(details);
                                            }

                                        }
                                        viewDailyBillsAdapter.notifyDataSetChanged();
                                    }

                                }
                            });
                            alertDialog.dismiss();

                        }

                    }
                });


            }
        });


    }

    public void search(String text) {
        firestore.collection("dailyBills").whereGreaterThanOrEqualTo("user_id", text).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                teaDetails.clear();
                for (DocumentChange change : value.getDocumentChanges()) {
                    TeaDetails details = change.getDocument().toObject(TeaDetails.class);
                    switch (change.getType()) {
                        case ADDED:
                            teaDetails.add(details);
                        case MODIFIED:
                            teaDetails.add(details);

                        case REMOVED:
                            teaDetails.remove(details);
                    }

                }
                viewDailyBillsAdapter.notifyDataSetChanged();
            }
        });
    }


}