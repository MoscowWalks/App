package com.bashalex.cityquest;

import com.google.android.gms.common.api.BooleanResult;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by Alex Bash on 08.10.16.
 */

public class CityQuestResponse {
    private String name;
    private String[] images;
    private String address;
    private String[] way;
    private String image;
    private String error;
    private boolean last;

    public CityQuestResponse(String name, JsonArray images, String address, JsonArray way,
                             String image, String error, Boolean last) {
        this.name = name;
        for (JsonElement img: images) {
            images.add(img.toString());
        }
        this.address = address;
        for (JsonElement step: way) {
            way.add(step.toString());
        }
        this.image = image;
        this.error = error;
        this.last = last;
    }

    public CityQuestResponse(Throwable error) {
        this.error = error.getMessage();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String[] getWay() {
        return way;
    }

    public void setWay(String[] way) {
        this.way = way;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    @Override
    public String toString() {
        return "CityQuestResponse{" +
                "name='" + name + '\'' +
                ", images=" + images +
                ", address='" + address + '\'' +
                ", way=" + way +
                ", image='" + image + '\'' +
                '}';
    }
}
