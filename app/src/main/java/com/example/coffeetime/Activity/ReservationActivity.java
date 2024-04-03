package com.example.coffeetime.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.coffeetime.Helper.ApiClient;
import com.example.coffeetime.Helper.ApiService;
import com.example.coffeetime.Helper.SubscriptionRequest;
import com.example.coffeetime.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ReservationActivity extends Activity {
    private String token;
    private ApiService apiService;
    ConstraintLayout baseReserv, standardReserv, premiumReserv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        token = getIntent().getStringExtra("access_token");
        baseReserv = findViewById(R.id.baseReserv);
        standardReserv = findViewById(R.id.standardReserv);
        premiumReserv = findViewById(R.id.premiumReserv);

        apiService = ApiClient.getClient().create(ApiService.class);

        baseReserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribe(15); // Указываем количество
            }
        });

        standardReserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribe(30); // Указываем количество
            }
        });

        premiumReserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribe(45); // Указываем количество
            }
        });
    }

    private void subscribe(int quantity) {
        SubscriptionRequest request = new SubscriptionRequest(quantity);
        Call<Void> call = apiService.subscribe("Bearer " + token, request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Обработка успешного ответа
                } else {
                    // Обработка неуспешного ответа
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Обработка ошибки
            }
        });
    }
}
