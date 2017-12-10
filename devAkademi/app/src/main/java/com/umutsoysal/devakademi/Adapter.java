package com.umutsoysal.devakademi;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapter extends BaseAdapter {
    // Declare Variables
    Dialog dialog;
    Context context=null;
    String[] fiyat;
    String[] tarih;

    LayoutInflater inflater;

    //ListviewAdapter constructor
    //Gelen değerleri set ediyor
    public Adapter(Context context, String[] last, String[] tarih) {
        this.context = context;
        this.fiyat = last;
        this.tarih = tarih;


    }

    @Override
    public int getCount() {
        return fiyat.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView text_last;
        TextView date;
        FrameLayout ayrinti_layout,bilgiler_layout;
        ImageView logo;
        RelativeLayout alarm;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.list_item, parent, false);//list_item_row dan yeni bir view oluşturuyoruz

        // oluşan itemviewin içindeki alanları Anasayfadan gelen değerler ile set ediyoruz
        text_last = (TextView) itemView.findViewById(R.id.last);
        date = (TextView) itemView.findViewById(R.id.date);
        bilgiler_layout=(FrameLayout)itemView.findViewById(R.id.bilgiler);


        text_last.setText(fiyat[position]+"  $");
        date.setText(tarih[position]);


        if(position%2==0)
        {
            bilgiler_layout.setBackgroundResource(R.color.after);
        }
        else {
            bilgiler_layout.setBackgroundResource(R.color.now);
        }

        return itemView;
    }






}
