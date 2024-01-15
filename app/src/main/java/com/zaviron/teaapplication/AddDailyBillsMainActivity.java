package com.zaviron.teaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.FirestoreGrpc;
import com.zaviron.teaapplication.model.TeaDetails;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddDailyBillsMainActivity extends AppCompatActivity {
    private final static String TAG = AddDailyBillsMainActivity.class.getName();
    private CalendarView calendarView;
    private TextView user_id, teaPacketPrice, totalRawTeaWeight, advancedFee, DolomiteFee, FertilizerFee;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_bills_main);
        calendarView = findViewById(R.id.calendarView);
        user_id = findViewById(R.id.user_id);
        teaPacketPrice = findViewById(R.id.teaPacket);
        totalRawTeaWeight = findViewById(R.id.totalTeaKg);
        advancedFee = findViewById(R.id.advancedFee);
        DolomiteFee = findViewById(R.id.dolomite);
        FertilizerFee = findViewById(R.id.fertilizerId);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String curDate = String.valueOf(dayOfMonth);
                String Year = String.valueOf(year);
                String Month = String.valueOf(month);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                calendarView.setDate(calendar.getTimeInMillis());
                Log.e("date", Year + "/" + Month + "/" + curDate);
            }
        });
        findViewById(R.id.addBillBtn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Double total_teaPacketPrice;
                String customer_id;
                Double total_raw_tea;
                Double total_advanced_fee;
                Double total_dolomite_fee;
                Double total_fertilizer_fee;
                if (user_id.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "කරුණාකර දලු සපයන්නාගේ අංකය ඇතුලත් කරන්න.", Toast.LENGTH_LONG).show();
                } else if (totalRawTeaWeight.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "කරුණාකර දලු ප්‍රමාණය ඇතුලත් කරන්න.(kg)", Toast.LENGTH_LONG).show();
                } else {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


                    String format = simpleDateFormat.format(calendarView.getDate());
                    System.out.println(format);

                    customer_id = user_id.getText().toString();
                    if (teaPacketPrice.getText().toString().isEmpty()) {
                        total_teaPacketPrice = 0.0;
                    } else {
                        total_teaPacketPrice = Double.parseDouble(teaPacketPrice.getText().toString());
                    }
                    if (advancedFee.getText().toString().isEmpty()) {
                        total_advanced_fee = 0.0;
                    } else {
                        total_advanced_fee = Double.parseDouble(advancedFee.getText().toString());
                    }
                    if (DolomiteFee.getText().toString().isEmpty()) {
                        total_dolomite_fee = 0.0;
                    } else {
                        total_dolomite_fee = Double.parseDouble(DolomiteFee.getText().toString());
                    }
                    if (FertilizerFee.getText().toString().isEmpty()) {
                        total_fertilizer_fee = 0.0;
                    } else {
                        total_fertilizer_fee = Double.parseDouble(FertilizerFee.getText().toString());
                    }

                    total_raw_tea = Double.parseDouble(totalRawTeaWeight.getText().toString());
                    firestore = FirebaseFirestore.getInstance();
                    firestore.collection("dailyBills").whereEqualTo("user_id", user_id.getText().toString()).whereEqualTo("date", format).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot result = task.getResult();

                                if (result != null && !result.isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "මෙම අංකයට අදාල දෙනික බිල්පතක් දැනටමත් සෑදී ඇත.", Toast.LENGTH_LONG).show();
                                    System.out.println(format);
                                } else {
                                    System.out.println(format + " " + customer_id + " " + total_teaPacketPrice.toString() + " " + total_raw_tea.toString() + " " + total_advanced_fee.toString() + " " + total_dolomite_fee.toString() + " " + total_fertilizer_fee.toString());
                                    Log.i(TAG, String.valueOf(format));
                                    TeaDetails teaDetails = new TeaDetails(customer_id, format, total_teaPacketPrice, total_raw_tea, total_advanced_fee, total_dolomite_fee, total_fertilizer_fee);
                                    firestore.collection("dailyBills").add(teaDetails).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "සාර්තකයි ", Toast.LENGTH_LONG).show();
                                            user_id.setText("");
                                            teaPacketPrice.setText("");
                                            totalRawTeaWeight .setText("");
                                            advancedFee.setText("");
                                            DolomiteFee.setText("");
                                            FertilizerFee.setText("");


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "මෙම කාර්ය සිදු කිරීමට නොහැක.", Toast.LENGTH_LONG).show();
                        }
                    });


                }


            }
        });
    }
}