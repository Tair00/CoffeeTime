package com.example.coffeetime.Activity;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.coffeetime.Adapter.CartListAdapter;
import com.example.coffeetime.Domain.CafeItem;
import com.example.coffeetime.Helper.ApiService;
import com.example.coffeetime.Helper.FirebaseHelper;
import com.example.coffeetime.Helper.ManagementCart;
import com.example.coffeetime.R;


public class CartActivity extends AppCompatActivity implements ManagementCart.CartListener {

    private RecyclerView recyclerViewList;
    static CartListAdapter priceAdapter;
    private String token;
    private CartListAdapter cartListAdapter;
    private double tax;
    private FirebaseHelper firebaseHelper;
    private NestedScrollView scrollView;
    private ConstraintLayout orderbtn, profileIcon;
    private ArrayList<CafeItem> orderlist = new ArrayList<>(); // Новый список для ресторанов
    private List<Integer> cafe_ids = new ArrayList<>();

    private int userIdFromFirstRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        String email = getIntent().getStringExtra("email");
        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.initFirebaseMessaging();
//        executeGetRequest2(email);
        System.out.println("123123123123123123123123" + email);
//        managementCart = ManagementCart.getInstance(this, this);
        executeGetRequest();
        initView();
        bottomNavigation();
        fetchRestaurantsFromServer();
    }

//    private void executeGetRequest2(String email) {
//        String token = getIntent().getStringExtra("access_token");
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "https://losermaru.pythonanywhere.com/user/" + email;
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            userIdFromFirstRequest = response.getInt("id"); // Сохраняем userId из первого запроса
//                            System.out.println("111111 " + userIdFromFirstRequest);
//                            executeGetRequest(); // Запускаем второй запрос после получения userId
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//
//                        // Обработка успешного ответа от сервера
//                    }
//
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Обработка ошибки запроса первого запроса
//                        handleErrorResponse(error);
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer " + token);
//                return headers;
//            }
//        };
//
//        queue.add(request);
//    }


    private void executeGetRequest() {
        String token = getIntent().getStringExtra("access_token");
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://losermaru.pythonanywhere.com/favorite/";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("1232133123123123123123");
                        parseResponse(response); // Передаем только ответ сервера
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleErrorResponse(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(request);

    }

    private void parseResponse(JSONArray response) {
        try {
            int length = response.length();
            for (int i = 0; i < length; i++) {
                JSONObject item = response.getJSONObject(i);
                int favId = item.getInt("id");
                int itemUserId = item.getInt("user_id");
                int cafe_id = item.getInt("cafe_id");

                System.out.println(" fav                     " + favId);

                    System.out.println("userId matches");
                    cafe_ids.add(cafe_id);

                    // Создаем объект CafeItem и устанавливаем в него favId
                    CafeItem cafeItem = new CafeItem();
                    System.out.println(" fav1                     " + favId);
                    cafeItem.setFavId(favId); // Устанавливаем favId
                    System.out.println("12222222322222 " +cafeItem.getFavId());
                    // Добавляем объект CafeItem в список orderlist
                    orderlist.add(cafeItem);

            }
            System.out.println("Restaurant IDs: " + cafe_ids);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(CartActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }




    private void handleErrorResponse(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.data != null) {
            String errorResponse = new String(error.networkResponse.data);
            // Обработка текста ошибки
            Log.e("ErrorResponse", "Error: " + errorResponse);
            // Дополнительная логика для обработки текста ошибки
        } else {
            // Обработка других видов ошибок (например, отсутствие сети и т. д.)
            Toast.makeText(CartActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("ErrorResponse", "Error: " + error.getMessage());
        }
    }

    protected void bottomNavigation() {
        // Добавьте свою логику для нижней навигации
    }

    protected void calculateCart() {
        // Выполните расчет общей суммы корзины и обновите соответствующие представления
    }

    protected void initView() {
//        profileIcon = findViewById(R.id.profile_icon);
        recyclerViewList = findViewById(R.id.view);
        scrollView = findViewById(R.id.scrollView);
    }

    @Override
    public void onCartUpdated() {
        // Обработка обновления корзины
        calculateCart();
    }


    private void fetchRestaurantsFromServer() {
        token = getIntent().getStringExtra("access_token");
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request originalRequest = chain.request();
                // Создание нового запроса с добавленным заголовком авторизации
                okhttp3.Request newRequest = originalRequest.newBuilder()
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
            public void onResponse(Call<List<CafeItem>> call, retrofit2.Response<List<CafeItem>> response) {
                if (response.isSuccessful()) {
                    List<CafeItem> restaurants = response.body();
                    if (restaurants != null) {
                        // Фильтрация списка restaurants на основе сохраненных айди ресторанов
                        List<CafeItem> filteredRestaurants = new ArrayList<>();
                        for (CafeItem restaurant : restaurants) {
                            if (cafe_ids.contains(restaurant.getId())) { // Проверяем наличие айди ресторана в списке restaurantIds
                                filteredRestaurants.add(restaurant); // Добавляем только нужные рестораны
                            }
                        }

                        Collections.sort(filteredRestaurants, new Comparator<CafeItem>() {
                            @Override
                            public int compare(CafeItem o1, CafeItem o2) {
                                // Сравниваем по убыванию рейтинга
                                return Double.compare(o2.getStar(), o1.getStar());
                            }
                        });

                        // Устанавливаем данные в RecyclerView после сортировки отфильтрованного списка
                        orderlist.clear();
                        orderlist.addAll(filteredRestaurants);
                        setProductRecycler(orderlist);
                        priceAdapter.notifyDataSetChanged();
                    }
                } else {
                    // Обработка ошибки
                    System.out.println("ПУПУПУПУПУПУПУПУП");
                }
            }

            @Override
            public void onFailure(Call<List<CafeItem>> call, Throwable t) {

            }

            private void setProductRecycler(ArrayList<CafeItem> restorans) {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CartActivity.this, RecyclerView.VERTICAL, false);
                String email = getIntent().getStringExtra("email");
                recyclerViewList = findViewById(R.id.view);
                recyclerViewList.setLayoutManager(layoutManager);
                priceAdapter = new CartListAdapter(CartActivity.this, email, token);
                recyclerViewList.setAdapter(priceAdapter);
                recyclerViewList.smoothScrollToPosition(100000);
                recyclerViewList.setHasFixedSize(true);
                priceAdapter.updateProducts(restorans);

                // Вызов метода setTouchHelper для настройки свайпа
                priceAdapter.setTouchHelper(recyclerViewList);
            }
        });
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        CafeItem deletedItem = orderlist.get(position);
        int favId = deletedItem.getFavId();
        System.out.println(favId + "1231231231231222222");
//        System.out.println("1231233333" + deletedItem.);
//        System.out.println("fav id   "+ favId);
        // Получаем favId удаляемого элемента из избранного
        executeDeleteRequest(favId);

        // Удаление элемента из списка и уведомление адаптера
        orderlist.remove(position);
        priceAdapter.notifyItemRemoved(position);
    }


    private void executeDeleteRequest(int favId) {
        String token = getIntent().getStringExtra("access_token");
        Context context = this; // Укажите контекст активити

        RequestQueue queue = Volley.newRequestQueue(context);
        System.out.println(" fav3" + favId);
        String url = "https://losermaru.pythonanywhere.com/favorite/" + favId; // Используем favId для удаления элемента из избранного

        StringRequest request = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(" УСПЕХ");
                        executeGetRequest();
                        Toast.makeText(CartActivity.this, "Удалено", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println(favId + "НЕ УСПЕХ: " + error.toString());
                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(request);
    }
}