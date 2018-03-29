package com.razorpay.rn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends Activity implements PaymentResultListener {
    public static Listener listner;
    interface Listener{
        void onSuccess(String id);
        void onError();
    }
    public static Intent createIntent(Context context, String options){
        Intent intent= new Intent(context, PaymentActivity.class);
        intent.putExtra("OPTIONS", options);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            String optionsString = getIntent().getStringExtra("OPTIONS");
            Log.e("*******",optionsString);
            Checkout checkout = new Checkout();
            JSONObject optionsObject = new JSONObject(optionsString);
            checkout.setKeyID(optionsObject.getString("key"));
            checkout.open(this, optionsObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        listner.onSuccess(s);
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        listner.onError();
        finish();
    }
}
