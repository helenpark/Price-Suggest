package com.td.innovate.app.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.td.innovate.app.Activity.MainViewPagerActivity;
import com.td.innovate.app.Adapter.StoreAdapter;
import com.td.innovate.app.Model.LatestOffers;
import com.td.innovate.app.Model.Offer;
import com.td.innovate.app.Model.Product;
import com.td.innovate.app.Model.Result;
import com.td.innovate.app.R;
import com.td.innovate.app.Utils.UpdateProductInfoCallBack;
import com.td.innovate.app.Utils.UpdateStoreInfoCallBack;

import java.util.ArrayList;

public class StoreFragment extends Fragment
    implements UpdateStoreInfoCallBack {

    private View view;
    private ListView offersListView;
    private Product mainProduct;
    private boolean hasOffersReceived = false;


    private static final String mLogTag = "StoreFragmentTag";


    // TODO: Rename and change types and number of parameters
    public static StoreFragment newInstance(String param1, String param2) {
        StoreFragment fragment = new StoreFragment();
        return fragment;
    }

    public StoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(mLogTag,"oncreateStore");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_store, container, false);

        offersListView = (ListView) view.findViewById(R.id.main_options_offer_listview);
        return view;
    }

    @Override
    public void onStart() {
        Log.i(mLogTag, "start");
        super.onStart();

        if(hasOffersReceived){
            update();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        Log.i(mLogTag, "storeattach");
        super.onAttach(activity);
    }

    public void update(){
        Log.i(mLogTag, "YAY UPDATED FRAGMENT 22");

        StoreAdapter adapter = new StoreAdapter(getContext(), mainProduct.getOffers());
        offersListView.setAdapter(adapter);

      /*  myList.clear();
        myList.addAll(MainViewPagerActivity.results);
        Log.i("yaymylist", String.valueOf(myList.size()));
        lo.clear();
        for (int i = 0 ; i < myList.size() ; i++){
            lo.addAll(MainViewPagerActivity.results.get(i).getLatestOffers());
        }
//        lo.addAll(MainViewPagerActivity.results.get(0).getLatestOffers());
        Log.i("yaymylist", String.valueOf(lo.size()));
        storeAdapter.notifyDataSetChanged();
        storeName = (TextView) view.findViewById(R.id.storeName);
        storeName.setText(myList.get(0).getName());
*/
    }


    @Override
    public void updateFragmentsCall() {
        hasOffersReceived = true;
        mainProduct = MainViewPagerActivity.mainProduct;
        update();
    }
}
