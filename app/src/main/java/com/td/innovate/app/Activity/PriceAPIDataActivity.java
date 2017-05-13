package com.td.innovate.app.Activity;

import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.td.innovate.app.Model.Offer;
import com.td.innovate.app.Adapter.OfferArrayAdapter;
import com.td.innovate.app.Model.Product;
import com.td.innovate.app.R;
import com.td.innovate.app.Model.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class PriceAPIDataActivity extends AppCompatActivity {


    //-----------Price API Variables--------------//
    private static final int REFRESH_CHECK_STATUS_INTERVAL = 1000;
    private static final String COUNTRY_CODE = "us";
    private static final String SOURCE = "google-shopping";
    private static final String KEY = "keyword";
    private static final String BARCODE = "gtin";

    private String keywords;
    private String barcode;
    private ArrayList<Offer> currentOffers;
    private ArrayAdapter<String> reviewsAdapter;
    private ArrayList<String> reviews;
    private ListView productsListView;
    private OfferArrayAdapter adapter;
    private EditText keywordEditText;
    private Product mainProduct;
    private boolean isProccessRunning = false;

    private TextView productNameTV;
    private TextView reviewsInPercentTV;
    private ImageView productImage;
    private Button reviewsButton;
    private Button priceCompareButton;

    public static ArrayList<Result> results;


    String productUrl = "https://api.priceapi.com/products/single";
    private final OkHttpClient client = new OkHttpClient();
    private String CONSUMER_KEY = "SEM399C407A4C8EF144BD2B3D3B3CDAC74D3";
    private String CONSUMER_SECRET = "NWM1NjFjM2M1MGJlMTYyNDg4YTY4MDYxNmZjODBmZTY";


    //-------------End of Price API----------------//

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_apidata);
        context = this;

        productNameTV = (TextView) findViewById(R.id.product_name_tv);
        reviewsInPercentTV= (TextView) findViewById(R.id.product_review_tv);
        productImage= (ImageView) findViewById(R.id.product_imageview);
        reviewsButton = (Button) findViewById(R.id.ReviewsButton);
        priceCompareButton = (Button) findViewById(R.id.priceCompareButton);

        currentOffers = new ArrayList<>();
        reviews = new ArrayList<String>();

        reviewsAdapter = new ArrayAdapter<String>(this, R.layout.review_list_item, reviews);

        productsListView = (ListView) findViewById(R.id.productList);
        adapter = new OfferArrayAdapter(this, currentOffers);
        productsListView.setAdapter(adapter);

        reviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsListView.setAdapter(reviewsAdapter);
            }
        });

        priceCompareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productsListView.setAdapter(adapter);
            }
        });


        keywordEditText = (EditText) findViewById(R.id.keyword_text);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.keywords = extras.getString("keywords");
            this.barcode = extras.getString("barcode");

            if (keywords.equals("null")) {
                keywordEditText.setText("UPC: " + this.barcode);
            } else {
                keywordEditText.setText(this.keywords);
            }
            try {
                getMatches(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException ioe) {
                        // Something went wrong
                        Log.e("Network error", "Error making semantics get request");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        Result results [];
                        boolean resultsFound = false;

                        if (response.isSuccessful()) {
                            String responseStr = response.body().string();
                            // Do what you want to do with the response.
                            System.out.println(responseStr);
                            try {
                                Log.d("semantics test","get here 1");
                                JSONObject responseObject = new JSONObject(responseStr);

                                Log.d("semantics test","get here 2");
                                if(! responseObject.get("results_count").equals("0")) {
                                    Log.d("semantics test","get here 3");

                                    JSONArray resultsArray = responseObject.getJSONArray("results");
                                    Log.d("semantics test","get here 4 JSON length: "+ resultsArray.length());

                                    results = new Result[responseObject.getJSONArray("results").length()];
                                    for (int i = 0; i < results.length; i++) {
                                        results[i] = new Result(resultsArray.getJSONObject(i));
                                    }
                                    Log.d("semantics test", "get here 5");

                                    for (int i = 0; i < results.length; i++) {
                                        System.out.println("\n i: "+i+" =========== \n " + results[i].toString());
                                    }

                                }else{
                                    System.out.println("No Results Found");
                                }
                            } catch (JSONException je) {
                                Log.e("Post response not JSON", je.getMessage());
                            }
                        } else {
                            // Request not successful
                            System.out.println("Request not successful");
                            System.out.println(response.code());
                            System.out.println(response.message());
                            System.out.println(response.body().string());
                        }
                    }
                });
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }else{
            Toast.makeText(this, "Oops, Keywords weren't passed in", Toast.LENGTH_SHORT).show();
        }
    }

    Call getMatches(Callback callback) throws Throwable {
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);

        String sendUrl;

        if (keywords.equals("null")) {
            String encodedBarcode = URLEncoder.encode(this.barcode, "UTF-8");
            sendUrl = productUrl + "?q=%7B%22upc%22%3A%22" + encodedBarcode + "%22%7D";
        } else {
            String encodedKeywords = URLEncoder.encode(this.keywords, "UTF-8");
            sendUrl = productUrl + "?q=%7B%22search%22%3A%22" + encodedKeywords + "%22%7D";
        }

        client.interceptors().add(new SigningInterceptor(consumer));

        Request request = new Request.Builder()
                .url(sendUrl)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }





    private void getAndPrintJsonResponse(){

    }
























