package com.example.ghost.vaksin;

import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static com.example.ghost.vaksin.ConnectionDetector.connectionDetectorListener;
import static com.example.ghost.vaksin.ConnectionDetector.isConnected;
import static com.example.ghost.vaksin.Login.email;

public class Reset extends AppCompatActivity {

    EditText password, kode;
    Button forgot;
    RelativeLayout snackbar;
    public static final String UPDATE_URL = "http://rumahvaksin.com/android/updatepass.php";
    private ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        getSupportActionBar().setTitle("Reset Password");

        kode = (EditText) findViewById(R.id.editText10);
        password = (EditText) findViewById(R.id.editText9);
        forgot= (Button) findViewById(R.id.kirim);
        snackbar = (RelativeLayout) findViewById(R.id.snackbar1);
        cd = new ConnectionDetector();

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((password.getText().length() == 0) || (kode.getText().length() == 0)) {
                    Toast.makeText(getApplicationContext(),"Masukan alamat Email dan kode Vertifikasi Anda",Toast.LENGTH_LONG).show();
                } else if (cd != null){
                    reset();
                } else  {
                    Snackbar.make(snackbar, "Gagal mengirim data, " + "Cek Koneksi Internet Anda" , Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
                                    finish();
                                }
                            })
                            .setActionTextColor(Color.BLUE)
                            .show();
                }
            }
        });
    }

    private void reset (){
       final String emai = ""+Login.email.getText().toString();
        Log.i("email",String.valueOf(emai));
        final  String kodeVer = kode.getText().toString();
        final  String pass = password.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("sukses", response);
                        if (response.equals("success")){
                            Toast.makeText(Reset.this, "Password baru Berhasil di update, silahkan cek email anda", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Reset.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Reset.this, "Kode Verifikasi tidak cocok",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("password",pass);
                params.put("kode",kodeVer);
                params.put("email", emai);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onBackPressed(){
        Intent intent = new Intent(Reset.this, Login.class);
        startActivity(intent);
        finish();
    }

}
