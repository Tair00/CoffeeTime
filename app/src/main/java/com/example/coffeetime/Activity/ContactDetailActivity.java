package com.example.coffeetime.Activity;

import android.app.Activity;
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
import com.example.coffeetime.R;

public class ContactDetailActivity extends Activity {
    private String token;
    private Integer quantity;
    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        quantity = getIntent().getIntExtra("quantity", 0);
        System.out.println("   " + quantity);
        token = getIntent().getStringExtra("access_token");
    }
}
