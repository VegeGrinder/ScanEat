package com.example.shangsheingoh.scaneat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shangsheingoh.scaneat.Model.TemporaryDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AcceptAdapter extends RecyclerView.Adapter<AcceptAdapter.LocationViewHolder> {

    int locationCounter;

    public static class Data1{
        public int locationCount;

        public int getLocationCount(){return locationCount;}
    }

    private Context mCtx;

    String slotID ;
    String address ;
    String requesterName ;
    String requesterID;

    private List<TemporaryDetails> tempList = new ArrayList<>();

    double finallatitude, finallongitude;

    public AcceptAdapter(Context mCtx, List<TemporaryDetails> tempList) {
        this.mCtx = mCtx;
        this.tempList = tempList;
    }

    @Override
    public AcceptAdapter.LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.accept_row,parent, false);
        return new AcceptAdapter.LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AcceptAdapter.LocationViewHolder holder, int position) {
        //getting the product of the specified position
        TemporaryDetails accept = tempList.get(position);

        double finallatitude1 = accept.getLatitude();
        double finallongitude1 = accept.getLongitude();

        Geocoder gcd = new Geocoder(mCtx,Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(finallatitude1,finallongitude1,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size()>0){
            String addressLine = addresses.get(0).getAddressLine(0);
            Log.d("o0o",addressLine);
            holder.textViewAddress.setText(addressLine);
        }

        //binding the data with the viewholder views
        holder.textViewUser.setText(accept.getTempUserName());
        holder.textViewSlot.setText(String.valueOf(accept.getSlotID()));

        holder.textViewrequesterID.setText(accept.getTempUserID());
        holder.textviewLat.setText(String.valueOf(accept.getLatitude()));
        holder.textviewLong.setText(String .valueOf(accept.getLongitude()));

        holder.textviewLat.setTextColor(Color.rgb(0,0,0));
        holder.textviewLong.setTextColor(Color.rgb(0,0,0));
        holder.textViewrequesterID.setTextColor(Color.rgb(0,0,0));
    }

    @Override
    public int getItemCount() {
        return tempList.size();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {

        TextView textViewAddress, textViewUser, textViewSlot, textViewrequesterID, textviewLat,textviewLong;
        ImageView imageView;

        public LocationViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    slotID = textViewSlot.getText().toString();
                    address = textViewAddress.getText().toString();
                    requesterName = textViewUser.getText().toString();
                    requesterID =textViewrequesterID.getText().toString();
                    finallatitude = Double.parseDouble(textviewLat.getText().toString());
                    finallongitude = Double.parseDouble(textviewLong.getText().toString());


                    DatabaseReference currentLocationCounter =FirebaseDatabase.getInstance().getReference().child("delivery").child("timeSlot").child("slotList").child(slotID);
                    currentLocationCounter.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ConfirmDelivery.Data1 data = dataSnapshot.getValue(ConfirmDelivery.Data1.class);
                            Log.d("locationkaopeh", String.valueOf(data.getLocationCount()));
                            locationCounter = data.getLocationCount();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    Context context = v.getContext();


                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(context);
                    }
                    builder.setTitle("Willing to deliver?")
                            .setMessage("Requester information?\n"+"Requester:"+requesterName+"\n"+"Deliver Location:\n"+address)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                    DatabaseReference addDBlatitude = databaseReference.child("delivery").child("timeSlot").child("slotList").child(slotID).child("deliverList").child("Location_" + locationCounter + "").child("latitude");
                                    DatabaseReference addDBlongitude = databaseReference.child("delivery").child("timeSlot").child("slotList").child(slotID).child("deliverList").child("Location_" + locationCounter + "").child("longitude");
                                    DatabaseReference addDBrequesterName = databaseReference.child("delivery").child("timeSlot").child("slotList").child(slotID).child("deliverList").child("Location_" + locationCounter + "").child("requesterName");
                                    DatabaseReference addDBrequesterID = databaseReference.child("delivery").child("timeSlot").child("slotList").child(slotID).child("deliverList").child("Location_" + locationCounter + "").child("requesterID");

                                    addDBlatitude.setValue(finallatitude);
                                    addDBlongitude.setValue(finallongitude);
                                    addDBrequesterName.setValue(requesterName);
                                    addDBrequesterID.setValue(requesterID);

                                    // RMB TO DELETE TEMPORARY LIST

                                    DatabaseReference dbcurrentLocationCount = databaseReference.child("delivery").child("timeSlot").child("slotList").child(slotID).child("locationCount");
                                    locationCounter++;
                                    dbcurrentLocationCount.setValue(locationCounter);

                                    DatabaseReference deleteTempRequest = databaseReference.child("delivery").child("slotList").child(slotID).child("temporaryList").child(requesterID);
                                    deleteTempRequest.removeValue();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                 /*   Intent intent = new Intent(context, confirmDelivery.class);
                    intent.putExtra("address",textViewAddress.getText());
                    intent.putExtra("user",textViewUser.getText());
                    intent.putExtra("latitude",finallatitude);
                    intent.putExtra("longitude",finallongitude);
                    intent.putExtra("slotID",textViewSlot.getText());
                    context.startActivity(intent); */

                }
            });

            textViewAddress = itemView.findViewById(R.id.poptextViewAddress);
            textViewUser = itemView.findViewById(R.id.poptextViewUserName);
            textViewSlot = itemView.findViewById(R.id.poptextViewSlot);
            textviewLat = itemView.findViewById(R.id.textView14);
            textviewLong = itemView.findViewById(R.id.textView15);
            textViewrequesterID = itemView.findViewById(R.id.textView69);
            //  textViewPrice = itemView.findViewById(R.id.textViewPrice);
            //  imageView = itemView.findViewById(R.id.imageView);
        }
    }

}

