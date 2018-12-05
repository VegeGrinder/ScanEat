package com.example.shangsheingoh.scaneat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.shangsheingoh.scaneat.Common.Common;
import com.example.shangsheingoh.scaneat.Model.Request;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PinDeliveryLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng globalLatLng = null;
    double latitude ;
    double longitude ;
    boolean onButton = false ;
    String addressLine = null;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_delivery_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        String userID = null;
        String pickupTime = null;
        String slotID = null;

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            userID =(String) b.get("userID");
            pickupTime = (String) b.get("pickupTime");
            slotID = (String) b.get("slotID");
        }

        final String finalUserID = userID;
        final String finalpickupTime = pickupTime;
        final String finalslotID = slotID;

        final String requesterName = Common.currentUser.getUserName();
        final String requesterID = Common.currentUser.getUserPhone();
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton2);

        floatingActionButton.setVisibility(View.GONE);
        if (onButton = true){
            floatingActionButton.setVisibility(View.VISIBLE);
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(PinDeliveryLocation.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(PinDeliveryLocation.this);
                }
                builder.setTitle("Confirm details")
                        .setMessage("Are you sure the information is correct?\n"+"Time:"+finalpickupTime+"\n"+"User:"+finalUserID+"\n"+"Location:\n"+addressLine)
                        //   .setMessage("Time:"+finalpickupTime+"/n")
                        //   .setMessage("User:"+finalUserID+"/n")
                        //   .setMessage("Location:"+addressLine+"/n")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                DatabaseReference temporaryLocation = FirebaseDatabase.getInstance().getReference("delivery").child("timeSlot").child("slotList").child(finalslotID);
                                DatabaseReference addtemporaryLocationLat = temporaryLocation.child("temporaryList").child(requesterID).child("latitude");
                                DatabaseReference addtemporaryLocationLong = temporaryLocation.child("temporaryList").child(requesterID).child("longitude");
                                DatabaseReference addtemporaryUserID = temporaryLocation.child("temporaryList").child(requesterID).child("tempUserID");
                                DatabaseReference addtemporaryUserName =  temporaryLocation.child("temporaryList").child(requesterID).child("tempUserName");

                                addtemporaryLocationLat.setValue(latitude);
                                addtemporaryLocationLong.setValue(longitude);
                                addtemporaryUserID.setValue(requesterID);
                                addtemporaryUserName.setValue(requesterName);
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });



    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMinZoomPreference((float) 11.5);
        LatLng penangLocation = new LatLng(5.384873,100.254162);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(penangLocation));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                globalLatLng = latLng;
                latitude = globalLatLng.latitude;
                longitude = globalLatLng.longitude;

                Geocoder gcd = new Geocoder(PinDeliveryLocation.this,Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(latitude,longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size()>0){
                    addressLine = addresses.get(0).getAddressLine(0);
                    Log.d("o0o",addressLine);
                    // holder.textViewAddress.setText(addressLine);
                }

                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(addressLine)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                onButton = true;
            }
        });

    }
}
