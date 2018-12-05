package com.example.shangsheingoh.scaneat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shangsheingoh.scaneat.Model.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class register extends AppCompatActivity {
    private EditText username,userpassword,useremail,useric,userphone;
    private Button registerbutton;
    private FirebaseAuth firebaseAuth;
    String name,email,password,ic,phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUIviews();
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    String user_email= useremail.getText().toString().trim();
                    String user_password= userpassword.getText().toString().trim();
                    firebaseAuth= FirebaseAuth.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendUserData();
                                firebaseAuth.signOut();
                                Toast.makeText(register.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(register.this,MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(register.this,"Registration Fail",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    private void setupUIviews(){
        username=findViewById(R.id.etusername);
        userpassword=findViewById(R.id.etuserpassword);
        useremail=findViewById(R.id.etemail) ;
        useric=findViewById(R.id.etic);
        userphone=findViewById(R.id.etPhone);
        registerbutton=findViewById(R.id.bttnregister);
    }

    private Boolean validate(){
        Boolean result =false;
        name = username.getText().toString();
        password= userpassword.getText().toString();
        email= useremail.getText().toString();
        ic = useric.getText().toString();
        phone=userphone.getText().toString();


        if(name.isEmpty() || password.isEmpty()|| email.isEmpty()||ic.isEmpty()||phone.isEmpty()){
            Toast.makeText(this,"Please enter all the details",Toast.LENGTH_SHORT).show();
        }
        else{
            result = true;
        }
        return result;

    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
        DatabaseReference Ref = firebaseDatabase.getReference("User Information").child(firebaseAuth.getUid());
        UserProfile userProfile= new UserProfile(name,email,ic,phone);
        Ref.setValue(userProfile);
    }
}


