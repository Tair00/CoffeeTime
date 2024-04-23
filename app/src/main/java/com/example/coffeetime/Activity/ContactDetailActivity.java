package com.example.coffeetime.Activity;

import android.app.Activity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.coffeetime.Helper.ApiClient;
import com.example.coffeetime.Helper.ApiService;
import com.example.coffeetime.Helper.FirebaseHelper;
import com.example.coffeetime.Helper.SubscriptionRequest;
import com.example.coffeetime.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.coffeetime.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ContactDetailActivity extends Activity {
    private String token;
    private int quantity;
    private ApiService apiService;
    TextView priceTextView;
    private FirebaseHelper firebaseHelper;
    ConstraintLayout puyBtn;
    TextInputEditText editTextCVC,outlinedEditTextField,editTextMM;
    TextInputLayout outlinedTextField,textCVC,textMM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.initFirebaseMessaging();
        // Инициализация TextView после вызова setContentView()
        priceTextView = findViewById(R.id.price);
        puyBtn = findViewById(R.id.puyBtn);
        editTextCVC=findViewById(R.id.EditTextCVC);
        outlinedEditTextField = findViewById(R.id.outlinedEditTextField);
        editTextMM = findViewById(R.id.EditTextMM);

        outlinedTextField = findViewById(R.id.outlinedTextField);
        textCVC = findViewById(R.id.TextCVC);
        textMM = findViewById(R.id.TextMM);
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
                // Получаем анимацию из XML
                Animation animation = AnimationUtils.loadAnimation(ContactDetailActivity.this, R.anim.scale_up_down);

                // Применяем анимацию к кнопке
                puyBtn.startAnimation(animation);
                outlinedTextField.setError(null);
                textCVC.setError(null);
                textMM.setError(null);


                String inputText = outlinedEditTextField.getText().toString().trim();
                String inputTextCVC = editTextCVC.getText().toString().trim();
                String inputTextMM = editTextMM.getText().toString().trim();
                System.out.println("123123123123"+ outlinedEditTextField.getText().toString().trim());
                System.out.println("21111111111 "  + inputText.length());
                if (inputText.length() == 16 && inputTextCVC.length() == 3 && inputTextMM.length() == 4) {
                    subscribe(quantity);


                } else {
                    if (inputText.length() != 16) {
                        outlinedTextField.setError("Неверные данные");
                    }
                    if (inputTextCVC.length() != 3) {
                       textCVC.setError("Неверные данные");
                    }
                    if (inputTextMM.length() != 4) {
                        textMM.setError("Неверные данные");
                    }


                }

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
