package com.example.shangsheingoh.scaneat.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.shangsheingoh.scaneat.Cart;
import com.example.shangsheingoh.scaneat.Database.Database;
import com.example.shangsheingoh.scaneat.Interface.ItemClickListener;
import com.example.shangsheingoh.scaneat.Model.Order;
import com.example.shangsheingoh.scaneat.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txt_cart_name, txt_price;
    public ElegantNumberButton btn_quantity;

    private ItemClickListener itemClickListener;

    public void setTxt_cart_name(TextView txt_cart_name){
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cart_name = (TextView)itemView.findViewById(R.id.cart_item_name);
        txt_price = (TextView)itemView.findViewById(R.id.cart_item_price);
        btn_quantity = (ElegantNumberButton) itemView.findViewById(R.id.btn_quantity);
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{
    private List<Order> listData = new ArrayList<>();
    private Cart cart;

    public CartAdapter(List<Order> listData, Cart cart) {
        this.listData = listData;
        this.cart = cart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout,viewGroup,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, final int i) {
//        TextDrawable drawable = TextDrawable.builder()
//                .buildRound(""+listData.get(i).getQuantity(), Color.RED);
//        cartViewHolder.img_cart_count.setImageDrawable(drawable);

        cartViewHolder.btn_quantity.setNumber(listData.get(i).getQuantity());
        cartViewHolder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = listData.get(i);
                order.setQuantity(String.valueOf(newValue));
                new Database(cart).updateCart(order);

                //update total price of cart
                int total=0;
                List<Order> orders = new Database(cart).getCarts();
                for(Order item:orders){
                    total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(item.getQuantity()));
                    Locale locale = new Locale("en","US");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                    cart.txtTotalPrice.setText(fmt.format(total));
                }
            }
        });

        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price =  (Integer.parseInt(listData.get(i).getPrice()))*(Integer.parseInt(listData.get(i).getQuantity()));
        cartViewHolder.txt_price.setText(fmt.format(price));

        cartViewHolder.txt_cart_name.setText(listData.get(i).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
