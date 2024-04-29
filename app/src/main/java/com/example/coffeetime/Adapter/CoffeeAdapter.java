package com.example.coffeetime.Adapter;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeetime.Domain.CoffeeDomain;
import com.example.coffeetime.Fragments.DatePickerFragment;
import com.example.coffeetime.Fragments.TimePickerFragment;
import com.example.coffeetime.Fragments.UserNameFragment;
import com.example.coffeetime.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.TableViewHolder> {
    private Context context;
    private ArrayList<CoffeeDomain> products;
    private OnItemClickListener onItemClickListener;
    private String selectedDate;
    private String selectedTime;
    private String userName;

    public CoffeeAdapter(Context context, ArrayList<CoffeeDomain> products) {
        this.context = context;
        this.products = products;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coffee_item, parent, false);
        return new TableViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        CoffeeDomain coffee = products.get(position);

        holder.coffeeTitles.setText(coffee.getName());
        holder.coffeePrice.setText(String.valueOf(coffee.getDescription()));
        System.out.println("Task1");
        Picasso.get().load(coffee.getImage()).into(holder.coffeeImage);

        // Установка слушателя выбора даты

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "datePicker");
                datePickerFragment.setOnDateSetListener(new DatePickerFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        updateDate(year, month, dayOfMonth);

                        showTimePickerDialog(); // Передаем адаптер в мето

                    }
                });

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(coffee, selectedDate, selectedTime, userName);
                }
            }
        });


    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    private void updateDate(int year, int month, int dayOfMonth) {
        DatePickerFragment datePickerFragment = (DatePickerFragment) ((AppCompatActivity) context).getSupportFragmentManager().findFragmentByTag("datePicker");
        if (datePickerFragment != null) {
            datePickerFragment.updateDate(year, month, dayOfMonth);
            selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);

        }
    }
    private void showTimePickerDialog() {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setOnTimeSetListener(new TimePickerFragment.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                updateTime(hourOfDay, minute);
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                showDialogFragment();
            }
        });
        newFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "timePicker");
    }
    private void showDialogFragment() {
        UserNameFragment userNameFragment = UserNameFragment.newInstance();
        userNameFragment.setOnUserNameSetListener(new UserNameFragment.OnUserNameSetListener() {
            @Override
            public void onUserNameSet(String userName) {

                setUserName(userName);
                notifyDataSetChanged();
            }
        });
        userNameFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "userNameDialog");
    }
    private void updateTime(int hourOfDay, int minute) {
        TimePickerFragment timePickerFragment = (TimePickerFragment) ((AppCompatActivity) context).getSupportFragmentManager().findFragmentByTag("timePicker");
        if (timePickerFragment != null) {
            timePickerFragment.updateTime(hourOfDay, minute);

        }
    }
    @Override
    public int getItemCount() {
        return products.size();
    }

    public interface OnItemClickListener {
        void onItemClick(CoffeeDomain table,String selectedDate, String selectedTime, String userName);
    }

    public class TableViewHolder extends RecyclerView.ViewHolder {
        ImageView coffeeImage;
        TextView coffeeTitles, coffeePrice;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            coffeeImage = itemView.findViewById(R.id.coffee_cart);
            coffeeTitles = itemView.findViewById(R.id.name);
            coffeePrice = itemView.findViewById(R.id.price);
        }
    }
}