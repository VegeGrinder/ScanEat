package com.example.shangsheingoh.scaneat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.shangsheingoh.scaneat.Common.Common;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    protected SupportMapFragment mapFragment;
    Location globalLocation = null;
    double latitude;
    double longitude;
    int locationCounter;
   // String userID1 = "Super Lau";
  //  String userID2 = "Bobby Tan";
   // String userID3 = "Jason";

    ArrayList<String> locationList = new ArrayList<String>();

    public String slotIDD, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new locationListener();

        final String userID = Common.currentUser.getUserPhone();
        String userName =Common.currentUser.getUserName();

        DatabaseReference deliverLocation1 = FirebaseDatabase.getInstance().getReference("delivery").child("timeSlot").child("slotList");
        deliverLocation1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("okok", "huhu");
                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                    if (locationSnapshot.child("userID").getValue().toString().equals(userID)) {
                        //         userName = locationSnapshot.child("deliverList").child("requester").getValue().toString();
                        slotIDD = locationSnapshot.child("slotID").getValue().toString();
                        setmMap();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

     /*   DatabaseReference currentLocationCounter =FirebaseDatabase.getInstance().getReference("mapItem");
        currentLocationCounter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Data data = dataSnapshot.getValue(Data.class);
                Log.d("locationcounter", String.valueOf(data.getLocationCount()));
                locationCounter = data.getLocationCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }); */


        FloatingActionButton FAButton = findViewById(R.id.floatingActionButton);
        FAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latitude = globalLocation.getLatitude();
                longitude = globalLocation.getLongitude();
                LatLng currentLocation = new LatLng(latitude, longitude);
                //   mMap.setMinZoomPreference(18);
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                /*   DatabaseReference currentLocation1 = FirebaseDatabase.getInstance().getReference();

                DatabaseReference dbcurrentLatitude = currentLocation1.child("mapItem").child("locationList").child("Location_" + locationCounter + "").child("Latitude");
                DatabaseReference dbcurrentLongitude = currentLocation1.child("mapItem").child("locationList").child("Location_" + locationCounter + "").child("Longitude");
                dbcurrentLatitude.setValue(latitude);
                dbcurrentLongitude.setValue(longitude);
                DatabaseReference dbcurrentLocationCount = currentLocation1.child("locationCount");
                locationCounter++;
                dbcurrentLocationCount.setValue(locationCounter);

*/
            }
        });
        if (Build.VERSION.SDK_INT < 23) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        }


    }


    public class locationListener implements LocationListener {
        public void onLocationChanged(Location location) {
            globalLocation = location;
            latitude = globalLocation.getLatitude();
            longitude = globalLocation.getLongitude();
        }

        public void onProviderDisabled(String provider) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }

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

    public void setmMap() {

        final Geocoder gcd = new Geocoder(this, Locale.getDefault());

        DatabaseReference deliverLocation2 = FirebaseDatabase.getInstance().getReference().child("delivery").child("timeSlot").child("slotList").child(slotIDD).child("deliverList");
        deliverLocation2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMap.clear();
                for (DataSnapshot locationSnapshot2 : dataSnapshot.getChildren()) {
                  //  locationList.add(locationSnapshot2.getValue().toString());
                    String stringlat = locationSnapshot2.child("latitude").getValue().toString();
                    String stringlong = locationSnapshot2.child("longitude").getValue().toString();
                    String requestername = locationSnapshot2.child("requesterName").getValue().toString();
                    Log.d("dicky",stringlat+" "+stringlong+" ");
                    double showLatitude = Double.parseDouble(stringlat);
                    double showLongitude = Double.parseDouble(stringlong);
                    String address=null;

                    List<Address> addresses = null;
                    try {
                        addresses = gcd.getFromLocation(showLatitude,showLongitude,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses.size()>0){
                        String addressLine = addresses.get(0).getAddressLine(0);
                        address= addressLine;
                    }

                    LatLng addMarker = new LatLng(showLatitude, showLongitude);
                    mMap.addMarker(new MarkerOptions().position(addMarker).title(address).snippet(requestername));
                }
              /*   for (int i = 0; i < locationList.size(); i++) {
                    Log.d("babi", locationList.get(i));
                    String hohoho = "";
                    String splitEqual[] = locationList.get(i).split("=");

                    String splitOther1[] = splitEqual[2].split(Pattern.quote(","));
                    final double showLatitude = Double.parseDouble(splitOther1[0]);

                    String splitOther2[] = splitEqual[3].split(Pattern.quote("}"));
                    final double showLongitude = Double.parseDouble(splitOther2[0]);

                    String splitOther3[] =splitEqual[1].split(Pattern.quote(","));
                    userName = splitOther3[0]; */



            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference((float) 11.5);
        LatLng penangLocation = new LatLng(5.384873, 100.254162);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(penangLocation));


        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                double desLat, desLong;
                LatLng destination = marker.getPosition();
                desLat = destination.latitude;
                desLong = destination.longitude;

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/dir/?api=1&origin="+latitude+","+longitude+"&destination="+desLat+","+desLong+"&travelmode=driving"));
                startActivity(intent);
            }
        });
     /*   DatabaseReference deliverLocation = FirebaseDatabase.getInstance().getReference("mapItem");
        deliverLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> locationList = new ArrayList<String>();
                for (DataSnapshot locationSnapshot : dataSnapshot.child("locationList").getChildren()){
                    locationList.add(locationSnapshot.getValue().toString());
                }*/
     /*   mMap.clear();
        for (int i = 0; i < locationList.size(); i++) {
            Log.d("babi", locationList.get(i));
            String splitEqual[] = locationList.get(i).split("=");

            String splitOther1[] = splitEqual[1].split(Pattern.quote(","));
            double showLatitude = Double.parseDouble(splitOther1[0]);

            String splitOther2[] = splitEqual[2].split(Pattern.quote("}"));
            double showLongitude = Double.parseDouble(splitOther2[0]);

            LatLng addMarker = new LatLng(showLatitude, showLongitude);
            mMap.addMarker(new MarkerOptions().position(addMarker).title("Current Location"));
        }
    }

        // Add a marker in Sydney and move the camera
     //   LatLng sydney = new LatLng(-34, 151);
    //    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
     //   mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney)); */


    }
}

