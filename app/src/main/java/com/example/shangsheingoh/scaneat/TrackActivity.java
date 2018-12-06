package com.example.shangsheingoh.scaneat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shangsheingoh.scaneat.Common.Common;
import com.example.shangsheingoh.scaneat.Interface.ItemClickListener;
import com.example.shangsheingoh.scaneat.Model.Request;
import com.example.shangsheingoh.scaneat.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.shangsheingoh.scaneat.Common.Common1.convertCodeToStatus;

public class TrackActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference deliveryRef;
    private FirebaseAuth firebaseAuth;

    TextView track_id,track_status,track_phone,track_address;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        database = FirebaseDatabase.getInstance();
        deliveryRef = database.getReference("delivery").child("timeSlot").child("slotList").child("slot_1").child("deliverList").child("oQW3qoO9Treli6uWMk5dKqTkmXu2").child("1544049830950");
        firebaseAuth=FirebaseAuth.getInstance();

        track_address = (TextView)findViewById(R.id.track_address);
        track_id = (TextView)findViewById(R.id.track_id);
        track_status = (TextView)findViewById(R.id.track_status);
        track_phone = (TextView)findViewById(R.id.track_phone);
        button = (Button)findViewById(R.id.button);

        track_id.setText(deliveryRef.getKey());
        deliveryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot abc:dataSnapshot.getChildren()){
                String aaa = dataSnapshot.child("status").getValue().toString();
                String def = dataSnapshot.child("phone").getValue().toString();

                track_status.setText(convertCodeToStatus(aaa));
                track_phone.setText(def);}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
     //   track_status.setText(deliveryRef.child("status")..toString());
     //   track_phone.setText(deliveryRef.child("orderId").toString());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrackActivity.this,QRCodeGen.class);
                intent.putExtra("text",track_id.getText().toString());
                startActivity(intent);
            }
        });
//        recyclerView = (RecyclerView)findViewById(R.id.listTracker);
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);

//        if(getIntent()==null){
//        deliveryRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot i:dataSnapshot.getChildren()){
//                    Log.d("check1",i.child("userID").toString());
//                    String slotid = i.child("slotID").getValue().toString();
//                    Log.d("check1.5",slotid);
//                 //    if(i.child("userID").getValue().toString().equals(firebaseAuth.getUid())){
//                        for(DataSnapshot j:i.child("deliverList").getChildren()){
//                            String tempuserid = j.child("tempUserID").getValue().toString();
//                            Log.d("check2",tempuserid);
//                            loadOrders(tempuserid);
//                        }
//                    //    deliveryRef.child(slotid).child("deliverList").
//                     //   loadOrders(slotid);
//
//                }
//            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        }
//        else{
//            loadOrders(getIntent().getStringExtra("userPhone"));
//        }
    }

//    private void loadOrders(String slotid) {
//        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
//                Request.class,
//                R.layout.order_layout,
//                OrderViewHolder.class,
//                deliveryRef.child("slot_1").child("deliverList").child(slotid).child("1544049830950")
//        ) {
//            @Override
//            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
//                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
//                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
//                viewHolder.txtOrderAddress.setText(model.getAddress());
//                viewHolder.txtOrderPhone.setText(model.getPhone());
//
//                viewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//                        Intent QRcode = new Intent(TrackActivity.this,QRCodeGen.class);
//                        QRcode.putExtra("text",adapter.getRef(position).getKey());
//                        startActivity(QRcode);
//                    }
//                });
//            }
//        };
//        recyclerView.setAdapter(adapter);
//    }


}
