package com.example.shangsheingoh.scaneat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shangsheingoh.scaneat.Common.Common;
import com.example.shangsheingoh.scaneat.Common.Common1;
import com.example.shangsheingoh.scaneat.Model.User;
import com.example.shangsheingoh.scaneat.Model.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    EditText editPhone, editPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editPassword = (MaterialEditText)findViewById(R.id.editPassword);
        editPhone = (MaterialEditText)findViewById(R.id.editPhone);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final ProgressDialog nDialog = new ProgressDialog(SignIn.this);
                nDialog.setMessage("Please wait");
                nDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child(editPhone.getText().toString()).exists()){
                            nDialog.dismiss();
                            User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                            user.setPhone(editPhone.getText().toString());
                            if(user.getPassword().equals(editPassword.getText().toString())){

                                Intent homeIntent = new Intent(SignIn.this, Home.class);
                                Common1.currentUser = user;
                                startActivity(homeIntent);
                                finish();

                                Toast.makeText(SignIn.this, "Sign in sucessfully !", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(SignIn.this, "Sign in failed!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            nDialog.dismiss();
                            Toast.makeText(SignIn.this, "User not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
