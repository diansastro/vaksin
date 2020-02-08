package com.example.ghost.vaksin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.ghost.vaksin.billing.Inventory;
import com.example.ghost.vaksin.billing.Purchase;
import com.example.ghost.vaksin.billing.IabHelper;
import com.example.ghost.vaksin.billing.IabResult;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.example.ghost.vaksin.ConnectionDetector.isConnected;

public class Order extends AppCompatActivity implements View.OnClickListener {

    private EditText tanggalEditText;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat simpleDateFormat;
    private ProgressDialog progressDialog;
    private static RadioGroup jenisVaksin;
    private static RadioButton vaksin1,vaksin2,vaksin3,vaksin4,
            vaksin5,vaksin6 /*,vaksin7,vaksin8,vaksin9,vaksin10,vaksin11 */;
    EditText alamat;
    String namaVaksin;
    IabHelper mHelper;
    private Button bayar;
    static String SKU;
    static final String ITEM_SKU = "com.rumahvaksin.billing";
    static final String ITEM_SKU2 = "com.rumahvaksin.billing12";
    static final String ITEM_SKU3 = "com.rumahvaksin.billing3";
    static final String ITEM_SKU4 = "com.rumahvaksin.billing4";
    static final String ITEM_SKU5 = "com.rumahvaksin.billing5";
    static final String ITEM_SKU6 = "com.rumahvaksin.billing6";
    /*static final String ITEM_SKU7 = "com.rumahvaksin.billing7";
    static final String ITEM_SKU8 = "com.rumahvaksin.billing8";
    static final String ITEM_SKU9 = "com.rumahvaksin.billing9";
    static final String ITEM_SKU10 = "com.rumahvaksin.billing10";
    static final String ITEM_SKU11 = "com.rumahvaksin.billing11"; */
    private static final String TAG = "InAppBilling";
    private ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getSupportActionBar().setTitle("Order Vaksin");
        cd = new ConnectionDetector();

