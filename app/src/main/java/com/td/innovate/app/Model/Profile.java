package com.td.innovate.app.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by mmmoussa on 2015-11-10.
 */
public class Profile {
    private SharedPreferences prefs;
    private String name = "";
    private Double accountBalance = 0.0;
    private Double creditBalance = 0.0;
    private Double creditLimit = 0.0;
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private ArrayList<Result> pastResults = new ArrayList<>();

    // Done by AI
    private Double currentMonthSpending = 0.0;
    private Double recurringTransactionAmount = 0.0;
    private Double expectedExpenditureByEndOfMonth = 0.0;

    public Profile(Context context) {
        this.prefs = context.getSharedPreferences("com.td.innovate.app", Context.MODE_PRIVATE);
    }

    public String getName() {
        return prefs.getString("name", "No name found");
    }

    public Double getAccountBalance() {
        return Double.longBitsToDouble(prefs.getLong("accountBalance", Double.doubleToLongBits(0.0)));
    }

    public Double getCreditBalance() {
        return Double.longBitsToDouble(prefs.getLong("creditBalance", Double.doubleToLongBits(0.0)));
    }

    public Double getCreditLimit() {
        return Double.longBitsToDouble(prefs.getLong("creditLimit", Double.doubleToLongBits(0.0)));
    }

    public ArrayList<Transaction> getTransactions() {
        Gson gson = new Gson();
        String jsonString = prefs.getString("transactions", null);
        Type type = new TypeToken<ArrayList<Transaction>>() {}.getType();
        return gson.fromJson(jsonString, type);
    }

    public ArrayList<Result> getPastResults() {
        Gson gson = new Gson();
        String jsonString = prefs.getString("pastResults", null);
        if (jsonString == null) {
            return new ArrayList<Result>();
        } else {
            Type type = new TypeToken<ArrayList<Result>>() {
            }.getType();
            return gson.fromJson(jsonString, type);
        }
    }

    public Double getCurrentMonthSpending() {
        return Double.longBitsToDouble(prefs.getLong("currentMonthSpending", Double.doubleToLongBits(0.0)));
    }

    public Double getRecurringTransactionAmount() {
        return Double.longBitsToDouble(prefs.getLong("recurringTransactionAmount", Double.doubleToLongBits(0.0)));
    }

    public Double getExpectedExpenditureByEndOfMonth() {
        return Double.longBitsToDouble(prefs.getLong("expectedExpenditureByEndOfMonth", Double.doubleToLongBits(0.0)));
    }


    public void setName(String name) {
        prefs.edit().putString("name", name).apply();
    }

    public void setAccountBalance(Double accountBalance) {
        prefs.edit().putLong("accountBalance", Double.doubleToRawLongBits(accountBalance)).apply();
    }

    public void setCreditBalance(Double creditBalance) {
        prefs.edit().putLong("creditBalance", Double.doubleToRawLongBits(creditBalance)).apply();
    }

    public void setCreditLimit(Double creditLimit) {
        prefs.edit().putLong("creditLimit", Double.doubleToRawLongBits(creditLimit)).apply();
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        Gson gson = new Gson();
        prefs.edit().putString("transactions", gson.toJson(transactions)).apply();
    }

    public void setPastResults(ArrayList<Result> pastResults) {
        Gson gson = new Gson();
        prefs.edit().putString("pastResults", gson.toJson(pastResults)).apply();
    }

    public void setCurrentMonthSpending(Double currentMonthSpending) {
        prefs.edit().putLong("currentMonthSpending", Double.doubleToRawLongBits(currentMonthSpending)).apply();
    }

    public void setRecurringTransactionAmount(Double recurringTransactionAmount) {
        prefs.edit().putLong("recurringTransactionAmount", Double.doubleToRawLongBits(recurringTransactionAmount)).apply();
    }

    public void setExpectedExpenditureByEndOfMonth(Double expectedExpenditureByEndOfMonth) {
        prefs.edit().putLong("expectedExpenditureByEndOfMonth", Double.doubleToRawLongBits(expectedExpenditureByEndOfMonth)).apply();
    }
}
