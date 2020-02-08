package com.example.ghost.vaksin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class dokter extends AppCompatActivity {

    //String myJSON;
    ListView listView;
    //JSONArray dokter = null;
    /*ArrayList<HashMap<String,String>> listdokter;
    private static final String RESULT = "result";
    private static final String ID = "id";
    private static final String NAMA = "name";
    private static final String PIC = "gambar";
    private static final String DESCRIPT = "deskripsi";
    private static final String HP = "telp"; */

    private String info[] = {
            "Spesialis Pediatrik/Infeksi Tropis",
            "Spesialis Nutrisi Metabolik",
            "Spesialis Kardiologi",
            "Spesialis Gastro Hepatologi"
    };

    private String desc[] = {
            "Klik untuk melihat detail", "Klik untuk melihat detail",
            "Klik untuk melihat detail", "Klik untuk melihat detail"
    };

    private Integer imageid[] = {
            R.drawable.doc1, R.drawable.doc2, R.drawable.doc3, R.drawable.doc4
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dokter);
        getSupportActionBar().setTitle("Info Dokter");
        //listdokter = new ArrayList<HashMap<String, String>>();


        list_info_dokter info_dokter = new list_info_dokter(this, info, desc, imageid);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(info_dokter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AlertDialog alertDialog;

                switch (position) {
                    case 0:
                        alertDialog = new AlertDialog.Builder(dokter.this)
                                .setTitle("dr. Darlan Darwis, SpA")
                                .setIcon(R.drawable.doctor)
                                .setMessage(R.string.dokter1)  
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("Panggil", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri number = Uri.parse("tel:0850000");
                                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                                        startActivity(callIntent);
                                    }
                                }).create();
                        alertDialog.show();
                        break;
                    case 1:
                        alertDialog = new AlertDialog.Builder(dokter.this)
                                .setTitle("dr. Ratno Juniarto, SpA")
                                .setIcon(R.drawable.doctor)
                                .setMessage(R.string.dokter2)
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("Panggil", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri number = Uri.parse("tel:0850000");
                                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                                        startActivity(callIntent);
                                    }
                                }).create();
                        alertDialog.show();
                        break;
                    case 2:
                        alertDialog = new AlertDialog.Builder(dokter.this)
                                .setTitle("dr. Endah Citra Resmi, SpA")
                                .setIcon(R.drawable.doctor)
                                .setMessage(R.string.dokter3)
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("Panggil", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri number = Uri.parse("tel:0850000");
                                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                                        startActivity(callIntent);
                                    }
                                }).create();
                        alertDialog.show();
                        break;
                    case 3:
                        alertDialog = new AlertDialog.Builder(dokter.this)
                                .setTitle("dr. Partini Pudjiastuti, SpA")
                                .setIcon(R.drawable.doctor)
                                .setMessage(R.string.dokter4)
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("Panggil", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri number = Uri.parse("tel:0850000");
                                        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                                        startActivity(callIntent);
                                    }
                                }).create();
                        alertDialog.show();
                        break;
                }
            }
        });
    }

   /* protected void showList(){
        try {
            JSONObject jsonObject = new JSONObject(myJSON);
            dokter = jsonObject.getJSONArray(RESULT);

            for (int i=0; i<dokter.length(); i++){
                JSONObject object = dokter.getJSONObject(i);
                String id = object.getString(ID);
                String nama = object.getString(NAMA);
                String des = object.getString(DESCRIPT);
                String nohp = object.getString(HP);

                HashMap<String, String> doctor = new HashMap<String, String>();
                doctor.put(ID,id);
                doctor.put(NAMA, nama);
                doctor.put(DESCRIPT, des);

                listdokter.add(doctor);
            }

            ListAdapter adapter = new SimpleAdapter(
                    com.example.ghost.vaksin.dokter.this, listdokter, R.layout.list_info_dokter,
                    new String[]{ID,NAMA,DESCRIPT}, new int[]{R.id.id,R.id.nama,R.id.des});
            listView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpClient =
                return null;
            }
        }
    } */

    public void back (View view){
        Intent intent = new Intent(dokter.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(dokter.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}