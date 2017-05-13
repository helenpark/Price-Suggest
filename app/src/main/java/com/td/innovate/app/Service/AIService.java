package com.td.innovate.app.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.td.innovate.app.Model.Profile;
import com.td.innovate.app.Model.Transaction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class AIService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_AI = "com.td.innovate.app.Service.action.AI";

    private static Profile profile;

    public AIService() {
        super("AIService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_AI.equals(action)) {
                handleActionAI();
            }
        }
    }

    /**
     * Handle action AI in the provided background thread with the provided
     * parameters.
     */
    private void handleActionAI() {
//        // create a handler to post messages to the main thread
//        Handler mHandler = new Handler(getMainLooper());
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
//            }
//        });

        profile = new Profile(getApplicationContext());
        ArrayList<Transaction> transactions = profile.getTransactions();
        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int todayMonthDay = today.get(Calendar.DAY_OF_MONTH);
        Calendar beginningOfThisMonth = Calendar.getInstance();
        beginningOfThisMonth.set(todayYear, todayMonth, 1);
        Calendar beginningOfThisMonthAYearAgo = Calendar.getInstance();
        beginningOfThisMonthAYearAgo.set(todayYear - 1, todayMonth, 1);

        Double currentMonthSpending = 0.0;
        Double[] monthExpensesAfterDate = new Double[12];
        Arrays.fill(monthExpensesAfterDate, 0.0);
        List<Double> monthExpensesAfterDateList = Arrays.asList(monthExpensesAfterDate);
        Calendar oldDate = Calendar.getInstance();
        oldDate.set(1800, Calendar.JANUARY, 1);
        Calendar earliestDate = oldDate;

        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            Calendar transactionDate = new GregorianCalendar();
            transactionDate.setTime(transaction.getDate());

            if (transactionDate.get(Calendar.MONTH) == todayMonth) {
                currentMonthSpending += transaction.getCost();
            }

            if (transactionDate.compareTo(beginningOfThisMonthAYearAgo) > 0 && transactionDate.compareTo(beginningOfThisMonth) < 0) {
                if (transactionDate.get(Calendar.DAY_OF_MONTH) > todayMonthDay) {
                    int tempListIndex = transactionDate.get(Calendar.MONTH);
                    monthExpensesAfterDateList.set(tempListIndex, monthExpensesAfterDateList.get(tempListIndex) + transaction.getCost());
                }
            }
        }

        System.out.println(currentMonthSpending);

        Double totalHistoricTransactionCostUntilEndOfMonth = 0.0;
        int numOfHistoricMonthsToConsider = 0;

        // Known issue with this method: If you simply don't have any transactions for a month it gets omitted from the average spending until the end of the month calculation
        for (int i = 0; i < 12; i++) {
            Double monthBeingCheckedTotal = monthExpensesAfterDateList.get(i);
            if (monthBeingCheckedTotal != 0.0) {
                totalHistoricTransactionCostUntilEndOfMonth += monthBeingCheckedTotal;
                numOfHistoricMonthsToConsider += 1;
            }
        }

        Double averageSpendingUntilEndOfMonth = totalHistoricTransactionCostUntilEndOfMonth / numOfHistoricMonthsToConsider;
        System.out.println(averageSpendingUntilEndOfMonth);
    }
}
