package com.td.innovate.app.Model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mmmoussa on 2015-11-10.
 */
public class Transaction implements Serializable {
    private String name = "";
    private String stringDate = "";
    private Date date;
    private Double cost = 0.0;

    public Transaction(String name, String stringDate, Double cost) {
        this.name = name;
        this.stringDate = stringDate;
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.date = date;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public String getStringDate() {
        return stringDate;
    }

    public Date getDate() {
        return date;
    }

    public Double getCost() {
        return cost;
    }
}
