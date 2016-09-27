package com.example.navanee.quizapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by navanee on 25-09-2016.
 */
public class GetImageAsync extends AsyncTask<String, Void, Bitmap> {
    private String url;
    private Bitmap bitmap;
    private ImageInterface activity;
    private int queID = 0;

    public GetImageAsync(ImageInterface activity) {
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        activity.setImage(bitmap,queID);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL(params[0]);
            queID = Integer.parseInt(params[1]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static interface ImageInterface
    {
        void setImage(Bitmap bitmap, int qID);
    }
}

