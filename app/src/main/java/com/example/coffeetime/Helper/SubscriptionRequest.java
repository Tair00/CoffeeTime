package com.example.coffeetime.Helper;

import com.google.gson.annotations.SerializedName;

public class SubscriptionRequest {

    @SerializedName("quantity")
    private int quantity;

    public SubscriptionRequest(int quantity) {
        this.quantity = quantity;
    }
}
