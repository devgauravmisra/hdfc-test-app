package com.razorpay.sampleapp.java;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;
import com.razorpay.Razorpay;
import com.razorpay.sampleapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class PaymentOptions extends Activity implements PaymentResultListener {

    private Razorpay razorpay;
    private WebView webview;
    private ViewGroup outerBox;
    private static final String TAG = PaymentOptions.class.getSimpleName();
    private JSONObject payload;
    private Button pay;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_options);

        pay = (Button)findViewById(R.id.pay);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitVasDetails();
            }
        });

        webview = (WebView) findViewById(R.id.payment_webview);
        outerBox = (ViewGroup) findViewById(R.id.outerbox);

        initRazorpay();
    }

    private void submitVasDetails() {
        try {
            payload = new JSONObject("{currency: 'INR'}");
            payload.put("amount", "100");
            payload.put("prefill[contact]", "9999999999");
            payload.put("prefill[email]", "customer@name.com");
            payload.put("name","Anwer");
            payload.put("description","Test Vas");
            payload.put("notes[0]","test note");
            webview.setVisibility(View.VISIBLE);
            outerBox.setVisibility(View.GONE);
            razorpay.openCheckout(payload,this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRazorpay() {
        razorpay = new Razorpay(this);
        razorpay.setWebView(webview);
    }


    /**
     * Is called if the payment was successful
     * You can now destroy the webview
     */
    @Override
    public void onPaymentSuccess(String s) {
        webview.setVisibility(View.GONE);
        outerBox.setVisibility(View.VISIBLE);
    }


    /**
     * Is called if the payment failed
     * possible values for code in this sdk are:
     * 2: in case of network error
     * 4: response parsing error
     * 5: This will contain meaningful message and can be shown to user
     * Format: {"error": {"description": "Expiry year should be greater than current year"}}
     */
    @Override
    public void onPaymentError(int i, String s) {
        webview.setVisibility(View.GONE);
        outerBox.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBackPressed() {
        razorpay.onBackPressed();
        super.onBackPressed();
        webview.setVisibility(View.GONE);
        outerBox.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        razorpay.onActivityResult(requestCode,resultCode,data);
    }

}
