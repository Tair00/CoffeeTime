package com.example.coffeetime.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.coffeetime.Adapter.CafeAdapter;
import com.example.coffeetime.Domain.CafeItem;
import com.example.coffeetime.Helper.ApiService;
import com.example.coffeetime.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter, adapter2;
    private RecyclerView recyclerViewCategoryList, recyclerViewPopularList, productRecycler;

    static ArrayList<CafeItem> orderlist1 = new ArrayList<>();
    static ArrayList<CafeItem> fullOrderlist = new ArrayList<>();

    static CafeAdapter priceAdapter;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation();
        fullOrderlist.clear();
        setProductRecycler(orderlist1);
        token = getIntent().getStringExtra("access_token");
        fetchRestaurantsFromServer();
    }
    @Override
    protected void onResume() {
        super.onResume();
        fetchRestaurantsFromServer();
    }
    private void setProductRecycler(ArrayList<CafeItem> restorans) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        String email = getIntent().getStringExtra("email");
        System.out.println("====================================" + token);
        productRecycler = findViewById(R.id.restoranRecycler);
        productRecycler.setLayoutManager(layoutManager);
        priceAdapter = new CafeAdapter(this, email,token);
        productRecycler.setAdapter(priceAdapter);
        productRecycler.smoothScrollToPosition(100000);
        productRecycler.setHasFixedSize(true);
        priceAdapter.updateProducts(restorans);
    }
    private void bottomNavigation() {
//        LinearLayout profileBtn = findViewById(R.id.profileBtn);
//        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        LinearLayout setting = findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReservationActivity.class);
                String  token = getIntent().getStringExtra("access_token");
                String email = getIntent().getStringExtra("email");
                intent.putExtra("email", email);
                intent.putExtra("access_token", token);
                startActivity(intent);
            }
        });
//        profileBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(MainActivity.this, BookingListActivity.class);
//                String  token = getIntent().getStringExtra("access_token");
//                String email = getIntent().getStringExtra("email");
//                intent1.putExtra("email", email);
//                intent1.putExtra("access_token", token);
//                startActivity(intent1);
//            }
//        });
//        homeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, MainActivity.class));
//            }
//        });
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                String  token = getIntent().getStringExtra("access_token");
                String email = getIntent().getStringExtra("email");
                intent.putExtra("email", email);
                intent.putExtra("access_token", token);
                startActivity(intent);
            }
        });
    }
    private void fetchRestaurantsFromServer() {
        Log.d("MainActivity", "Fetching restaurants from server...");

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                // Создание нового запроса с добавленным заголовком авторизации
                Request newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .build();

                return chain.proceed(newRequest);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://losermaru.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<CafeItem>> call = apiService.getCafe();
        call.enqueue(new Callback<List<CafeItem>>() {
            @Override
            public void onResponse(Call<List<CafeItem>> call, Response<List<CafeItem>> response) {
                Log.d("MainActivity", "Server response received.");

                if (response.isSuccessful()) {
                    List<CafeItem> restaurants = response.body();
                    if (restaurants != null) {
                        Log.d("MainActivity", "Received " + restaurants.size() + " restaurants from server.");

                        Collections.sort(restaurants, new Comparator<CafeItem>() {
                            @Override
                            public int compare(CafeItem o1, CafeItem o2) {
                                // Сравниваем по убыванию рейтинга
                                return Double.compare(o2.getStar(), o1.getStar());
                            }
                        });

                        // Устанавливаем данные в RecyclerView после сортировки
                        orderlist1.clear();
                        orderlist1.addAll(restaurants);
                        setProductRecycler(orderlist1);
                        priceAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("MainActivity", "Received null response body from server.");
                    }
                } else {
                    Log.e("MainActivity", "Server responded with error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<CafeItem>> call, Throwable t) {
                Log.e("MainActivity", "Failed to fetch restaurants from server: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

}
