package com.example.ghost.vaksin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Laporkan extends AppCompatActivity {
    EditText testimoni;
    Button submit;
    public static final String SUBMIT_URL = "http://rumahvaksin.com/android/testimoni.php";
    private ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporkan);
        getSupportActionBar().setTitle("Testimoni");
        cd = new ConnectionDetector();

        testimoni = (EditText)findViewById(R.id.editText15);
        submit = (Button)findViewById(R.id.button6);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testimoni.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(),"Isikan testimoni Anda",Toast.LENGTH_LONG).show();
                } else {
                    kirim();
                }
            }
        });
    }

    private void kirim(){
        final  String mail = MainActivity.email;
        final String kirim = testimoni.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SUBMIT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            Toast.makeText(Laporkan.this, "Testimoni sudah terkirim, Terimakasih", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Laporkan.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
          @Override
            protected Map<String, String> getParams(){
              Map<String, String> params =new HashMap<String, String>();
              params.put("testimoni",kirim);
              params.put("email", mail);
              return params;
          }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Laporkan.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
