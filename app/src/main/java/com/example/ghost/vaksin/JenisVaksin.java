package com.example.ghost.vaksin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class JenisVaksin extends AppCompatActivity {

    private ListView listView;
    private String info[] = {
            "Imunisasi Polio", "Imunisasi Campak", "Imunisasi DPT", "Imunisasi Hepatitis B",
            "Imunisasi MMR", "Imunisasi BCG" /*"Vaksin Jerap Td", "Vaksin Jerap DT", "Vaksin TT"
            "Vaksin DTP", "Vaksin DTP-HB"
            "Umur 11 Tahun" "Umur 12 Tahun", "Umur 13 Tahun", "Umur 14 Tahun", "Umur 15 Tahun",
            "Umur 16 Tahun", "Umur 17 Tahun", "Umur 18 Tahun", "Umur 19 Tahun", "Umur 20 Tahun" */
    };

    private String desc[] = {
            "Klik untuk melihat detail", "Klik untuk melihat detail ",
            "Klik untuk melihat detail", "Klik untuk melihat detail",
            "Klik untuk melihat detail", "Klik untuk melihat detail"
            /*"Klik untuk melihat detail Vaksin", "Klik untuk melihat detail Vaksin",
            "Klik untuk melihat detail Vaksin"
            "Klik untuk melihat detail Vaksin",
            "Klik untuk melihat detail Vaksin" */
    };

    private Integer imageid[] = {
            R.drawable.satu,
            R.drawable.dua,
            R.drawable.tiga,
            R.drawable.empat,
            R.drawable.lima,
            R.drawable.enam
            //R.drawable.tuju,
            //R.drawable.delapan,
            //R.drawable.sembilan
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_vaksin);
        getSupportActionBar().setTitle("Info Detail Vaksin");

        //custom_list custom_list = new custom_list(this, info, desc, imageid);
        list_info_vaksin listInfo =  new list_info_vaksin(this, info, desc, imageid);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listInfo);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*Toast.makeText(getApplicationContext(),"Memuat Informasi"+info[i], Toast.LENGTH_SHORT).show(); */
                final AlertDialog alertDialog;

                switch (i){
                    case 0:
                        alertDialog = new AlertDialog.Builder(JenisVaksin.this)
                                .setIcon(R.drawable.vaksin)
                                .setTitle("Imunisasi Polio")
                                .setMessage(R.string.polio)
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("Order Vaksin", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(JenisVaksin.this, Order.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).create();
                        alertDialog.show();
                        break;
                    case 1:
                        alertDialog = new AlertDialog.Builder(JenisVaksin.this)
                                .setIcon(R.drawable.vaksin)
                                .setTitle("Imunisasi Campak")
                                .setMessage(R.string.campak)
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("Order Vaksin", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(JenisVaksin.this, Order.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).create();
                        alertDialog.show();
                        break;

                    case 2:
                        alertDialog = new AlertDialog.Builder(JenisVaksin.this)
                                .setIcon(R.drawable.vaksin)
                                .setTitle("Vaksin DPT")
                                .setMessage(R.string.dpt)
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("Order Vaksin", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(JenisVaksin.this, Order.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).create();
                        alertDialog.show();
                        break;

                    case 3:
                        alertDialog = new AlertDialog.Builder(JenisVaksin.this)
                                .setIcon(R.drawable.vaksin)
                                .setTitle("Vaksin Hepatitis B")
                                .setMessage(R.string.hepatitis)
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("Order Vaksin", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(JenisVaksin.this, Order.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).create();
                        alertDialog.show();
                        break;

                    case 4:
                        alertDialog = new AlertDialog.Builder(JenisVaksin.this)
                                .setIcon(R.drawable.vaksin)
                                .setTitle("Vaksin MMR")
                                .setMessage(R.string.mmr)
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("Order Vaksin", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(JenisVaksin.this, Order.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).create();
                        alertDialog.show();
                        break;

                    case 5:
                        alertDialog = new AlertDialog.Builder(JenisVaksin.this)
                                .setIcon(R.drawable.vaksin)
                                .setTitle("Vaksin BCG")
                                .setMessage(R.string.bcg)
                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("Order Vaksin", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(JenisVaksin.this, Order.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).create();
                        alertDialog.show();
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(JenisVaksin.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void back (View view){
        Intent intent = new Intent(JenisVaksin.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
