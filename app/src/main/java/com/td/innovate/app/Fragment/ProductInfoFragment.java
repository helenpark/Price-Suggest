package com.td.innovate.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.koushikdutta.ion.Ion;
import com.td.innovate.app.Activity.LoginActivity;
import com.td.innovate.app.Activity.MainViewPagerActivity;
import com.td.innovate.app.Model.Product;
import com.td.innovate.app.Model.Result;
import com.td.innovate.app.R;

import java.util.ArrayList;

import com.td.innovate.app.Utils.UpdateProductInfoCallBack;

public class ProductInfoFragment extends Fragment
    implements UpdateProductInfoCallBack {

    private TextView productNameTV;
    private TextView productDescTV;
    private ImageView productIV;
    private Product mainProduct;

    @Override
    public void updateFragmentsCall() {
        mainProduct = MainViewPagerActivity.mainProduct;
        productUpdate();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_info, container, false);

        productNameTV = (TextView) view.findViewById(R.id.productNameTV);
        productDescTV = (TextView) view.findViewById(R.id.product_desc_tv);
        productIV = (ImageView)view.findViewById(R.id.productIV);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void productUpdate(){
        Log.d("Updated Fragments", "YAY UPDATED FRAGMENTS");

        productNameTV.setText(mainProduct.getName());
        productDescTV.setText(mainProduct.getDescription());

        if(! mainProduct.getImage_url().equals("null"))
            Ion.with(productIV).load(mainProduct.getImage_url());

/*
        myResults.clear();
        myResults.addAll(MainViewPagerActivity.results);

        mainResult = myResults.get(0);
        if (mainResult.getName()!=null) {
            productNameTV.setText(mainResult.getName());
        }
        if (mainResult.getDescription()!=null) {
            productDescTV.setText(mainResult.getDescription());
        } else {
            productDescTV.setText("No description available");
        }
        if (mainResult.getImages().get(0)!=null) {
            Ion.with(productIV).load(mainResult.getImages().get(0));
        }

*/
    }


}