        String base64EncodedPublicKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxi/lcSnEAioYpp0/mX75Ij7yyLR2J1WSaJSoBzHL8K0Z5T4bcpbbFL+3fGpbJxwWFZKUkAg79iETSUnfKnhg4RPJUbrld41H0SKEz3E7m1EHLEvNTSgyC2kewz8oT5+cf//As4KcE6lRTd43p0Iw40z0bUgcb9KOWI0Ib+OfoydB8x9ecUqLszZPFKmL/2P0RDg4DZU77lIVugOdrGhTyw5ZQOLdYQmCF/E/DMlAli0xcckwyY5F/ioabgggHT1/xGL/AwCIykEtoHSLjwsocefpLDrxvY1csFXsCb/5ZLhw46CKtcutE8za2iyU3KTZibi5XHkSLxvQD2G+J7YZHwIDAQAB";
        bayar = (Button) findViewById(R.id.klikBayar);

        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()){
                    //Toast.makeText(MainActivity.this,result.toString(),Toast.LENGTH_LONG).show();
                    Log.d(TAG, "In-App Billing Setup failed: " + result);
                } else {
                    Log.d(TAG, "In App Setup Billing OK");
                }
            }
        });

        jenisVaksin = (RadioGroup) findViewById(R.id.radioGroup2);
        vaksin1 = (RadioButton) findViewById(R.id.radioButton1);
        vaksin2 = (RadioButton) findViewById(R.id.radioButton2);
        vaksin3 = (RadioButton) findViewById(R.id.radioButton3);
        vaksin4 = (RadioButton) findViewById(R.id.radioButton4);
        vaksin5 = (RadioButton) findViewById(R.id.radioButton5);
        vaksin6 = (RadioButton) findViewById(R.id.radioButton6);
        /*vaksin7 = (RadioButton) findViewById(R.id.radioButton7);
        vaksin8 = (RadioButton) findViewById(R.id.radioButton8);
        vaksin9 = (RadioButton) findViewById(R.id.radioButton9);
        vaksin10 = (RadioButton) findViewById(R.id.radioButton10);
        vaksin11 = (RadioButton) findViewById(R.id.radioButton11); */
        alamat = (EditText) findViewById(R.id.alamatKirim);
        tanggalEditText = (EditText)findViewById(R.id.editText11);
        tanggalEditText.requestFocus();
        progressDialog = new ProgressDialog(this);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        setTanggal();

        RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (vaksin1.isChecked()){
                    namaVaksin = getString(R.string.vaksinA);
                    Log.i("vaksin",getString(R.string.vaksinA));
                    SKU = ITEM_SKU;
                } else if (vaksin2.isChecked()){
                    namaVaksin = getString(R.string.vaksinB);
                    SKU = ITEM_SKU2;
                } else if (vaksin3.isChecked()){
                    namaVaksin = getString(R.string.vaksinC);
                    SKU = ITEM_SKU3;
                } else if (vaksin4.isChecked()){
                    namaVaksin = getString(R.string.vaksinD);
                    SKU = ITEM_SKU4;
                } else if (vaksin5.isChecked()){
                    namaVaksin = getString(R.string.vaksinE);
                    SKU = ITEM_SKU5;
                } else if (vaksin6.isChecked()){
                    namaVaksin = getString(R.string.vaksinF);
                    SKU = ITEM_SKU6;
                } /*else if (vaksin7.isChecked()){
                    namaVaksin = "Vaksin Jerap TD";
                    SKU = ITEM_SKU7;
                } else if (vaksin8.isChecked()){
                    namaVaksin = "Vaksin Jerap DT";
                    SKU = ITEM_SKU8;
                } else if (vaksin9.isChecked()){
                    namaVaksin = "Vaksin TT";
                    SKU = ITEM_SKU9;
                } else if (vaksin10.isChecked()){
                    namaVaksin = "Vaksin DTP";
                    SKU = ITEM_SKU10;
                } else if (vaksin11.isChecked()){
                    namaVaksin = "Vaksin DTP-HB";
                    SKU = ITEM_SKU11;
                } */
            }
        };
        jenisVaksin.setOnCheckedChangeListener(listener);
    }

    private void setTanggal(){
        tanggalEditText.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newCalendar = Calendar.getInstance();
                newCalendar.set(year, monthOfYear,dayOfMonth);
                Log.i("tanggal", "" + simpleDateFormat.format(newCalendar.getTime()));
                tanggalEditText.setText(simpleDateFormat.format(newCalendar.getTime()));
                //checkAntrian();
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {
        if(v == tanggalEditText){
            datePickerDialog.show();
        }
    }

    //bayar bayar coyyy
    public void bayar (View view){
        mHelper.launchPurchaseFlow(this, SKU, 10001,
                mPurchaseFinishedListener, "mypurchasetoken");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)){
            super.onActivityResult(requestCode, resultCode,data);
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()){
                return;
            } else if (purchase.getSku().equals(SKU)){
                customItem();
                bayar.setEnabled(false);
            }
        }
    };

    public void customItem(){
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (result.isFailure()){

            } else {
                mHelper.consumeAsync(inventory.getPurchase(SKU), mConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener
            = new IabHelper.OnConsumeFinishedListener() {
        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (result.isSuccess()){
                //clickButton.setEnabled(true);
                //insert data pembelian ke database
                String alamatKirimText = alamat.getText().toString();
                String namaOrder = MainActivity.nama;
                BackgroundSubmitOrder backgroundSubmitOrder =new BackgroundSubmitOrder(Order.this);
                backgroundSubmitOrder.execute(namaOrder,alamatKirimText,namaVaksin);
                //Toast.makeText(Order.this, "Berhasil Cuyy", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    public class BackgroundSubmitOrder extends AsyncTask<String, Void, String>{
        Context context;
        AlertDialog alertDialog;
        BackgroundSubmitOrder(Context ctx) { context = ctx;}

        @Override
        protected String doInBackground(String... params) {
            String submit_url = "http://www.rumahvaksin.com/android/order.php";
            try {
                String alamatKirim = params[0];
                String vaksin = params[1];
                String namaOrder = params[2];
                URL url = new URL(submit_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("alamatKirim","UTF-8")+"="+URLEncoder.encode(alamatKirim,"UTF-8")+"&"
                                    +URLEncoder.encode("namaOrder","UTF-8")+"="+URLEncoder.encode(namaOrder,"UTF-8")+"&"
                                    +URLEncoder.encode("vaksin","UTF-8")+"="+URLEncoder.encode(vaksin,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine())!=null){
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return  result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute(){
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Status Pembelian");
        }

        @Override
        protected void onPostExecute(String result){
            if (result.equals("success")){
                alertDialog.setMessage(result);
                alertDialog.show();
            }else {
                alertDialog.setMessage(result);
                alertDialog.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Order.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
