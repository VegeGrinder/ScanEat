package com.example.shangsheingoh.scaneat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PinDeliveryLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng globalLatLng = null;
    double latitude ;
    double longitude ;
    boolean onButton = false ;

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

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton2);

        floatingActionButton.setVisibility(View.GONE);
        if (onButton = true){
            floatingActionButton.setVisibility(View.VISIBLE);
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latitude = globalLatLng.latitude;
                longitude = globalLatLng.longitude;
                Intent intent = new Intent(PinDeliveryLocation.this, PopUp.class);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("userID",finalUserID);
                intent.putExtra("pickupTime",finalpickupTime);
                intent.putExtra("slotID",finalslotID);
                PinDeliveryLocation.this.startActivity(intent);

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
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("You are here")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                onButton = true;
            }
        });

    }
}
