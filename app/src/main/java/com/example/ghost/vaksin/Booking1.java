package com.example.ghost.vaksin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.example.ghost.vaksin.ConnectionDetector.connectionDetectorListener;

/**
 * Created by ghost on 21/06/16.
 */
public class Booking1 extends AppCompatActivity implements View.OnClickListener {

    public static final String REGISTER_URL = "http://www.rumahvaksin.com/android/booking.php";
    public static final String CANCEL_URL = "http://www.rumahvaksin.com/android/cancelAntrian.php";
    public static final String CHECK_URL = "http://www.rumahvaksin.com/android/ambilantrian.php";
    public static final String KEY_NAMA = "username";
    public static final String KEY_TANGGAL = "tanggal";
    public static final String KEY_ANTRIAN = "antrian";

    private EditText tanggalEditText;
    private RelativeLayout pertama, kedua;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat simpleDateFormat;
    private ProgressDialog progressDialog;
    private TextView antrianSkrg, antrianAnda, berhasil;
    private int nomorSkrg, nomorAnda ,antrian=0;
    private ConnectionDetector cd;
    Button booking,butonCancel;
    SessionManager manager;
    RelativeLayout snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        manager = new SessionManager();
        getSupportActionBar().setTitle("Booking Antrian");
        snackbar = (RelativeLayout) findViewById(R.id.snackbar1);

        booking = (Button) findViewById(R.id.button);
        butonCancel = (Button) findViewById(R.id.button7);
        pertama = (RelativeLayout)findViewById(R.id.layout1);
        kedua = (RelativeLayout)findViewById(R.id.Layout2);
        antrianSkrg = (TextView)findViewById(R.id.textView2);
        antrianAnda = (TextView)findViewById(R.id.textView4);
        berhasil = (TextView)findViewById(R.id.textView7);
        tanggalEditText = (EditText)findViewById(R.id.editText);
        tanggalEditText.requestFocus();
        progressDialog = new ProgressDialog(this);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        booking.setOnClickListener(this);
        butonCancel.setOnClickListener(this);
        cekBooking();
        setTanggal();
        cd = new ConnectionDetector();

       /* if (cd != null ){
            Snackbar.make(snackbar, "Gagal Update data, " + "Cek Koneksi Internet Anda" , Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
                        }
                    })
                    .setActionTextColor(Color.BLUE)
                    .show();
        } */
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
                checkAntrian();
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void cekBooking(){
        if(manager.getPreferences(Booking1.this,"Booking").equals("1")) {
            tanggalEditText.setText(manager.getPreferences(Booking1.this,"TanggalAntrian"));
            antrianAnda.setText(manager.getPreferences(Booking1.this,"NomorAntrian"));
            Log.i("Antrian",manager.getPreferences(Booking1.this,"NomorAntrian"));

            tanggalEditText.setClickable(false);
            tanggalEditText.setEnabled(false);
            booking.setEnabled(false);
            booking.setClickable(false);
        }
    }

    private void setAntrian(int antrian) {
        nomorSkrg = antrian;
        nomorAnda = antrian+1;
        antrianSkrg.setText(String.valueOf(nomorSkrg));
        antrianAnda.setText(String.valueOf(nomorAnda));
    }

    private void checkAntrian(){
        antrian=0;
        final String tanggal = tanggalEditText.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CHECK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       /* alertDialog[0] = new AlertDialog.Builder(Booking1.this)
                                .setTitle("Booking Status")
                                .setMessage("Tanggal :\n" + tanggalEditText.getText().toString() + "\nNomor Antrian :\n" + nomorAnda)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Booking1.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }).create();
                        alertDialog[0].show();*/

                        Log.i("Antrian",response);

                        antrian = Integer.parseInt(response);
                        setAntrian(antrian);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Booking1.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_TANGGAL,tanggal);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void registerUser(){
        final String username = MainActivity.nama.toString();
        final String tanggal = tanggalEditText.getText().toString();
        final String antrian = antrianAnda.getText().toString();
        final AlertDialog[] alertDialog = new AlertDialog[1];

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        manager.setPreferences(Booking1.this, "Booking", "1");
                        manager.setPreferences(Booking1.this,"TanggalAntrian",tanggal.toString());
                        manager.setPreferences(Booking1.this,"NomorAntrian",antrian.toString());

                        tanggalEditText.setClickable(false);
                        tanggalEditText.setEnabled(false);
                        booking.setEnabled(false);
                        booking.setClickable(false);

                        alertDialog[0] = new AlertDialog.Builder(Booking1.this)
                                .setTitle("Booking Status")
                                .setMessage("Tanggal :\n" + tanggalEditText.getText().toString() + "\nNomor Antrian :\n" + nomorAnda)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Booking1.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).create();
                        alertDialog[0].show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Booking1.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_NAMA,username);
                params.put(KEY_TANGGAL,tanggal);
                params.put(KEY_ANTRIAN, antrian);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //cancel antrian
    private void cancelUser(){
        final String username = MainActivity.nama.toString();
        final String tanggal = tanggalEditText.getText().toString();
        final String antrian = antrianAnda.getText().toString();
        final AlertDialog[] alertDialog = new AlertDialog[1];

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CANCEL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       alertDialog[0] = new AlertDialog.Builder(Booking1.this)
                                .setTitle("Cancel Antrian")
                                .setMessage("Anda yakin ingin membatalkan antrian ini?\n Tanggal Antrian :\n" + tanggalEditText.getText().toString() + "\nNomor Antrian :\n" + nomorAnda)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        manager.setPreferences(Booking1.this, "Booking", "0");

                                        tanggalEditText.setClickable(true);
                                        tanggalEditText.setEnabled(true);
                                        booking.setEnabled(true);
                                        booking.setClickable(true);
                                    }
                                }).create();
                        alertDialog[0].show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Booking1.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_NAMA,username);
                params.put(KEY_TANGGAL,tanggal);
                params.put(KEY_ANTRIAN, antrian);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if(v == booking){
            registerUser();
        } else if (v == tanggalEditText){
            datePickerDialog.show();
        } else  if ( v == butonCancel ){
            cancelUser();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Booking1.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
