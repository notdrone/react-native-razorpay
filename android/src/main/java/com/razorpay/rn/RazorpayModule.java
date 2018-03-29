
package com.razorpay.rn;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.razorpay.CheckoutActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class RazorpayModule extends ReactContextBaseJavaModule implements PaymentActivity.Listener {

    public static final int RZP_REQUEST_CODE = 72967729;
    public static final String MAP_KEY_RZP_PAYMENT_ID = "razorpay_payment_id";
    public static final String MAP_KEY_PAYMENT_ID = "payment_id";
    public static final String MAP_KEY_ERROR_CODE = "code";
    public static final String MAP_KEY_ERROR_DESC = "description";
    public static final String MAP_KEY_PAYMENT_DETAILS = "details";
    public static final String MAP_KEY_WALLET_NAME = "name";
    ReactApplicationContext reactContext;


    @Override
    public void onSuccess(String id) {
        try {
            sendEvent("Razorpay::PAYMENT_SUCCESS", Utils.jsonToWritableMap(new JSONObject("{id:"+id+"}")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError() {
        Log.e("Hello", "********");
        try {
            sendEvent("Razorpay::PAYMENT_ERROR", Utils.jsonToWritableMap(new JSONObject("{err:123}")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public RazorpayModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RazorpayCheckout";
    }

    @ReactMethod
    public void open(ReadableMap options) {
        PaymentActivity.listner = this;
        Activity currentActivity = getCurrentActivity();
        try {
            JSONObject optionsJSON = Utils.readableMapToJson(options);
            Intent intent2 = PaymentActivity.createIntent(currentActivity, optionsJSON.toString());
            currentActivity.startActivityForResult(intent2, 0);
        } catch (Exception e) {
            Log.e("****", e.toString());
        }
    }

    private void sendEvent(String eventName, WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

}
