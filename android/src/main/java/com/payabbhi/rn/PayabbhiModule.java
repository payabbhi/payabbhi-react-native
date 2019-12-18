package com.payabbhi.rn;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Map;
import com.payabbhi.Payabbhi;
import org.json.JSONObject;
import android.app.Activity;
import com.payabbhi.PaymentResponse;
import com.payabbhi.PaymentCallback;
import com.payabbhi.CheckoutActivity;

import android.content.Intent;
import com.facebook.react.bridge.WritableMap;
import org.json.JSONException;
import static android.app.Activity.RESULT_OK;

public class PayabbhiModule extends ReactContextBaseJavaModule implements ActivityEventListener {
  private static final int RN_REQUEST_CODE = 999;

  ReactApplicationContext reactContext;
  public PayabbhiModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    reactContext.addActivityEventListener(this);
  }

  @Override
  public String getName() {
    return "PayabbhiCheckout";
  }

  @ReactMethod
  public void open(ReadableMap options) {
    Activity currentActivity = getCurrentActivity();
    try {
      JSONObject optionsJSON = Utils.readableMapToJson(options);
      Intent intent = new Intent(currentActivity, CheckoutActivity.class);
      intent.putExtra("OPTIONS", optionsJSON.toString());
      currentActivity.startActivityForResult(intent, RN_REQUEST_CODE);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
    onActivityResult(requestCode, resultCode, data);
  }

  public void onNewIntent(Intent intent) {}


  public void onActivityResult(int requestCode, int resultCode, Intent intent){
    if (requestCode == RN_REQUEST_CODE && resultCode == RESULT_OK) {
      Payabbhi.handleCheckoutActivity(intent, new PaymentCallback() {
        @Override
        public void onPaymentSuccess(PaymentResponse paymentResponse) {
            JSONObject response = new JSONObject();
            try {
                response.put("order_id", paymentResponse.getOrderID());
                response.put("payment_id", paymentResponse.getPaymentID());
                response.put("payment_signature", paymentResponse.getPaymentSignature());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendEvent("Payabbhi::PAYMENT_SUCCESS", Utils.jsonToWritableMap(response));
        }

        @Override
        public void onPaymentError(int code, String message) {
            JSONObject response = new JSONObject();
            try {
                response.put("code", code);
                response.put("message", message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendEvent("Payabbhi::PAYMENT_ERROR", Utils.jsonToWritableMap(response));
        }
      });
    }
  }

  private void sendEvent(String eventName, WritableMap params) {
  reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit(eventName, params);
  }
}
