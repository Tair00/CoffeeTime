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
        // Инициализация объекта ManagementCart
        // Производите необходимые операции, связанные с корзиной
        // Например, получение данных из базы данных или файла

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
        // Добавление элемента в корзину
        // Выполните необходимые операции
        listCart.add(item);
        cartListener.onCartUpdated();
    }

    public void removeItem(ArrayList<CafeItem> list, int position) {
        if (list != null && position >= 0 && position < list.size()) {
            CafeItem item = list.get(position);
            list.remove(position);
            // Выполните здесь необходимые действия при удалении элемента, например, пересчет общей суммы или обновление интерфейса
            cartListener.onCartUpdated();
        }
    }

    public void deleteCartItem(CafeItem item) {
        // Удаление элемента корзины
        // Выполните необходимые операции
        listCart.remove(item);
        cartListener.onCartUpdated();
    }

    // Остальные методы класса

    // Внутренний интерфейс для обратного вызова
    public interface CartListener {
        void onCartUpdated();

        void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction);
    }
}