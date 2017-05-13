package com.td.innovate.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.td.innovate.app.Model.Offer;
import com.td.innovate.app.R;

import java.util.ArrayList;

/**
 * Created by mmmoussa on 2015-11-05.
 */
public class OfferArrayAdapter extends ArrayAdapter<Offer> {
    private final Context context;
    private final ArrayList<Offer> offers;

    public OfferArrayAdapter(Context context, ArrayList<Offer> offers) {
        super(context, -1, offers);
        this.context = context;
        this.offers = offers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.price_data_list_item, parent, false);
        TextView itemName = (TextView) rowView.findViewById(R.id.itemName);
        TextView shopName = (TextView) rowView.findViewById(R.id.storeName);
        TextView price = (TextView) rowView.findViewById(R.id.price);
        TextView currency = (TextView) rowView.findViewById(R.id.currency);

        Offer offer = offers.get(position);

        if (offer.getName().equals("null")) {
            itemName.setVisibility(View.GONE);
        } else {
            itemName.setText(offer.getName());
        }
        shopName.setText(offer.getShop_name());
        price.setText("Price: " + offer.getPrice());
        currency.setText("Currency: " + offer.getCurrency());

        return rowView;
    }
}