//    private class GetDataRequestTask extends AsyncTask<String, Void, String> {
//        ProgressDialog pd;
//
//
//        @Override
//        protected void onPreExecute() {
//            pd = new ProgressDialog(PriceAPIDataActivity.this);
//            pd.setMessage("Loading");
//            pd.show();
//            pd.setCanceledOnTouchOutside(false);
//            isProccessRunning = true;
//
//            productNameTV.setVisibility(View.GONE);
//            reviewsInPercentTV.setVisibility(View.GONE);
//            productImage.setVisibility(View.GONE);
//
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            currentOffers.clear();
//            if (keywords.equals("null")) {
//                runRequest(barcode);
//            } else {
//                runRequest(keywords);
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            if( mainProduct != null) {
//                if (!mainProduct.getName().equals("null")) {
//                    productNameTV.setVisibility(View.VISIBLE);
//                    productNameTV.setText(mainProduct.getName());
//
//                } else {
//                    productNameTV.setVisibility(View.GONE);
//                }
//
//                if (!mainProduct.getReview_rating().equals("null")) {
//                    reviewsInPercentTV.setVisibility(View.VISIBLE);
//                    reviewsInPercentTV.setText("Review: " + mainProduct.getReview_rating() + " %");
//                } else {
//                    reviewsInPercentTV.setVisibility(View.GONE);
//                }
//
//                if (!mainProduct.getImage_url().equals("null")) {
//                    productImage.setVisibility(View.VISIBLE);
//                    Ion.with(productImage).load(mainProduct.getImage_url());
//                } else {
//                    productImage.setVisibility(View.GONE);
//                }
//
//                adapter.notifyDataSetChanged();
//            }else{
//                Toast.makeText(context,"Oops, Cannot Find This Product",Toast.LENGTH_SHORT).show();
//            }
//
//            Log.i("yay",mainProduct.getUrl());
//            if (mainProduct.getUrl() != ""){
//                Log.i("yay","madeit");
//
//                JSONObject obj;
//                AsyncHttpClient client = new AsyncHttpClient();
//                RequestParams params = new RequestParams();
//                params.put("token", "acb8456ddde73e0e02fc778da19f5808");
//                params.put("url", mainProduct.getUrl());
////                params.put("fields","title");
//                client.get("http://api.diffbot.com/v3/discussion", params, new TextHttpResponseHandler() {
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, String res) {
//                                // called when response HTTP status is "200 OK"
//                                Log.i("yayme", res);
//                                try {
//
//                                    JSONObject obj = new JSONObject(res);
//                                    Log.i("yay2 - obj", obj.toString());
//                                    JSONArray postArray = obj.getJSONArray("objects").getJSONObject(0).getJSONArray("posts");
//                                    for (int i = 0; i < postArray.length(); i++) {
//                                        reviews.add(postArray.getJSONObject(i).getString("text"));
//                                    }
//                                    reviewsAdapter.notifyDataSetChanged();
//
//
//                                } catch (Throwable t) {
//                                    Log.i("My App", "Could not parse malformed JSON: \"" + res + "\"");
//                                }
//
//                                if (pd != null)
//                                {
//                                    pd.dismiss();
//                                }
//                                isProccessRunning = false;
//                            }
//
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
////                                 called when response HTTP status is "4XX" (eg. 401, 403, 404)
//                                CharSequence text = "BAD toast!";
//                                int duration = Toast.LENGTH_SHORT;
//                                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
//                                toast.show();
//                                Log.i("yay", res);
//                                if (pd != null)
//                                {
//                                    pd.dismiss();
//                                }
//                                isProccessRunning = false;
//                            }
//
//
//                        }
//
//
//                );
////                if (pd != null)
////                {
////                    pd.dismiss();
////                }
////                isProccessRunning = false;
//            }
//
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... values) {}
//    }
//
//
//
//
//
//
//
//    //----------------PRICE API-------------------//
//    private void runRequest(String value){
//
//        //There's 3 parts to a request,
//        //First is sending the request (BulkRequest.request())
//        //Second is to check the status of the request (BulkRequest.getStatus())
//        //Third is to get the actual info (BulkRequest.getResults()) (once the status is finished)
//
//        Log.d("databack", "Starting to send Request");
//
//        //-----------Sending a Request---------//
//        JSONObject response = null;
//        BulkRequest bulk = new BulkRequest();
//        JSONObject bulkStatus;
//        if (keywords.equals("null")) {
//            bulkStatus = bulk.request(value,
//                    SOURCE, COUNTRY_CODE, BARCODE);
//        } else {
//            bulkStatus = bulk.request(value,
//                    SOURCE, COUNTRY_CODE, KEY);
//        }
//
//        Log.d("databack", "Sent A Request Step 1");
//        String jobId = "";
//        try {
//            jobId = (String) bulkStatus.get("job_id");
//        } catch (JSONException e1) {
//            e1.printStackTrace();
//        }
//
//
//        Boolean done = false;
//        while (!done) {
//            try {
//                Thread.sleep(REFRESH_CHECK_STATUS_INTERVAL);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            //-----------Getting a Status---------//
//            bulkStatus = bulk.getStatus(jobId);
//            Log.d("databack", "Got The Status");
//
//            //----------Getting The Actual Result---------//
//            Boolean isComplete = false;
//            try {
//                String status = (String) bulkStatus.get("status");
//                isComplete = status.equals("finished");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            if (isComplete) {
//                response = bulk.getResults(jobId, "json");
//                Log.d("databack", "Got The Results");
//                done = true;
//            }
//        }
//
//        Log.d("databack", "Total Response: " + response.toString());
//        try {
//            Log.d("databack", "data: 1");
//
//            JSONObject jsonObject = new JSONObject(response.toString());
//            Log.d("databack", "data: 2| ");
//
//            JSONArray productsArray = jsonObject.getJSONArray("products");
//            Log.d("databack", "data: 3| "+productsArray.toString());
//
//            mainProduct = new Product(productsArray.get(0).toString());
//            if(mainProduct.getSuccess().equals("false")){
//                return; //QUITS IF nothing found
//            }
//
//            JSONArray jsonArray =  (new JSONObject(productsArray.get(0).toString())).getJSONArray("offers");
//            Log.d("databack", "data: 4| "+jsonArray.toString());
//
//
//            // JSONArray jsonArray = jsonObject.getJSONArray("offers");
//            Log.d("databack", "data: 5| ");
//
//
//            for(int i=0; i<jsonArray.length(); i++) {
//                Log.d("databack", "data: 6| " + jsonArray.get(i).toString());
//                Offer newOffer = new Offer(jsonArray.get(i).toString());
//                currentOffers.add(newOffer);
//                Log.d("databack", " i: " + i + "Successful into JSONObj: " + newOffer.toString());
//            }
//
//        } catch (JSONException e) {
//            Log.d("databack", "NOT Successful into JSONObj: " + response.toString());
//            e.printStackTrace();
//        }
//    }
//
//    //---------------End Of Price API---------------//
//

    public void submitKeyword(View view){
    }
}

