package com.example.shangsheingoh.scaneat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shangsheingoh.scaneat.Common.Common;
import com.example.shangsheingoh.scaneat.Database.Database;
import com.example.shangsheingoh.scaneat.Model.Order;
import com.example.shangsheingoh.scaneat.Model.Request;
import com.example.shangsheingoh.scaneat.ViewHolder.CartAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    public TextView txtTotalPrice;
    FButton btnPlace,btnReset;

    List<Order> cart =  new ArrayList<>();
    int finalItem;
    CartAdapter adapter;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        requests=database.getReference("Reque" +
                "sts");
        firebaseAuth=FirebaseAuth.getInstance();
        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPlace = (FButton)findViewById(R.id.btnPlaceOrder);
        btnReset = (FButton)findViewById(R.id.btnReset);


        loadListFood();

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).cleanCart();
                adapter.notifyDataSetChanged();
                finish();
            }
        });

    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        String[] animals = {"Self-pickup and help others", "Cannot pickup, need help", "Self-pickup only"};
        int checkedItem = 0;

        alertDialog.setSingleChoiceItems(animals, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finalItem = which;
            }
        });

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Request request = new Request(
                        firebaseAuth.getUid(),
                        Common.currentUser.getUserName(),
                        txtTotalPrice.getText().toString(),
                        cart
                );
                Common.currentRequest = request;

                if(finalItem==0){
                    Intent intent = new Intent(Cart.this,TimePicker.class);
                    startActivity(intent);
                }
                else if(finalItem==1){
                    Intent intent = new Intent(Cart.this,DeliveryActivity.class);
                    startActivity(intent);
                }
                else if(finalItem==2){
                    requests.child(String.valueOf(System.currentTimeMillis()))
                            .setValue(request);
                    new Database(getBaseContext()).cleanCart();
                    Toast.makeText(Cart.this, "Thank you, Order Placed", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        int total=0;
        for(Order order:cart){
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
            Locale locale = new Locale("en","US");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            txtTotalPrice.setText(fmt.format(total));
        }
    }

}
