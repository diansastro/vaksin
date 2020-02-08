package com.example.ghost.vaksin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText password;
    public static EditText username,email;
    SessionManager manager;
    String user = "user";
    Button tentang,submit;
    AlertDialog alertDialog;
    private PopupWindow pw;
    private ConnectionDetector cd;
    RelativeLayout snackbar;
    public static final String RESET_URL = "http://rumahvaksin.com/android/reset.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tentang = (Button) findViewById(R.id.about);
        manager = new SessionManager();
        checkLogin();
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        snackbar = (RelativeLayout) findViewById(R.id.snackbar1);
        cd = new ConnectionDetector();

        //run app intro layout
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                SharedPreferences sharedPreferences = getSharedPreferences(Config.FLAG, Context.MODE_PRIVATE);

                if (sharedPreferences.getBoolean(Config.FLAG,true)){
                    startActivity(new Intent(Login.this,DefaultIntro.class));
                    SharedPreferences.Editor e= sharedPreferences.edit();
                    e.putBoolean(Config.FLAG,false);
                    e.apply();
                }
            }
        });
        thread.start();
    }

    private void showPopup(){
        try {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = layoutInflater.inflate(R.layout.popup,
                    (ViewGroup) findViewById(R.id.popup1));
            pw = new PopupWindow(layout,(WindowManager.LayoutParams.MATCH_PARENT) , (WindowManager.LayoutParams.WRAP_CONTENT), true);
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

            submit = (Button) layout.findViewById(R.id.button11);
            email = (EditText) layout.findViewById(R.id.editText13);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (email.getText().length() == 0) {
                        Snackbar.make(snackbar,"Masukan Alamat Email Anda",Snackbar.LENGTH_LONG).show();
                    } else {
                       if (cd != null){
                           kirimKode();
                       } else {
                           //Snackbar.make(snackbar, "Tidakada koneksi internet" ,Snackbar.LENGTH_LONG).show();
                           Toast.makeText(Login.this, "Seses", Toast.LENGTH_SHORT).show();
                       }
                    }
                }
            });

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void kirimKode (){
       final String mailUser = email.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RESET_URL+"?email="+mailUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")){
                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Snackbar.make(snackbar,"Berhasil, Cek Email Anda",Snackbar.LENGTH_INDEFINITE)
                                            .setAction("OK", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Intent intent = new Intent(Login.this, Reset.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .setActionTextColor(Color.BLUE)
                                            .show();
                                            pw.dismiss();
                                }
                            });
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
                params.put("email",mailUser);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void Tentang (View view){
        if (view == tentang){
          alertDialog = new AlertDialog.Builder(Login.this)
                  .setIcon(R.drawable.vaksin)
                  .setTitle("Tentang Rumah Vaksin")
                  .setMessage("Aplikasi kesehatan berbasis Android yang bertujuan untuk memudahkan pengguna untuk mengetahui jadwal Vaksinasi, Penggunaan Vaksin dan Portal Informasi mengenai Vaksinasi secara Umum ")
                  .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                      }
                  }).create();
            alertDialog.show();
        }
    }

    public void Login(View view) {
        if (username.getText().length()== 0 || password.getText().length() == 0){
            Snackbar.make(snackbar, "Email dan Password tidak boleh kosong", Snackbar.LENGTH_LONG).show();
        } else {
            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();
            BackgroundLogin backgroundLogin = new BackgroundLogin(this);
            backgroundLogin.execute(usernameText, passwordText);
        }
    }

    public class BackgroundLogin extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        BackgroundLogin(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String login_url = "http://rumahvaksin.com/android/cobalogin.php";
                try {
                    String uname = params[0];
                    String pass = params[1];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("uname", "UTF-8") + "=" + URLEncoder.encode(uname, "UTF-8") + "&"
                                     + URLEncoder.encode("pass", "UTF-8") + "=" + URLEncoder.encode(pass, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
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
            alertDialog.setTitle("Login Gagal");
        }
        @Override
        protected void onPostExecute(String result){
            if (result.equals("success")){
                manager.setPreferences(Login.this, "status", "1");
                manager.setPreferences(Login.this,"username",username.getText().toString());
                Intent intent = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                Log.i("status : ",manager.getPreferences(Login.this,"status"));
                finish();

            }else{
                alertDialog.setMessage(String.valueOf(result));
                alertDialog.show();
            }
        }
    }

    public void registerOnClick(View view){
        Intent intent =new Intent(Login.this,Register.class);
        startActivity(intent);
        finish();
    }

    public void resetOnClick(View view){
        showPopup();
    }

    public void checkLogin(){
        if(manager.getPreferences(Login.this,"status").equals("1")) {
            user = manager.getPreferences(Login.this,"usernmae");
            startActivity(new Intent(Login.this,Login.class));
            finish();
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}