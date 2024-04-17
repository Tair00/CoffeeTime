package com.example.coffeetime.Helper;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeetime.Activity.CartActivity;
import com.example.coffeetime.Domain.CafeItem;

import java.util.ArrayList;


public class ManagementCart {
    private static ManagementCart instance;
    private ArrayList<CafeItem> listCart = new ArrayList<>();
    private CartListener cartListener;

    private ManagementCart(Context context, CartListener listener) {
        this.cartListener = listener;
    }

    public static ManagementCart getInstance(Context context, CartActivity listener) {
        if (instance == null) {
            instance = new ManagementCart(context, listener);
        }
        return instance;
    }

    public ArrayList<CafeItem> getListCart() {
        return listCart;
    }

    public void addItem(CafeItem item) {

        listCart.add(item);
        cartListener.onCartUpdated();
    }

    public void removeItem(ArrayList<CafeItem> list, int position) {
        if (list != null && position >= 0 && position < list.size()) {
            CafeItem item = list.get(position);
            list.remove(position);
            cartListener.onCartUpdated();
        }
    }

    public void deleteCartItem(CafeItem item) {

        listCart.remove(item);
        cartListener.onCartUpdated();
    }

    public interface CartListener {
        void onCartUpdated();

        void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction);
    }
}