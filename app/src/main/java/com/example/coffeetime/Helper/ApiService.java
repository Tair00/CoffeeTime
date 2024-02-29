package com.example.coffeetime.Helper;

import com.example.coffeetime.Domain.CafeItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {



    @GET("/restaurant")
    Call<List<CafeItem>> getCafe();


}