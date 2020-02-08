package com.example.ghost.vaksin;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

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
import java.util.Date;
import java.util.Locale;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private SimpleDateFormat simpleDateFormat;
    private DatePickerDialog datePickerDialog;
    private EditText namaAnak, namaOrtu,tlahir,umur,nohp,email,password,alamat;
    private static RadioGroup kelamin;
    private static RadioButton laki;
    private static RadioButton perempuan;
    private AwesomeValidation awesomeValidation;
    private ConnectionDetector cd;
    String jeniskelamin;
    public static int tahun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Daftar Akun");
        awesomeValidation =  new AwesomeValidation(ValidationStyle.BASIC);

        namaAnak = (EditText) findViewById(R.id.editText);
        namaOrtu = (EditText) findViewById(R.id.editText2);
        tlahir = (EditText) findViewById(R.id.editText5);
        umur = (EditText) findViewById(R.id.editText10);
        nohp = (EditText) findViewById(R.id.editText6);
        email = (EditText) findViewById(R.id.editText7);
        password = (EditText) findViewById(R.id.editText8);
        alamat = (EditText) findViewById(R.id.editText4);
        kelamin = (RadioGroup) findViewById(R.id.radioGroup);
        laki = (RadioButton) findViewById(R.id.radioButton);
        perempuan = (RadioButton) findViewById(R.id.radioButton2);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        setTanggal();

        awesomeValidation.addValidation(this,R.id.editText,  "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this,R.id.editText6, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);
        awesomeValidation.addValidation(this,R.id.editText7,  Patterns.EMAIL_ADDRESS, R.string.emailerror);

        RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (laki.isChecked()){
                    jeniskelamin = "Laki-Laki";
                } else if (perempuan.isChecked()) {
                    jeniskelamin = "Perempuan";
                }
            }
        };
        kelamin.setOnCheckedChangeListener(listener);
        cd = new ConnectionDetector();
    }

    private void setTanggal(){
        tlahir.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newCalendar = Calendar.getInstance();
                newCalendar.set(year, monthOfYear,dayOfMonth);
                Date dateNow = Calendar.getInstance().getTime();
                Long time = dateNow.getTime() / 1000 - newCalendar.getTimeInMillis()/ 1000;

                int years = Math.round(time) / 31536000; //detik dlm 1 thn
                int months = Math.round(time - years * 31536000) / 2628000;
                int days = Math.round((time - years * 31536000) - months * 2628000) / 86400;

                if (years < 15){
                    namaOrtu.setEnabled(true);
                    umur.setText(years + " Tahun " +months + " Bulan " +days + " Hari ");
                } else {
                    namaOrtu.setEnabled(false);
                    umur.setText(years + " Tahun");
                }

                Log.i("tanggal", "" + years+"  "+months+ " "+days);
                tlahir.setText(simpleDateFormat.format(newCalendar.getTime()));
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

    }

    public void Register(View view){
        awesomeValidation.validate();
        String namaAnakText = namaAnak.getText().toString();
        String namaOrtuText="";
        String tlahirText = tlahir.getText().toString();
        String umurText = umur.getText().toString();
        String nohpText = nohp.getText().toString();
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        String alamatText = alamat.getText().toString();

        if (namaOrtu.getText().toString().equals("")||namaOrtu.getText().toString().equals(" ")){
            namaOrtuText = "-";
        } else {
            namaOrtuText = namaOrtu.getText().toString();
        }

        if (namaAnakText != null && namaOrtuText != null && tlahirText != null && umurText!=null && nohpText != null &&
                emailText != null && passwordText != null && alamatText != null && jeniskelamin != null){

            BackgroundSubmit backgroundSubmit = new BackgroundSubmit(this);
            backgroundSubmit.execute(namaAnakText,namaOrtuText,tlahirText,umurText,jeniskelamin,nohpText,alamatText,emailText,passwordText);
        } else {
            Toast.makeText(Register.this, "Isi data dengan lengkap", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        datePickerDialog.show();
    }

    public class BackgroundSubmit extends AsyncTask<String, Void, String>{
        Context context;
        AlertDialog alertDialog;
        RelativeLayout snackbar;

        BackgroundSubmit(Context ctx) { context = ctx;}


        @Override
        protected String doInBackground(String... params) {
            String submit_url = "http://www.rumahvaksin.com/android/register.php";
            try {
                String namaanak   = params[0];
                String namaortu   = params[1];
                String tgllahir   = params[2];
                String umur_      = params[3];
                String jnskelamin = params[4];
                String nomrhp     = params[5];
                String alamat_    = params[6];
                String mail       = params[7];
                String pass       = params[8];
                URL url = new URL(submit_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("namaanak","UTF-8")+"="+URLEncoder.encode(namaanak,"UTF-8")+"&"
                                    +URLEncoder.encode("namaortu","UTF-8")+"="+URLEncoder.encode(namaortu,"UTF-8")+"&"
                                    +URLEncoder.encode("tgllahir","UTF-8")+"="+URLEncoder.encode(tgllahir,"UTF-8")+"&"
                                    +URLEncoder.encode("umur_","UTF-8")+"="+URLEncoder.encode(umur_,"UTF-8")+"&"
                                    +URLEncoder.encode("jnskelamin","UTF-8")+"="+URLEncoder.encode(jnskelamin,"UTF-8")+"&"
                                    +URLEncoder.encode("nomrhp","UTF-8")+"="+URLEncoder.encode(nomrhp,"UTF-8")+"&"
                                    +URLEncoder.encode("alamat_","UTF-8")+"="+URLEncoder.encode(alamat_,"UTF-8")+"&"
                                    +URLEncoder.encode("mail","UTF-8")+"="+URLEncoder.encode(mail,"UTF-8")+"&"
                                    +URLEncoder.encode("pass","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");
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
            alertDialog.setTitle("Register Status");
            snackbar = (RelativeLayout) findViewById(R.id.snackbar1);
        }

        @Override
        protected void onPostExecute(String result){
            if (result.equals("success")){
                alertDialog.setMessage(result);
                alertDialog.show();
                Snackbar.make(snackbar,"Berhasil, silahkan cek email untuk verifikasi akun anda", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Register.this, Login.class); //cocok mi kah
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setActionTextColor(Color.BLUE)
                        .show();
            }else {
                alertDialog.setMessage(result);
                alertDialog.show();
            }
        }
    }

    public void onBackPressed(){
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }
}
