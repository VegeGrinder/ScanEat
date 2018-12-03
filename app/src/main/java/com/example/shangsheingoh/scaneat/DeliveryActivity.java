package com.example.shangsheingoh.scaneat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.shangsheingoh.scaneat.Model.LocationDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryActivity extends AppCompatActivity {

    //a list to store all the products
    List<LocationDetails> slotList= new ArrayList<>();

    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final LocationAdapter adapter = new LocationAdapter(this, slotList);

        //initializing the productlist

        DatabaseReference timeSlot = FirebaseDatabase.getInstance().getReference();
        timeSlot.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child("delivery").child("timeSlot").child("slotList").getChildren()) {
                    LocationDetails user = dataSnapshot1.getValue(LocationDetails.class);
                    String abc = user.getPickUpTime();
                    String def = user.getUser();
                    String ghi = user.getSlotID();
                    slotList.add(new LocationDetails(def,abc,ghi));
                }
                adapter.notifyDataSetChanged();
                // Log.d("pukimamamama",slotList.toString());
                //      ArrayList<String> timeslotList = new ArrayList<String>();
                //      for (DataSnapshot timeslotSnapshot : dataSnapshot.child("slot list").getChildren()){
                //          timeslotList.add(timeslotSnapshot.getValue().toString());
                //     }
             /*   for (int i=0;i<timeslotList.size();i++){
                    Log.d("hehehehe",timeslotList.get(i));
                    String splitEqual[] = timeslotList.get(i).split("=");
                    String splitOther1[] = splitEqual[1].split(Pattern.quote(","));

                    String splitOther2[] = splitEqual[2].split(Pattern.quote("}")); // get 0

                    productList.add(new locationDetails(splitOther1[0],splitOther2[0]));
                } */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //  slotList.add(new locationDetails("gjhg","oijoij"));
//        Log.d("puki",slotList.get(0).getPickUpTime());
//        Log.d("puki",slotList.get(1).getPickUpTime());
        //adding some items to our list
     /*   productList.add(
                new locationDetails(
                        1,
                        "Apple MacBook Air Core i5 5th Gen - (8 GB/128 GB SSD/Mac OS Sierra)",
                        "13.3 inch, Silver, 1.35 kg",
                        4.3,
                        60000));

        productList.add(
                new locationDetails(
                        1,
                        "Dell Inspiron 7000 Core i5 7th Gen - (8 GB/1 TB HDD/Windows 10 Home)",
                        "14 inch, Gray, 1.659 kg",
                        4.3,
                        60000));

        productList.add(
                new locationDetails(
                        1,
                        "Microsoft Surface Pro 4 Core m3 6th Gen - (4 GB/128 GB SSD/Windows 10)",
                        "13.3 inch, Silver, 1.35 kg",
                        4.3,
                        60000));
                        */



        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }
}