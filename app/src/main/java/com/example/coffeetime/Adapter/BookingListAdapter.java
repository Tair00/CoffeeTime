package com.example.coffeetime.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.coffeetime.Domain.BookingItem;
import com.example.coffeetime.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.ViewHolder> {
    private Context context;
    private List<BookingItem> bookingList;

    public BookingListAdapter(Context context, List<BookingItem> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    public void fetchData() {
        String serverUrl = "https://losermaru.pythonanywhere.com/orders/";
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, serverUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Ошибка при получении данных: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println("Ошибка при получении данных: " + error.getMessage());
                    }
                });
        queue.add(request);
    }

    private void parseResponse(JSONObject response) {
        try {
            boolean hasNewApprovedItems = false;

            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(String.valueOf(i));
                int id = jsonObject.getInt("id");
                String status = jsonObject.getString("status");

                if ("approved".equals(status)) {
                    String name = jsonObject.getString("name");
                    JSONObject cafeObject = jsonObject.getJSONObject("cafe");
                    int cafeId = cafeObject.getInt("id");
                    String cafeName = cafeObject.getString("name");

                    JSONObject coffeeObject = jsonObject.getJSONObject("coffee");
                    int coffeeId = coffeeObject.getInt("id");
                    String coffeeName = coffeeObject.getString("name");
                    String coffeeDescription = coffeeObject.getString("description");
                    int cafeIdFromCoffee = coffeeObject.getInt("cafe_id");
                    String coffeeImage = coffeeObject.getString("image");

                    String pickUpTime = jsonObject.getString("pick_up_time").substring(0, 16);

                    System.out.println(id + " " + status + " " + name + " " + cafeId + " " + cafeName + " "
                            + coffeeId + " " + coffeeName + " " + coffeeDescription + " " + cafeIdFromCoffee
                            + " " + coffeeImage + " " + pickUpTime);

                    BookingItem booking = new BookingItem(status, coffeeImage, pickUpTime, name, cafeName);
                }
            }
            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Ошибка при разборе ответа", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.booking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (BookingItem item : bookingList) {
            String status = item.getStatus();
            if ("approved".equals(status)) {
                count++;
            }
        }
        return count;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int count = 0;
        for (BookingItem item : bookingList) {
            String status = item.getStatus();
            if ("approved".equals(status)) {
                if (count == position) {
                    Picasso.get().load(item.getPicture()).into(holder.imageView);

                    holder.timeTextView.setText(item.getTime());
                    holder.nameTextView.setText(item.getName());
                    holder.restTitle.setText(item.getCafeName());
                    break;
                }
                count++;
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public TextView timeTextView;
        public TextView nameTextView;
        public TextView restTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            // Инициализация элементов интерфейса booking_item
            // Например:
            imageView = itemView.findViewById(R.id.pic);
            timeTextView = itemView.findViewById(R.id.time);
            nameTextView = itemView.findViewById(R.id.title);
            restTitle = itemView.findViewById(R.id.restTitle);
        }
    }
}