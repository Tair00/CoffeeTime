package com.example.coffeetime.Activity;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.example.coffeetime.Adapter.CoffeeAdapter;
import com.example.coffeetime.Domain.CafeItem;
import com.example.coffeetime.Domain.CoffeeDomain;
import com.example.coffeetime.Fragments.DatePickerFragment;
import com.example.coffeetime.Helper.ManagementCart;
import com.example.coffeetime.Fragments.TimePickerFragment;
import com.example.coffeetime.Interface.CartListener;
import com.example.coffeetime.R;
import com.squareup.picasso.Picasso;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import com.example.coffeetime.Fragments.UserNameFragment;



public class ShowDetailActivity extends AppCompatActivity implements CartListener, UserNameFragment.OnUserNameSetListener{

    private TextView titleTxt, feeTxt, description, starTxt, coffeeTxt;
    private ImageView heart, restoranPic;
    private String time;
    static CoffeeAdapter coffeeAdapter;
    private CafeItem object;
    private RecyclerView coffeeRecycler;
    private ImageView star;
    private String day;
    String serverUrl = "https://losermaru.pythonanywhere.com/coffee/";
    static ArrayList<CoffeeDomain> coffeeList = new ArrayList<>();
    Float price;
    Integer restaurantId;
    private String name;
    private ManagementCart managementCart;
    private int numberOrder = 1;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        initView();
        getBundle();
        CartActivity cartActivity = new CartActivity();
        managementCart = ManagementCart.getInstance(this, cartActivity);
        setupButtonListeners();
        showTimePickerDialog();
        coffeeList.clear();
        setTableRecycler(coffeeList);
        showDatePickerDialog();
        showDialogFragment();
        token = getIntent().getStringExtra("access_token");
        String feeTxtValue = getIntent().getStringExtra("feeTxt");
        price = 0.0F;
        restaurantId = getIntent().getIntExtra("restorantId", 0);
        executeGetRequest();
    }
    @Override
    public void onResume(String selectedRating) {
        super.onResume();
        updateRating(selectedRating);
    }
    private void getBundle() {
        object = (CafeItem) getIntent().getSerializableExtra("object");
        int restaurantId = getIntent().getIntExtra("restorantId", 0);
        if (object == null && restaurantId != 0) {
            fetchRestaurantDetails(restaurantId);
        } else {
            // Используем информацию из объекта RestoranDomain, если она уже доступна
            if (object != null) {
                titleTxt.setText(object.getName());
                description.setText(object.getDescription());
                starTxt.setText(String.valueOf(object.getStar()));
                Picasso.get().load(object.getPicture()).into(restoranPic);
            }
        }
    }
    private void updateRating(String newRating) {
        // Обновляем текстовое поле с оценкой новым значением
        starTxt.setText(String.valueOf(newRating));
    }
    private void fetchRestaurantDetails(int restaurantId) {

    }

    private void setupButtonListeners() {

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showRatingDialog();
            }
        });
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFavoriteToServer();
            }
        });
    }

    private void sendFavoriteToServer() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String email = getIntent().getStringExtra("email");
        String userUrl = "https://losermaru.pythonanywhere.com/user/" + email;
        StringRequest userRequest = new StringRequest(Request.Method.GET, userUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject userJson = new JSONObject(response);
                            int userId = userJson.getInt("id");
                            int restaurantId = getIntent().getIntExtra("restorantId", 0);

                            String favoriteUrl = "http://losermaru.pythonanywhere.com/favorite/";

                            JSONObject jsonBody = new JSONObject();
                            jsonBody.put("user_id", userId);
                            jsonBody.put("restaurant_id", restaurantId);

                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, favoriteUrl, jsonBody,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Toast.makeText(ShowDetailActivity.this, "Добавлено в избранное", Toast.LENGTH_SHORT).show();

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            String errorMessage = "Ресторан уже добавлен";
                                            Toast.makeText(ShowDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                            System.out.println("1222222222222Ошибка Volley: " + error.getMessage() + ". " + errorMessage);
                                        }
                                    }) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> headers = new HashMap<>();

                                    String token = getIntent().getStringExtra("access_token");
                                    headers.put("Authorization", "Bearer " + token);
                                    return headers;
                                }
                            };

                            queue.add(request);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.getMessage();
                        Toast.makeText(ShowDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            String responseData = new String(error.networkResponse.data);
                            Log.e("ErrorResponse", "Status Code: " + statusCode);
                            Log.e("ErrorResponse", "Response Data: " + responseData);
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                String token = getIntent().getStringExtra("access_token");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(userRequest);
    }
    private void showRatingDialog() {
        MaterialRatingBar ratingBar = new MaterialRatingBar(this);
        ratingBar.setNumStars(5); // Установка количества звезд
        ratingBar.setRating(0); // Установка начальной оценки
        ratingBar.setStepSize(1); // Установка шага изменения оценки

        // Устанавливаем цвета для звезд
        ratingBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FFD17842"))); // Заполненные звезды
        ratingBar.setSecondaryProgressTintList(ColorStateList.valueOf(Color.parseColor("#FFFFCA42"))); // Пустые звезды

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        ratingBar.setLayoutParams(params);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Оцените заведение");
        builder.setView(ratingBar);

        // Установка цвета текста для кнопки "Оценить"
        builder.setPositiveButton("Оценить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                float selectedRating = ratingBar.getRating();
                sendRatingToServer(String.valueOf(selectedRating));

                Toast.makeText(ShowDetailActivity.this, "Оценка заведения: " + selectedRating, Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        // Установка цвета текста для кнопки "Отменить"
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        // Установка цвета текста для кнопок в диалоговом окне
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);

                positiveButton.setTextColor(Color.parseColor("#FF0C0F14")); // Белый цвет текста
                positiveButton.setBackgroundColor(Color.TRANSPARENT); // Прозрачный фон
                negativeButton.setTextColor(Color.parseColor("#FF0C0F14")); // Оранжевый цвет текста
            }
        });

        dialog.show();
    }
    private void sendRatingToServer(String selectedRating) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String email = getIntent().getStringExtra("email");
        String userUrl = "https://losermaru.pythonanywhere.com/user/" + email ;

        StringRequest userRequest = new StringRequest(Request.Method.GET, userUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject userJson = new JSONObject(response);
                            int userId = userJson.optInt("id");
                            int restaurantId = getIntent().getIntExtra("restorantId", 0);

                            String ratingUrl = "https://losermaru.pythonanywhere.com/rating/";

                            JSONObject jsonBody = new JSONObject();
                            jsonBody.put("rating", Math.round(Float.parseFloat(selectedRating)));
                            jsonBody.put("user_id", userId);
                            jsonBody.put("restaurant_id", restaurantId);
                            updateRating(selectedRating);
                            onResume(selectedRating);
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ratingUrl, jsonBody,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
//                                            getBundle();
                                            Toast.makeText(ShowDetailActivity.this, "Спасибо за оценку", Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            String errorMessage = "Error sending rating: " + error.getMessage();
                                            Toast.makeText(ShowDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                            if (error.networkResponse != null) {
                                                int statusCode = error.networkResponse.statusCode;
                                                String responseData = new String(error.networkResponse.data);
                                                Log.e("ErrorResponse", "Status Code: " + statusCode);
                                                Log.e("ErrorResponse", "Response Data: " + responseData);
                                            }
                                        }
                                    }) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> headers = new HashMap<>();

                                    token = getIntent().getStringExtra("access_token");
                                    headers.put("Authorization", "Bearer " + token);
                                    return headers;
                                }
                            };

                            queue.add(request);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ShowDetailActivity.this, "Error parsing user response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.getMessage();
                        Toast.makeText(ShowDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            String responseData = new String(error.networkResponse.data);
                            Log.e("ErrorResponse", "Status Code: " + statusCode);
                            Log.e("ErrorResponse", "Response Data: " + responseData);
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                token = getIntent().getStringExtra("access_token");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(userRequest);
    }
    @Override
    public void onCartUpdated() {
        Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show();
    }
    private void initView() {
        star=findViewById(R.id.star);
        heart = findViewById(R.id.heart);
        titleTxt = findViewById(R.id.nameCafe);
        description = findViewById(R.id.descriptionTxt);
        restoranPic = findViewById(R.id.restoranPic);
        starTxt = findViewById(R.id.starTxt);
    }

    private void setTableRecycler(ArrayList<CoffeeDomain> table) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        coffeeRecycler = findViewById(R.id.table_recycler);
        coffeeRecycler.setLayoutManager(layoutManager);
        coffeeAdapter = new CoffeeAdapter(this, table);
        coffeeAdapter.setOnItemClickListener(new CoffeeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CoffeeDomain table) {
                String number = table.getTitle();
                executePostRequest(number, name);
            }
        });
        coffeeRecycler.setAdapter(coffeeAdapter);
        coffeeRecycler.smoothScrollToPosition(0);
        coffeeRecycler.setHasFixedSize(true);
    }
    public void showTimePickerDialog() {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setOnTimeSetListener(new TimePickerFragment.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                updateTime(hourOfDay, minute);
            }
        });
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    public void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setOnDateSetListener(new DatePickerFragment.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                updateDate(year, month, dayOfMonth);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void executeGetRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShowDetailActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                int statusCode = response.statusCode;
                if (statusCode == 422) {
                    String errorMessage = "Data validation error";
                    return Response.error(new VolleyError(errorMessage));
                } else {
                    return super.parseNetworkResponse(response);
                }
            }
        };
        queue.add(stringRequest);
    }

    private void parseResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String title = jsonObject.getString("number");
                String seat = jsonObject.getString("seat");
                String restId = jsonObject.getString("restaurant_id");
                if (restId.equals(String.valueOf(restaurantId))) {
                    coffeeList.add(new CoffeeDomain(id, title, seat, restId));
                }
            }
            coffeeAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ShowDetailActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialogFragment() {
        UserNameFragment userNameFragment = UserNameFragment.newInstance();
        userNameFragment.setOnUserNameSetListener(this);
        userNameFragment.show(getSupportFragmentManager(), "userNameDialog");
    }
    public void onUserNameSet(String userName) {
        name = userName;
    }

    private void executePostRequest(String number, String name) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String email = getIntent().getStringExtra("email");
        String restoranPic = getIntent().getStringExtra("restoranPic");
        String userUrl = "http://losermaru.pythonanywhere.com/user/" + email + "/";
        StringRequest userRequest = new StringRequest(Request.Method.GET, userUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject userJson = new JSONObject(response);
                            int userId = userJson.getInt("id");
                            JSONObject jsonBody = new JSONObject();
                            jsonBody.put("day", day);
                            jsonBody.put("time", time);
                            jsonBody.put("number", number);
                            jsonBody.put("name", name);
                            jsonBody.put("price", price);
                            jsonBody.put("restaurant_id", restaurantId);
                            jsonBody.put("user_id", userId);
                            jsonBody.put("status", "waiting");
                            jsonBody.put("picture", restoranPic);
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://losermaru.pythonanywhere.com/reservation", jsonBody,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Toast.makeText(ShowDetailActivity.this, "Запрос отправлен", Toast.LENGTH_SHORT).show();
//                                            Intent intent1 = new Intent(BookingActivity2.this, MainActivity.class);
//                                            startActivity(intent1);
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            String errorMessage = "Error: " + error.getMessage();
                                            Toast.makeText(ShowDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                            if (error.networkResponse != null) {
                                                int statusCode = error.networkResponse.statusCode;
                                                String responseData = new String(error.networkResponse.data);
                                                Log.e("ErrorResponse", "Status Code: " + statusCode);
                                                Log.e("ErrorResponse", "Response Data: " + responseData);
                                            }
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ShowDetailActivity.this, "Error parsing user response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error: " + error.getMessage();
                        Toast.makeText(ShowDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            String responseData = new String(error.networkResponse.data);
                            Log.e("ErrorResponse", "Status Code: " + statusCode);
                            Log.e("ErrorResponse", "Response Data: " + responseData);
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        queue.add(userRequest);
    }
    private void updateDate(int year, int month, int dayOfMonth) {
        DatePickerFragment datePickerFragment = (DatePickerFragment) getSupportFragmentManager().findFragmentByTag("datePicker");
        if (datePickerFragment != null) {
            datePickerFragment.updateDate(year, month, dayOfMonth);

        }
        day = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
    }

    private void updateTime(int hourOfDay, int minute) {
        TimePickerFragment timePickerFragment = (TimePickerFragment) getSupportFragmentManager().findFragmentByTag("timePicker");
        if (timePickerFragment != null) {
            timePickerFragment.updateTime(hourOfDay, minute);
        }
        time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
    }
}