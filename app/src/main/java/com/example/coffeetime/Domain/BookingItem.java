package com.example.coffeetime.Domain;

public  class BookingItem {
    private String picture;
    private String date;
    private String time;
    private String name;
    private String status;
    private String number;

    public BookingItem(String picture, String date, String time, String name, String status, String number, Integer id, String cafeName) {
        this.picture = picture;
        this.date = date;
        this.time = time;
        this.name = name;
        this.status = status;
        this.number = number;
        this.id = id;
        this.cafeName = cafeName;
    }

    public String getCafeName() {
        return cafeName;
    }

    public void setCafeName(String cafeName) {
        this.cafeName = cafeName;
    }

    private Integer id;

    private String cafeName;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BookingItem(String status,String picture,  String time, String name,  String cafeName) {
        this.picture = picture;
        this.cafeName = cafeName;
        this.time = time;
        this.name = name;
        this.number = number;
        this.status = status;
    }

    public BookingItem(String status, String picture, String date, String time, String name, Integer id,String number) {
        this.id = id;
        this.picture = picture;
        this.date = date;
        this.time = time;
        this.name = name;
        this.status=status;
        this.number=number;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getPicture() {
        return picture;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}