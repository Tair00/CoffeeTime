package com.example.coffeetime.Domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "cart_items1")
public class CafeItem implements Serializable {


    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String picture;
    private String description;
    private Float price;
    private Float star;
    private int coffee;
    private int cat_id;
    private String imageUrl;
    private int favId; // Добавьте поле для favId

    public int getFavId() {
        return favId;
    }

    public void setFavId(int favId) {
        this.favId = favId;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getStar() {
        if (star != null) {
            return Math.round(star * 10.0f) / 10.0f;
        }
        return null;
    }

    public void setStar(Float star) {
        if (star != null) {
            this.star = Math.round(star * 10.0f) / 10.0f;
        } else {
            this.star = null;
        }
    }

    public int getCoffee() {
        return coffee;
    }

    public void setCoffee(int table) {
        this.coffee = table;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CafeItem(String name, String picture, String description, Float price, Float star, int coffee) {
        this.name = name;
        this.picture = picture;
        this.description = description;
        this.price = price;
        this.star = star;
        this.coffee = coffee;
    }
    public CafeItem() {

    }


}