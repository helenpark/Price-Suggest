package com.td.innovate.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.td.innovate.app.Adapter.MainViewPagerFragmentAdapter;
import com.td.innovate.app.Fragment.ProductInfoFragment;
import com.td.innovate.app.Fragment.StoreFragment;
import com.td.innovate.app.Model.Product;
import com.td.innovate.app.Model.Profile;
import com.td.innovate.app.Model.Result;
import com.td.innovate.app.R;
import com.td.innovate.app.Utils.JSONReader;
import com.td.innovate.app.Utils.ShakeDetector;
import com.td.innovate.app.Utils.UpdateProductInfoCallBack;
import com.td.innovate.app.Utils.UpdateStoreInfoCallBack;
import com.viewpagerindicator.IconPageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

public class MainViewPagerActivity extends FragmentActivity{


    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    private SharedPreferences prefs;


    //Callbacks for the fragment
    public static UpdateProductInfoCallBack updateProductInfoCallBack;
    public static UpdateStoreInfoCallBack updateStoreInfoCallBack;

    ViewPager mPager;
    IconPageIndicator mIndicator;

    private String keywords;
    private String barcode;

    public static ArrayList<Result> results;
    public static boolean hasFoundResults = false;

    String productUrl = "https://api.semantics3.com/v1/products";
    private final OkHttpClient client = new OkHttpClient();
    private String CONSUMER_KEY = "SEM3DA42711FE1471178AEE4FFB34E28A756";
    private String CONSUMER_SECRET = "OGI2ZDdkYTViODdjYWRhMjVmYjhkNDBlMGQwYWFjZmM";

    private MainViewPagerFragmentAdapter pageAdapter;

    private Context context;
    private Profile profile;

    private static final String mLogTag = "MainViewPagerActivity";

    //--------Price API--------//
    private static final String PRICEAPI_URL = "https://api.priceapi.com/products/single?";
    private static final String TOKEN = "PBKWGQXKRNUBGUVQQUWVSPBKPOEALZJFMSBTGSGIPUEQBDYWBKOOLFUKFPJREDLL";
    private static final String COUNRTY = "us";
    private static final String SOURCE = "google-shopping";
    private static final String CURRENTNESS = "daily_updated";
    private static final String COMPLETENESS = "one_page";
    private static String KEY = "keyword";
    private String value ;

    public static Product mainProduct ;
    JSONObject json = null ;

    //--------Price API--------//



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Log.i(mLogTag,"CREATE");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_pager);

        results = new ArrayList<Result>();
        hasFoundResults = false;

        context = this;
        profile = new Profile(context);

        pageAdapter = new MainViewPagerFragmentAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager)findViewById(R.id.myViewPager);
        pager.setAdapter(pageAdapter);

        mPager = (ViewPager)findViewById(R.id.myViewPager);
        mPager.setAdapter(pageAdapter);
        mPager.setCurrentItem(1);

        mIndicator = (IconPageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.setCurrentItem(1);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.keywords = extras.getString("keywords");
            this.barcode = extras.getString("barcode");
            new GetDataRequestTask().execute();
        }

