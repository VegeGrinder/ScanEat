package com.example.shangsheingoh.scaneat;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.shangsheingoh.scaneat.Common.Common;
import com.example.shangsheingoh.scaneat.Model.TemporaryDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AcceptActivity extends AppCompatActivity {

    FirebaseAuth authUser;

    //a list to store all the products
    List<TemporaryDetails> acceptList= new ArrayList<>();

    //the recyclerview
    RecyclerView recyclerView1;

    public String userName2;
    double gloLatitude, gloLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);

        //getting the recyclerview from xml
        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerView1);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        authUser = FirebaseAuth.getInstance();
        final AcceptAdapter adapter1 = new AcceptAdapter(this, acceptList);

        DatabaseReference acceptSlot = FirebaseDatabase.getInstance().getReference("delivery").child("timeSlot").child("slotList");
        acceptSlot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                    if (dataSnapshot2.child("userID").getValue().toString().equals(authUser.getUid())) {
                        //    String slotIden = dataSnapshot2.child("slotID").toString();
                        for (DataSnapshot hohoho : dataSnapshot2.child("temporaryList").getChildren()) {
                            TemporaryDetails accept = hohoho.getValue(TemporaryDetails.class);
                            String userid = accept.getTempUserID();
                            String username = accept.getTempUserName();
                            double lat = accept.getLatitude();
                            double lon = accept.getLongitude();
                            String slot = dataSnapshot2.child("slotID").getValue().toString();
                            Log.d("alalala", username + lat + lon + slot);
                            acceptList.add(new TemporaryDetails(userid,username, lat, lon, slot));
                        }
                    }
                }
                adapter1.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerView1.setAdapter(adapter1);


    }
}
