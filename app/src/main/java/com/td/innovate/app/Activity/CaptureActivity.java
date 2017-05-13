package com.td.innovate.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.scandit.barcodepicker.BarcodePicker;
import com.scandit.barcodepicker.OnScanListener;
import com.scandit.barcodepicker.ScanSession;
import com.scandit.barcodepicker.ScanSettings;
import com.scandit.barcodepicker.ScanditLicense;
import com.scandit.recognition.Barcode;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CaptureActivity extends AppCompatActivity implements OnScanListener {

    // The main object for recognizing a displaying barcodes.
    private BarcodePicker mBarcodePicker;
    private static String SCANDIT_KEY = "AOZZqrqMI1R66YZRZXmAR7ARPfE721w2oFkWA5dFwO4";
    private Context context;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    private final OkHttpClient client = new OkHttpClient();
    private String CLOUDSIGHT_KEY = "7cGLDi98UTkMcRci44YclA";
    private String reqUrl = "https://api.cloudsightapi.com/image_requests";
    private String resUrl = "https://api.cloudsightapi.com/image_responses/";
    private boolean gotKeywords;
    private ProgressDialog progressDialog;
    private String keywords = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // super.onCreate(savedInstanceState) is in initializeAndStartBarcodeScanning()
        context = this;


//        // Capture image
//
//        gotKeywords = false;
//
//        // create Intent to take a picture and return control to the calling application
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
//
//        // start the image capture Intent
//        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);


        // Capture Barcode

        ScanditLicense.setAppKey(SCANDIT_KEY);

        // Initialize and start the bar code recognition.
        initializeAndStartBarcodeScanning(savedInstanceState);

//        setContentView(R.layout.activity_barcode_capture);
    }

    @Override
    protected void onPause() {
        // When the activity is in the background immediately stop the
        // scanning to save resources and free the camera.
        mBarcodePicker.stopScanning();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // Once the activity is in the foreground again, restart scanning.
        mBarcodePicker.startScanning();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        mBarcodePicker.stopScanning();
        finish();
    }

    /**
     * Initializes and starts the bar code scanning.
     */
    public void initializeAndStartBarcodeScanning(Bundle savedInstanceState) {
        // Switch to full screen.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // The scanning behavior of the barcode picker is configured through scan
        // settings. We start with empty scan settings and enable a very generous
        // set of symbologies. In your own apps, only enable the symbologies you
        // actually need.
        ScanSettings settings = ScanSettings.create();
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_EAN13, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_UPCA, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_EAN8, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_UPCE, true);

        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_QR, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_DATA_MATRIX, true);

        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_CODE39, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_CODE128, true);

        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_INTERLEAVED_2_OF_5, true);

        settings.setCameraFacingPreference(ScanSettings.CAMERA_FACING_BACK);


        // Some Android 2.3+ devices do not support rotated camera feeds. On these devices, the
        // barcode picker emulates portrait mode by rotating the scan UI.
        boolean emulatePortraitMode = !BarcodePicker.canRunPortraitPicker();
        if (emulatePortraitMode) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        super.onCreate(savedInstanceState);

        BarcodePicker picker = new BarcodePicker(this, settings);

        setContentView(picker);
        mBarcodePicker = picker;

        // Register listener, in order to be notified about relevant events
        // (e.g. a successfully scanned bar code).
        mBarcodePicker.setOnScanListener(this);

    }

    /**
     *  Called when a barcode has been decoded successfully.
     */
    public void didScan(ScanSession session) {
        String message = "";
        for (Barcode code : session.getNewlyRecognizedCodes()) {
            String data = code.getData();
            // truncate code to certain length
            String cleanData = data;
            if (data.length() > 30) {
                cleanData = data.substring(0, 25) + "[...]";
            }
//            if (message.length() > 0) {
//                message += "\n\n\n";
//            }
            message += cleanData;
//            message += "\n\n(" + code.getSymbologyName().toUpperCase() + ")";
        }

        System.out.println(message);

        Intent intent = new Intent(context, MainViewPagerActivity.class);
        intent.putExtra("keywords", "null");
        intent.putExtra("barcode", message);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        progressDialog = ProgressDialog.show(this, "Getting results", "Processing image", true);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent

                try {
                    postImage(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException ioe) {
                            // Something went wrong
                            Log.e("Network error", "Error making image post request");
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String responseStr = response.body().string();
                                // Do what you want to do with the response.
                                Log.d("response","responseSTR: "+responseStr);
                                System.out.println(responseStr);
                                try {
                                    JSONObject myObject = new JSONObject(responseStr);
                                    final String token = myObject.getString("token");

                                    checkForKeywords(token);
                                } catch (JSONException je) {
                                    Log.e("Post response not JSON", je.getMessage());
                                }
                            } else {
                                // Request not successful
                                System.out.println("Request not successful");
                                System.out.println(response.code());
                                System.out.println(response.message());
                                System.out.println(response.body().string());
                                progressDialog.dismiss();
                            }
                        }
                    });
                } catch (Throwable e) {
                    Log.e("Image call error", e.getMessage());
                    progressDialog.dismiss();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                progressDialog.dismiss();
            } else {
                // Image capture failed, advise user
                progressDialog.dismiss();
            }
        }
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyTestCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    Call postImage(Callback callback) throws Throwable {
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"image_request[locale]\""),
                        RequestBody.create(null, "en-US"))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"image_request[image]\"; filename=\"testimage.jpg\""),
                        RequestBody.create(MEDIA_TYPE_JPG, new File(fileUri.getPath())))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "CloudSight " + CLOUDSIGHT_KEY)
                .url(reqUrl)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    Call getKeywords(String token, Callback callback) throws Throwable {
        String tokenUrl = resUrl + token;

        Request request = new Request.Builder()
                .header("Authorization", "CloudSight " + CLOUDSIGHT_KEY)
                .url(tokenUrl)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    private void checkForKeywords(final String token) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                progressDialog.setMessage("Identifying image");

                final Handler h = new Handler();
                final int delay = 3000; // milliseconds

                h.postDelayed(new Runnable() {
                    public void run() {
                        //do something
                        try {
                            getKeywords(token, new Callback() {
                                @Override
                                public void onFailure(Request request, IOException ioe) {
                                    // Something went wrong
                                    Log.e("Network error", "Error making token request");
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onResponse(Response response) throws IOException {
                                    if (response.isSuccessful()) {
                                        String responseStr = response.body().string();
                                        // Do what you want to do with the response.
                                        System.out.println(responseStr);
                                        try {
                                            JSONObject myObject = new JSONObject(responseStr);
                                            String status = myObject.getString("status");
                                            if (status.equals("completed")) {
                                                keywords = myObject.getString("name");
                                                gotKeywords = true;
                                            }
                                        } catch (JSONException je) {
                                            Log.e("Get response not JSON", je.getMessage());
                                            progressDialog.dismiss();
                                        }
                                    } else {
                                        // Request not successful
                                        System.out.println("Request not successful");
                                        System.out.println(response.code());
                                        System.out.println(response.message());
                                        System.out.println(response.body().string());
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        } catch (Throwable e) {
                            Log.e("Token call error", e.getMessage());
                            progressDialog.dismiss();
                        }

                        if (!gotKeywords) {
                            h.postDelayed(this, delay);
                        } else {
                            System.out.println(keywords);

                            if (progressDialog != null){
                                progressDialog.dismiss();
                            }

                            Intent intent = new Intent(context, MainViewPagerActivity.class);
                            intent.putExtra("keywords", keywords);
                            intent.putExtra("barcode", "null");
                            startActivity(intent);
                        }
                    }
                }, delay);
            }
        });
    }
}
