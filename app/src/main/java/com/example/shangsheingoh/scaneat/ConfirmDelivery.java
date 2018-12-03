package com.example.shangsheingoh.scaneat;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ConfirmDelivery extends AppCompatActivity {

    int locationCounter;

    public static class Data1{
        public int locationCount;

        public int getLocationCount(){return locationCount;}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_delivery);

        Button accept = (Button)findViewById(R.id.acceptButton);
        Button reject = (Button) findViewById(R.id.rejectButton);

        String slotID = null;
        Double latitude = null;
        Double longitude =null;

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            slotID = (String) b.get("slotID");
            latitude = (Double) b.get("latitude");
            longitude = (Double) b.get("longitude");
        }
        final String finalslotID = slotID;
        final Double finallatitude = latitude;
        final Double finallongitude = longitude;

        TextView nearLocation = findViewById(R.id.textView7);

        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(finallatitude,finallongitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size()>0){
            String addressLine = addresses.get(0).getAddressLine(0);
            nearLocation.setText(addressLine);
        }


        DatabaseReference currentLocationCounter =FirebaseDatabase.getInstance().getReference().child("delivery").child("timeSlot").child("slotList").child(finalslotID);
        currentLocationCounter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Data1 data = dataSnapshot.getValue(Data1.class);
                Log.d("locationkaopeh", String.valueOf(data.getLocationCount()));
                locationCounter = data.getLocationCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("lilala",finalslotID);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference addDBlatitude = databaseReference.child("delivery").child("timeSlot").child("slotList").child(finalslotID).child("deliverList").child("Location_" + locationCounter + "").child("latitude");
                DatabaseReference addDBlongitude = databaseReference.child("delivery").child("timeSlot").child("slotList").child(finalslotID).child("deliverList").child("Location_" + locationCounter + "").child("longitude");
                addDBlatitude.setValue(finallatitude);
                addDBlongitude.setValue(finallongitude);

                DatabaseReference dbcurrentLocationCount = databaseReference.child("delivery").child("timeSlot").child("slotList").child(finalslotID).child("locationCount");
                locationCounter++;
                dbcurrentLocationCount.setValue(locationCounter);

            }
        });
    }
}
