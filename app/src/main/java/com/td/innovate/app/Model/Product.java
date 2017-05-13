package com.td.innovate.app.Model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zunairsyed on 2015-11-05.
 */
public class Product {

    private String source;
    private String country;
    private String key;
    private String value;
    private String success;
    private String name;
    private String brand_name;
    private String category_name;
    private String review_count;
    private String review_rating;
    private String url;
    private String image_url;
    private String description;

    private boolean couldNotFindProduct = false;
    private ArrayList<Offer> offers;

    public Product(String jsonAsString) throws JSONException {
        this(new JSONObject(jsonAsString));
    }

    public Product(JSONObject jsonObject) throws JSONException {
        offers = new ArrayList<Offer>();

        this.source = jsonObject.get("source").toString();
        this.country= jsonObject.get("country").toString();
        this.key= jsonObject.get("key").toString();
        this.value= jsonObject.get("value").toString();
        this.success = jsonObject.get("success").toString();

        this.name = jsonObject.get("name").toString();
        this.brand_name = jsonObject.get("brand_name").toString();
        this.category_name = jsonObject.get("category_name").toString();
        this.review_count = jsonObject.get("review_count").toString();
        this.review_rating = jsonObject.get("review_rating").toString();
        this.url = jsonObject.get("url").toString();
        this.image_url = jsonObject.get("image_url").toString();
        this.description = jsonObject.get("description").toString();

        try {
            for (int i = 0; i < jsonObject.getJSONArray("offers").length(); i++) {
                offers.add(new Offer(jsonObject.getJSONArray("offers").get(i).toString()));
            }
        }catch (Exception e){
            Log.d("ERROR PARSING","OOPS, Either Offers were not available, or parsing went wrong");
        }


    }

    @Override
    public String toString() {
        return "name: " + name
                + "\nbrand_name: " + brand_name
                + "\ndescription: $" + description;

    }


    public String getSource(){
        return source;
    }

    public String getCountry(){
        return country;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public ArrayList <Offer> getOffers() {
        return offers;
    }

    public String getCategory_name() {
        return category_name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getReview_count() {
        return review_count;
    }

    public String getReview_rating() {
        return review_rating;
    }

    public String getSuccess() {
        return success;
    }

    public String getUrl() {
        return url;
    }

    public String getValue() {
        return value;
    }

    public boolean getCouldNotFindProduct() {
        return couldNotFindProduct;
    }

    public void setCouldNotFindProduct(boolean couldNotFindProduct) {
        this.couldNotFindProduct = couldNotFindProduct;
    }
}





//
//"source": "google-shopping",
//        "country": "us",
//        "key": "keyword",
//        "value": "xbox",
//        "success": true,
//        "updated_at": "2015-11-05T15:46:53Z",
//        "id": "loose-offers-keyword-xbox",
//        "condition_filter": "all",
//        "gtins": null,
//        "eans": null,
//        "name": null,
//        "brand_name": null,
//        "category_name": null,
//        "review_count": null,
//        "review_rating": null,
//        "url": "https://www.google.com/search?tbm=shop&tbs=vw:l,p_ord:p&q=xbox",
//        "image_url": null,
//        "description": null,
//        "offers"
