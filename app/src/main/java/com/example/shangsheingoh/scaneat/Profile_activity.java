package com.example.shangsheingoh.scaneat;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shangsheingoh.scaneat.Model.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile_activity extends AppCompatActivity {
    private TextView profileName,profilePhone,profileIc,profileEmail;
    private Button profileEdit;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);

        profileName=findViewById( R.id.tvUserName);
        profilePhone=findViewById( R.id.tvUserPhone);
        profileIc=findViewById(R.id.tvUserIc);
        profileEmail=findViewById(R.id.tvUserEmail);
        profileEdit=findViewById(R.id.btnEditProfile);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("User Information").child(firebaseAuth.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                profileName.setText("Name:"+ userProfile.getUserName());
                profilePhone.setText("Contact Number:"+ userProfile.getUserPhone());
                profileIc.setText("IC Number:"+ userProfile.getUserIc());
                profileEmail.setText("Email:"+userProfile.getUserEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Profile_activity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Profile_activity.this,update_profile.class);
                startActivity(intent);
            }
        });
    }
}



