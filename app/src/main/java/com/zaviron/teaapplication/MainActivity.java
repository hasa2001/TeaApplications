package com.zaviron.teaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.zaviron.teaapplication.model.User;

public class MainActivity extends AppCompatActivity {

    private EditText customer_number;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firestore = FirebaseFirestore.getInstance();
        findViewById(R.id.addDailybills).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddDailyBillsMainActivity.class));
            }
        });

        findViewById(R.id.addUsers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View dialog = getLayoutInflater().inflate(R.layout.customer_register_dialog, null);
                builder.setView(dialog);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                customer_number = dialog.findViewById(R.id.customer_number);
                dialog.findViewById(R.id.customer_register_btn).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (customer_number.getText().toString().isEmpty()) {
                            Toast.makeText(getApplicationContext(), "නව ගනුදෙනු කර්නුවන්ගේ අංකය ඇතුලත් කරන්න", Toast.LENGTH_LONG).show();
                        } else {

                            firestore.collection("user").whereEqualTo("id", customer_number.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot snapshot = task.getResult();
                                        if (snapshot != null && !snapshot.isEmpty()) {
                                            Toast.makeText(getApplicationContext(), "ගනුදෙනුකරු දැනටමත් ලියපදිංචි වී ඇත", Toast.LENGTH_LONG).show();
                                        } else {
                                            User user = new User(customer_number.getText().toString());

                                            firestore.collection("user").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(getApplicationContext(), "නව ගනුදෙනු කරුවෙකු ලියාපදිංචි කරන ලදී.", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }

                                    }
                                }
                            });

                        }
                    }
                });

            }
        });

        findViewById(R.id.ViewDailyBills).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewDailyBillsActivity.class));
            }
        });

    }


}