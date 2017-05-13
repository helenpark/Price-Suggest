package com.td.innovate.app.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zunairsyed on 2015-11-10.
 */
public class Result {

    private String brand;
    private String category;
    private String manufacturer;
    private String name;
    private String price_currency;
    private String images_total;
    private String price;
    private String color;
    private String description;
    private ArrayList<String> images;
    private LatestOffers[] latestOffers;

    public Result(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public Result(String jsonAsString) throws JSONException {
        this(new JSONObject(jsonAsString));
    }

    public Result(JSONObject jsonObject) throws JSONException {

        this.brand = (jsonObject.has("brand")) ? jsonObject.get("brand").toString() : "null";
        this.category = (jsonObject.has("category")) ? jsonObject.get("category").toString() : "null";
        this.manufacturer = (jsonObject.has("manufacturer")) ? jsonObject.get("manufacturer").toString() : "null";
        this.name = (jsonObject.has("name")) ? jsonObject.get("name").toString() : "null";
        this.price_currency = (jsonObject.has("price_currency")) ? jsonObject.get("price_currency").toString() : "null";
        this.images_total = (jsonObject.has("images_total")) ? jsonObject.get("images_total").toString() : "null";
        this.price = (jsonObject.has("price")) ? jsonObject.get("price").toString() : "null";
        this.color = (jsonObject.has("color")) ? jsonObject.get("color").toString() : "null";
        this.brand = (jsonObject.has("brand")) ? jsonObject.get("brand").toString() : "null";
        this.description = (jsonObject.has("description")) ? jsonObject.get("description").toString() : "null";

        if (jsonObject.has("images_total")) {
            if (Integer.parseInt(jsonObject.get("images_total").toString()) > 0 && jsonObject.has("images")) {
                images = new ArrayList<String>();
                JSONArray imagesArray = jsonObject.getJSONArray("images");
                for (int i = 0; i < imagesArray.length(); i++) {
                    String imagearrayobj = imagesArray.getString(i);
                    String string = imagearrayobj.toString();
                    images.add(string);
                }
            }
        }

        try {
            Log.d("Parsing", "Got to Step 1");
            JSONArray latestOffersJSONArray = jsonObject
                    .getJSONArray("sitedetails")
                    .getJSONObject(0)
                    .getJSONArray("latestoffers");
            Log.d("Parsing", "Got to Step 2");

            latestOffers = new LatestOffers[latestOffersJSONArray.length()];
            Log.d("Parsing", "Got to Step 3 : length " + latestOffersJSONArray.length());

            for (int i = 0; i < latestOffers.length; i++) {
                Log.d("Parsing", "Got to Step 3.5 : " + latestOffersJSONArray.toString());
                Log.d("Parsing", "Got to Step 4 : " + latestOffersJSONArray.getJSONObject(i).toString());

                latestOffers[i] = new LatestOffers(latestOffersJSONArray.getJSONObject(i));
            }
            Log.d("Parsing", "Got to Step 5 : ");

        } catch (Exception e) {
            Log.d("Parsing", "OOPS did not get latestOffers");
        }

    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public String getColor() {
        return color;
    }

    public String getImages_total() {
        return images_total;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getPrice() {
        return price;
    }

    public String getPrice_currency() {
        return price_currency;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getImages() {
        return images;
    }
    public ArrayList<LatestOffers> getLatestOffers(){
        if (latestOffers != null){
           return new ArrayList<LatestOffers>(Arrays.asList(latestOffers));
        }
        else {
           return new ArrayList<LatestOffers>();
        }
    }


    @Override
    public String toString() {
        return "name: " + name
                + "\nbrand_name: " + brand
                + "\nprice: $" + price;
    }

}
