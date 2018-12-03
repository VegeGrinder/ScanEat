package com.example.shangsheingoh.scaneat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shangsheingoh.scaneat.Model.LocationDetails;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder>{


    //this context we will use to inflate the layout
    private Context mCtx;



    //we are storing all the products in a list
    private List<LocationDetails> locationList = new ArrayList<>();

    //getting the context and product list with constructor
    public LocationAdapter(Context mCtx, List<LocationDetails> locationList) {
        this.mCtx = mCtx;
        this.locationList = locationList;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.location_row,parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        //getting the product of the specified position
        LocationDetails location = locationList.get(position);



        //binding the data with the viewholder views
        holder.textViewTitle.setText(location.getPickUpTime());
        holder.textViewShortDesc.setText(location.getUser());
        holder.textViewRating.setText(String.valueOf(location.getSlotID()));

        //  holder.textViewPrice.setText(String.valueOf(location.getPrice()));

    }


    @Override
    public int getItemCount() {
        return locationList.size();
    }


    class LocationViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;

        public LocationViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, PinDeliveryLocation.class);
                    intent.putExtra("userID",textViewTitle.getText());
                    intent.putExtra("pickupTime",textViewShortDesc.getText());
                    intent.putExtra("slotID",textViewRating.getText());
                    context.startActivity(intent);

                }
            });

            textViewTitle = itemView.findViewById(R.id.poptextViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.poptextViewShortDesc);
            textViewRating = itemView.findViewById(R.id.poptextViewRating);
            //  textViewPrice = itemView.findViewById(R.id.textViewPrice);
            //  imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
