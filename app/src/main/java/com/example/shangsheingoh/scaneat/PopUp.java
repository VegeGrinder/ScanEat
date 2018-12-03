package com.example.shangsheingoh.scaneat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PopUp extends AppCompatActivity {

    public final String CHANNEL_ID = "my_notification_channel";
    public final int NOTIFICATION_ID = 101;

    TextView textViewUser , textViewTime,textViewSlotID;
    Double globalLat, globalLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        String userID = null;
        String pickupTime = null;
        String slotID = null;
        Double latitude = null;
        Double longitude = null;

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            userID =(String) b.get("userID");
            pickupTime = (String) b.get("pickupTime");
            slotID = (String) b.get("slotID");
            latitude = (Double) b.get("latitude");
            longitude = (Double) b. get("longitude");
        }
        textViewUser = findViewById(R.id.poptextViewShortDesc);
        textViewUser.setText(userID);
        textViewTime = findViewById(R.id.poptextViewTitle);
        textViewTime.setText(pickupTime);
        textViewSlotID = findViewById(R.id.poptextViewRating);
        textViewSlotID.setText(slotID);

        Button btn = (Button)findViewById(R.id.confirmButton);

        final String finalUserID = userID;
        final String finalpickupTime = pickupTime;
        final String finalslotID = slotID;
        final Double finallatitude = latitude;
        final Double finallongitude = longitude;

        globalLat = finallatitude;
        globalLong = finallongitude;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hola", finalUserID);
                Log.d("hola", finalpickupTime);
                Log.d("hola",finalslotID);
                Log.d("hola",finallatitude.toString());
                Log.d("hola",finallongitude.toString());
                displayNotification(v);
             /*   String title = "Delivery Request Notification".trim();
                String subject = "New delivery Request".trim();
                String body = "Click to reply the request".trim();

                NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new Notification.Builder
                        (getApplicationContext()).setContentTitle(title).setContentText(body).
                        setContentTitle(subject).setSmallIcon(R.drawable.ic_launcher_foreground).build();

                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(0,notification); */
            }
        });

        getWindow().setLayout((int)(width*.8),(int)(height*.6));
    }

    public void displayNotification(View view)
    {
        createNotificationChannel();

        Intent landingIntent = new Intent (this,ConfirmDelivery.class);
        landingIntent.putExtra("latitude",globalLat);
        landingIntent.putExtra("longitude",globalLong);
        landingIntent.putExtra("slotID",textViewSlotID.getText());
        landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent landingPendingIntent = PendingIntent.getActivity(this,0,landingIntent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setContentTitle("New Delivery Request");
        builder.setContentText("Click to reply it");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentIntent(landingPendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID,builder.build());
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            CharSequence name = "Personal Notification";
            String description = "Include all the personal notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
