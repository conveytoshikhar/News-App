package com.example.shikhar.newspage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URL;

/**
 * Created by Shikhar on 02-01-2018.
 */

public class DownloadTask extends AsyncTask<String,Void,Bitmap> {
    ImageView v;
    String url;
    public DownloadTask(ImageView v, String url){
        this.v=v;
        this.url=url;
    }


    protected Bitmap doInBackground(String... params){
        Bitmap b=null;
        try{
            URL newurl=new URL(url);
            b= BitmapFactory.decodeStream(newurl.openConnection().getInputStream());


        }catch(Exception e){
            b=null;
        }


        return b;
    }

    protected void onPostExecute(Bitmap result){
        super.onPostExecute(result);
        v.setImageBitmap(result);

    }
}
