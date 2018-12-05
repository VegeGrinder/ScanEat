package com.example.shangsheingoh.scaneat;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shangsheingoh.scaneat.Model.UserProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class update_profile extends AppCompatActivity {
    private EditText newUserName, newUserIc, newUserPhone,newUserEmail;
    private Button save;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        newUserName= findViewById(R.id.etNameUpdate);
        newUserIc= findViewById(R.id.etIcUpdate);
        newUserPhone= findViewById(R.id.etPhoneUpdate);
        newUserEmail= findViewById(R.id.etEmailUpdate);
        save= findViewById(R.id.btnSave);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        final DatabaseReference databaseReference = firebaseDatabase.getReference("User Information").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                newUserName.setText(userProfile.getUserName());
                newUserPhone.setText(userProfile.getUserPhone());
                newUserIc.setText(userProfile.getUserIc());
                newUserEmail.setText(userProfile.getUserEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(update_profile.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= newUserName.getText().toString();
                String phone= newUserPhone.getText().toString();
                String ic= newUserIc.getText().toString();
                String email= newUserEmail.getText().toString();

                UserProfile userProfile= new UserProfile(name,email,ic,phone);
                databaseReference.setValue(userProfile);
                finish();
            }
        });

    }
}


