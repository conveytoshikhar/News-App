package com.example.shikhar.newspage;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shikhar on 02-01-2018.
 */

public class MyListAdapter extends SimpleAdapter {
    private Context mContext;
    public LayoutInflater inflater=null;
    public MyListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {

        super(context, data, resource, from, to);
        //Toast.makeText(mContext, "Contructor called", Toast.LENGTH_SHORT).show();
        this.mContext=context;
        inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public View getView(int position, View convertView, ViewGroup parent){
        View vi=convertView;
        if(convertView==null){
            vi=inflater.inflate(R.layout.newslayout,null);
        }
        HashMap<String,Object> data=(HashMap<String,Object>) getItem(position);


        DownloadTask ob=new DownloadTask((ImageView)vi.findViewById(R.id.img),(String) data.get("urlToImage"));
        ob.execute();


        TextView t1=(TextView) vi.findViewById(R.id.tv1);
        Typeface typeface1= Typeface.createFromAsset(mContext.getAssets(), "fonts/Walkway_Oblique_Bold.ttf");
        t1.setText((String) data.get("title"));
        t1.setTypeface(typeface1);
        TextView t2=(TextView) vi.findViewById(R.id.tv2);
        Typeface typeface2= Typeface.createFromAsset(mContext.getAssets(),"fonts/Raleway-MediumItalic.ttf");
        t2.setTypeface(typeface2);
        t2.setText((String) data.get("description"));
        TextView t3=(TextView) vi.findViewById(R.id.tv3);
        t3.setText((String) data.get("publishedAt"));


        return vi;
    }
}
