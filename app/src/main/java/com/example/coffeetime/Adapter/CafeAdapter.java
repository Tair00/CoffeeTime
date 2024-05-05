package com.example.coffeetime.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeetime.Activity.ShowDetailActivity;
import com.example.coffeetime.Domain.CafeItem;
import com.example.coffeetime.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CafeAdapter extends RecyclerView.Adapter<CafeAdapter.RestoranViewHolder> {
    private Context context;

    private ArrayList<CafeItem> products;
    private String email,token;
    private Integer cafe_id;
    public CafeAdapter(Context context, String email,String token ) {
        this.token = token;
        this.context = context;
        this.products = new ArrayList<>();
        this.email = email;

    }

    public void updateProducts(ArrayList<CafeItem> newProducts) {
        products.clear();
        products.addAll(newProducts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RestoranViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.cafe_item, parent, false);
        return new RestoranViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RestoranViewHolder holder, int position) {
        ArrayList<CafeItem> sortedProducts = new ArrayList<>(products);
        Collections.sort(sortedProducts, new Comparator<CafeItem>() {
            @Override
            public int compare(CafeItem o1, CafeItem o2) {
                // Сравниваем по убыванию рейтинга
                return Double.compare(o2.getStar(), o1.getStar());
            }
        });

        CafeItem product = products.get(position);


        holder.productTitles.setText(product.getName());
        holder.productPrice.setText(String.valueOf(product.getPrice()));

        Picasso.get().load(product.getPicture()).into(holder.productImage);

        String formattedRating = String.format("%.1f", product.getStar());

        holder.grade.setText(formattedRating);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context,
                        new Pair<View, String>(holder.productImage, "productImage"));
                cafe_id = product.getId();
                Intent intent = new Intent(holder.itemView.getContext(), ShowDetailActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("access_token",token);
                intent.putExtra("object", product);
                intent.putExtra("cafe_id",cafe_id);
                System.out.println("1231" + cafe_id +"!!!!");
                holder.itemView.getContext().startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class RestoranViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productTitles, productPrice;
        TextView grade;


        public RestoranViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.pic);
            productTitles = itemView.findViewById(R.id.title);
            productPrice = itemView.findViewById(R.id.fee);
            grade = itemView.findViewById(R.id.grade);
        }
    }
}