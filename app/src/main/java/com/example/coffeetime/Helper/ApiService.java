package com.example.coffeetime.Helper;

import com.example.coffeetime.Domain.CafeItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {



    @GET("/restaurant")
    Call<List<CafeItem>> getCafe();


    @POST("subscription/")
    Call<Void> subscribe(
            @Header("Authorization") String token,
            @Body SubscriptionRequest request
    );
}