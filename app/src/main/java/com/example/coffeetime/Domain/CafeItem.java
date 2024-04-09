package com.example.coffeetime.Domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "cart_items1")
public class CafeItem implements Serializable {


    @PrimaryKey(autoGenerate = true)
    CafeItem cafeItem;
    private int id;
    private String name;
    private String image;
    private String description;
    private String address;
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
        return image;
    }

    public void setPicture(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return address;
    }

    public void setPrice(String address) {
        this.address = address;
    }

    public Float getStar() {
        if (star != null) {
            System.out.println("123123123123123123123123GETGET GET");
            return Math.round(star * 10.0f) / 10.0f;
        }else {
            System.out.println("not GOOOOOOOOOD");
        }
        return null;
    }

    public void setStar(Float star) {
        if (star != null) {
            System.out.println("123123123123123123123123 SET");
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

    public CafeItem(String name, String picture, String description, String address, Float star, int coffee, int favId) {
        this.name = name;
        this.image = picture;
        this.description = description;
        this.address = address;
        this.star = star;
        this.coffee = coffee;
        this.favId = favId;
    }
    public CafeItem() {

    }
    public void DisplayInfo(CafeItem cafeItem){
        System.out.println(name + " = name " + id+ " = id " + favId + "= favId " + address + " address ");
    }

}