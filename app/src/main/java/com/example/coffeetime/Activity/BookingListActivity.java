package com.example.coffeetime.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.coffeetime.Adapter.BookingListAdapter;
import com.example.coffeetime.Domain.BookingItem;
import com.example.coffeetime.Helper.FirebaseHelper;
import com.example.coffeetime.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingListActivity extends Activity {
    private RecyclerView recyclerView;
    private BookingListAdapter adapter;
    private List<BookingItem> bookingList;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);
        recyclerView = findViewById(R.id.view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingList = new ArrayList<>();
        adapter = new BookingListAdapter(BookingListActivity.this, bookingList);
        recyclerView.setAdapter(adapter);
        executeGetRequest();
        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.initFirebaseMessaging();
    }

    private void executeGetRequest() {
        String token = getIntent().getStringExtra("access_token"); // Получение значения токена

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://losermaru.pythonanywhere.com/orders/";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Обработка успешного ответа от сервера
                        parseResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Обработка ошибки запроса
                        Toast.makeText(BookingListActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println("-------------------" + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token); // Добавление заголовка авторизации
                return headers;
            }
        };

        queue.add(request);
    }
    private void parseResponse(JSONArray response) {
        try {
            boolean hasNewApprovedItems = false;

            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(i);
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

                    String pickUpTime = jsonObject.getString("pick_up_time").substring(0, 16).replace('T', ' ');


                    System.out.println(id + " " + status + " " + name + " " + cafeId + " " + cafeName + " "
                            + coffeeId + " " + coffeeName + " " + coffeeDescription + " " + cafeIdFromCoffee
                            + " " + coffeeImage + " " + pickUpTime);

                    BookingItem booking = new BookingItem(status, coffeeImage, pickUpTime, name, cafeName);
                    if (!bookingList.contains(booking)) {
                        bookingList.add(booking);
                        hasNewApprovedItems = true;
                    }
                }
            }
            adapter.notifyDataSetChanged();

            if (hasNewApprovedItems) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MyTag", "Ошибка при разборе ответа: " + e.getMessage());
            Toast.makeText(BookingListActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }
    private void fetchRestaurantName(int id, BookingItem booking) {
        String url = "https://losermaru.pythonanywhere.com/orders/" + id;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name = response.getString("name");
                            booking.setName(name);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(request);
    }
}