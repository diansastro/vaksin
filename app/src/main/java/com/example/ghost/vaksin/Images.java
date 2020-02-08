package com.example.ghost.vaksin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static com.example.ghost.vaksin.ConnectionDetector.connectionDetectorListener;


public class Images extends AppCompatActivity implements View.OnClickListener{

    public static final String UPLOAD_URL = "http://rumahvaksin.com/photo/member/update.php";
    public static final String UPLOAD_KEY = "image";
    private int PICK_IMAGE_REQUEST = 1;
    private Button buttonUpload;
    private ImageView imageView;
    private Bitmap bitmap;
    private Uri filePath;
    RelativeLayout snackbar;
    private ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        getSupportActionBar().setTitle("Upload Foto");

        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        imageView = (ImageView) findViewById(R.id.imageView);
        snackbar = (RelativeLayout) findViewById(R.id.snackbar1);
        buttonUpload.setOnClickListener(this);
        Log.i("tes",MainActivity.username);
        showFileChooser();
        cd = new ConnectionDetector();
    }

    public void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodeImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return  encodeImage;
    }

    private void uploadImage(){
        if (imageView.getDrawable() == null ){
            //buttonUpload.setEnabled(false);
            buttonUpload.setClickable(false);
            Snackbar.make(snackbar, "Pilih dulu Foto anda", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showFileChooser();
                        }
                    })
                    .setActionTextColor(Color.BLUE)
                    .show();
        }else if (cd != null){
            class UploadImage extends AsyncTask<Bitmap, Void, String> {
                ProgressDialog loading;
                RequestHandler rh = new RequestHandler();

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(Images.this, "Mengunggah Gambar", "Silahkan Tunggu...", true, true);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Images.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                protected String doInBackground(Bitmap... params) {
                    Bitmap bitmap = params[0];
                    String uploadImage = getStringImage(bitmap);
                    HashMap<String, String> data = new HashMap<>();
                    data.put(UPLOAD_KEY, uploadImage);
                    data.put("email", MainActivity.username);
                    String result = rh.sendPostRequest(UPLOAD_URL, data);
                    return result;
                }
            }


            UploadImage ui = new UploadImage();
            ui.execute(bitmap);
        }  else if (cd == null ){
            Snackbar.make(snackbar, "Gagal mengirim data, " + "Cek Koneksi Internet Anda" , Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setActionTextColor(Color.BLUE)
                    .show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonUpload){
            uploadImage();
        }
    }

    @Override
    public void onBackPressed(){
       Intent intent = new Intent(Images.this, MainActivity.class);
       startActivity(intent);
       finish();
   }
}
