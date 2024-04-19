package com.example.coffeetime.Domain;

import java.io.Serializable;

public class CoffeeDomain implements Serializable {
    private int id;

    private String name;
    private String description;
    private String image;
    private String cafe_id;
    private String cafeName;
    public String getCafe_id() {
        return cafe_id;
    }

    public void setCafe_id(String cafe_id) {
        this.cafe_id = cafe_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CoffeeDomain(int id, String name, String description, String cafe_id,String image,String cafeName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.cafe_id = cafe_id;
        this.cafeName = cafeName;
    }

    public String getCafeName() {
        return cafeName;
    }

    public void setCafeName(String cafeName) {
        this.cafeName = cafeName;
    }
}
