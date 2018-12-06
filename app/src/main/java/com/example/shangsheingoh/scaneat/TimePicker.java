package com.example.shangsheingoh.scaneat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shangsheingoh.scaneat.Common.Common;
import com.example.shangsheingoh.scaneat.Database.Database;
import com.example.shangsheingoh.scaneat.Model.Order;
import com.example.shangsheingoh.scaneat.Model.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TimePicker extends AppCompatActivity {
    private android.widget.TimePicker timePicker1;
    int hour, minute;

    int slotCounter;
    FirebaseAuth authUser;

    String totalPrice;
    List<Order> cart =  new ArrayList<>();

    public static class Data2{
        public int slotCount;

        public int getSlotCount(){return slotCount;}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);
        timePicker1 = (android.widget.TimePicker) findViewById(R.id.timePicker1);

        Button confirmButton = (Button) findViewById(R.id.confirmButton2);
        Button returnButton = (Button) findViewById(R.id.returnButton2);

        Intent getIntent = getIntent();
        totalPrice = getIntent.getStringExtra("Request");
        authUser = FirebaseAuth.getInstance();

        DatabaseReference currentLocationCounter = FirebaseDatabase.getInstance().getReference().child("delivery").child("timeSlot");
        currentLocationCounter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TimePicker.Data2 data = dataSnapshot.getValue(TimePicker.Data2.class);
                Log.d("slotkaopeh", String.valueOf(data.getSlotCount()));
                slotCounter = data.getSlotCount();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cart = new Database(this).getCarts();


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = timePicker1.getCurrentHour();
                minute = timePicker1.getCurrentMinute();
                if (hour >= 11 && hour < 21) {

                    int hourMax;
                    if (hour == 23) {
                        hourMax = 0;
                    } else {
                        hourMax = hour + 1;
                    }
                    DatabaseReference dbAddTimeSlot = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference dbAddTime = dbAddTimeSlot.child("delivery").child("timeSlot").child("slotList").child("slot_" + slotCounter).child("pickUpTime");
                    DatabaseReference dbAddSlotID = dbAddTimeSlot.child("delivery").child("timeSlot").child("slotList").child("slot_" + slotCounter).child("slotID");
                    DatabaseReference dbAddUserID = dbAddTimeSlot.child("delivery").child("timeSlot").child("slotList").child("slot_" + slotCounter).child("userID");
                    DatabaseReference dbAddUserName = dbAddTimeSlot.child("delivery").child("timeSlot").child("slotList").child("slot_" + slotCounter).child("userName");
                    dbAddSlotID.setValue("slot_" + slotCounter);
                    dbAddTime.setValue(String.valueOf(hour) + String.valueOf(minute) + "-" + String.valueOf(hourMax) + String.valueOf(minute));
                    slotCounter++;
                    DatabaseReference dbAddSlotCounter = dbAddTimeSlot.child("delivery").child("timeSlot").child("slotCount");
                    dbAddSlotCounter.setValue(slotCounter);
                    Toast.makeText(TimePicker.this, "Time slot added!", Toast.LENGTH_LONG).show();
                    finish();

                    Request request = new Request(
                            authUser.getUid(),
                            Common.currentUser.getUserName(),
                            totalPrice,
                            cart
                    );
                    String userid = request.getPhone();
                    String username = request.getName();
                    dbAddUserID.setValue(userid);
                    dbAddUserName.setValue(username);
                    DatabaseReference dbAddRequest = FirebaseDatabase.getInstance().getReference("Request");
                    dbAddRequest.child(String.valueOf(System.currentTimeMillis()))
                            .setValue(request);
                    new Database(getBaseContext()).cleanCart();
                    Toast.makeText(TimePicker.this, "Thank you, Order Placed", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(TimePicker.this, "Please enter time between 11am - 9pm", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

}

