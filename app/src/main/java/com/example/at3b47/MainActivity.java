package com.example.at3b47;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ImageView imageView;
    ConnectivityManager connectivityManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      progressBar=findViewById(R.id.progress);
      imageView=findViewById(R.id.imageview);


      connectivityManager= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
      getImage();
    }
    private void getImage()
    {
        if (connectivityManager!=null)
        {
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if (networkInfo!=null && networkInfo.isConnected())
            {
                Log.e("MainActivity","Connected");
                new AsyncTaskToFetchImage().execute("https://www.google.com/url?sa=i&source=images&cd=&ved=2ahUKEwj3nPral9rlAhXJinAKHW1xBx8QjRx6BAgBEAQ&url=https%3A%2F%2Feconomictimes.indiatimes.com%2Fnews%2Fdefence%2Fpakistani-soldiers-killed-by-indian-army-in-jammu-and-kashmirs-kupwara%2Farticleshow%2F65397559.cms&psig=AOvVaw3rHj4M6t9-r_Y2iPVlTyAC&ust=1573287444199160");
            }
        }
    }


    class AsyncTaskToFetchImage extends AsyncTask<String,Void, Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap=null;
            String s=strings[0];
            try {
                URL url=new URL(s);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(300000);
                httpURLConnection.setConnectTimeout(100000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                int code=httpURLConnection.getResponseCode();
                if (code==HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    if (inputStream != null) {
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        Log.e("MainActivity", "Got image");
                    }
                    else
                        Log.e("MainActivity","Input stream error");
                }
                else
                {
                    Log.e("MainActivity","Unable to open connection");
                }

            }catch (Exception e)
            {
                e.printStackTrace();
                Log.e("MainActivity",e.getMessage());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap!=null)
            {
                imageView.setImageBitmap(bitmap);
                progressBar.setVisibility(View.INVISIBLE);
                Log.e("MainActivity","Got it");
            }
            else
            {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "unable to get image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
