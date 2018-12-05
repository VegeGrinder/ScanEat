package com.example.shangsheingoh.scaneat;

import android.content.Context;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shangsheingoh.scaneat.Model.PostDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Announcement extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        //initialize recyclerview and FIrebase objects
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //mDatabase = FirebaseDatabase.getInstance().getReference().child("Owner Announcement").child("Announcement_0");
        mAuth = FirebaseAuth.getInstance();
////        mAuthListener = new FirebaseAuth.AuthStateListener() {
////            @Override
////            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (mAuth.getCurrentUser()==null){
//                    Intent loginIntent = new Intent(MainActivity.this, RegisterActivity.class);
//                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);startActivity(loginIntent);
//                }
//            }
//        };
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Owner announcement");

        FirebaseRecyclerAdapter<PostDetails, AnnouncementViewHolder> FBRA = new FirebaseRecyclerAdapter<PostDetails, AnnouncementViewHolder>(
                PostDetails.class,
                R.layout.card_items,
                AnnouncementViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(AnnouncementViewHolder viewHolder, PostDetails model, int position) {
                final String post_key = getRef(position).getKey().toString();
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImageUrl(getApplicationContext(), model.getImageUrl());
//                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent singleActivity = new Intent(MainActivity.this, SinglePostActivity.class);
//                        singleActivity.putExtra("PostID", post_key);
//                        startActivity(singleActivity);
//                    }
//                });
            }
        };
        recyclerView.setAdapter(FBRA);
    }


    public static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public AnnouncementViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            TextView post_title = mView.findViewById(R.id.post_title_txtview);
            post_title.setText(title);
        }

        public void setDesc(String desc) {
            TextView post_desc = mView.findViewById(R.id.post_desc_txtview);
            post_desc.setText(desc);
        }

        public void setImageUrl(Context ctx, String imageUrl) {
            ImageView post_image = mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(imageUrl).into(post_image);
        }

    }
}


