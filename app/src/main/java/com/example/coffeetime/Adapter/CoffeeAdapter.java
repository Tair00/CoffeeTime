package com.example.coffeetime.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeetime.Domain.CoffeeDomain;
import com.example.coffeetime.R;

import java.util.ArrayList;

public class CoffeeAdapter extends RecyclerView.Adapter<CoffeeAdapter.TableViewHolder> {
    private Context context;
    private ArrayList<CoffeeDomain> products;
    private OnItemClickListener onItemClickListener;

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
        CoffeeDomain table = products.get(position);

        holder.coffeeTitles.setText(table.getName());
        holder.coffeePrice.setText(String.valueOf(table.getDescription()));
        System.out.println("Task1");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(table);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public interface OnItemClickListener {
        void onItemClick(CoffeeDomain table);
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
