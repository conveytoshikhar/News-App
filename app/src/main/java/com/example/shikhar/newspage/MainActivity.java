package com.example.shikhar.newspage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ListView list;
    ArrayList<String> content=new ArrayList<>();
    ImageView scrollDown;
    RelativeLayout rel;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=(ListView) findViewById(R.id.mylist);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        final int height = display.getHeight();
        list.animate().translationYBy(height).setDuration(1);
        scrollDown=(ImageView) findViewById(R.id.scrollBottom);

        GetJSONData on=new GetJSONData(this);
        on.execute();


        rel=(RelativeLayout) findViewById(R.id.rel);




    }


    class GetJSONData extends AsyncTask<String,Integer,String>{
        Context ctx;

        public GetJSONData(Context ctx){
            this.ctx=ctx;
        }
        @Override
        protected String doInBackground(String... strings) {
            try{
                String myurl="https://newsapi.org/v2/top-headlines?sources=bloomberg&apiKey=d2b71909d2424d578aafb8175bc8192c";
                URL url= new URL(myurl);
                HttpURLConnection con=(HttpURLConnection) url.openConnection();
                con.connect();
                InputStream stream=con.getInputStream();
                InputStreamReader ins=new InputStreamReader(stream);
                BufferedReader reader=new BufferedReader(ins);
                StringBuffer buffer=new StringBuffer();

                String line="";


                while((line=reader.readLine())!=null){
                    buffer.append(line+"\n");
                }
                
                return buffer.toString();
            }catch(Exception e){
                Log.d("Background:","Hello Error");
                return e.getMessage().toString();
            }
        }
        
        @Override
        protected void onPreExecute() {
        
        } 
        
        @Override
        protected void onProgressUpdate(Integer... values) {
            
        }

        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
            ArrayList<HashMap<String,String>> al=new ArrayList<HashMap<String, String>>();
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            final int height = display.getHeight();

            rel.animate().translationYBy(-height).setDuration(2000);
            list.animate().translationYBy(-height).setDuration(2000);



            try {
                //Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
                JSONObject jsonOb=new JSONObject(result);
                JSONArray news=jsonOb.getJSONArray("articles");

                for(int i=0;i<news.length();i++){
                    JSONObject c=news.getJSONObject(i);
                    String title=c.getString("title");
                    String description=c.getString("description");
                    String publishedAt=c.getString("publishedAt");
                    String urlToImage=c.getString("urlToImage");



                    if(!c.isNull("url")){
                        content.add(c.getString("url"));
                    }else{
                        content.add("Url does not exist");
                    }

                    HashMap<String,String> hm=new HashMap<String,String>();


                    hm.put("title",title);
                    hm.put("description",description);
                    hm.put("publishedAt",publishedAt);
                    hm.put("urlToImage",urlToImage);

                    al.add(hm);
                }


                for(int i=0;i<content.size();i++){
                    Log.i("Content"+i,content.get(i));
                }
                //Toast.makeText(ctx, "Success:", Toast.LENGTH_SHORT).show();
                MyListAdapter adapter=new MyListAdapter(MainActivity.this, al, R.layout.newslayout,new String[]{"title","description","urlToImage","publishedAt"},new int[]{R.id.tv1,R.id.tv2,R.id.img,R.id.tv3});
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=new Intent(getApplicationContext(),DisplayContent.class);
                        intent.putExtra("content",content.get(position));
                        startActivity(intent);


                    }
                });

            } catch (JSONException e) {
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


        }



    }


    class putImages extends AsyncTask<String,Void,Bitmap> {
        Context ctx;

        public putImages(Context ctx) {
            this.ctx = ctx;
        }


        @Override
        protected Bitmap doInBackground(String... strings) {
            String url=strings[0];
            Bitmap result=null;
            try{

                java.net.URL myurl= new java.net.URL(url);
                InputStream in=myurl.openStream();
                result= BitmapFactory.decodeStream(in);
            }catch(Exception e){
                Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return result;
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            try {
                //         img.setImageBitmap(result);
            }catch(Exception e){
                Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }
    }
}
