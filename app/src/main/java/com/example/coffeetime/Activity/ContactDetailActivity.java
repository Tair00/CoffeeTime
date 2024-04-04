package com.example.coffeetime.Activity;

import android.app.Activity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.coffeetime.Helper.ApiClient;
import com.example.coffeetime.Helper.ApiService;
import com.example.coffeetime.Helper.SubscriptionRequest;
import com.example.coffeetime.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.coffeetime.R;

public class ContactDetailActivity extends Activity {
    private String token;
    private int quantity;
    private ApiService apiService;
    TextView priceTextView;
    ConstraintLayout puyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        // Инициализация TextView после вызова setContentView()
        priceTextView = findViewById(R.id.price);
        puyBtn = findViewById(R.id.puyBtn);
        // Получаем экземпляр ApiService из ApiClient
        apiService = ApiClient.getClient().create(ApiService.class);

        quantity = getIntent().getIntExtra("quantity", 0);
        Toast.makeText(ContactDetailActivity.this, "quantity" + quantity, Toast.LENGTH_SHORT).show();

        int price;

        if (quantity <= 15) {
            price = 700;
        } else if (quantity <= 30) {
            price = 1350;
        } else {
            price = 1900;
        }

        String priceString = String.format("%d руб", price);
        priceTextView.setText(priceString);


        puyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                subscribe(quantity);
            }
        });
    }

    private void subscribe(int quantity) {
        token = getIntent().getStringExtra("access_token");
        System.out.println("111111111123123 "  +quantity + token);
        SubscriptionRequest request = new SubscriptionRequest(quantity);
        Call<Void> call = apiService.subscribe("Bearer " + token, request);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Обработка успешного ответа
                    Toast.makeText(ContactDetailActivity.this, "Подписка успешно оформлена", Toast.LENGTH_SHORT).show();
                } else {
                    // Обработка неуспешного ответа
                    Log.e("ContactDetailActivity", "Ошибка: " + response.code());
                    Toast.makeText(ContactDetailActivity.this, "Ошибка при оформлении подписки", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ContactDetailActivity.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
