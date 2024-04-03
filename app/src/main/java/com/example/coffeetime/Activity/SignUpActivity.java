package com.example.coffeetime.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


import com.example.coffeetime.R;

public class SignUpActivity extends AppCompatActivity {

    String serverUrl = "https://losermaru.pythonanywhere.com/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        EditText mail = findViewById(R.id.mail);
        EditText password = findViewById(R.id.password);
        EditText password2 = findViewById(R.id.password2);
        ConstraintLayout button = findViewById(R.id.startBtn1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mail.getText().toString();
                String userPassword = password.getText().toString();
                String confirmPassword = password2.getText().toString();

                new SignupAsyncTask().execute(email, userPassword, confirmPassword);
            }
        });
    }

    private class SignupAsyncTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            String email = params[0];
            String userPassword = params[1];
            String confirmPassword = params[2];

            try {
                URL url = new URL(serverUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                JSONObject jsonParams = new JSONObject();
                jsonParams.put("email", email);
                jsonParams.put("password", userPassword);
                jsonParams.put("confirm_password", confirmPassword);
                jsonParams.put("role", "user");

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(jsonParams.toString());
                outputStream.close();

                int responseCode = connection.getResponseCode();
                connection.disconnect();
                return responseCode;
            } catch (IOException | JSONException e) {
                Log.e("SignUpActivity", "Error occurred: " + e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            if (responseCode != null) {
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Обработка успешного ответа (код 200)
                    // Например, переход на следующий экран
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                } if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    // Обработка ошибки неверных данных (код 400)
                    // Например, вывод сообщения об ошибке
                    Toast.makeText(SignUpActivity.this, "Неверные данные", Toast.LENGTH_SHORT).show();
                } else {
                    // Обработка других кодов ответа
                    // Например, вывод сообщения об ошибке

                    Toast.makeText(SignUpActivity.this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            } else {
                // Обработка ошибки во время выполнения запроса
                // Например, вывод сообщения об ошибке
                System.out.println("11111");

                Toast.makeText(SignUpActivity.this, "Ошибка соединения", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
