package com.example.coffeetime.Activity;

import android.app.Activity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
    private Integer quantity;
    private ApiService apiService;
    TextView priceTextView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        priceTextView = findViewById(R.id.price);
        quantity = getIntent().getIntExtra("quantity", 0);
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
        System.out.println("   " + quantity);
        token = getIntent().getStringExtra("access_token");
    }
}
