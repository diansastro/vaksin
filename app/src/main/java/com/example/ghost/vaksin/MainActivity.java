package com.example.ghost.vaksin;


import android.app.*;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.ghost.vaksin.ConnectionDetector.connectionDetectorListener;
import static java.lang.String.valueOf;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private RequestHandler requestHandler;
    private CircleImageView imageView;

        RelativeLayout snackbar;
        SessionManager manager;
        public static String username = "username";
        public static String nama = "";
        public static String ortu = "";
        public static String tlahir = "";
        public static String umur = "";
        public static String kelamin = "";
        public static  String nohp = "";
        public static String email = "";
        public static String alamat = "";
        private TextView welcome;
        private View mHeaderView;
        private Context ctx;
        private ConnectionDetector cd;
        public static int age;
        public static  int bulan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        requestHandler = new RequestHandler();
        getSupportActionBar().setTitle("Rumah Vaksin dr.Bob");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        manager = new SessionManager();
        checkLogin();

        mHeaderView = navigationView.getHeaderView(0);
        welcome= (TextView) mHeaderView.findViewById(R.id.textWelcome);
        imageView = (CircleImageView) mHeaderView.findViewById(R.id.imageViewProfil);
        snackbar = (RelativeLayout) findViewById(R.id.snackbar1);
        imageView.setOnClickListener(this);
        tampilData();
        cd = new ConnectionDetector();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final AlertDialog alertDialog;

        if (id == R.id.person) {
            Intent intent = new Intent(MainActivity.this, Profil.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.booking) {
            Intent intent = new Intent(MainActivity.this,Booking1.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.dokter) {
            Intent intent = new Intent(MainActivity.this, dokter.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.logout) {
            manager.logout(MainActivity.this);
                     startActivity(new Intent(MainActivity.this,Login.class));
                     finish();
        } else if (id == R.id.lokasi) {
            Intent intent = new Intent(MainActivity.this, Lokasi.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.laporakan) {
            Intent intent = new Intent(MainActivity.this,Laporkan.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.tentang) {
            alertDialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Tentang Pengembang")
                    .setIcon(R.drawable.developer)
                    .setMessage("Create by: gHostDev, IT UNHAS 2012 \n" +
                            "email : support@rumahvaksin.com")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create();
            alertDialog.show();

        } else if (id == R.id.home){
            Intent intent = new Intent(MainActivity.this, Images.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.order){
            Intent intent = new Intent(MainActivity.this, Order.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.shareApp){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.rumahvaksin.ghost.vaksin");
            startActivity(Intent.createChooser(intent, "Share Aplikasi Rumah Vaksin"));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkLogin(){
        if(manager.getPreferences(MainActivity.this,"status").equals("1")) {
            username = manager.getPreferences(MainActivity.this,"username");
        }else{
            startActivity(new Intent(MainActivity.this,Login.class));
            finish();
        }
    }

    private void tampilData() {

        String email = ""+MainActivity.username;
        Log.i("email",email);
        if (email.equals("")){
            return;
        }
        String url = Config.data_url+email;
            StringRequest stringRequest = new StringRequest (url, new Response.Listener<String>() {
            @Override
            public void onResponse (String response) {
                showJSON(response);
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.json_array);
            JSONObject collegeData = result.getJSONObject(0);
            nama = collegeData.getString(Config.key_nama);
            ortu = collegeData.getString(Config.key_ortu);
            tlahir = collegeData.getString(Config.key_lahir);
            umur = collegeData.getString(Config.key_umur);
            kelamin = collegeData.getString(Config.key_kelamin);
            nohp = collegeData.getString(Config.key_nohp);
            email = collegeData.getString(Config.key_email);
            alamat = collegeData.getString(Config.key_alamat);
        }catch (JSONException e){
            e.printStackTrace();
        }
        welcome.setText("Selamat Datang di Rumah Vaksin\n" +nama);
        viewImage();

        //setting pemberitahuan
        if (!username.equals("username")){
            alarm();
        }
    }

    private void viewImage() {
        String mail = email;
        class GetImage extends AsyncTask<String, Void, Bitmap> {

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Bitmap b){
                super.onPostExecute(b);
                //Log.i("Tes", String.valueOf(b));
                if ((b!=null)){
                    imageView.setImageBitmap(b);
                }
            }
            @Override
            protected Bitmap doInBackground(String... params) {
                String emai = params[0];
                String add = "http://www.rumahvaksin.com/photo/member/viewimages.php?email="+emai;
                URL url = null;
                Bitmap image = null;
                try {
                    url = new URL(add);
                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return image;
            }
        }

        GetImage gi = new GetImage();
        gi.execute(mail);
    }

    public void alarm() {
        if (manager.getPreferences(MainActivity.this, "notifvaksin").equals("")){
            manager.setPreferences(MainActivity.this, "notifvaksin","belum");
        }
        Log.i("alarm", "hgf"+manager.getPreferences(MainActivity.this, "notifvaksin"));
        String namaVaksin = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendar = Calendar.getInstance();
        int year = 0, monthOfYear = 0, dayOfMonth = 0;
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, monthOfYear, dayOfMonth);

        Date tglLahir = new Date();
        try {
            tglLahir = simpleDateFormat.parse(tlahir);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(tglLahir);
        Long time = calendar.getTimeInMillis() / 1000 -calendar1.getTimeInMillis()/ 1000;
        int years = Math.round(time) / 31536000; //detik dlm 1 thn
        int month = (Math.round(time) - (years * 31536000)) / 2628000;
        Log.i("Bulan", String.valueOf(month + year));

        age = years;
        //bulan = (month + year);
        bulan = month;
        first();


        if (bulan < 3 && bulan >1){
            namaVaksin = "Hallo " + nama + " umur anda " + umur + "\n" + (getString(R.string.vaksin1));
        } else if (bulan <= 9 && bulan > 3){
            namaVaksin = "Halo " + nama + " umur anda " + umur + "\n" + (getString(R.string.vaksin2));
        } else if ((bulan <= 18 && bulan > 12)|| bulan == 1 ||age == 5){
            namaVaksin = "Hallo " + nama + " umur anda " + umur + "\n" + (getString(R.string.vaksin3));
        } else if (bulan == 12 || bulan ==15 || age == 6 ){
            namaVaksin = "Hallo " + nama + " umur anda " + umur + "\n" + (getString(R.string.vaksin5));
        } else if (bulan == 6){
            namaVaksin = "Halo " + nama + " umur anda " + umur + "\n" + (getString(R.string.vaksin6));
        }

        if (calendar1.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)){
            if (calendar1.get(Calendar.DAY_OF_MONTH)== calendar.get(Calendar.DAY_OF_MONTH)){
                if (manager.getPreferences(MainActivity.this, "notifvaksin").equals("belum")){
                    NotificationManager mNotificationManager = (NotificationManager)
                            this.getSystemService(Context.NOTIFICATION_SERVICE);

                    final Intent i = new Intent(this, MainActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.drawable.vaksin)
                                    .setContentTitle("Rumah Vaksin dr.Bob")
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(namaVaksin))
                                    .setContentText(namaVaksin);

                    mBuilder.setContentIntent(contentIntent);
                    mBuilder.setAutoCancel(true);
                    mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                    mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
                    mNotificationManager.notify(0, mBuilder.build());
                    manager.setPreferences(MainActivity.this, "notifvaksin","sudah");
                }
            } else if (calendar1.get(Calendar.DAY_OF_MONTH) < calendar.get(Calendar.DAY_OF_MONTH)){
                manager.setPreferences(MainActivity.this, "notifvaksin","belum");
            }
        } else {
            manager.setPreferences(MainActivity.this, "notifvaksin","belum");
        }
    }

    public void first(){
        SharedPreferences preferences = getSharedPreferences("pref", 0);
        boolean firstRun  = preferences.getBoolean("firstRun", true);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (firstRun){
            if (bulan < 3 && bulan > 1){
                preferences.edit().putBoolean("firstRun", false).apply();
                builder.setIcon(R.drawable.doctor);
                builder.setTitle(R.string.judulVaksin);
                builder.setMessage("Hallo " + nama + " umur anda " + umur + "\n" + (getString(R.string.vaksin1)) );
                builder.setPositiveButton("Info Detail", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent I = new Intent(MainActivity.this, JenisVaksin.class);
                        startActivity(I);
                        finish();
                    }
                });
                builder.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }else if (bulan <= 9 && bulan > 3){
                preferences.edit().putBoolean("firstRun", false).apply();
                builder.setIcon(R.drawable.doctor);
                builder.setTitle(R.string.judulVaksin);
                builder.setMessage("Hallo " + nama + " umur anda "  + umur + "\n" + (getString(R.string.vaksin2)));
                builder.setPositiveButton("Info Detail", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainActivity.this, JenisVaksin.class);
                        startActivity(i);
                        finish();
                    }
                });
                builder.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            } if ((bulan == 18 && bulan > 12)|| bulan == 1 || age == 5){
                preferences.edit().putBoolean("firstRun", false).apply();
                builder.setIcon(R.drawable.doctor);
                builder.setTitle(R.string.judulVaksin);
                builder.setMessage("Hallo " + nama + " umur anda " + umur + "\n" + (getString(R.string.vaksin3)));
                builder.setPositiveButton("Info Detail", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainActivity.this, JenisVaksin.class);
                        startActivity(i);
                        finish();
                    }
                });
                builder.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            } else if (bulan == 12 || bulan == 15 || age == 6){
                preferences.edit().putBoolean("firstRun", false).apply();
                builder.setIcon(R.drawable.doctor);
                builder.setTitle(R.string.judulVaksin);
                builder.setMessage("Hallo " + nama + " umur anda " + umur + "\n" + (getString(R.string.vaksin5)));
                builder.setPositiveButton("Info Detail", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainActivity.this, JenisVaksin.class);
                        startActivity(i);
                        finish();
                    }
                });
                builder.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            } else if ( bulan > 1 && bulan < 6){
                preferences.edit().putBoolean("firstRun", false).apply();
                builder.setIcon(R.drawable.doctor);
                builder.setTitle(R.string.judulVaksin);
                builder.setMessage("Hallo " + nama + " umur anda "+ umur + "\n" + (getString(R.string.vaksin6)));
                builder.setPositiveButton("Info Detail", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainActivity.this, JenisVaksin.class);
                        startActivity(i);
                        finish();
                    }
                });
                builder.setNegativeButton("Tutup", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        }
    }

    public void order (View view){
        Intent intent = new Intent(MainActivity.this, Order.class);
        startActivity(intent);
        finish();
    }

    public void booking (View view){
        Intent intent = new Intent(MainActivity.this,Booking1.class);
        startActivity(intent);
        finish();
    }

    public void jadwal (View view){
        Intent intent = new Intent(MainActivity.this, JenisVaksin.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == imageView){
            Intent i = new Intent(MainActivity.this, Images.class);
            startActivity(i);
            finish();
        }
    }
}
