package com.example.ghost.vaksin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.example.ghost.vaksin.ConnectionDetector.isConnected;

public class Profil extends AppCompatActivity {
    
    private EditText textAnak;
    private EditText textOrtu;
    private EditText textLahir;
    private EditText textUmur;
    private EditText textNope;
    private EditText textMail;
    private EditText textAlamat;
    private EditText textKelamin;
    private ConnectionDetector cd;
    //private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        getSupportActionBar().setTitle("Profil Saya");

        textAnak = (EditText) findViewById(R.id.textAnak);
        textOrtu = (EditText) findViewById(R.id.textOrtu);
        textLahir = (EditText) findViewById(R.id.textLahir);
        textUmur = (EditText) findViewById(R.id.textUmur);
        textNope = (EditText) findViewById(R.id.textNope);
        textMail = (EditText) findViewById(R.id.textMail);
        textAlamat = (EditText) findViewById(R.id.textAlamat);
        textKelamin = (EditText) findViewById(R.id.textKelamin);

        textAnak.setText(MainActivity.nama);
        textOrtu.setText(MainActivity.ortu);
        textLahir.setText(MainActivity.tlahir);
        textUmur.setText(MainActivity.umur);
        textKelamin.setText(MainActivity.kelamin);
        textNope.setText(MainActivity.nohp);
        textMail.setText(MainActivity.email);
        textAlamat.setText(MainActivity.alamat);
        cd = new ConnectionDetector();
    }

    public void kembali (View view){
        Intent intent = new Intent(Profil.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Profil.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