//            try {
//                getMatches(new Callback() {
//                    @Override
//                    public void onFailure(Request request, IOException ioe) {
//                        // Something went wrong
//                        Log.e("Network error", "Error making semantics get request");
//                    }
//
//                    @Override
//                    public void onResponse(Response response) throws IOException {
//
//                        if (response.isSuccessful()) {
//                            String responseStr = response.body().string();
//                            // Do what you want to do with the response.
//                            System.out.println(responseStr);
//                            try {
//                                Log.d("semantics test","get here 1");
//                                JSONObject responseObject = new JSONObject(responseStr);
//
//                                Log.d("semantics test","get here 2");
//                                if(! responseObject.get("results_count").equals("0")) {
//                                    Log.d("semantics test","get here 3");
//
//                                    JSONArray resultsArray = responseObject.getJSONArray("results");
//                                    Log.d("semantics test","get here 4 JSON length: "+ resultsArray.length());
//
//                                  //  results = new Result[responseObject.getJSONArray("results").length()];
//                                    for (int i = 0; i < responseObject.getJSONArray("results").length(); i++) {
//                                        results.add(new Result(resultsArray.getJSONObject(i)));
//                         //               results[i] = new Result(resultsArray.getJSONObject(i));
//
//                                    }
//                                    hasFoundResults = true;
////                                    pageAdapter.update();
//
//                                    if (results.get(0) != null) {
//                                        Result firstMatch = results.get(0);
//                                        String firstResultName = firstMatch.getName();
//                                        ArrayList<Result> pastResults = profile.getPastResults();
//                                        System.out.println(pastResults);
//                                        boolean alreadyInPastResults = false;
//                                        for (int i = 0; i < pastResults.size(); i++) {
//                                            if (firstResultName.equals(pastResults.get(i).getName())) {
//                                                alreadyInPastResults = true;
//                                            }
//                                        }
//                                        if (!alreadyInPastResults) {
//                                            pastResults.add(firstMatch);
//                                            profile.setPastResults(pastResults);
//                                        }
//                                    }
//
//                                    Log.d("semantics test", "get here 5");
//
//                                    for (int i = 0; i < results.size(); i++) {
//                                        System.out.println("\n i: " + i + " =========== \n " + results.get(i).toString());
//                                    }
//
//                                    for (int i = 0; i < results.size(); i++) {
//                                        if(Integer.parseInt(results.get(i).getImages_total()) > 0){
//                                            String url = results.get(i).getImages().get(0);
//                                            System.out.println("\n i: " + i + " =========== \n " + url);
//                                        }
//                                    }
//
//
//                                    Log.d("semantics test", "get here 6666");
//
//                                    //        callback.callbackForDataReturned();
//
//                                }else{
//                                    System.out.println("No Results Found");
//                                }
//                            } catch (JSONException je) {
//                                Log.e("Post response not JSON", je.getMessage());
//                                je.printStackTrace();
//                            }
//                        } else {
//                            // Request not successful
//                            System.out.println("Request not successful");
//                            System.out.println(response.code());
//                            System.out.println(response.message());
//                            System.out.println(response.body().string());
//                        }
//                    }
//                });
//            } catch (Throwable throwable) {
//                throwable.printStackTrace();
//            }
//        }else{
//            Toast.makeText(this, "Oops, Keywords weren't passed in", Toast.LENGTH_SHORT).show();
//        }








        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
				/*
				 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */
                Toast.makeText(context, "Shaken", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(mLogTag, "startmain");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_main_view_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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


    public void onRequestData(){
//        Log.i("yayRD", "RD");
//     //   while (hasFoundResults != true){}
//        Log.i("yayRD", String.valueOf(results.size()));
//    //    pageAdapter.update();
    }

    public void onProductRequestData(){
//        Log.i("yayPRD", "PRD");
//     //   while (hasFoundResults != true){}
//        Log.i("yayPRD", String.valueOf(results.size()));
//     //   pageAdapter.productUpdate();
    }



    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }











    private class GetDataRequestTask extends AsyncTask<String, Void, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(MainViewPagerActivity.this);
            pd.setMessage("Loading");
            pd.show();
            pd.setCanceledOnTouchOutside(false);

        }

        @Override
        protected String doInBackground(String... params) {

            if(keywords.equals("null")){
                KEY = "gtin";
                value = barcode;
            }else{
                KEY = "keyword";
                value = keywords;
            }
            Log.d("Values","Key : "+KEY + " |  value "+value);

            try {
                json = JSONReader.readJsonFromUrl(PRICEAPI_URL + "token=" + TOKEN
                        +"&country=" + COUNRTY + "&source=" +SOURCE+"&currentness="
                        + CURRENTNESS +"&completeness=" + COMPLETENESS + "&key="
                        + KEY + "&value=" + URLEncoder.encode(value, "UTF-8"));

                hasFoundResults = true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(mLogTag, "testing:1 ");


            System.out.println(json.toString());

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd != null)
            {
                pd.dismiss();
            }
            hasFoundResults = true;

            try {
                mainProduct = new Product(json.getJSONArray("products").get(0).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(mLogTag, "testing: "+ mainProduct.toString());

            updateProductInfoCallBack.updateFragmentsCall();
            updateStoreInfoCallBack.updateFragmentsCall();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof ProductInfoFragment)
            updateProductInfoCallBack = (ProductInfoFragment) fragment;
        Log.d(mLogTag, "ATTACHED FRAGMENT: " + (updateProductInfoCallBack == null));


        if (fragment instanceof StoreFragment)
            updateStoreInfoCallBack = (StoreFragment) fragment;
        Log.d(mLogTag,"ATTACHED FRAGMENT: " + (updateStoreInfoCallBack == null));

    }






}





























/*
 super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_pager);

        MainViewPagerFragmentAdapter pageAdapter = new MainViewPagerFragmentAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager)findViewById(R.id.myViewPager);
        pager.setAdapter(pageAdapter);

        mPager = (ViewPager)findViewById(R.id.myViewPager);
        mPager.setAdapter(pageAdapter);

        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator = indicator;
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
        // indicator.setBackgroundColor(0xFFCCCCCC);
        indicator.setRadius(15 * density);
        indicator.setPageColor(0x33888888);
        indicator.setFillColor(0x88551A8B);
        // indicator.setStrokeColor(0xFF000000);
        //  indicator.setStrokeWidth(2 * density);
        // indicator.set

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("PageSelected", "Position: "+position) ;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
 */